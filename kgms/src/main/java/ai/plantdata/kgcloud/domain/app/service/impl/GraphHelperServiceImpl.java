package ai.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.MongoApi;
import ai.plantdata.kg.api.pub.RelationApi;
import ai.plantdata.kg.api.pub.SchemaApi;
import ai.plantdata.kg.api.pub.req.FilterRelationFrom;
import ai.plantdata.kg.api.pub.req.MongoQueryFrom;
import ai.plantdata.kg.common.bean.BasicInfo;
import ai.plantdata.kgcloud.constant.AppErrorCodeEnum;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.app.converter.ConditionConverter;
import ai.plantdata.kgcloud.domain.app.converter.EntityConverter;
import ai.plantdata.kgcloud.domain.app.converter.InfoBoxConverter;
import ai.plantdata.kgcloud.domain.app.converter.graph.GraphRspConverter;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.sdk.req.app.function.*;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import ai.plantdata.kgcloud.domain.app.bo.GraphCommonBO;
import ai.plantdata.kgcloud.domain.app.dto.GraphRspDTO;
import ai.plantdata.kgcloud.domain.app.service.GraphHelperService;
import ai.plantdata.kgcloud.domain.app.util.DefaultUtils;
import ai.plantdata.kgcloud.domain.app.util.JsonUtils;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroupDetails;
import ai.plantdata.kgcloud.domain.graph.attr.repository.GraphAttrGroupDetailsRepository;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.GraphStatisticRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:33
 */
@Service
@Slf4j
public class GraphHelperServiceImpl implements GraphHelperService {
    @Autowired
    private GraphAttrGroupDetailsRepository graphAttrGroupDetailsRepository;
    @Autowired
    private SchemaApi schemaApi;
    @Autowired
    private GraphHelperService graphHelperService;
    @Autowired
    private ConceptEntityApi conceptEntityApi;
    @Autowired
    private RelationApi relationApi;
    @Autowired
    private EntityApi entityApi;
    @Autowired
    private MongoApi mongoApi;


    @Override
    public Map<Long, BasicInfo> getConceptIdMap(String kgName) {
        Optional<List<BasicInfo>> treeOpt = RestRespConverter.convert(conceptEntityApi.tree(KGUtil.dbName(kgName), NumberUtils.LONG_ZERO));
        return treeOpt.map(basicInfos -> basicInfos.stream().collect(Collectors.toMap(BasicInfo::getId, Function.identity()))).orElse(Collections.emptyMap());
    }

    @Override
    public <T extends StatisticRsp> T buildExploreRspWithStatistic(String kgName, List<BasicStatisticReq> configList, T pathAnalysisRsp, GraphRspDTO graphAfter) {
        //统计
        Supplier<Map<Long, BasicInfo>> conceptIdMapSup = () -> graphHelperService.getConceptIdMap(kgName);
        List<GraphStatisticRsp> statisticRspList = CollectionUtils.isEmpty(configList)
                ? Collections.emptyList()
                : GraphRspConverter.buildStatisticResult(graphAfter.getGraphVo(), configList);
        //组装结果
        return GraphRspConverter.graphVoToStatisticRsp(statisticRspList, conceptIdMapSup.get(), pathAnalysisRsp, graphAfter);
    }

    @Override
    public <T extends BasicGraphExploreReqList> T keyToId(String kgName, T exploreReq) {

        //replace attrKey
        replaceByAttrKey(kgName, exploreReq);
        //replace conceptKey
        replaceByConceptKey(kgName, exploreReq);

        if (CollectionUtils.isEmpty(exploreReq.getReplaceClassIds()) && !CollectionUtils.isEmpty(exploreReq.getReplaceClassKeys())) {
            exploreReq.setReplaceClassIds(queryConceptByKey(kgName, exploreReq.getReplaceClassKeys()));
        }
        //replace attrGroupIds->attrIds
        if (!CollectionUtils.isEmpty(exploreReq.getAllowAttrGroups())) {
            List<GraphAttrGroupDetails> detailsList = graphAttrGroupDetailsRepository.findAllByGroupIdIn(exploreReq.getAllowAttrGroups());
            List<Integer> attrDefIds = detailsList.stream().map(GraphAttrGroupDetails::getAttrId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(exploreReq.getAllowAttrs())) {
                exploreReq.setAllowAttrs(attrDefIds);
            } else {
                exploreReq.getAllowAttrs().addAll(attrDefIds);
            }
        }
        return exploreReq;

    }


