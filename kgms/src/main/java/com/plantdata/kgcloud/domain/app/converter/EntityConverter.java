package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.EntityFilterFrom;
import ai.plantdata.kg.api.pub.req.KgServiceEntityFrom;
import ai.plantdata.kg.api.pub.req.SearchByAttributeFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.GisEntityVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.hiekn.pddocument.bean.element.PdEntity;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphCommonConverter;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisInfoRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NamedEntityRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public static EntityFilterFrom buildEntityFilterFrom(List<Long> entityIdSet, Map<String, Object> kvMap) {
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
        entity.setType(EntityTypeEnum.ENTITY.getValue());
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


    public static SearchByAttributeFrom entityQueryReqToSearchByAttributeFrom(EntityQueryReq entityQueryReq) {
        SearchByAttributeFrom attributeFrom = new SearchByAttributeFrom();
        if (StringUtils.isNoneBlank(entityQueryReq.getQuery())) {
            attributeFrom.setKvMap(JacksonUtils.readValue(entityQueryReq.getQuery(), new TypeReference<Map<String, Object>>() {
            }));
        }

        attributeFrom.setLimit(entityQueryReq.getLimit());
        attributeFrom.setSkip(entityQueryReq.getOffset());
        attributeFrom.setConceptIds(Lists.newArrayList(entityQueryReq.getConceptId()));
        return attributeFrom;
    }

    public static NamedEntityRsp pdEntityToNamedEntityRsp(@NonNull PdEntity pdEntity) {
        NamedEntityRsp namedEntity = new NamedEntityRsp();
        namedEntity.setName(pdEntity.getName());
        namedEntity.setTag(pdEntity.getTag());
        namedEntity.setPos(pdEntity.getIndex());
        return namedEntity;
    }


}
