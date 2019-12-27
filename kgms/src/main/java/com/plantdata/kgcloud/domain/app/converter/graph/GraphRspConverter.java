package com.plantdata.kgcloud.domain.app.converter.graph;

import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kg.api.pub.resp.SimpleEntity;
import ai.plantdata.kg.api.pub.resp.SimpleRelation;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocus;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphReqAfterInterface;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.GraphStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:57
 */
public class GraphRspConverter extends BasicConverter {

    public static CommonBasicGraphExploreRsp graphVoToCommonRsp(GraphVO graph, Map<Long, BasicInfo> conceptIdMap, GraphReqAfterInterface graphAfter) {
        List<CommonEntityRsp> commonEntityRspList = DefaultUtils.executeIfNoNull(graph.getEntityList(), a -> buildCommonEntityList(a, conceptIdMap, graphAfter.getReplaceClassIds()));
        List<GraphRelationRsp> relationRspList = DefaultUtils.executeIfNoNull(graph.getRelationList(), a -> GraphCommonConverter.simpleRelationToGraphRelationRsp(a, graphAfter.isRelationMerge()));
        return new CommonBasicGraphExploreRsp(relationRspList, graph.getLevel1HasNext(), commonEntityRspList);
    }

    public static <T extends StatisticRsp> T graphVoToStatisticRsp(GraphVO graph, List<GraphStatisticRsp> statisticRspList, Map<Long, BasicInfo> conceptIdMap, T analysisRsp, GraphReqAfterInterface graphAfter) {
        List<CommonEntityRsp> commonEntityRspList = DefaultUtils.executeIfNoNull(graph.getEntityList(), a -> buildCommonEntityList(a, conceptIdMap, graphAfter.getReplaceClassIds()));
        List<GraphRelationRsp> relationRspList = DefaultUtils.executeIfNoNull(graph.getRelationList(), a -> GraphCommonConverter.simpleRelationToGraphRelationRsp(a, graphAfter.isRelationMerge()));
        analysisRsp.setEntityList(commonEntityRspList);
        analysisRsp.setHasNextPage(NumberUtils.INTEGER_ONE);
        analysisRsp.setRelationList(relationRspList);
        analysisRsp.setStatisticResult(statisticRspList);
        return analysisRsp;
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
            List<SimpleEntity> entityRspList = conceptIdKeyMap.get(config.getConceptId());
            if (CollectionUtils.isEmpty(entityRspList)) {
                continue;
            }
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
            if (CollectionUtils.isEmpty(entityIds)) {
                continue;
            }
            Map<Long, Long> countMap = entityIds.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            List<GraphStatisticRsp.GraphStatisticDetailRsp> detailList = countMap.entrySet().stream().map(entry -> new GraphStatisticRsp.GraphStatisticDetailRsp(entry.getKey(), entry.getValue().intValue())).collect(Collectors.toList());
            //将结果写回configBean
            statisticRspList.add(new GraphStatisticRsp(config.getKey(), config.getConceptId(), config.getAttrIdList(), detailList));
        }
        return statisticRspList;
    }

    public static Optional<GraphInitRsp> rebuildGraphInitRsp(GraphConfFocus initGraphBean, GraphInitRsp graphInitRsp) {
        graphInitRsp.setConfig(JacksonUtils.readValue(initGraphBean.getFocusConfig(), new TypeReference<Map<String, Object>>() {
        }));
        graphInitRsp.setCreateTime(initGraphBean.getCreateAt());
        if (initGraphBean.getEntities() != null && initGraphBean.getEntities().fieldNames().hasNext()) {
            graphInitRsp.setEntities(JacksonUtils.readValue(JacksonUtils.writeValueAsString(initGraphBean.getEntities()), new TypeReference<List<GraphInitRsp.GraphInitEntityRsp>>() {
            }));
            return Optional.of(graphInitRsp);
        }
        return Optional.empty();
    }

    private static List<CommonEntityRsp> buildCommonEntityList(@NonNull List<SimpleEntity> simpleEntityList, Map<Long, BasicInfo> conceptMap, List<Long> replaceClassIds) {
        Set<Long> replaceClassIdsSet = listToSetNoNull(replaceClassIds, Sets::newHashSet);
        return simpleEntityList.stream().map(a -> GraphCommonConverter.simpleToGraphEntityRsp(new CommonEntityRsp(), a, conceptMap, replaceClassIdsSet)).collect(Collectors.toList());
    }


}
