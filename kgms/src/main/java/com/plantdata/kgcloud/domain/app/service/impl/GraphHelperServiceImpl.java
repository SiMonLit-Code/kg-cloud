package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.RelationApi;
import ai.plantdata.kg.api.pub.SchemaApi;
import ai.plantdata.kg.api.pub.req.FilterRelationFrom;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.domain.app.bo.GraphCommonBO;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.ConditionConverter;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.converter.InfoBoxConverter;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphRspConverter;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroupDetails;
import com.plantdata.kgcloud.domain.graph.attr.repository.GraphAttrGroupDetailsRepository;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.function.AttrDefKeyReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.ConceptKeyListReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.ConceptKeyReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.GraphReqAfterInterface;
import com.plantdata.kgcloud.sdk.req.app.function.SecondaryScreeningInterface;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.GraphStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
 * @date 2019/12/2 16:33
 */
@Service
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


    @Override
    public Map<Long, BasicInfo> getConceptIdMap(String kgName) {
        Optional<List<BasicInfo>> treeOpt = RestRespConverter.convert(conceptEntityApi.tree(KGUtil.dbName(kgName), NumberUtils.LONG_ZERO));
        return treeOpt.map(basicInfos -> basicInfos.stream().collect(Collectors.toMap(BasicInfo::getId, Function.identity()))).orElse(Collections.emptyMap());
    }

    @Override
    public <T extends StatisticRsp> T buildExploreRspWithStatistic(String kgName, List<BasicStatisticReq> configList, GraphVO graphVO, T pathAnalysisRsp, GraphReqAfterInterface graphAfter) {
        //统计
        List<GraphStatisticRsp> statisticRspList = CollectionUtils.isEmpty(configList) ? Collections.emptyList() : GraphRspConverter.buildStatisticResult(graphVO, configList);
        //组装结果
        Map<Long, BasicInfo> conceptIdMap = graphHelperService.getConceptIdMap(KGUtil.dbName(kgName));
        return GraphRspConverter.graphVoToStatisticRsp(graphVO, statisticRspList, conceptIdMap, pathAnalysisRsp, graphAfter);
    }

    @Override
    public <T extends BasicGraphExploreReq> T keyToId(String kgName, T exploreReq) {

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
     * @return
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
        List<CommonEntityRsp> entity = rsp.getEntityList();
        Map<String, Object> queryMaps = ConditionConverter.entityListToMap(req.getEntityFilters());
        if (!queryMaps.isEmpty()) {
            List<Long> entityIds = entity.stream().map(CommonEntityRsp::getId).collect(Collectors.toList());
            Optional<List<Long>> entityIdOpt = RestRespConverter.convert(entityApi.filterIds(KGUtil.dbName(kgName), EntityConverter.buildEntityFilterFrom(entityIds, queryMaps)));
            Set<Long> entityIdSet = !entityIdOpt.isPresent() ? Collections.emptySet() : Sets.newHashSet(entityIdOpt.get());
            rsp.setEntityList(entity.stream().filter(a -> entityIdSet.contains(a.getId())).collect(Collectors.toList()));
        }
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
    public void replaceByAttrKey(String kgName, AttrDefKeyReqInterface attrDefKeyReq) {
        if (!CollectionUtils.isEmpty(attrDefKeyReq.getAllowAttrs()) || CollectionUtils.isEmpty(attrDefKeyReq.getAllowAttrsKey())) {
            return;
        }
        Optional<Map<String, Integer>> keyConvertOpt = RestRespConverter.convert(schemaApi.getAttrIdByKey(KGUtil.dbName(kgName), attrDefKeyReq.getAllowAttrsKey()));
        if (!keyConvertOpt.isPresent()) {
            return;
        }
        attrDefKeyReq.setAllowAttrs(Lists.newArrayList(keyConvertOpt.get().values()));
    }

}
