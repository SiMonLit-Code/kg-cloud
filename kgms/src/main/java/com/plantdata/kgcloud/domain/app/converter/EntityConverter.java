package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.EntityFilterFrom;
import ai.plantdata.kg.api.pub.req.KgServiceEntityFrom;
import ai.plantdata.kg.api.pub.req.SearchByAttributeFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.GisEntityVO;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphCommonConverter;
import com.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.EntityLinksRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.ExploreConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisInfoRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 17:12
 */
public class EntityConverter {

    public static KgServiceEntityFrom buildIdsQuery(@NonNull Collection<Long> entityIdList) {
        KgServiceEntityFrom entityFrom = new KgServiceEntityFrom();
        entityFrom.setIds(Lists.newArrayList(entityIdList));
        entityFrom.setReadObjectAttribute(true);
        entityFrom.setReadMetaData(true);
        entityFrom.setReadReverseObjectAttribute(false);
        return entityFrom;
    }

    public static EntityFilterFrom buildEntityFilterFrom(List<Long> entityIdSet,Map<String,Object> kvMap){
        EntityFilterFrom entityFilterFrom = new EntityFilterFrom();
        entityFilterFrom.setKvMap(kvMap);
        entityFilterFrom.setIds(entityIdSet);
        return entityFilterFrom;
    }


    public static <T extends BasicEntityRsp> T voToBasicEntityRsp(EntityVO entityVO, T entity) {
        entity.setId(entityVO.getId());
        Optional<ImageRsp> imgOpt = ImageConverter.stringT0Image(entityVO.getImageUrl());
        imgOpt.ifPresent(entity::setImg);
        entity.setConceptId(entityVO.getConceptId());
        entity.setMeaningTag(entityVO.getMeaningTag());
        entity.setName(entityVO.getName());
        entity.setType(EntityTypeEnum.ENTITY);
        MetaConverter.fillMetaWithNoNull(entityVO.getMetaData(), entity);
        return entity;
    }

    public static OpenEntityRsp voToOpenEntityRsp(EntityVO entityVO) {
        OpenEntityRsp openEntityRsp = voToBasicEntityRsp(entityVO, new OpenEntityRsp());
        openEntityRsp.setConceptIdList(Lists.newArrayList(entityVO.getConceptId()));
        openEntityRsp.setAttributes(entityVO.getDataAttributes());
        openEntityRsp.setSynonyms(entityVO.getSynonyms());
        return openEntityRsp;
    }

    public static List<GraphInitRsp.GraphInitEntityRsp> entityVoToGraphInitEntityRsp(@NonNull List<EntityVO> entityList) {
        return entityList.stream().map(entity -> {
            GraphInitRsp.GraphInitEntityRsp simpleEntity = new GraphInitRsp.GraphInitEntityRsp();
            simpleEntity.setId(entity.getId());
            simpleEntity.setName(entity.getName());
            simpleEntity.setClassId(entity.getConceptId());
            return simpleEntity;
        }).collect(Collectors.toList());
    }

    public static List<GisEntityRsp> voToGisRsp(@NonNull List<GisEntityVO> entityVOList) {
        List<GisEntityRsp> entityRspList = Lists.newArrayListWithCapacity(entityVOList.size());
        GisEntityRsp gisEntityRsp;
        for (GisEntityVO gisEntity : entityVOList) {
            gisEntityRsp = new GisEntityRsp();
            gisEntityRsp.setConceptIdList(gisEntity.getConceptIdList());
            //todo
            //gisEntityRsp.setCoordinates();
            gisEntityRsp.setCreationTime(gisEntity.getCreateTime());
            gisEntityRsp.setStartTime(gisEntity.getStartTime());
            gisEntityRsp.setEndTime(gisEntity.getEndTime());
            gisEntityRsp.setMeaningTag(gisEntity.getMeaningTag());
            gisEntityRsp.setConceptId(gisEntity.getConceptId());
            gisEntityRsp.setConceptName(gisEntity.getConceptName());
            gisEntityRsp.setClassId(gisEntity.getTopConceptId());
            Optional<ImageRsp> imageRsp = ImageConverter.stringT0Image(gisEntity.getImage());
            imageRsp.ifPresent(gisEntityRsp::setImg);
            gisEntityRsp.setGis(new GisInfoRsp(gisEntity.getOpenGis(), gisEntity.getLng(), gisEntity.getLat(), gisEntity.getAddress()));
            entityRspList.add(gisEntityRsp);
        }
        return entityRspList;
    }

