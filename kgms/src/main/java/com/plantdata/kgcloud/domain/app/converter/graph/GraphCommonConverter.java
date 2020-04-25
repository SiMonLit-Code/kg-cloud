package com.plantdata.kgcloud.domain.app.converter.graph;

import ai.plantdata.kg.api.pub.req.CommonFilter;
import ai.plantdata.kg.api.pub.req.EntityFilter;
import ai.plantdata.kg.api.pub.req.GraphFrom;
import ai.plantdata.kg.api.pub.req.MetaData;
import ai.plantdata.kg.api.pub.resp.EdgeVO;
import ai.plantdata.kg.api.pub.resp.SimpleEntity;
import ai.plantdata.kg.api.pub.resp.SimpleRelation;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.ConceptConverter;
import com.plantdata.kgcloud.domain.app.converter.ConditionConverter;
import com.plantdata.kgcloud.domain.app.converter.MetaConverter;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReqList;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:43
 */
@Slf4j
public class GraphCommonConverter extends BasicConverter {

    /**
     * 图探索填充概念信息
     *
     * @param conceptId  实体概念id
     * @param entityRsp  实体
     * @param conceptMap 概念map
     * @param <T>        填充后对象
     */
    public static <T extends GraphEntityRsp> void fillConcept(Long conceptId, T entityRsp, Map<Long, BasicInfo> conceptMap) {
        BasicInfo concept = conceptMap.get(conceptId);
        BasicInfo topConcept = ConceptConverter.getTopConcept(conceptId, conceptMap);
        if (concept == null || topConcept == null) {
            log.warn("conceptId:{}概念不存在", conceptId);
        }
        consumerIfNoNull(concept, a -> {
            entityRsp.setConceptName(a.getName());
            entityRsp.setConceptIdList(ConceptConverter.getAllParentConceptId(Lists.newArrayList(conceptId), conceptId, conceptMap));
        });
        entityRsp.setClassId(topConcept == null ? NumberUtils.LONG_ZERO : topConcept.getId());
    }

    /**
     * 填充基础参数
     *
     * @param exploreReq req
     * @param graphFrom  remote 参数
     * @param <T>        子类
     * @param <E>        子类
     * @return 。。。
     */
    static <T extends BasicGraphExploreReqList, E extends CommonFilter> E basicReqToRemote(T exploreReq, E graphFrom) {
        CommonFilter highLevelFilter = new GraphFrom();
        consumerIfNoNull(exploreReq.getEntityFilters(), a -> {
            EntityFilter entityFilter = new EntityFilter();
            entityFilter.setAttr(ConditionConverter.entityListToIntegerKeyMap(a));
            highLevelFilter.setEntityFilter(entityFilter);
        });
        //设置边属性筛选
        consumerIfNoNull(exploreReq.getEdgeAttrFilters(), a -> highLevelFilter.setEdgeFilter(Maps.newHashMap(ConditionConverter.relationAttrReqToMap(a))));
        consumerIfNoNull(exploreReq.getAllowAttrs(), highLevelFilter::setAllowAttrs);
        consumerIfNoNull(exploreReq.getAllowConcepts(), highLevelFilter::setAllowTypes);
        consumerIfNoNull(exploreReq.getDisAllowConcepts(), highLevelFilter::setDisAllowTypes);

        highLevelFilter.setInherit(exploreReq.isInherit());
        //层级通用
        graphFrom.setHighLevelFilter(highLevelFilter);

        consumerIfNoNull(exploreReq.getDistance(), graphFrom::setDistance);

        //默认读取私有关系
        highLevelFilter.setQueryPrivate(true);
        graphFrom.setQueryPrivate(true);
        //默认读取元数据
        MetaData entityMetaData = new MetaData();
        entityMetaData.setRead(true);
        graphFrom.setEntityMeta(entityMetaData);
        MetaData relationMetaData = new MetaData();
        relationMetaData.setRead(true);
        graphFrom.setRelationMeta(relationMetaData);

        return graphFrom;
    }


