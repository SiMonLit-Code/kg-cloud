package com.plantdata.kgcloud.domain.app.converter.graph;

import ai.plantdata.kg.api.pub.req.CommonFilter;
import ai.plantdata.kg.api.pub.req.GraphFrom;
import ai.plantdata.kg.api.pub.req.MetaData;
import ai.plantdata.kg.api.pub.resp.SimpleEntity;
import ai.plantdata.kg.api.pub.resp.SimpleRelation;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.app.converter.ConceptConverter;
import com.plantdata.kgcloud.domain.app.converter.ImageConverter;
import com.plantdata.kgcloud.domain.app.converter.MetaConverter;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.ExploreConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:43
 */
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
        entityRsp.setConceptName(concept.getName());
        BasicInfo topConcept = ConceptConverter.getTopConcept(conceptId, conceptMap);
        entityRsp.setClassId(topConcept.getId());
        entityRsp.setConceptIdList(ConceptConverter.getAllParentConceptId(Lists.newArrayList(conceptId), conceptId, conceptMap));
    }

    /**
     * 填充基础参数
     *
     * @param page       分页
     * @param exploreReq req
     * @param graphFrom  remote 参数
     * @param <T>        子类
     * @param <E>        子类
     * @return 。。。
     */
    static <T extends BasicGraphExploreReq, E extends CommonFilter> E basicReqToRemote(BaseReq page, T exploreReq, E graphFrom) {
        CommonFilter commonFilter = new GraphFrom();
        commonFilter.setSkip(NumberUtils.INTEGER_ZERO);
        commonFilter.setLimit(exploreReq.getHighLevelSize());
        graphFrom.setHighLevelFilter(commonFilter);
        graphFrom.setAllowAttrs(exploreReq.getAllowAttrs());
        graphFrom.setAllowTypes(exploreReq.getAllowConcepts());
        graphFrom.setDirection(exploreReq.getDirection());
        graphFrom.setInherit(exploreReq.isInherit());
        graphFrom.setDistance(exploreReq.getDistance());
        graphFrom.setSkip(page.getPage());
        graphFrom.setDisAllowTypes(exploreReq.getDisAllowConcepts());
        graphFrom.setLimit(page.getSize());
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
    static List<BasicRelationRsp> simpleRelationToBasicRelationRsp(@NonNull List<SimpleRelation> simpleRelationList) {
        List<BasicRelationRsp> relationRspList = Lists.newArrayList();
        BasicRelationRsp relationRsp;
        for (SimpleRelation relation : simpleRelationList) {
            relationRsp = new BasicRelationRsp();
            relationRsp.setAttId(relation.getAttrId());
            relationRsp.setAttName(relation.getAttrName());
            relationRsp.setDirection(relation.getDirection());
            relationRsp.setStartTime(relation.getAttrTimeFrom());
            relationRsp.setEndTime(relation.getAttrTimeFrom());
            relationRsp.setId(relation.getId());
            relationRspList.add(relationRsp);
        }
        return relationRspList;
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
        graphEntityRsp.setName(simpleEntity.getName());
        graphEntityRsp.setType(EntityTypeEnum.parseById(simpleEntity.getType()));
        graphEntityRsp.setMeaningTag(simpleEntity.getMeaningTag());
        Optional<ImageRsp> imageRsp = ImageConverter.stringT0Image(simpleEntity.getImageUrl());
        imageRsp.ifPresent(graphEntityRsp::setImg);
        //todo查询所有概念
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
