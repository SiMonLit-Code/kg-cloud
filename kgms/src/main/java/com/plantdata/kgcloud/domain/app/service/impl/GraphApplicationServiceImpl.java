package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.AttributeApi;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.req.BasicDetailFilter;
import ai.plantdata.kg.api.edit.req.RelationListFrom;
import ai.plantdata.kg.api.edit.resp.AttrDefVO;
import ai.plantdata.kg.api.edit.resp.SchemaVO;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.MongoApi;
import ai.plantdata.kg.api.pub.RelationApi;
import ai.plantdata.kg.api.pub.req.MongoQueryFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.RelationVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.converter.*;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphRspConverter;
import com.plantdata.kgcloud.domain.app.dto.CoordinatesDTO;
import com.plantdata.kgcloud.domain.app.service.DataSetSearchService;
import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.common.converter.ApiReturnConverter;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicReq;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.ConceptService;
import com.plantdata.kgcloud.domain.graph.attr.dto.AttrDefGroupDTO;
import com.plantdata.kgcloud.domain.graph.attr.service.GraphAttrGroupService;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocus;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfFocusRepository;
import com.plantdata.kgcloud.domain.graph.manage.entity.Graph;
import com.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.sdk.constant.GraphInitBaseEnum;
import com.plantdata.kgcloud.sdk.req.app.ComplexGraphVisualReq;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReqList;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.req.app.dataset.PageReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReqList;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.UserApkRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import com.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.DataLinkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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
    private DataSetSearchService dataSetSearchService;
    @Autowired
    private BasicInfoService basicInfoService;
    @Autowired
    private RelationApi relationApi;

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
    public InfoBoxRsp infoBox(String kgName, String userId, InfoBoxReq infoBoxReq) throws IOException {
        BatchInfoBoxReqList batchInfoBoxReq = new BatchInfoBoxReqList();
        batchInfoBoxReq.setAllowAttrs(infoBoxReq.getAllowAttrs());
        batchInfoBoxReq.setAllowAttrsKey(infoBoxReq.getAllowAttrsKey());
        batchInfoBoxReq.setIds(Lists.newArrayList(infoBoxReq.getId()));
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
        List<DataLinkRsp> dataLinks = dataSetSearchService.getDataLinks(kgName, userId, infoBoxRsp.getSelf().getId());
        infoBoxRsp.getSelf().setDataLinks(dataLinks);
        return infoBoxRsp;
    }

    @Override
    public List<InfoBoxRsp> infoBox(String kgName, BatchInfoBoxReqList req) {
        //实体
        graphHelperService.replaceByAttrKey(kgName, req);
        List<InfoBoxRsp> infoBoxRspList = Lists.newArrayList();
        //实体
        BasicDetailFilter detailFilter = InfoBoxConverter.batchInfoBoxReqToBasicDetailFilter(req);
        detailFilter.setEntity(true);
        //概念
        Optional<List<ai.plantdata.kg.api.edit.resp.EntityVO>> entityListOpt = RestRespConverter.convert(conceptEntityApi.listByIds(KGUtil.dbName(kgName), detailFilter));
        if(entityListOpt.isPresent() && req.getRelationAttrs()) {
            RelationListFrom relationListFrom = new RelationListFrom();
            relationListFrom.setEntityId(req.getIds().get(0));
            BasicReq basicReq = new BasicReq();
            basicReq.setId(req.getIds().get(0));
            basicReq.setIsEntity(true);
            relationListFrom.setConceptId(basicInfoService.getDetails(kgName,basicReq).getConceptId());
            entityListOpt.get().get(0).setAttrValue(conceptEntityApi.relationList(kgName,relationListFrom).getData());
        }

        detailFilter.setEntity(false);
        Optional<List<ai.plantdata.kg.api.edit.resp.EntityVO>> conceptListOpt = RestRespConverter.convert(conceptEntityApi.listByIds(KGUtil.dbName(kgName), detailFilter));

        entityListOpt.ifPresent(entityList ->
        {
            List<Long> entityIds = entityList.stream().map(ai.plantdata.kg.api.edit.resp.EntityVO::getId).collect(Collectors.toList());

            BasicConverter.consumerIfNoNull(req.getAllowAttrs(), allowAttrIds -> entityList.forEach(entity -> {
                BasicConverter.consumerIfNoNull(entity.getAttrValue(),
                        a -> a.removeIf(b -> !allowAttrIds.contains(b.getId())));
            }));
            //查询对象属性
            Optional<List<RelationVO>> relationOpt = RestRespConverter.convert(relationApi.listRelation(KGUtil.dbName(kgName), RelationConverter.buildEntityIdsQuery(entityIds)));
            Map<Long, List<RelationVO>> positiveMap = Maps.newHashMap();
            Map<Long, List<RelationVO>> reverseMap = Maps.newHashMap();
            if (req.getRelationAttrs()) {
                relationOpt.ifPresent(relations -> relations.forEach(a -> {
                    positiveMap.computeIfAbsent(a.getFrom().getId(), v -> Lists.newArrayList()).add(a);
                }));
            }
            if (req.getReverseRelationAttrs()) {
                relationOpt.ifPresent(relations -> relations.forEach(a -> {
                    reverseMap.computeIfAbsent(a.getTo().getId(), v -> Lists.newArrayList()).add(a);
                }));
            }
            BasicConverter.consumerIfNoNull(BasicConverter.listToRsp(entityList,
                    a -> InfoBoxConverter.entityToInfoBoxRsp(a,
                            positiveMap.get(a.getId()), reverseMap.get(a.getId()))), infoBoxRspList::addAll);

        });
        conceptListOpt.ifPresent(conceptList ->
                BasicConverter.consumerIfNoNull(BasicConverter.listToRsp(conceptList, InfoBoxConverter::conceptToInfoBoxRsp), infoBoxRspList::addAll));
        return infoBoxRspList;
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
}