    /**
     * 关系转换
     *
     * @param simpleRelationList 关系结果
     * @return 。。。
     */
    static List<GraphRelationRsp> simpleRelationToGraphRelationRsp(@NonNull List<SimpleRelation> simpleRelationList, boolean relationMerge) {

        List<GraphRelationRsp> graphRelationRspList = listToRsp(simpleRelationList, GraphCommonConverter::simpleRelationToGraphRelationRsp);
        if (relationMerge) {
            Map<String, List<GraphRelationRsp>> rspMap = graphRelationRspList.stream().collect(Collectors.groupingBy(a -> a.getFrom() + "_" + a.getAttId() + "_" + a.getTo()));
            return rspMap.values().stream().map(a -> {
                GraphRelationRsp one = a.get(0);
                if (a.size() >= 2) {
                    one.setSourceRelationList(listToRsp(a, b -> BasicConverter.copy(b, GraphRelationRsp.class)));
                }
                return one;
            }).collect(Collectors.toList());
        }
        return graphRelationRspList;
    }


    private static GraphRelationRsp simpleRelationToGraphRelationRsp(@NonNull SimpleRelation relation) {
        GraphRelationRsp relationRsp = new GraphRelationRsp();
        relationRsp.setFrom(relation.getFrom());
        relationRsp.setTo(relation.getTo());
        relationRsp.setAttId(relation.getAttrId());
        relationRsp.setAttName(relation.getAttrName());
        relationRsp.setDirection(relation.getDirection());
        relationRsp.setStartTime(relation.getAttrTimeFrom());
        relationRsp.setEndTime(relation.getAttrTimeTo());
        relationRsp.setId(relation.getId());
        consumerIfNoNull(relation.getMetaData(), a -> MetaConverter.fillMetaWithNoNull(a, relationRsp));
        consumerIfNoNull(relation.getEdgeNumericAttr(), a -> relationRsp.setDataValAttrs(edgeVoListToEdgeInfo(a)));
        consumerIfNoNull(relation.getEdgeObjAttr(), a -> relationRsp.setObjAttrs(edgeVoListToEdgeObjInfo(a)));
        return relationRsp;
    }

    private static List<BasicRelationRsp.EdgeObjectInfo> edgeVoListToEdgeObjInfo(@NonNull List<EdgeVO> edgeList) {
        return listToRsp(edgeList, a -> new BasicRelationRsp.EdgeObjectInfo(a.getName(), a.getSeqNo(), a.getEntityName(), a.getDataType(), a.getObjRange()));
    }

    private static List<BasicRelationRsp.EdgeDataInfo> edgeVoListToEdgeInfo(@NonNull List<EdgeVO> edgeList) {
        return listToRsp(edgeList, a -> new BasicRelationRsp.EdgeDataInfo(a.getName(), a.getSeqNo(), a.getValue(), a.getDataType()));
    }

    /**
     * 创建基础的entity
     *
     * @param graphEntityRsp rsp
     * @param simpleEntity   vo
     * @param conceptMap     概念
     * @param <T>            子类
     * @return 。。。
     */
    static <T extends GraphEntityRsp> T simpleToGraphEntityRsp(T graphEntityRsp, SimpleEntity simpleEntity, Map<Long, BasicInfo> conceptMap, Set<Long> replaceClassIds) {
        graphEntityRsp.setId(simpleEntity.getId());
        graphEntityRsp.setConceptId(simpleEntity.getConceptId());
        graphEntityRsp.setName(simpleEntity.getName());
        graphEntityRsp.setType(simpleEntity.getType());
        graphEntityRsp.setMeaningTag(simpleEntity.getMeaningTag());
        graphEntityRsp.setImgUrl(simpleEntity.getImageUrl());
        GraphCommonConverter.fillConcept(simpleEntity.getConceptId(), graphEntityRsp, conceptMap);
        Map<String, Object> metaDataMap = simpleEntity.getMetaData();
        consumerIfNoNull(metaDataMap, a -> MetaConverter.fillMetaWithNoNull(a, graphEntityRsp));
        if (!CollectionUtils.isEmpty(replaceClassIds)) {
            Optional<Long> first = graphEntityRsp.getConceptIdList().stream().filter(replaceClassIds::contains).findFirst();
            first.ifPresent(graphEntityRsp::setClassId);
        }
        return graphEntityRsp;
    }
}
