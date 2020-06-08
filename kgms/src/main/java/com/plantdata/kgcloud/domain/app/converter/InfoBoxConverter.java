package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.edit.req.BasicDetailFilter;
import ai.plantdata.kg.api.edit.resp.EntityAttributeValueVO;
import ai.plantdata.kg.api.edit.resp.EntityVO;
import ai.plantdata.kg.api.pub.req.FilterRelationFrom;
import ai.plantdata.kg.api.pub.resp.RelationVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReqList;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchMultiModalReqList;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.FileRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.*;
import com.plantdata.kgcloud.sdk.rsp.edit.KnowledgeIndexRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
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


    public static BasicDetailFilter batchInfoBoxReqToBasicDetailFilter(BatchInfoBoxReqList boxReq) {
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
        infoBoxRsp.setSelf(voToSelf(entity, Collections.emptyList(), Collections.emptyList()));
        infoBoxRsp.setParents(listToRsp(entity.getParent(), InfoBoxConverter::basicInfoToInfoBoxConceptRsp));
        infoBoxRsp.setSons(listToRsp(entity.getSons(), InfoBoxConverter::basicInfoToInfoBoxConceptRsp));
        return infoBoxRsp;
    }

    public static InfoBoxRsp entityToInfoBoxRsp(EntityVO entity,  List<KnowledgeIndexRsp> knowledgeIndexRsps, List<RelationVO> relationList,
                                                List<RelationVO> reverseRelationList) {

        InfoBoxRsp infoBoxRsp = new InfoBoxRsp();
        //设置父概念
        infoBoxRsp.setParents(listToRsp(entity.getParent(), InfoBoxConverter::basicInfoToInfoBoxConceptRsp));
        //设置子概念
        infoBoxRsp.setSons(listToRsp(entity.getSons(), InfoBoxConverter::basicInfoToInfoBoxConceptRsp));
        //基本字段
        infoBoxRsp.setSelf(voToSelf(entity, entity.getAttrValue(),  knowledgeIndexRsps));
        //对象属性
        BasicConverter.consumerIfNoNull(relationList, a -> infoBoxRsp.setAttrs(convertObjectAttr(a, false)));
        //反向对象属性
        BasicConverter.consumerIfNoNull(reverseRelationList, a -> infoBoxRsp.setReAttrs(convertObjectAttr(a, true)));
        return infoBoxRsp;
    }

    public static InfoboxMultiModelRsp entityToInfoBoxMultiModelRsp(EntityVO entity, List<MultiModalRsp> modalRsps) {

        //基本字段
        InfoboxMultiModelRsp infoBoxRsp = EntityConverter.entityVoToBasicEntityRsp(entity, new InfoboxMultiModelRsp());

        if (!CollectionUtils.isEmpty(modalRsps)) {
            infoBoxRsp.setMultiModals(modalRsps);
        }
        return infoBoxRsp;
    }

    private static EntityLinksRsp voToSelf(EntityVO entity, List<EntityAttributeValueVO> dataAttrList,  List<KnowledgeIndexRsp> knowledgeIndexRsps) {
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
        consumerIfNoNull(entity.getAbs(), a -> extraList.add(new EntityLinksRsp.ExtraRsp(-1, "简介", null, a, null)));

        if (!CollectionUtils.isEmpty(entity.getSynonym())) {
            String synonyms = org.springframework.util.StringUtils.collectionToDelimitedString(entity.getSynonym(), ",");
            extraList.add(new EntityLinksRsp.ExtraRsp(-1, "别称", null, synonyms, null));
        }
        //设置数值,私有属性
        consumerIfNoNull(dataAttrList, a -> fillAttr(extraList, a));
        self.setExtraList(extraList);
        if (!CollectionUtils.isEmpty(knowledgeIndexRsps)){
            self.setKnowledgeIndexs(knowledgeIndexRsps);
        }
        return self;
    }

    private static List<InfoBoxRsp.InfoBoxAttrRsp> convertObjectAttr(@NonNull List<RelationVO> relationVOList, boolean reverse) {
        return relationVOList.stream().collect(Collectors.groupingBy(RelationVO::getAttrId))
                .entrySet().stream()
                .map(entry -> attrValToInfoBoxAttrRsp(entry.getValue(), entry.getKey(), entry.getValue().get(0).getAttrName(), reverse))
                .collect(Collectors.toList());
    }

    private static InfoBoxRsp.InfoBoxAttrRsp attrValToInfoBoxAttrRsp(List<RelationVO> relationList, Integer attrId, String attrDefName, boolean reverse) {
        InfoBoxRsp.InfoBoxAttrRsp objectAttributeRsp = new InfoBoxRsp.InfoBoxAttrRsp();
        consumerIfNoNull(relationList, a -> objectAttributeRsp.setEntityList(listToRsp(a, b -> InfoBoxConverter.relationAttrValueVOToEntity(b, reverse))));
        objectAttributeRsp.setAttrDefId(attrId);
        objectAttributeRsp.setAttrDefName(attrDefName);
        return objectAttributeRsp;
    }

    private static PromptEntityRsp relationAttrValueVOToEntity(@NonNull RelationVO relationVO, boolean reverse) {
        BasicInfo entity = reverse ? relationVO.getFrom() : relationVO.getTo();
        PromptEntityRsp promptEntityRsp = new PromptEntityRsp();
        promptEntityRsp.setId(entity.getId());
        promptEntityRsp.setConceptId(entity.getConceptId());
        promptEntityRsp.setMeaningTag(entity.getMeaningTag());
        promptEntityRsp.setName(entity.getName());
        promptEntityRsp.setQa(false);
        EntityTypeEnum entityTypeEnum = EntityTypeEnum.parseById(entity.getType());
        promptEntityRsp.setType(entityTypeEnum);
        return promptEntityRsp;
    }

    private static void fillAttr(List<EntityLinksRsp.ExtraRsp> extraList, List<EntityAttributeValueVO> attrValueList) {
        for (EntityAttributeValueVO value : attrValueList) {
            //私有属性
            if (value.getType() == null) {
                extraList.add(new EntityLinksRsp.ExtraRsp(-1, value.getName(), value.getDomainValue(), value.getDataValue(), value.getDataType()));
            }
            //数值属性
            else if (value.getType() == 0) {
                extraList.add(dataAttrToExtraRsp(value));
            }
            //私有对象属性
            else if (value.getType() == 1 && !CollectionUtils.isEmpty(value.getObjectValues())) {
                value.getObjectValues().forEach(a -> {
                    Map<Integer, List<BasicInfo>> relationObjectValues = a.getRelationObjectValues();
                    extraList.add(new EntityLinksRsp.ExtraRsp(a.getAttrId(), a.getName(), value.getDomainValue(), relationObjectValues.values(), value.getDataType()));
                });
            }

        }
    }

    private static EntityLinksRsp.ExtraRsp dataAttrToExtraRsp(EntityAttributeValueVO attributeValue) {
        AttributeDataTypeEnum dataType = AttributeDataTypeEnum.parseById(attributeValue.getDataType());
        if (dataType == null) {
            log.error("attributeDefinition:{},不存在", attributeValue.getDataType());
            return new EntityLinksRsp.ExtraRsp(-1, attributeValue.getName(), attributeValue.getDomainValue(), attributeValue.getDataValue(), attributeValue.getDataType());
        }
        EntityLinksRsp.ExtraRsp extraRsp = new EntityLinksRsp.ExtraRsp(attributeValue.getId(), attributeValue.getName(), attributeValue.getDomainValue(), attributeValue.getDataType());
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

    public static BasicDetailFilter batchInfoBoxMultiModalReqToBasicDetailFilter(BatchMultiModalReqList req) {
        BasicDetailFilter detailFilter = new BasicDetailFilter();
        detailFilter.setIds(req.getIds());
        detailFilter.setReadObj(false);
        detailFilter.setReadReverseObj(false);
        detailFilter.setEntity(true);
        return detailFilter;
    }
}