    public static List<GisEntityRsp> basicInfoToGisEntity(@NonNull List<BasicInfo> basicInfoList, Map<Long, BasicInfo> conceptMap) {
        List<GisEntityRsp> entityRspList = Lists.newArrayListWithCapacity(basicInfoList.size());
        GisEntityRsp gisEntityRsp;
        for (BasicInfo entity : basicInfoList) {
            gisEntityRsp = new GisEntityRsp();
            ///todo
            //gisEntityRsp.setCoordinates();
            //gisEntityRsp.setCreationTime();
            gisEntityRsp.setMeaningTag(entity.getMeaningTag());

            Optional<ImageRsp> imageRsp = ImageConverter.stringT0Image(entity.getImageUrl());
            imageRsp.ifPresent(gisEntityRsp::setImg);
            Map<String, Object> metaData = entity.getMetaData();
            if (!CollectionUtils.isEmpty(metaData)) {
                GisInfoRsp gisInfoRsp = new GisInfoRsp();
                MetaConverter.fillMetaWithNoNull(metaData, gisInfoRsp);
                gisEntityRsp.setGis(gisInfoRsp);
                MetaConverter.fillMetaWithNoNull(metaData, gisEntityRsp);
            }
            GraphCommonConverter.fillConcept(entity.getConceptId(), gisEntityRsp, conceptMap);
            entityRspList.add(gisEntityRsp);
        }
        return entityRspList;
    }

    public static List<InfoBoxRsp> voToInfoBox(@NonNull List<EntityVO> entityList, List<AttributeDefinition> attrDefList,
                                               List<ai.plantdata.kg.api.edit.resp.EntityVO> relationEntityList) {
        Map<Integer, AttributeDefinition> definitionMap = attrDefList.stream().collect(Collectors.toMap(AttributeDefinition::getId, Function.identity()));
        Map<Long, ai.plantdata.kg.api.edit.resp.EntityVO> entityMap = relationEntityList.stream().collect(Collectors.toMap(ai.plantdata.kg.api.edit.resp.EntityVO::getId, Function.identity()));
        return entityList.stream().map(entity -> voToInfoBoxRsp(entity, definitionMap, entityMap)).collect(Collectors.toList());
    }


    public static SearchByAttributeFrom entityQueryReqToSearchByAttributeFrom(EntityQueryReq entityQueryReq) {
        SearchByAttributeFrom attributeFrom = new SearchByAttributeFrom();

        attributeFrom.setKvMap(entityQueryReq.getQuery().isEmpty() ? null : entityQueryReq.getQuery());
        attributeFrom.setLimit(entityQueryReq.getLimit());
        attributeFrom.setSkip(entityQueryReq.getOffset());
        attributeFrom.setConceptIds(Lists.newArrayList(entityQueryReq.getConceptId()));
        return attributeFrom;
    }

    private static InfoBoxRsp voToInfoBoxRsp(EntityVO entity, Map<Integer, AttributeDefinition> attrDefMap, Map<Long, ai.plantdata.kg.api.edit.resp.EntityVO> entityMap) {
        InfoBoxRsp infoBoxRsp = new InfoBoxRsp();
        //基本字段
        infoBoxRsp.setSelf(voToSelf(entity, attrDefMap));
        //对象属性
        if (!CollectionUtils.isEmpty(entity.getObjectAttributes())) {
            infoBoxRsp.setAttrs(voToInfoBoxAttrRsp(entity.getObjectAttributes(), entityMap, attrDefMap));
        }
        if (!CollectionUtils.isEmpty(entity.getReverseObjectAttributes())) {
            infoBoxRsp.setReAttrs(voToInfoBoxAttrRsp(entity.getReverseObjectAttributes(), entityMap, attrDefMap));
        }
        return infoBoxRsp;
    }

    private static PromptEntityRsp voToPromptEntityRsp(@NonNull ai.plantdata.kg.api.edit.resp.EntityVO entityVO) {
        PromptEntityRsp entityRsp = new PromptEntityRsp();
        entityRsp.setConceptId(entityVO.getConceptId());
        entityRsp.setId(entityVO.getId());
        entityRsp.setMeaningTag(entityRsp.getMeaningTag());
        entityRsp.setName(entityRsp.getName());
        entityRsp.setType(EntityTypeEnum.ENTITY);
        return entityRsp;
    }


