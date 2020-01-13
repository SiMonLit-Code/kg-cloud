package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.edit.req.BasicDetailFilter;
import ai.plantdata.kg.api.edit.resp.EntityAttributeValueVO;
import ai.plantdata.kg.api.edit.resp.EntityVO;
import ai.plantdata.kg.api.edit.resp.RelationAttrValueVO;
import ai.plantdata.kg.api.pub.req.FilterRelationFrom;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.sdk.constant.AttrDefinitionTypeEnum;
import com.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.FileRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.EntityLinksRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 13:58
 */
@Slf4j
public class InfoBoxConverter extends BasicConverter {


    public static BasicDetailFilter batchInfoBoxReqToBasicDetailFilter(BatchInfoBoxReq boxReq) {
        BasicDetailFilter detailFilter = new BasicDetailFilter();
        detailFilter.setIds(boxReq.getIds());
        detailFilter.setReadObj(boxReq.getRelationAttrs());
        detailFilter.setReadReverseObj(boxReq.getReverseRelationAttrs());
        return detailFilter;
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
            relationFrom.setRelationAttrFilters(ConditionConverter.relationAttrReqToMapV1(attrReqList));
        }
        relationFrom.setMetaFilters(executeNoNull(reversedAttrReqList, a -> Maps.newHashMap(ConditionConverter.relationAttrReqToMap(a))));
        return relationFrom;
    }


    public static InfoBoxRsp conceptToInfoBoxRsp(EntityVO entity) {
        InfoBoxRsp infoBoxRsp = new InfoBoxRsp();
        infoBoxRsp.setSelf(voToSelf(entity, Collections.emptyList()));
        infoBoxRsp.setParents(listToRsp(entity.getParent(), InfoBoxConverter::basicInfoToInfoBoxConceptRsp));
        infoBoxRsp.setSons(listToRsp(entity.getSons(), InfoBoxConverter::basicInfoToInfoBoxConceptRsp));
        return infoBoxRsp;
    }

    public static InfoBoxRsp entityToInfoBoxRsp(EntityVO entity) {
        List<EntityAttributeValueVO> objAttrList = Lists.newArrayList();
        List<EntityAttributeValueVO> resObjAttrList = Lists.newArrayList();
        List<EntityAttributeValueVO> otherDataAttrList = Lists.newArrayList();
        //属性
        if (!CollectionUtils.isEmpty(entity.getAttrValue())) {
            entity.getAttrValue().forEach(a -> {
                if (a.getType() != null && AttrDefinitionTypeEnum.OBJECT.fetchId().equals(a.getType())) {
                    if (a.getDomainValue() == null) {
                        return;
                    }
                    EntityAttributeValueVO copy;
                    if (!CollectionUtils.isEmpty(a.getObjectValues())) {
                        //正向对象属性
                        copy = copy(a, EntityAttributeValueVO.class);
                        copy.setObjectValues(a.getObjectValues());
                        objAttrList.add(copy);
                    }
                    if (!CollectionUtils.isEmpty(a.getReverseObjectValues())) {
                        //反向对象属性
                        copy = copy(a, EntityAttributeValueVO.class);
                        copy.setReverseObjectValues(a.getReverseObjectValues());
                        resObjAttrList.add(copy);
                    }
                } else {
                    otherDataAttrList.add(a);
                }
            });
        }
        InfoBoxRsp infoBoxRsp = new InfoBoxRsp();
        //设置父概念
        infoBoxRsp.setParents(listToRsp(entity.getParent(), InfoBoxConverter::basicInfoToInfoBoxConceptRsp));
        //设置子概念
        infoBoxRsp.setSons(listToRsp(entity.getSons(), InfoBoxConverter::basicInfoToInfoBoxConceptRsp));
        //基本字段
        infoBoxRsp.setSelf(voToSelf(entity, otherDataAttrList));
        // 正向对象属性
        consumerIfNoNull(objAttrList, a -> infoBoxRsp.setAttrs(listToRsp(a, InfoBoxConverter::attrValToInfoBoxAttrRsp)));
        // 反向对象属性
        consumerIfNoNull(resObjAttrList, a -> infoBoxRsp.setReAttrs(listToRsp(resObjAttrList, InfoBoxConverter::attrValToInfoBoxAttrRsp)));
        return infoBoxRsp;
    }

    private static EntityLinksRsp voToSelf(EntityVO entity, List<EntityAttributeValueVO> dataAttrList) {
        EntityLinksRsp self = EntityConverter.entityVoToBasicEntityRsp(entity, new EntityLinksRsp());
        if (StringUtils.isNotEmpty(entity.getImageUrl())) {
            self.setImgUrl(entity.getImageUrl());
        }
        //扩展属性
        List<EntityLinksRsp.ExtraRsp> extraList = new ArrayList<>();
        consumerIfNoNull(entity.getMetaData(), a -> {
            fillDefaultAttr(extraList, a);
            self.setTags(MetaConverter.getTags(a));
        });
        consumerIfNoNull(entity.getAbs(), a -> extraList.add(new EntityLinksRsp.ExtraRsp(-1, "简介", a, null)));

        if (!CollectionUtils.isEmpty(entity.getSynonym())) {
            String synonyms = org.springframework.util.StringUtils.collectionToDelimitedString(entity.getSynonym(), ",");
            extraList.add(new EntityLinksRsp.ExtraRsp(-1, "别称", synonyms, null));
        }
        //设置数值,私有属性
        consumerIfNoNull(dataAttrList, a -> fillAttr(extraList, a));
        self.setExtraList(extraList);
        return self;
    }

    private static InfoBoxRsp.InfoBoxAttrRsp attrValToInfoBoxAttrRsp(EntityAttributeValueVO attrVal) {
        InfoBoxRsp.InfoBoxAttrRsp objectAttributeRsp = new InfoBoxRsp.InfoBoxAttrRsp();
        if (CollectionUtils.isEmpty(attrVal.getObjectValues()) && CollectionUtils.isEmpty(attrVal.getReverseObjectValues())) {
            return null;
        }
        consumerIfNoNull(attrVal.getObjectValues(), a -> objectAttributeRsp.setEntityList(listToRsp(a, InfoBoxConverter::relationAttrValueVOToEntity)));
        consumerIfNoNull(attrVal.getReverseObjectValues(), a -> objectAttributeRsp.setEntityList(listToRsp(a, InfoBoxConverter::relationAttrValueVOToEntity)));
        objectAttributeRsp.setAttrDefId(attrVal.getId());
        objectAttributeRsp.setAttrDefName(attrVal.getName());
        return objectAttributeRsp;
    }

    private static PromptEntityRsp relationAttrValueVOToEntity(@NonNull RelationAttrValueVO attrValueVO) {
        PromptEntityRsp promptEntityRsp = new PromptEntityRsp();
        promptEntityRsp.setId(attrValueVO.getId());
        promptEntityRsp.setConceptId(attrValueVO.getConceptId());
        promptEntityRsp.setMeaningTag(attrValueVO.getMeaningTag());
        promptEntityRsp.setName(attrValueVO.getName());
        promptEntityRsp.setQa(false);
        EntityTypeEnum entityTypeEnum = EntityTypeEnum.parseById(attrValueVO.getType());
        promptEntityRsp.setType(entityTypeEnum);
        return promptEntityRsp;
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
        EntityLinksRsp.ExtraRsp extraRsp = new EntityLinksRsp.ExtraRsp(attributeValue.getId(), attributeValue.getName(), attributeValue.getDataType());
        switch (dataType) {
            case ATTACHMENT:
            case VIDEO:
                consumerIfNoNull(attributeValue.getDataValue(), a -> {
                    FileRsp fileRsp = JsonUtils.parseObj((String) attributeValue.getDataValue(), FileRsp.class);
                    consumerIfNoNull(fileRsp, extraRsp::setValue);
                });
                break;
            case IMAGE:
            case URL:
                consumerIfNoNull(attributeValue.getDataValue(), a -> {
                    Optional<ImageRsp> imageRsp = ImageConverter.stringT0Image((String) attributeValue.getDataValue());
                    consumerIfNoNull(imageRsp.orElse(null), extraRsp::setValue);
                });
                break;
            default:
                consumerIfNoNull(attributeValue.getDataValue(), a -> {
                    String val = StringUtils.isNotEmpty(attributeValue.getDataUnit()) ? attributeValue.getDataValue() + attributeValue.getDataUnit() : attributeValue.getDataValue().toString();
                    extraRsp.setValue(val);
                });
                break;
        }
        return extraRsp;
    }


    private static void fillDefaultAttr(List<EntityLinksRsp.ExtraRsp> extraList, Map<String, Object> metaDataMap) {
        final int defaultDefId = -1;
        //默认属性
        if (metaDataMap.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
            extraList.add(EntityLinksRsp.ExtraRsp.buildDefault(defaultDefId, "来源", metaDataMap.get(MetaDataInfo.SOURCE.getFieldName())));
        }
        if (metaDataMap.containsKey(MetaDataInfo.SCORE.getFieldName())) {
            extraList.add(EntityLinksRsp.ExtraRsp.buildDefault(defaultDefId, "权重", metaDataMap.get(MetaDataInfo.SCORE.getFieldName())));
        }
        if (metaDataMap.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
            extraList.add(EntityLinksRsp.ExtraRsp.buildDefault(defaultDefId, "置信度", metaDataMap.get(MetaDataInfo.RELIABILITY.getFieldName())));
        }
        if (metaDataMap.containsKey(MetaDataInfo.FROM_TIME.getFieldName())) {
            extraList.add(EntityLinksRsp.ExtraRsp.buildDefault(defaultDefId, "开始时间", metaDataMap.get(MetaDataInfo.FROM_TIME.getFieldName())));
        }
        if (metaDataMap.containsKey(MetaDataInfo.TO_TIME.getFieldName())) {
            extraList.add(EntityLinksRsp.ExtraRsp.buildDefault(defaultDefId, "结束时间", metaDataMap.get(MetaDataInfo.TO_TIME.getFieldName())));
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
