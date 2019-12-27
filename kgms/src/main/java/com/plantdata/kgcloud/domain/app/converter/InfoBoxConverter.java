package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.edit.resp.EntityAttributeValueVO;
import ai.plantdata.kg.api.edit.resp.EntityVO;
import ai.plantdata.kg.api.pub.req.FilterRelationFrom;
import ai.plantdata.kg.api.pub.req.KgServiceEntityFrom;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.sdk.constant.AttrDefinitionTypeEnum;
import com.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.EntityLinksRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 13:58
 */
@Slf4j
public class InfoBoxConverter extends BasicConverter {

    public static KgServiceEntityFrom batchInfoBoxReqToKgServiceEntityFrom(BatchInfoBoxReq boxReq) {
        KgServiceEntityFrom entityFrom = new KgServiceEntityFrom();
        entityFrom.setIds(boxReq.getEntityIdList());
        entityFrom.setReadObjectAttribute(boxReq.getRelationAttrs());
        entityFrom.setReadMetaData(true);
        entityFrom.setReadReverseObjectAttribute(boxReq.getReverseRelationAttrs());
        entityFrom.setAllowAtts(boxReq.getAllowAttrs());
        return entityFrom;
    }

    public static FilterRelationFrom reqToFilterRelationFrom(BasicGraphExploreRsp rsp, List<RelationAttrReq> attrReqList, List<RelationAttrReq> reversedAttrReqList) {
        FilterRelationFrom relationFrom = new FilterRelationFrom();
        if (!CollectionUtils.isEmpty(rsp.getRelationList())) {
            List<String> relationIdSet = Lists.newArrayList();
            List<Integer> attrIds = Lists.newArrayList();
            rsp.getRelationList().forEach(a -> {
                relationIdSet.add(a.getId());
                attrIds.add(a.getAttId());
            });
            relationFrom.setRelationIds(relationIdSet);
            relationFrom.setAttrIds(attrIds);
        }
        if (!CollectionUtils.isEmpty(rsp.getEntityList())) {
            List<Long> entityIdSet = rsp.getEntityList().stream().filter(a -> a.getConceptId() != null).map(CommonEntityRsp::getId).collect(Collectors.toList());
            relationFrom.setEntityIds(entityIdSet);
        }
        if (!CollectionUtils.isEmpty(attrReqList)) {
            relationFrom.setRelationAttrFilters(ConditionConverter.relationAttrReqToMap(attrReqList));
        }
        relationFrom.setMetaFilters(executeNoNull(reversedAttrReqList, a -> Maps.newHashMap(ConditionConverter.relationAttrReqToMap(a))));
        return relationFrom;
    }

    public static List<InfoBoxRsp> voToInfoBox(@NonNull List<Long> sourceEntityIds,
                                               List<EntityVO> relationEntityList) {
        Map<Long, EntityVO> entityMap = relationEntityList.stream().collect(Collectors.toMap(ai.plantdata.kg.api.edit.resp.EntityVO::getId, Function.identity()));
        return listToRsp(sourceEntityIds, entity -> voToInfoBoxRsp(entity, entityMap));
    }


    private static InfoBoxRsp voToInfoBoxRsp(Long sourceEntityId,
                                             Map<Long, EntityVO> entityMap) {
        EntityVO entity = entityMap.get(sourceEntityId);
        if (entity == null) {
            return null;
        }
        List<EntityAttributeValueVO> objAttrList = Lists.newArrayList();
        List<EntityAttributeValueVO> otherDataAttrList = Lists.newArrayList();
        //属性
        if (!CollectionUtils.isEmpty(entity.getAttrValue())) {
            entity.getAttrValue().forEach(a -> {
                if (a.getType() != null && AttrDefinitionTypeEnum.OBJECT.fetchId().equals(a.getType())) {
                    objAttrList.add(a);
                } else {
                    otherDataAttrList.add(a);
                }
            });
        }
        InfoBoxRsp infoBoxRsp = new InfoBoxRsp();
        //设置父概念
        infoBoxRsp.setParents(listToRsp(entity.getParent(), InfoBoxConverter::basicInfoToInfoBoxConceptRsp));
        //设置子概念 todo
        infoBoxRsp.setSons(listToRsp(entity.getSons(), InfoBoxConverter::basicInfoToInfoBoxConceptRsp));
        //基本字段
        infoBoxRsp.setSelf(voToSelf(entity, otherDataAttrList));
        //对象属性
        infoBoxRsp.setAttrs(listToRsp(objAttrList, a -> attrValToInfoBoxAttrRsp(a, entityMap)));
        return infoBoxRsp;
    }

