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
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.app.converter.ConceptConverter;
import com.plantdata.kgcloud.domain.app.converter.ConditionConverter;
import com.plantdata.kgcloud.domain.app.converter.ImageConverter;
import com.plantdata.kgcloud.domain.app.converter.MetaConverter;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
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
public class GraphCommonConverter {

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
            log.error("conceptId:{}概念不存在", conceptId);
            return;
        }
        entityRsp.setConceptName(concept.getName());
        entityRsp.setClassId(topConcept.getId());
        entityRsp.setConceptIdList(ConceptConverter.getAllParentConceptId(Lists.newArrayList(conceptId), conceptId, conceptMap));
    }

    /**
     * 填充基础参数
     *
     * @param page       分页参数 仅使用 page->(default:0) size->(default:10)
     * @param exploreReq req
     * @param graphFrom  remote 参数
     * @param <T>        子类
     * @param <E>        子类
     * @return 。。。
     */
    static <T extends BasicGraphExploreReq, E extends CommonFilter> E basicReqToRemote(BaseReq page, T exploreReq, E graphFrom) {
        CommonFilter commonFilter = new GraphFrom();
        if (page == null) {
            page = new BaseReq();
            page.setPage(NumberUtils.INTEGER_ZERO);
            page.setSize(10);
        }
        commonFilter.setSkip(page.getOffset());
        commonFilter.setDirection(exploreReq.getDirection());
        commonFilter.setDistance(exploreReq.getDistance());
        commonFilter.setLimit(exploreReq.getHighLevelSize() == null ? page.getLimit() : exploreReq.getHighLevelSize());
        graphFrom.setSkip(page.getPage());
        graphFrom.setLimit(page.getSize());
        if (!CollectionUtils.isEmpty(exploreReq.getEntityFilters())) {
            EntityFilter entityFilter = new EntityFilter();
            entityFilter.setAttr(ConditionConverter.entityListToIntegerKeyMap(exploreReq.getEntityFilters()));
            commonFilter.setEntityFilter(entityFilter);
        }
        //设置边属性筛选
        if (!CollectionUtils.isEmpty(exploreReq.getEdgeAttrFilters())) {
            commonFilter.setEdgeFilter(Maps.newHashMap(ConditionConverter.relationAttrReqToMap(exploreReq.getEdgeAttrFilters())));
        }
        if (!CollectionUtils.isEmpty(exploreReq.getEdgeAttrSorts())) {
            commonFilter.setEdgeSort(ConditionConverter.relationAttrSortToMap(exploreReq.getEdgeAttrSorts()));
        }

        graphFrom.setHighLevelFilter(commonFilter);
        graphFrom.setAllowAttrs(exploreReq.getAllowAttrs());
        graphFrom.setAllowTypes(exploreReq.getAllowConcepts());
        graphFrom.setDirection(exploreReq.getDirection());
        graphFrom.setInherit(exploreReq.isInherit());
        graphFrom.setDistance(exploreReq.getDistance());

        graphFrom.setDisAllowTypes(exploreReq.getDisAllowConcepts());

        //读取元数据
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
        List<GraphRelationRsp> relationRspList = Lists.newArrayList();
        GraphRelationRsp relationRsp;
        Map<Long, Set<Long>> relationMap = Maps.newHashMap();
        for (SimpleRelation relation : simpleRelationList) {
            relationRsp = new GraphRelationRsp();
            relationRsp.setFrom(relation.getFrom());
            relationRsp.setTo(relation.getTo());
            relationRsp.setAttId(relation.getAttrId());
            relationRsp.setAttName(relation.getAttrName());
            relationRsp.setDirection(relation.getDirection());
            relationRsp.setStartTime(relation.getAttrTimeFrom());
            relationRsp.setEndTime(relation.getAttrTimeFrom());
            relationRsp.setId(relation.getId());
            if (!CollectionUtils.isEmpty(relation.getMetaData())) {
                Map<String, Object> additionalMap = (Map<String, Object>) relation.getMetaData().get(MetaDataInfo.ADDITIONAL.getFieldName());
                relationRsp.setLabelStyle((Map<String, Object>) additionalMap.get("labelStyle"));
                relationRsp.setLinkStyle((Map<String, Object>) additionalMap.get("linkStyle"));
            }
            if (!CollectionUtils.isEmpty(relation.getEdgeNumericAttr())) {
                relationRsp.setDataValAttrs(edgeVoListToEdgeInfo(relation.getEdgeNumericAttr()));
            }
            if (!CollectionUtils.isEmpty(relation.getEdgeObjAttr())) {
                relationRsp.setObjAttrs(edgeVoListToEdgeInfo(relation.getEdgeNumericAttr()));
            }
            if (!relationMerge) {
                relationRspList.add(relationRsp);
                continue;
            }
            //关系合并
            Set<Long> toSet = relationMap.computeIfAbsent(relation.getFrom(), Sets::newHashSet);
            if (toSet.contains(relation.getTo())) {
                relationRsp.getSourceRelationList().add(relationRsp);
            } else {
                relationRspList.add(relationRsp);
                toSet.add(relation.getTo());
            }
        }
        return relationRspList;
    }

    private static List<BasicRelationRsp.EdgeInfo> edgeVoListToEdgeInfo(@NonNull List<EdgeVO> edgeList) {
        return edgeList.stream().map(a -> new BasicRelationRsp.EdgeInfo(a.getName(), a.getSeqNo(), a.getValue(), a.getDataType(), a.getObjRange())).collect(Collectors.toList());
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
    static <T extends GraphEntityRsp> T simpleToGraphEntityRsp(T graphEntityRsp, SimpleEntity simpleEntity, Map<Long, BasicInfo> conceptMap) {
        graphEntityRsp.setId(simpleEntity.getId());
        graphEntityRsp.setConceptId(simpleEntity.getConceptId());
        graphEntityRsp.setName(simpleEntity.getName());
        graphEntityRsp.setType(EntityTypeEnum.parseById(simpleEntity.getType()));
        graphEntityRsp.setMeaningTag(simpleEntity.getMeaningTag());
        Optional<ImageRsp> imageRsp = ImageConverter.stringT0Image(simpleEntity.getImageUrl());
        imageRsp.ifPresent(graphEntityRsp::setImg);
        if (EntityTypeEnum.ENTITY.equals(graphEntityRsp.getType())) {
            GraphCommonConverter.fillConcept(simpleEntity.getConceptId(), graphEntityRsp, conceptMap);
        }
        Map<String, Object> metaDataMap = simpleEntity.getMetaData();
        if (!CollectionUtils.isEmpty(metaDataMap)) {
            MetaConverter.fillMetaWithNoNull(metaDataMap, graphEntityRsp);
        }
        return graphEntityRsp;
    }
}