    /**
     * 后置筛选
     *
     * @return .
     */
    @Override
    public <T extends BasicGraphExploreRsp> Optional<T> graphSearchBefore(String kgName, SecondaryScreeningInterface req, T rsp) {
        if (req.getGraphReq() == null) {
            return Optional.empty();
        }
        BasicGraphExploreRsp graphReq = req.getGraphReq();
        if (graphReq.getEntityList() == null && graphReq.getRelationList() == null) {
            return Optional.empty();
        }
        rsp.setEntityList(req.getGraphReq().getEntityList());
        rsp.setRelationList(req.getGraphReq().getRelationList());

        FilterRelationFrom relationFrom = InfoBoxConverter.reqToFilterRelationFrom(rsp, req.getEdgeAttrFilters(), req.getReservedEdgeAttrFilters());

        //关系筛选
        List<GraphRelationRsp> relationList = rsp.getRelationList();
        if (!CollectionUtils.isEmpty(relationFrom.getRelationAttrFilters()) || !CollectionUtils.isEmpty(relationFrom.getMetaFilters())) {
            Optional<List<String>> relationOpt = RestRespConverter.convert(relationApi.filterRelation(KGUtil.dbName(kgName), relationFrom));
            Set<String> relationIds = !relationOpt.isPresent() ? Collections.emptySet() : Sets.newHashSet(relationOpt.get());
            rsp.setRelationList(relationList.stream().filter(a -> relationIds.contains(a.getId())).collect(Collectors.toList()));
        }
        //实体筛选
        Map<String, Object> queryMaps = ConditionConverter.entityListToMap(req.getEntityFilters());
        BasicConverter.consumerIfNoNull(queryMaps, a -> BasicConverter.consumerIfNoNull(rsp.getEntityList(), entityList -> {
            List<Long> entityIds = BasicConverter.listConvert(entityList, CommonEntityRsp::getId);
            Optional<List<Long>> entityIdOpt = RestRespConverter.convert(entityApi.filterIds(KGUtil.dbName(kgName), EntityConverter.buildEntityFilterFrom(entityIds, queryMaps)));
            Set<Long> entityIdSet = !entityIdOpt.isPresent() ? Collections.emptySet() : Sets.newHashSet(entityIdOpt.get());
            log.debug("NeedSaveEntityIds:{},entityIdSet:{}", JsonUtils.objToJson(req.fetchNeedSaveEntityIds()), JsonUtils.objToJson(entityIdSet));
            entityList.removeIf(b -> !req.fetchNeedSaveEntityIds().contains(b.getId()) && !entityIdSet.contains(b.getId()));
        }));
        GraphCommonBO.rebuildGraphRelationAndEntity(rsp, req.fetchNeedSaveEntityIds());
        return Optional.of(rsp);

    }

    @Override
    public void replaceByConceptKey(String kgName, ConceptKeyListReqInterface conceptKeyReq) {
        if (!CollectionUtils.isEmpty(conceptKeyReq.getAllowConcepts()) || CollectionUtils.isEmpty(conceptKeyReq.getAllowConceptsKey())) {
            return;
        }
        conceptKeyReq.setAllowConcepts(queryConceptByKey(kgName, conceptKeyReq.getAllowConceptsKey()));
    }

    @Override
    public void replaceByConceptKey(String kgName, ConceptKeyReqInterface conceptKeyReq) {
        if (conceptKeyReq.getConceptId() != null || conceptKeyReq.getConceptKey() == null) {
            return;
        }
        List<Long> ids = queryConceptByKey(kgName, Lists.newArrayList(conceptKeyReq.getConceptKey()));
        BasicConverter.consumerIfNoNull(ids, a -> conceptKeyReq.setConceptId(a.get(NumberUtils.INTEGER_ZERO)));
    }

