package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.FilterRelationFrom;
import ai.plantdata.kg.api.pub.req.KgServiceEntityFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.RelationAttrReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.DataLinkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.EntityLinksRsp;
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
public class InfoBoxConverter {


    public static KgServiceEntityFrom batchInfoBoxReqToKgServiceEntityFrom(BatchInfoBoxReq boxReq) {
        KgServiceEntityFrom entityFrom = new KgServiceEntityFrom();
        entityFrom.setIds(boxReq.getEntityIdList());
        entityFrom.setReadObjectAttribute(boxReq.getRelationAttrs());
        entityFrom.setReadMetaData(true);
        entityFrom.setReadReverseObjectAttribute(boxReq.getReverseRelationAttrs());
        entityFrom.setAllowAtts(boxReq.getAllowAttrs());
        return entityFrom;
    }

    public static FilterRelationFrom reqToFilterRelationFrom(BasicGraphExploreRsp rsp, List<RelationAttrReq> attrReqList, List<RelationAttrReq> revserdAttrReqList) {
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
        if (!CollectionUtils.isEmpty(revserdAttrReqList)) {
            relationFrom.setMetaFilters(Maps.newHashMap(ConditionConverter.relationAttrReqToMap(revserdAttrReqList)));
        }
        return relationFrom;
    }

    public static List<InfoBoxRsp> voToInfoBox(@NonNull List<EntityVO> entityList, List<AttributeDefinition> attrDefList,
                                               List<ai.plantdata.kg.api.edit.resp.EntityVO> relationEntityList) {
        Map<Integer, AttributeDefinition> definitionMap = attrDefList.stream().collect(Collectors.toMap(AttributeDefinition::getId, Function.identity()));
        Map<Long, ai.plantdata.kg.api.edit.resp.EntityVO> entityMap = relationEntityList.stream().collect(Collectors.toMap(ai.plantdata.kg.api.edit.resp.EntityVO::getId, Function.identity()));
        return entityList.stream().map(entity -> voToInfoBoxRsp(entity, definitionMap, entityMap)).collect(Collectors.toList());
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
        //todo 设置父概念
        return infoBoxRsp;
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
        //私有属性
        if (!CollectionUtils.isEmpty(entity.getPrivateDataAttributes())) {

        }
        if (!CollectionUtils.isEmpty(entity.getPrivateObjectAttributes())) {

        }
        if (!CollectionUtils.isEmpty(entity.getPrivateReverseObjectAttributes())) {

        }
        self.setExtraList(extraList);
        return self;
    }

    /**
     * @param extraList   属性
     * @param dataAttrMap k->attrId v->attrVal
     */
    private static void fillDataAttr(List<EntityLinksRsp.ExtraRsp> extraList, Map<String, Object> dataAttrMap, Map<Integer, AttributeDefinition> attrDefMap) {

        for (Map.Entry<String, Object> entry : dataAttrMap.entrySet()) {
            AttributeDefinition definition = attrDefMap.get(Integer.parseInt(entry.getKey()));
            if (definition == null) {
                log.error("attributeDefinition:{},不存在", entry.getKey());
                continue;
            }
            AttributeDataTypeEnum dataType = AttributeDataTypeEnum.parseById(definition.getDataType());
            if (null == dataType) {
                continue;
            }
            switch (dataType) {
                case IMAGE:
                case URL:
                    extraList.add(new EntityLinksRsp.ExtraRsp(definition.getId(), definition.getName(), JacksonUtils.readValue(JacksonUtils.writeValueAsString(entry.getValue()), ImageRsp.class)));
                    break;
                default:
                    extraList.add(new EntityLinksRsp.ExtraRsp(definition.getId(), definition.getName(), StringUtils.isNotEmpty(definition.getDataUnit()) ? entry.getValue() + definition.getDataUnit() : entry.getValue()));
            }
        }
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

    private static PromptEntityRsp voToPromptEntityRsp(@NonNull ai.plantdata.kg.api.edit.resp.EntityVO entityVO) {
        PromptEntityRsp entityRsp = new PromptEntityRsp();
        entityRsp.setConceptId(entityVO.getConceptId());
        entityRsp.setId(entityVO.getId());
        entityRsp.setMeaningTag(entityRsp.getMeaningTag());
        entityRsp.setName(entityRsp.getName());
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

}
