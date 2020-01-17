package com.plantdata.kgcloud.domain.app.converter.graph;

import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kg.api.pub.resp.SimpleEntity;
import ai.plantdata.kg.api.pub.resp.SimpleRelation;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.dto.GraphReasoningDTO;
import com.plantdata.kgcloud.domain.app.dto.GraphRspDTO;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocus;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphReqAfterInterface;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.GraphStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:57
 */
public class GraphRspConverter extends BasicConverter {

    public static CommonBasicGraphExploreRsp fillEntityAndEntity(Map<Long, BasicInfo> conceptIdMap, GraphRspDTO graphAfter) {
        CommonBasicGraphExploreRsp exploreRsp = new CommonBasicGraphExploreRsp();
        fillEntityAndEntity(graphAfter.getGraphVo(), conceptIdMap, graphAfter.getGraphReq(), exploreRsp);
        consumerIfNoNull(graphAfter.getReasoningDTO(), a -> fillReasonEntityAndRelation(a, conceptIdMap, graphAfter.getGraphReq(), exploreRsp));
        return exploreRsp;
    }

    public static <T extends StatisticRsp> T graphVoToStatisticRsp(List<GraphStatisticRsp> statisticRspList, Map<Long, BasicInfo> conceptIdMap, T analysisRsp, GraphRspDTO graphAfter) {
        fillEntityAndEntity(graphAfter.getGraphVo(), conceptIdMap, graphAfter.getGraphReq(), analysisRsp);
        consumerIfNoNull(graphAfter.getReasoningDTO(), a -> fillReasonEntityAndRelation(a, conceptIdMap, graphAfter.getGraphReq(), analysisRsp));
        analysisRsp.setStatisticResult(statisticRspList);
        return analysisRsp;
    }

    private static <T extends BasicGraphExploreRsp, E extends GraphVO> void fillEntityAndEntity(E graph, Map<Long, BasicInfo> conceptIdMap, GraphReqAfterInterface graphAfter, T t) {
        List<CommonEntityRsp> commonEntityRspList = DefaultUtils.executeIfNoNull(graph.getEntityList(), a -> buildCommonEntityList(a, conceptIdMap, graphAfter.getReplaceClassIds()));
        List<GraphRelationRsp> relationRspList = DefaultUtils.executeIfNoNull(graph.getRelationList(), a -> GraphCommonConverter.simpleRelationToGraphRelationRsp(a, graphAfter.isRelationMerge()));
        t.setRelationList(relationRspList);
        t.setEntityList(commonEntityRspList);
        t.setHasNextPage(graph.getLevel1HasNext());
    }

    private static <T extends BasicGraphExploreRsp> void fillReasonEntityAndRelation(GraphReasoningDTO reasoningDTO, Map<Long, BasicInfo> conceptIdMap, GraphReqAfterInterface graphAfter, T rsp) {
        List<CommonEntityRsp> commonEntityRspList = DefaultUtils.executeIfNoNull(reasoningDTO.getEntityList(), a -> buildCommonEntityList(a, conceptIdMap, graphAfter.getReplaceClassIds()));
        List<GraphRelationRsp> relationRspList = DefaultUtils.executeIfNoNull(reasoningDTO.getRelationList(), a -> GraphCommonConverter.simpleRelationToGraphRelationRsp(a, graphAfter.isRelationMerge()));
        Map<Integer, Long> ruleIdMap = reasoningDTO.getRuleIdMap();
        BasicConverter.listConsumerIfNoNull(relationRspList, a -> a.setReasonRuleId(ruleIdMap.get(a.getAttId())));
        if (CollectionUtils.isEmpty(rsp.getEntityList())) {
            rsp.setEntityList(commonEntityRspList);
        } else {
            rsp.getEntityList().addAll(commonEntityRspList);
        }
        if (CollectionUtils.isEmpty(rsp.getRelationList())) {
            rsp.setRelationList(relationRspList);
        } else {
            rsp.getRelationList().addAll(relationRspList);
        }
    }