    private static EntityLinksRsp voToSelf(EntityVO entity, Map<Integer, AttributeDefinition> attrDefMap) {
        EntityLinksRsp self = new EntityLinksRsp();
        self.setId(entity.getId());
        self.setName(entity.getName());
        self.setType(EntityTypeEnum.ENTITY);
        self.setMeaningTag(entity.getMeaningTag());
        if (StringUtils.isNotEmpty(entity.getImageUrl())) {
            self.setImg(JacksonUtils.readValue(entity.getImageUrl(), ImageRsp.class));
        }
        //扩展属性
        List<EntityLinksRsp.ExtraRsp> extraList = new ArrayList<>();
        if (entity.getMetaData() != null) {
            fillDefaultAttr(extraList, entity.getMetaData());
        }
        if (!StringUtils.isEmpty(entity.getAbs())) {
            extraList.add(new EntityLinksRsp.ExtraRsp(-1, "简介", entity.getAbs()));
        }
        if (!CollectionUtils.isEmpty(entity.getSynonyms())) {
            String synonyms = org.springframework.util.StringUtils.collectionToDelimitedString(entity.getSynonyms(), ",");
            extraList.add(new EntityLinksRsp.ExtraRsp(-1, "别称", synonyms));
        }
        //数值属性
        if (!CollectionUtils.isEmpty(entity.getDataAttributes())) {
            fillDataAttr(extraList, entity.getDataAttributes(), attrDefMap);
        }
        return self;
    }

    /**
     * @param extraList   属性
     * @param dataAttrMap k->attrId v->attrVal
     */
    private static void fillDataAttr(List<EntityLinksRsp.ExtraRsp> extraList, Map<String, Object> dataAttrMap, Map<Integer, AttributeDefinition> attrDefMap) {
        dataAttrMap.forEach((k, v) -> {
            AttributeDefinition definition = attrDefMap.get(Integer.parseInt(k));
            AttributeDataTypeEnum dataType = AttributeDataTypeEnum.parseById(definition.getDataType());
            if (null == dataType || definition == null) {
                return;
            }
            switch (dataType) {
                case IMAGE:
                case URL:
                    extraList.add(new EntityLinksRsp.ExtraRsp(definition.getId(), definition.getName(), JacksonUtils.readValue(JacksonUtils.writeValueAsString(v), ImageRsp.class)));
                    break;
                default:
                    extraList.add(new EntityLinksRsp.ExtraRsp(definition.getId(), definition.getName(), StringUtils.isNotEmpty(definition.getDataUnit()) ? v + definition.getDataUnit() : v));
            }
        });
    }

    private static List<InfoBoxRsp.InfoBoxAttrRsp> voToInfoBoxAttrRsp(Map<String, List<Long>> relationEntityIdMap, Map<Long, ai.plantdata.kg.api.edit.resp.EntityVO> relationEntityMap, Map<Integer, AttributeDefinition> attrDefMap) {
        List<InfoBoxRsp.InfoBoxAttrRsp> rspList = Lists.newArrayList();
        relationEntityIdMap.forEach((k, v) -> {
            Integer attrDefId = Integer.parseInt(k);

            AttributeDefinition definition = attrDefMap.get(attrDefId);
            InfoBoxRsp.InfoBoxAttrRsp infoBoxAttrRsp = new InfoBoxRsp.InfoBoxAttrRsp();
            infoBoxAttrRsp.setAttrDefId(attrDefId);
            infoBoxAttrRsp.setAttrDefName(definition.getName());
            List<PromptEntityRsp> entityList = v.stream().filter(relationEntityMap::containsKey).map(entityId -> {
                ai.plantdata.kg.api.edit.resp.EntityVO entityVO = relationEntityMap.get(entityId);
                return voToPromptEntityRsp(entityVO);
            }).collect(Collectors.toList());
            infoBoxAttrRsp.setEntityList(entityList);
            rspList.add(infoBoxAttrRsp);
        });
        return rspList;
    }

    private static void fillDefaultAttr(List<EntityLinksRsp.ExtraRsp> extraList, Map<String, Object> metaDataMap) {
        final int defaultDefId = -1;
        //默认属性
        if (metaDataMap.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
            extraList.add(new EntityLinksRsp.ExtraRsp(defaultDefId, "来源", metaDataMap.get(MetaDataInfo.SOURCE.getFieldName())));
        }
        if (metaDataMap.containsKey(MetaDataInfo.SCORE.getFieldName())) {
            extraList.add(new EntityLinksRsp.ExtraRsp(defaultDefId, "权重", metaDataMap.get(MetaDataInfo.SOURCE.getFieldName())));
        }
        if (metaDataMap.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
            extraList.add(new EntityLinksRsp.ExtraRsp(defaultDefId, "置信度", metaDataMap.get(MetaDataInfo.RELIABILITY.getFieldName())));
        }
        if (metaDataMap.containsKey(MetaDataInfo.FROM_TIME.getFieldName())) {
            extraList.add(new EntityLinksRsp.ExtraRsp(defaultDefId, "开始时间", metaDataMap.get(MetaDataInfo.FROM_TIME.getFieldName())));
        }
        if (metaDataMap.containsKey(MetaDataInfo.TO_TIME.getFieldName())) {
            extraList.add(new EntityLinksRsp.ExtraRsp(defaultDefId, "结束时间", metaDataMap.get(MetaDataInfo.TO_TIME.getFieldName())));
        }
    }


}