    private static EntityLinksRsp voToSelf(EntityVO entity, List<EntityAttributeValueVO> dataAttrList) {
        EntityLinksRsp self = EntityConverter.entityVoToBasicEntityRsp(entity, new EntityLinksRsp());
        if (StringUtils.isNotEmpty(entity.getImageUrl())) {
            self.setImg(JacksonUtils.readValue(entity.getImageUrl(), ImageRsp.class));
        }
        //扩展属性
        List<EntityLinksRsp.ExtraRsp> extraList = new ArrayList<>();
        consumerIfNoNull(entity.getMetaData(), a -> {
            fillDefaultAttr(extraList, a);
            self.setTags(MetaConverter.getTags(a));
        });
        consumerIfNoNull(entity.getAbs(), a -> extraList.add(new EntityLinksRsp.ExtraRsp(-1, "简介", a)));

        if (!CollectionUtils.isEmpty(entity.getSynonym())) {
            String synonyms = org.springframework.util.StringUtils.collectionToDelimitedString(entity.getSynonym(), ",");
            extraList.add(new EntityLinksRsp.ExtraRsp(-1, "别称", synonyms));
        }
        //设置数值,私有属性
        fillAttr(extraList, dataAttrList);
        self.setExtraList(extraList);
        return self;
    }

    private static InfoBoxRsp.InfoBoxAttrRsp attrValToInfoBoxAttrRsp(EntityAttributeValueVO attrVal, Map<Long, EntityVO> entityMap) {
        if (CollectionUtils.isEmpty(attrVal.getObjectValues())) {
            return null;
        }
        List<EntityVO> entityList = attrVal.getObjectValues().stream().map(a -> entityMap.get(a.getId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        InfoBoxRsp.InfoBoxAttrRsp objectAttributeRsp = new InfoBoxRsp.InfoBoxAttrRsp();
        objectAttributeRsp.setAttrDefId(attrVal.getId());
        objectAttributeRsp.setAttrDefName(attrVal.getName());

        objectAttributeRsp.setEntityList(listToRsp(entityList, InfoBoxConverter::voToPromptEntityRsp));
        return objectAttributeRsp;
    }

    private static void fillAttr(List<EntityLinksRsp.ExtraRsp> extraList, List<EntityAttributeValueVO> attrValueList) {
        for (EntityAttributeValueVO value : attrValueList) {
            //私有属性
            if (value.getType() == null) {
                extraList.add(new EntityLinksRsp.ExtraRsp(-1, value.getName(), value.getDataValue(), value.getDataType()));
            }
            //数值属性
            else if (value.getType() == 0) {
                extraList.add(dataAttrToExtraRsp(value));
            }
            //私有对象属性
            else if (value.getType() == 1 && !CollectionUtils.isEmpty(value.getObjectValues())) {
                value.getObjectValues().forEach(a -> {
                    Map<Integer, List<BasicInfo>> relationObjectValues = a.getRelationObjectValues();
                    extraList.add(new EntityLinksRsp.ExtraRsp(a.getAttrId(), a.getName(), relationObjectValues.values(), value.getDataType()));
                });
            }

        }
    }

    private static EntityLinksRsp.ExtraRsp dataAttrToExtraRsp(EntityAttributeValueVO attributeValue) {
        AttributeDataTypeEnum dataType = AttributeDataTypeEnum.parseById(attributeValue.getDataType());
        if (dataType == null) {
            log.error("attributeDefinition:{},不存在", attributeValue.getDataType());
            return new EntityLinksRsp.ExtraRsp(-1, attributeValue.getName(), attributeValue.getDataValue(), attributeValue.getDataType());
        }
        switch (dataType) {
            case IMAGE:
            case URL:
                return new EntityLinksRsp.ExtraRsp(attributeValue.getId(), attributeValue.getName(), JacksonUtils.readValue(JacksonUtils.writeValueAsString(attributeValue.getDataValue()), ImageRsp.class), attributeValue.getDataType());
            default:
                return new EntityLinksRsp.ExtraRsp(attributeValue.getId(), attributeValue.getName(), StringUtils.isNotEmpty(attributeValue.getDataUnit()) ? attributeValue.getDataValue() + attributeValue.getDataUnit() : attributeValue.getDataValue(), attributeValue.getDataType());
        }

    }

    private static PromptEntityRsp voToPromptEntityRsp(@NonNull ai.plantdata.kg.api.edit.resp.EntityVO entityVO) {
        PromptEntityRsp entityRsp = new PromptEntityRsp();
        entityRsp.setConceptId(entityVO.getConceptId());
        entityRsp.setId(entityVO.getId());
        entityRsp.setMeaningTag(entityVO.getMeaningTag());
        entityRsp.setName(entityVO.getName());
        entityRsp.setType(EntityTypeEnum.ENTITY);
        return entityRsp;
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

    private static InfoBoxConceptRsp basicInfoToInfoBoxConceptRsp(@NonNull BasicInfo basicInfo) {
        InfoBoxConceptRsp entityRsp = new InfoBoxConceptRsp();
        entityRsp.setId(basicInfo.getId());
        entityRsp.setName(basicInfo.getName());
        entityRsp.setMeaningTag(basicInfo.getMeaningTag());
        entityRsp.setImageUrl(basicInfo.getImageUrl());
        return entityRsp;
    }
}