    /**
     * 路径分析 业务逻辑需要抽离
     */
    public static List<GraphStatisticRsp> buildStatisticResult(GraphVO statisticRsp, @NonNull List<BasicStatisticReq> configList) {
        Map<Long, List<SimpleEntity>> conceptIdKeyMap = listToMapNoNull(statisticRsp.getEntityList(), a -> a.stream().collect(Collectors.groupingBy(SimpleEntity::getConceptId)));
        Map<Integer, List<SimpleRelation>> attrIdKeyMap = listToMapNoNull(statisticRsp.getRelationList(), a -> a.stream().collect(Collectors.groupingBy(SimpleRelation::getAttrId)));
        List<GraphStatisticRsp> statisticRspList = Lists.newArrayList();
        for (BasicStatisticReq config : configList) {
            //拿要计算的type, 构建待计算的entityList
            GraphStatisticRsp graphStatisticRsp = new GraphStatisticRsp(config.getKey(), config.getConceptId(), config.getAttrIdList(), Collections.emptyList());
            List<SimpleEntity> entityRspList = conceptIdKeyMap.get(config.getConceptId());
            if (CollectionUtils.isEmpty(entityRspList)) {
                statisticRspList.add(graphStatisticRsp);
                continue;
            }
            List<Long> entityIds = fetchEntityIds(entityRspList, attrIdKeyMap, config);
            if (CollectionUtils.isEmpty(entityIds)) {
                statisticRspList.add(graphStatisticRsp);
                continue;
            }
            Map<Long, Long> countMap = entityIds.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            List<GraphStatisticRsp.GraphStatisticDetailRsp> detailList = countMap.entrySet().stream().map(entry -> new GraphStatisticRsp.GraphStatisticDetailRsp(entry.getKey(), entry.getValue().intValue())).collect(Collectors.toList());
            //将结果写回configBean
            graphStatisticRsp.setStatisticDetails(detailList);
            statisticRspList.add(graphStatisticRsp);
        }
        return statisticRspList;
    }

    private static List<Long> fetchEntityIds( List<SimpleEntity> entityRspList, Map<Integer, List<SimpleRelation>> attrIdKeyMap ,BasicStatisticReq config){
        Set<Long> entityIdSet = entityRspList.stream().map(SimpleEntity::getId).collect(Collectors.toSet());
        //在要计算的entityList中计算匹配的关系
        List<Long> entityIds = Lists.newArrayList();
        config.getAttrIdList().stream().filter(attrIdKeyMap::containsKey).forEach(a -> {
            List<SimpleRelation> relationRspList = attrIdKeyMap.get(a);
            relationRspList.forEach(rsp -> {
                if (entityIdSet.contains(rsp.getFrom())) {
                    entityIds.add(rsp.getFrom());
                }
                if (entityIdSet.contains(rsp.getTo())) {
                    entityIds.add(rsp.getTo());
                }
            });
        });
        return entityIds;
    }

    public static Optional<GraphInitRsp> rebuildGraphInitRsp(GraphConfFocus initGraphBean, GraphInitRsp graphInitRsp) {
        graphInitRsp.setConfig(JacksonUtils.readValue(initGraphBean.getFocusConfig(), new TypeReference<Map<String, Object>>() {
        }));
        graphInitRsp.setUpdateTime(graphInitRsp.getUpdateTime());
        graphInitRsp.setCreateTime(initGraphBean.getCreateAt());
        if (initGraphBean.getEntities() != null && initGraphBean.getEntities().size() > 0) {
            List<GraphInitRsp.GraphInitEntityRsp> entityRspList = Lists.newArrayList();
            initGraphBean.getEntities().forEach(a -> {
                GraphInitRsp.GraphInitEntityRsp entityRsp = new GraphInitRsp.GraphInitEntityRsp();
                consumerIfNoNull(a.findValue("id"), b -> entityRsp.setId(b.asLong()));
                consumerIfNoNull(a.findValue("conceptId"), b -> entityRsp.setClassId(b.asLong()));
                consumerIfNoNull(a.findValue("name"), b -> entityRsp.setName(b.asText()));
                entityRspList.add(entityRsp);
            });
            graphInitRsp.setEntities(entityRspList);
            return Optional.of(graphInitRsp);
        }
        return Optional.empty();
    }

    private static List<CommonEntityRsp> buildCommonEntityList(@NonNull List<SimpleEntity> simpleEntityList, Map<Long, BasicInfo> conceptMap, List<Long> replaceClassIds) {
        Set<Long> replaceClassIdsSet = listToSetNoNull(replaceClassIds, Sets::newHashSet);
        return simpleEntityList.stream().map(a -> GraphCommonConverter.simpleToGraphEntityRsp(new CommonEntityRsp(), a, conceptMap, replaceClassIdsSet)).collect(Collectors.toList());
    }


}