    @Override
    public List<Long> queryConceptByKey(String kgName, List<String> keyList) {
        Optional<Map<String, Long>> keyConvertOpt = RestRespConverter.convert(schemaApi.getConceptIdByKey(KGUtil.dbName(kgName), keyList));
        if (!keyConvertOpt.isPresent() || CollectionUtils.isEmpty(keyConvertOpt.get())) {
            return Collections.emptyList();
        }
        return Lists.newArrayList(keyConvertOpt.get().values());
    }

    @Override
    public void replaceByAttrKey(String kgName, AttrDefListKeyReqInterface attrDefKeyReq) {
        if (!CollectionUtils.isEmpty(attrDefKeyReq.getAllowAttrs()) || CollectionUtils.isEmpty(attrDefKeyReq.getAllowAttrsKey())) {
            return;
        }
        Optional<Map<String, Integer>> keyConvertOpt = RestRespConverter.convert(schemaApi.getAttrIdByKey(KGUtil.dbName(kgName), attrDefKeyReq.getAllowAttrsKey()));
        if (!keyConvertOpt.isPresent() || CollectionUtils.isEmpty(keyConvertOpt.get())) {
            throw BizException.of(AppErrorCodeEnum.ATTR_DEF_NOT_FOUNT);
        }
        attrDefKeyReq.setAllowAttrs(Lists.newArrayList(keyConvertOpt.get().values()));
    }

    @Override
    public void replaceByAttrKey(String kgName, AttrDefKeyReqInterface attrDefKeyReq, boolean requireAny) {
        if (requireAny && StringUtils.isEmpty(attrDefKeyReq.getAttrDefKey()) && attrDefKeyReq.getAttrDefId() == null) {
            throw BizException.of(AppErrorCodeEnum.ATTR_DEF_ANY_NO_NULL);
        }
        if (StringUtils.isEmpty(attrDefKeyReq.getAttrDefKey()) || attrDefKeyReq.getAttrDefId() != null) {
            return;
        }
        Optional<Map<String, Integer>> keyConvertOpt = RestRespConverter.convert(schemaApi.getAttrIdByKey(KGUtil.dbName(kgName), Lists.newArrayList(attrDefKeyReq.getAttrDefKey())));
        if (!keyConvertOpt.isPresent()) {
            return;
        }
        attrDefKeyReq.setAttrDefId(keyConvertOpt.get().get(attrDefKeyReq.getAttrDefKey()));
    }

    @Override
    public void replaceEntityNameToId(String kgName, BatchEntityName2Id req) {
        if (!CollectionUtils.isEmpty(req.getIds()) || CollectionUtils.isEmpty(req.getKws())) {
            return;
        }
        List<Long> entityIds = searchIdsByEntityName(kgName, req.getKws());
        if (!CollectionUtils.isEmpty(entityIds)) {
            req.setIds(entityIds);
        }
    }

    @Override
    public void replaceEntityNameToId(String kgName, EntityName2Id req) {
        if (req.getEntityId() == null) {
            String kw = req.getKw();
            if (StringUtils.isBlank(kw)) {
                return;
            }
            List<Long> entityIds = searchIdsByEntityName(kgName, Lists.newArrayList(kw));
            req.setEntityId(entityIds.get(0));
        }
    }

    private List<Long> searchIdsByEntityName(String kgName, List<String> nameList) {
        return nameList.stream()
                .flatMap(name -> {
                    MongoQueryFrom query = new MongoQueryFrom();
                    query.setKgName(KGUtil.dbName(kgName));
                    query.setCollection("basic_info");
                    List<Map<String, Object>> mathMapList = Lists.newArrayList();
                    mathMapList.add(DefaultUtils.oneElMap("$match", DefaultUtils.oneElMap("name", name)));
                    query.setQuery(mathMapList);
                    RestResp<List<Map<String, Object>>> listRestResp = mongoApi.postJson(query);
                    Optional<List<Map<String, Object>>> opt = RestRespConverter.convert(listRestResp);

                    return opt.map(a -> a.stream().map(s -> Long.valueOf(s.get("id").toString()))).orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}
