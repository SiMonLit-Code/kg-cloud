package ai.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kg.api.edit.AttributeApi;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.req.BasicDetailFilter;
import ai.plantdata.kg.api.edit.resp.AttrDefVO;
import ai.plantdata.kg.api.edit.resp.SchemaVO;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.MongoApi;
import ai.plantdata.kg.api.pub.RelationApi;
import ai.plantdata.kg.api.pub.req.CommonFilter;
import ai.plantdata.kg.api.pub.req.GraphFrom;
import ai.plantdata.kg.api.pub.req.MongoQueryFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kg.api.pub.resp.RelationVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import ai.plantdata.kgcloud.constant.AppErrorCodeEnum;
import ai.plantdata.kgcloud.domain.app.converter.*;
import ai.plantdata.kgcloud.domain.app.converter.graph.GraphRspConverter;
import ai.plantdata.kgcloud.domain.app.dto.CoordinatesDTO;
import ai.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import ai.plantdata.kgcloud.domain.app.service.GraphHelperService;
import ai.plantdata.kgcloud.domain.app.util.AsyncUtils;
import ai.plantdata.kgcloud.domain.common.converter.ApiReturnConverter;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import ai.plantdata.kgcloud.domain.edit.service.ConceptService;
import ai.plantdata.kgcloud.domain.edit.service.DomainDictService;
import ai.plantdata.kgcloud.domain.graph.attr.dto.AttrDefGroupDTO;
import ai.plantdata.kgcloud.domain.graph.attr.service.GraphAttrGroupService;
import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocus;
import ai.plantdata.kgcloud.domain.graph.config.repository.GraphConfFocusRepository;
import ai.plantdata.kgcloud.domain.graph.manage.entity.Graph;
import ai.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import ai.plantdata.kgcloud.sdk.UserClient;
import ai.plantdata.kgcloud.sdk.constant.GraphInitBaseEnum;
import ai.plantdata.kgcloud.sdk.req.app.*;
import ai.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReqList;
import ai.plantdata.kgcloud.sdk.req.app.infobox.BatchMultiModalReqList;
import ai.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import ai.plantdata.kgcloud.sdk.req.app.infobox.InfoboxMultiModalReq;
import ai.plantdata.kgcloud.sdk.rsp.UserApkRelationRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.main.*;
import ai.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import ai.plantdata.kgcloud.sdk.rsp.edit.DictRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.KnowledgeIndexRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 14:37
 */
@Service
public class GraphApplicationServiceImpl implements GraphApplicationService {

    @Autowired
    private ai.plantdata.kg.api.pub.GraphApi editGraphApi;
    @Autowired
    private GraphApi graphApi;
    @Autowired
    private MongoApi mongoApi;
    @Autowired
    private GraphAttrGroupService graphAttrGroupService;
    @Autowired
    private EntityApi entityApi;
    @Autowired
    private ConceptEntityApi conceptEntityApi;
    @Autowired
    private AttributeApi attributeApi;
    @Autowired
    private GraphRepository graphRepository;
    @Autowired
    private UserClient userClient;
    @Autowired
    private GraphConfFocusRepository graphConfFocusRepository;
    @Autowired
    private GraphHelperService graphHelperService;
    @Autowired
    private ConceptService conceptService;
    @Autowired
    private BasicInfoService basicInfoService;
    @Autowired
    private DomainDictService domainDictService;
    @Autowired
    private RelationApi relationApi;
    @Autowired
    private ai.plantdata.kg.api.pub.GraphApi pubGraphApi;

    @Override
    public SchemaRsp querySchema(String kgName) {
        SchemaRsp schemaRsp = new SchemaRsp();
        Optional<SchemaVO> schemaOptional = RestRespConverter.convert(graphApi.schema(KGUtil.dbName(kgName)));
        if (!schemaOptional.isPresent()) {
            return schemaRsp;
        }
        SchemaVO schemaVO = schemaOptional.get();
        schemaRsp.setAttrs(BasicConverter.listConvert(schemaVO.getAttrs(), AttrDefConverter::attrDefToAttrDefRsp));
        if (!CollectionUtils.isEmpty(schemaVO.getConcepts())) {
            schemaRsp.setKgTitle(ConceptConverter.getKgTittle(schemaVO.getConcepts()));
            schemaRsp.setTypes(ConceptConverter.voToRsp(schemaVO.getConcepts()));
        }
        List<AttrDefGroupDTO> attrDefGroupList = graphAttrGroupService.queryAllByKgName(kgName);
        schemaRsp.setAttrGroups(BasicConverter.listConvert(attrDefGroupList, AttrDefGroupConverter::dtoToRsp));
        return schemaRsp;
    }

