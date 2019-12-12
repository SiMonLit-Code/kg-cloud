package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.RelationApi;
import ai.plantdata.kg.api.pub.SchemaApi;
import ai.plantdata.kg.api.pub.req.FilterRelationFrom;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.domain.app.bo.GraphCommonBO;
import com.plantdata.kgcloud.domain.app.converter.ConditionConverter;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.converter.InfoBoxConverter;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphRspConverter;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroupDetails;
import com.plantdata.kgcloud.domain.graph.attr.repository.GraphAttrGroupDetailsRepository;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
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

import java.util.ArrayList;
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
        Optional<List<BasicInfo>> treeOpt = RestRespConverter.convert(conceptEntityApi.tree(kgName, NumberUtils.LONG_ZERO));
        return treeOpt.map(basicInfos -> basicInfos.stream().collect(Collectors.toMap(BasicInfo::getId, Function.identity()))).orElse(Collections.emptyMap());
    }

    @Override
    public <T extends StatisticRsp> T buildExploreRspWithStatistic(String kgName, List<BasicStatisticReq> configList, GraphVO graphVO, T pathAnalysisRsp, boolean relationMerge) {
        //统计
        List<GraphStatisticRsp> statisticRspList = CollectionUtils.isEmpty(configList) ? Collections.emptyList() : GraphRspConverter.buildStatisticResult(graphVO, configList);
        //组装结果
        Map<Long, BasicInfo> conceptIdMap = graphHelperService.getConceptIdMap(kgName);
        return GraphRspConverter.graphVoToStatisticRsp(graphVO, statisticRspList, conceptIdMap, pathAnalysisRsp, relationMerge);
    }

    @Override
    public <T extends BasicGraphExploreReq> T keyToId(String kgName, T exploreReq) {

        //replace attrKey
        if (CollectionUtils.isEmpty(exploreReq.getAllowAttrs()) && !CollectionUtils.isEmpty(exploreReq.getAllowAttrsKey())) {
            exploreReq.setAllowAttrs(replaceByAttrKey(kgName, exploreReq.getAllowAttrsKey()));
        }
        //replace conceptKey
        if (CollectionUtils.isEmpty(exploreReq.getAllowConceptsKey()) && !CollectionUtils.isEmpty(exploreReq.getAllowConceptsKey())) {
            exploreReq.setAllowConcepts(replaceByConceptKey(kgName, exploreReq.getAllowConceptsKey()));
        }
        if (CollectionUtils.isEmpty(exploreReq.getReplaceClassIds()) && !CollectionUtils.isEmpty(exploreReq.getReplaceClassKeys())) {
            exploreReq.setReplaceClassIds(replaceByConceptKey(kgName, exploreReq.getReplaceClassKeys()));
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
        if (rsp.getEntityList() == null && rsp.getRelationList() == null) {
            return Optional.empty();
        }
        rsp.setEntityList(req.getGraphReq().getEntityList());
        rsp.setRelationList(req.getGraphReq().getRelationList());

        FilterRelationFrom relationFrom = InfoBoxConverter.reqToFilterRelationFrom(rsp, req.getEdgeAttrFilters(), req.getReservedEdgeAttrFilters());

        //关系筛选
        List<GraphRelationRsp> relationList = rsp.getRelationList();
        if (!relationFrom.getRelationAttrFilters().isEmpty() || !relationFrom.getMetaFilters().isEmpty()) {
            Optional<List<String>> relationOpt = RestRespConverter.convert(relationApi.filterRelation(kgName, relationFrom));
            Set<String> relationIds = !relationOpt.isPresent() ? Collections.emptySet() : Sets.newHashSet(relationOpt.get());
            rsp.setRelationList(relationList.stream().filter(a -> relationIds.contains(a.getId())).collect(Collectors.toList()));
        }
        //实体筛选
        List<CommonEntityRsp> entity = rsp.getEntityList();
        Map<String, Object> queryMaps = ConditionConverter.entityListToMap(req.getEntityFilters());
        if (!queryMaps.isEmpty()) {
            List<Long> entityIds = entity.stream().map(CommonEntityRsp::getId).collect(Collectors.toList());
            Optional<List<Long>> entityIdOpt = RestRespConverter.convert(entityApi.filterIds(kgName, EntityConverter.buildEntityFilterFrom(entityIds, queryMaps)));
            Set<Long> entityIdSet = !entityIdOpt.isPresent() ? Collections.emptySet() : Sets.newHashSet(entityIdOpt.get());
            rsp.setEntityList(entity.stream().filter(a -> entityIdSet.contains(a.getId())).collect(Collectors.toList()));
        }
        GraphCommonBO.rebuildGraphRelationAndEntity(rsp, req.getNeedSaveEntityIds());
        return Optional.of(rsp);

    }

    @Override
    public List<Long> replaceByConceptKey(String kgName, List<String> keys) {
        Optional<Map<String, Long>> keyConvertOpt = RestRespConverter.convert(schemaApi.getConceptIdByKey(kgName, keys));
        if (!keyConvertOpt.isPresent()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(keyConvertOpt.orElse(Collections.emptyMap()).values());
    }

    @Override
    public List<Integer> replaceByAttrKey(String kgName, List<String> keys) {
        Optional<Map<String, Integer>> keyConvertOpt = RestRespConverter.convert(schemaApi.getAttrIdByKey(kgName, keys));
        if (!keyConvertOpt.isPresent()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(keyConvertOpt.orElse(Collections.emptyMap()).values());
    }

}