    @Override
    public List<ObjectAttributeRsp> knowledgeRecommend(String kgName, KnowledgeRecommendReqList knowledgeRecommendReq) {
        if (CollectionUtils.isEmpty(knowledgeRecommendReq.getAllowAttrs()) && CollectionUtils.isEmpty(knowledgeRecommendReq.getAllowAttrsKey())) {
            return Collections.emptyList();
        }
        //replace attrKey
        graphHelperService.replaceByAttrKey(kgName, knowledgeRecommendReq);
        // 实体id为空时，将实体名称转成实体id
        graphHelperService.replaceKwToId(kgName, knowledgeRecommendReq);

        Optional<Map<Integer, Set<Long>>> entityAttrOpt = RestRespConverter.convert(entityApi.entityAttributesObject(KGUtil.dbName(kgName), KnowledgeRecommendConverter.reqToFrom(knowledgeRecommendReq)));
        if (!entityAttrOpt.isPresent()) {
            return Collections.emptyList();
        }
        List<Long> entityIdList = entityAttrOpt.get().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(entityIdList)) {
            return Collections.emptyList();
        }
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(KGUtil.dbName(kgName), EntityConverter.buildIdsQuery(entityIdList, true)));
        return KnowledgeRecommendConverter.voToRsp(entityAttrOpt.get(), entityOpt.orElse(Collections.emptyList()));
    }

    @Override
    public BasicConceptTreeRsp visualModels(String kgName, boolean display, Long conceptId) {
        Optional<List<BasicInfo>> conceptOpt = RestRespConverter.convert(conceptEntityApi.tree(KGUtil.dbName(kgName), conceptId));
        BasicConceptTreeRsp treeRsp = new BasicConceptTreeRsp();
        if (!conceptOpt.isPresent()) {
            return treeRsp;
        }
        String kgTittle = ConceptConverter.getKgTittle(conceptOpt.get());
        treeRsp = new BasicConceptTreeRsp(NumberUtils.LONG_ZERO, kgTittle);
        if (!display) {
            return ConceptConverter.voToConceptTree(conceptOpt.get(), treeRsp);
        }
        Optional<List<AttrDefVO>> attrDefOpt = RestRespConverter.convert(attributeApi.getAll(KGUtil.dbName(kgName)));
        return ConceptConverter.voToConceptTree(conceptOpt.get(), attrDefOpt.orElse(Collections.emptyList()), treeRsp);
    }

    @Override
    public PageRsp<ApkRsp> listAllGraph(PageReq pageReq) {
        PageRequest page = PageRequest.of(pageReq.getPage() - 1, pageReq.getSize());
        Page<Graph> all = graphRepository.findAll(page);
        if (CollectionUtils.isEmpty(all.getContent())) {
            return PageRsp.empty();
        }
        List<String> userIds = all.getContent().stream().map(Graph::getUserId).distinct().collect(Collectors.toList());
        ApiReturn<List<UserApkRelationRsp>> apkRelationList = userClient.listUserApkRelation(userIds);
        Map<String, String> userApkMap = BasicConverter.listToMapNoNull(ApiReturnConverter.convert(apkRelationList), a -> a.stream().collect(Collectors.toMap(UserApkRelationRsp::getUserId, UserApkRelationRsp::getApk, (b, c) -> b)));
        List<ApkRsp> apkRspList = BasicConverter.listToRsp(all.getContent(), a -> ApkConverter.graphRspToApkRsp(a, userApkMap.get(a.getUserId())));
        return PageRsp.success(apkRspList, all.getTotalElements());
    }

    @Override
    public GraphInitRsp initGraphExploration(String kgName, GraphInitBaseEnum graphInitType) {
        Optional<GraphConfFocus> focusOpt = graphConfFocusRepository.findByKgNameAndType(kgName, graphInitType.getValue());
        GraphInitRsp graphInitRsp = new GraphInitRsp(kgName, graphInitType);
        if (focusOpt.isPresent()) {
            Optional<GraphInitRsp> initRspOpt = GraphRspConverter.rebuildGraphInitRsp(focusOpt.get(), graphInitRsp);
            if (initRspOpt.isPresent()) {
                return initRspOpt.get();
            }
        }
        Optional<List<Long>> entityIdOpt = RestRespConverter.convert(editGraphApi.getRelationEntity(KGUtil.dbName(kgName)));
        if (!entityIdOpt.isPresent()) {
            return graphInitRsp;
        }
        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(KGUtil.dbName(kgName), EntityConverter.buildIdsQuery(entityIdOpt.get(), true)));
        if (!entityOpt.isPresent()) {
            return graphInitRsp;
        }
        graphInitRsp.setEntities(EntityConverter.entityVoToGraphInitEntityRsp(entityOpt.get()));
        return graphInitRsp;
    }


    @Override
    public List<BasicInfoVO> conceptTree(String kgName, Long conceptId, String conceptKey) {

        if (null == conceptId && StringUtils.isNotEmpty(conceptKey)) {
            List<Long> longs = graphHelperService.queryConceptByKey(kgName, Lists.newArrayList(conceptKey));
            conceptId = CollectionUtils.isEmpty(longs) ? NumberUtils.LONG_ZERO : longs.get(0);
        }
        if (conceptId == null) {
            conceptId = NumberUtils.LONG_ZERO;
        }
        return conceptService.getConceptTree(kgName, conceptId);
    }

    @Override
    public InfoBoxRsp infoBox(String kgName, String userId, InfoBoxReq infoBoxReq) {
        BatchInfoBoxReqList batchInfoBoxReq = new BatchInfoBoxReqList();
        batchInfoBoxReq.setAllowAttrs(infoBoxReq.getAllowAttrs());
        batchInfoBoxReq.setAllowAttrsKey(infoBoxReq.getAllowAttrsKey());
        batchInfoBoxReq.setIds(Lists.newArrayList(infoBoxReq.getId()));
        batchInfoBoxReq.setKws(Lists.newArrayList(infoBoxReq.getKw()));
        batchInfoBoxReq.setRelationAttrs(infoBoxReq.getRelationAttrs());
        batchInfoBoxReq.setReverseRelationAttrs(infoBoxReq.getRelationAttrs());
        List<InfoBoxRsp> list = infoBox(kgName, batchInfoBoxReq);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        InfoBoxRsp infoBoxRsp = list.get(0);
        if (infoBoxRsp.getSelf() == null) {
            return null;
        }
        List<DictRsp> dictRspList = domainDictService.listDictByEntity(kgName, infoBoxReq.getId());
        infoBoxRsp.getSelf().setDictList(dictRspList);
        return infoBoxRsp;
    }

    @Override
    public List<InfoBoxRsp> infoBox(String kgName, BatchInfoBoxReqList req) {
        //实体
        graphHelperService.replaceByAttrKey(kgName, req);
        List<InfoBoxRsp> infoBoxRspList = Lists.newArrayList();
        // 判断id是否为空，为空用实体名称查询
        graphHelperService.replaceKwToId(kgName, req);
        //实体
        BasicDetailFilter detailFilter = InfoBoxConverter.batchInfoBoxReqToBasicDetailFilter(req);
        detailFilter.setEntity(true);
        Optional<List<ai.plantdata.kg.api.edit.resp.EntityVO>> entityOpt = RestRespConverter.convert(conceptEntityApi.listByIds(KGUtil.dbName(kgName), detailFilter));
        detailFilter.setEntity(false);
        Optional<List<ai.plantdata.kg.api.edit.resp.EntityVO>> conceptOpt = RestRespConverter.convert(conceptEntityApi.listByIds(KGUtil.dbName(kgName), detailFilter));
        entityOpt.ifPresent(entityList ->
        {
            List<Long> entityIds = entityList.stream().map(ai.plantdata.kg.api.edit.resp.EntityVO::getId).collect(Collectors.toList());

            BasicConverter.consumerIfNoNull(req.getAllowAttrs(), allowAttrIds -> entityList.forEach(entity -> {
                BasicConverter.consumerIfNoNull(entity.getAttrValue(),
                        a -> a.removeIf(b -> !allowAttrIds.contains(b.getId())));
            }));

            Supplier<Map<Long, List<MultiModalRsp>>> mapSup = AsyncUtils.async(() -> basicInfoService.listMultiModels(kgName, entityIds));
            Supplier<Map<Long, List<KnowledgeIndexRsp>>> indexMapSu = AsyncUtils.async(() -> basicInfoService.listKnowledgeIndexs(kgName, entityIds));

            Map<Long, List<MultiModalRsp>> map = mapSup.get();
            Map<Long, List<KnowledgeIndexRsp>> indexMap = indexMapSu.get();
            Map<Long, List<RelationVO>> positiveMap = Maps.newHashMap();
            Map<Long, List<RelationVO>> reverseMap = Maps.newHashMap();
            fillRelation(kgName, positiveMap, reverseMap, req.getAllowAttrs(), entityIds, req.getRelationAttrs(), req.getReverseRelationAttrs());
            //填充对象属性
            BasicConverter.consumerIfNoNull(BasicConverter.listToRsp(entityList,
                    a -> InfoBoxConverter.entityToInfoBoxRsp(a, map.get(a.getId()), indexMap.get(a.getId()),
                            positiveMap.get(a.getId()), reverseMap.get(a.getId()))), infoBoxRspList::addAll);

        });
        conceptOpt.ifPresent(conceptList ->
                BasicConverter.consumerIfNoNull(BasicConverter.listToRsp(conceptList, InfoBoxConverter::conceptToInfoBoxRsp), infoBoxRspList::addAll));
        return infoBoxRspList;
    }

    public void fillRelation(String kgName, Map<Long, List<RelationVO>> positiveMap,
                             Map<Long, List<RelationVO>> reverseMap,
                             List<Integer> allowAttrs,
                             List<Long> entityIds, boolean relationAttrs, boolean reverseRelationAttrs) {
        if (!relationAttrs && !reverseRelationAttrs) {
            return;
        }
        //查询对象属性
        Optional<List<RelationVO>> relationOpt = RestRespConverter.convert(relationApi.listRelation(KGUtil.dbName(kgName), RelationConverter.buildEntityIdsQuery(entityIds)));
        if (relationOpt.isPresent()) {

            BasicConverter.consumerIfNoNull(allowAttrs, allowAttrIds -> BasicConverter.consumerIfNoNull(relationOpt.get(),
                    a -> a.removeIf(b -> !allowAttrIds.contains(b.getAttrId()))));
        }
        if (relationAttrs) {

            relationOpt.ifPresent(relations -> relations.forEach(a -> {
                positiveMap.computeIfAbsent(a.getFrom().getId(), v -> Lists.newArrayList()).add(a);
            }));
        }
        if (reverseRelationAttrs) {
            relationOpt.ifPresent(relations -> relations.forEach(a -> {
                reverseMap.computeIfAbsent(a.getTo().getId(), v -> Lists.newArrayList()).add(a);
            }));
        }
    }


    @Override
    public ComplexGraphVisualRsp complexGraphVisual(String kgName, ComplexGraphVisualReq analysisReq) {
        ComplexGraphVisualRsp visualRsp = new ComplexGraphVisualRsp();
        MongoQueryFrom queryFrom = ComplexGraphAnalysisConverter.complexGraphVisualReqReqToMongoQueryFrom(kgName, analysisReq);
        Optional<List<Map<String, Object>>> mapOpt = RestRespConverter.convert(mongoApi.postJson(queryFrom));
        if (!mapOpt.isPresent() || CollectionUtils.isEmpty(mapOpt.get())) {
            return visualRsp;
        }
        Map<Long, CoordinatesDTO> dataMap = mapOpt.get().stream()
                .map(ComplexGraphAnalysisConverter::mapToCoordinatesDTO)
                .collect(Collectors.toMap(CoordinatesDTO::getId, Function.identity()));

        Optional<List<EntityVO>> entityOpt = RestRespConverter.convert(entityApi.serviceEntity(KGUtil.dbName(kgName), EntityConverter.buildIdsQuery(Lists.newArrayList(dataMap.keySet()), true)));
        if (!entityOpt.isPresent() || CollectionUtils.isEmpty(entityOpt.get())) {
            return visualRsp;
        }
        Map<Long, BasicInfo> conceptIdMap = graphHelperService.getConceptIdMap(kgName);
        List<ComplexGraphVisualRsp.CoordinatesEntityRsp> entityRspList = BasicConverter.listConvert(entityOpt.get(), a -> ComplexGraphAnalysisConverter.entityVoToCoordinatesEntityRsp(a, conceptIdMap, dataMap.get(a.getId())));
        visualRsp.setEntityList(entityRspList);
        return visualRsp;
    }

    @Override
    public InfoboxMultiModelRsp infoBoxMultiModal(String kgName, String userId, InfoboxMultiModalReq infoboxMultiModalReq) {
        BatchMultiModalReqList batchMultiModalReq = new BatchMultiModalReqList();
        batchMultiModalReq.setIds(Lists.newArrayList(infoboxMultiModalReq.getId()));
        batchMultiModalReq.setKws(Lists.newArrayList(infoboxMultiModalReq.getKw()));
        List<InfoboxMultiModelRsp> list = listInfoBoxMultiModal(kgName, batchMultiModalReq);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        InfoboxMultiModelRsp infoBoxRsp = list.get(0);
        return infoBoxRsp;
    }

    @Override
    public List<InfoboxMultiModelRsp> listInfoBoxMultiModal(String kgName, BatchMultiModalReqList req) {

        List<InfoboxMultiModelRsp> infoboxMultiModelRspList = Lists.newArrayList();
        // 判断id是否为空，为空用实体名称查询
        graphHelperService.replaceKwToId(kgName, req);

        //实体
        BasicDetailFilter detailFilter = InfoBoxConverter.batchInfoBoxMultiModalReqToBasicDetailFilter(req);
        Optional<List<ai.plantdata.kg.api.edit.resp.EntityVO>> entityListOpt = RestRespConverter.convert(conceptEntityApi.listByIds(KGUtil.dbName(kgName), detailFilter));

        entityListOpt.ifPresent(entityList ->
        {
            List<Long> entityIds = entityList.stream().map(ai.plantdata.kg.api.edit.resp.EntityVO::getId).collect(Collectors.toList());

            Map<Long, List<MultiModalRsp>> map = basicInfoService.listMultiModels(kgName, entityIds);
            BasicConverter.consumerIfNoNull(BasicConverter.listToRsp(entityList,
                    a -> InfoBoxConverter.entityToInfoBoxMultiModelRsp(a, map.get(a.getId()))), infoboxMultiModelRspList::addAll);

        });
        return infoboxMultiModelRspList;
    }

    @Override
    public List<ObjectAttributeRsp> layerKnowledgeRecommend(String kgName, LayerKnowledgeRecommendReqList recommendParam) {

        Map<Integer, KnowledgeRecommendCommonFilterReq> recommendCommonFilterReqMap = recommendParam.getLayerFilter();
        if (recommendCommonFilterReqMap == null || recommendCommonFilterReqMap.size() != 2) {
            return null;
        }


        // 实体id为空时，将实体名称转成实体id
        graphHelperService.replaceKwToId(kgName, recommendParam);

        if (recommendParam.getEntityId() == null && org.springframework.util.StringUtils.isEmpty(recommendParam.getKw())) {
            throw BizException.of(AppErrorCodeEnum.NULL_KW_AND_ID);
        }
        if (recommendParam.getPage() == null) {
            PageReq page = new PageReq();
            page.setPage(NumberUtils.INTEGER_ONE);
            page.setSize(BaseReq.DEFAULT_SIZE);
            recommendParam.setPage(page);
        }


        GraphFrom graphFrom = new GraphFrom();
        //通用参数
        graphFrom.setId(recommendParam.getEntityId());
        graphFrom.setName(recommendParam.getKw());

        Map<Integer, CommonFilter> layerFilter = new HashMap<>();
        //每层参数
        for (Map.Entry<Integer, KnowledgeRecommendCommonFilterReq> entry : recommendCommonFilterReqMap.entrySet()) {
            KnowledgeRecommendCommonFilterReq filter = entry.getValue();

            graphHelperService.keyToId(kgName, filter);

            CommonFilter commonFilter = knowledgeRecommendCommonFilterReq2CommonFilter(filter, new CommonFilter());

            if (entry.getKey() == 2) {
                commonFilter.setSkip((recommendParam.getPage().getOffset()));
                commonFilter.setLimit(recommendParam.getPage().getSize());
            }
            layerFilter.put(entry.getKey(), commonFilter);
        }

        graphFrom.setLayerFilters(layerFilter);
        graphFrom.setDistance(2);
        System.out.println(JacksonUtils.writeValueAsString(graphFrom));

        Optional<GraphVO> graphOpt = RestRespConverter.convert(pubGraphApi.graph(KGUtil.dbName(kgName), graphFrom));
        return KnowledgeRecommendConverter.graphVOToRsp(graphOpt.get(), 2);

    }

    private CommonFilter knowledgeRecommendCommonFilterReq2CommonFilter(KnowledgeRecommendCommonFilterReq filter, CommonFilter commonFilter) {
        BeanUtils.copyProperties(filter, commonFilter);
        commonFilter.setQueryPrivate(false);
        return commonFilter;

    }
}
