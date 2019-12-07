package com.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.req.AttributePrivateDataFrom;
import ai.plantdata.kg.api.edit.req.AttributeValueFrom;
import ai.plantdata.kg.api.edit.req.BasicInfoListFrom;
import ai.plantdata.kg.api.edit.req.DeleteRelationFrom;
import ai.plantdata.kg.api.edit.req.EdgeObjectValueFrom;
import ai.plantdata.kg.api.edit.req.EdgeValueFrom;
import ai.plantdata.kg.api.edit.req.EntityPrivateRelationFrom;
import ai.plantdata.kg.api.edit.req.EntityRelationFrom;
import ai.plantdata.kg.api.edit.req.MetaDataOptionFrom;
import ai.plantdata.kg.api.edit.req.ObjectAttributeValueFrom;
import ai.plantdata.kg.api.edit.req.UpdateRelationFrom;
import ai.plantdata.kg.api.edit.resp.BatchDeleteResult;
import ai.plantdata.kg.api.edit.resp.EntityVO;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.constant.MongoOperation;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicInfoListReq;
import com.plantdata.kgcloud.domain.edit.req.entity.BatchPrivateRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.BatchRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeleteEdgeObjectReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeletePrivateDataReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeleteRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EdgeNumericAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EdgeObjectAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityDeleteReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityMetaDeleteReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityTimeModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.GisInfoModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.NumericalAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.ObjectAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.PrivateAttrDataReq;
import com.plantdata.kgcloud.domain.edit.req.entity.SsrModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.UpdateRelationMetaReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.EntityService;
import com.plantdata.kgcloud.domain.edit.util.ParserBeanUtils;
import com.plantdata.kgcloud.domain.edit.vo.EntityLinkVO;
import com.plantdata.kgcloud.domain.edit.vo.EntityTagVO;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 18:34
 * @Description:
 */
@Service
public class EntityServiceImpl implements EntityService {

    @Autowired
    private ConceptEntityApi conceptEntityApi;

    @Autowired
    private BatchApi batchApi;

    @Autowired
    private BasicInfoService basicInfoService;

    @Override
    public void addMultipleConcept(String kgName, Long conceptId, Long entityId) {
        RestRespConverter.convertVoid(conceptEntityApi.addMultipleConcept(kgName, conceptId, entityId));
    }

    @Override
    public void deleteMultipleConcept(String kgName, Long conceptId, Long entityId) {
        RestRespConverter.convertVoid(conceptEntityApi.deleteMultipleConcept(kgName, conceptId, entityId));
    }

    @Override
    public Page<BasicInfoRsp> listEntities(String kgName, BasicInfoListReq basicInfoListReq) {
        BasicInfoListFrom basicInfoListFrom = ConvertUtils.convert(BasicInfoListFrom.class).apply(basicInfoListReq);
        basicInfoListFrom.setMetaData(parserFilterMetadata(basicInfoListReq));
        basicInfoListFrom.setSort(ParserBeanUtils.parserSortMetadata(basicInfoListReq.getSorts()));
        basicInfoListFrom.setSkip(basicInfoListReq.getPage());
        basicInfoListFrom.setLimit(basicInfoListReq.getSize() + 1);
        Optional<List<EntityVO>> optional = RestRespConverter.convert(conceptEntityApi.list(kgName, basicInfoListFrom));
        List<BasicInfoRsp> basicInfoRspList =
                optional.orElse(new ArrayList<>()).stream().map(ParserBeanUtils::parserEntityVO).collect(Collectors.toList());

        Page<BasicInfoRsp> pages;
        if (basicInfoRspList.size() > basicInfoListReq.getSize()) {
            pages = new PageImpl<>(basicInfoRspList, PageRequest.of(basicInfoListReq.getPage() - 1,
                    basicInfoListReq.getSize()), basicInfoListReq.getSize() + 1);
        } else {
            pages = new PageImpl<>(basicInfoRspList, PageRequest.of(basicInfoListReq.getPage() - 1,
                    basicInfoListReq.getSize()), basicInfoListReq.getSize() - 1);
        }
        return pages;
    }


    /**
     * 解析来源,置信度,批次号,标签,过滤
     *
     * @param basicInfoListReq
     * @return
     */
    private Map<String, Object> parserFilterMetadata(BasicInfoListReq basicInfoListReq) {
        Map<String, Object> filters = new HashMap<>();
        if (Objects.nonNull(basicInfoListReq.getReliability())) {
            filters.put(MetaDataInfo.RELIABILITY.getCode(), basicInfoListReq.getReliability());
        }
        if (StringUtils.hasText(basicInfoListReq.getSource())) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.EQUAL.getType(), basicInfoListReq.getSource());
            filters.put(MetaDataInfo.SOURCE.getCode(), operation);
        }
        if (StringUtils.hasText(basicInfoListReq.getBatchNo())) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.EQUAL.getType(), basicInfoListReq.getBatchNo());
            filters.put(MetaDataInfo.BATCH_NO.getCode(), operation);
        }
        if (Objects.nonNull(basicInfoListReq.getTags()) && !basicInfoListReq.getTags().isEmpty()) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.IN.getType(), basicInfoListReq.getTags());
            filters.put(MetaDataInfo.TAG.getCode(), operation);
        }
        return filters;
    }

    /**
     * 解析排序
     *
     * @param sortWay
     * @return
     */
    private int parserSort(String sortWay) {
        if (!(MongoOperation.DESC.getType()).equals(sortWay.toLowerCase())) {
            return 1;
        }
        return -1;
    }

    @Override
    public List<BatchDeleteResult> deleteByIds(String kgName, List<Long> ids) {
        Optional<List<BatchDeleteResult>> optional = RestRespConverter.convert(batchApi.deleteEntities(kgName, ids));
        return optional.orElse(new ArrayList<>());
    }

    @Override
    public void deleteByConceptId(String kgName, EntityDeleteReq entityDeleteReq) {
        conceptEntityApi.deleteEntities(kgName, entityDeleteReq.isInherit(), entityDeleteReq.getConceptId());
    }

    @Override
    public void updateScoreSourceReliability(String kgName, Long entityId, SsrModifyReq ssrModifyReq) {
        Map<String, Object> metadata = new HashMap<>();
        if (Objects.nonNull(ssrModifyReq.getScore())) {
            metadata.put(MetaDataInfo.SCORE.getFieldName(), ssrModifyReq.getScore());
        }
        if (Objects.nonNull(ssrModifyReq.getSource())) {
            metadata.put(MetaDataInfo.SOURCE.getFieldName(), ssrModifyReq.getSource());
        }
        if (Objects.nonNull(ssrModifyReq.getReliability())) {
            metadata.put(MetaDataInfo.RELIABILITY.getFieldName(), ssrModifyReq.getReliability());
        }
        conceptEntityApi.updateMetaData(kgName, entityId, metadata);
    }

    @Override
    public void updateEntityTime(String kgName, Long entityId, EntityTimeModifyReq entityTimeModifyReq) {

        Map<String, Object> metadata = new HashMap<>();
        if (Objects.nonNull(entityTimeModifyReq.getFromTime())) {
            metadata.put(MetaDataInfo.FROM_TIME.getFieldName(), entityTimeModifyReq.getFromTime());
        }

        if (Objects.nonNull(entityTimeModifyReq.getToTime())) {
            metadata.put(MetaDataInfo.TO_TIME.getFieldName(), entityTimeModifyReq.getToTime());
        }
        conceptEntityApi.updateMetaData(kgName, entityId, metadata);
    }

    @Override
    public void updateGisInfo(String kgName, Long entityId, GisInfoModifyReq gisInfoModifyReq) {
        Map<String, Object> metadata = new HashMap<>();
        //gis坐标
        List<Double> gisCoordinate = new ArrayList<>(2);
        gisCoordinate.add(0, gisInfoModifyReq.getLongitude());
        gisCoordinate.add(1, gisInfoModifyReq.getLatitude());
        metadata.put(MetaDataInfo.GIS_COORDINATE.getFieldName(), gisCoordinate);
        metadata.put(MetaDataInfo.GIS_ADDRESS.getFieldName(), gisInfoModifyReq.getAddress());
        conceptEntityApi.updateMetaData(kgName, entityId, metadata);
    }

    @Override
    public void deleteByMeta(String kgName, EntityMetaDeleteReq entityMetaDeleteReq) {
        Map<String, Object> metadataOption = new HashMap<>();
        Map<String, Object> filters = new HashMap<>();

        if (StringUtils.hasText(entityMetaDeleteReq.getSource())) {
            Map<String, Object> option = new HashMap<>();
            option.put(MongoOperation.EQUAL.getType(), entityMetaDeleteReq.getSource());
            filters.put(MetaDataInfo.SOURCE.getCode(), option);
        }
        if (StringUtils.hasText(entityMetaDeleteReq.getBatchNo())) {
            Map<String, Object> option = new HashMap<>();
            option.put(MongoOperation.EQUAL.getType(), entityMetaDeleteReq.getBatchNo());
            filters.put(MetaDataInfo.BATCH_NO.getCode(), option);
        }
        metadataOption.put("filters", filters);
        MetaDataOptionFrom metaDataOptionFrom = new MetaDataOptionFrom();
        metaDataOptionFrom.setFilters(metadataOption);
        conceptEntityApi.deleteEntityByMetaData(kgName, 1, metaDataOptionFrom);
    }

    @Override
    public void addEntityTag(String kgName, Long entityId, List<EntityTagVO> vos) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName, entityId);
        List<EntityTagVO> beforeTags = details.getTags();
        if (Objects.isNull(beforeTags) || beforeTags.isEmpty()) {
            beforeTags = new ArrayList<>();
        }
        beforeTags.addAll(vos);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MetaDataInfo.TAG.getFieldName(), beforeTags);
        conceptEntityApi.updateMetaData(kgName, entityId, metadata);

    }

    @Override
    public void updateEntityTag(String kgName, Long entityId, List<EntityTagVO> vos) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName, entityId);
        List<EntityTagVO> beforeTags = details.getTags();
        if (Objects.isNull(beforeTags) || beforeTags.isEmpty()) {
            return;
        }
        Map<String, EntityTagVO> voMap = beforeTags.stream().collect(Collectors.toMap(EntityTagVO::getName,
                Function.identity()));
        vos.stream().filter(entityTagVO -> !voMap.containsKey(entityTagVO.getName()))
                .forEach(entityTagVO -> voMap.put(entityTagVO.getName(), entityTagVO));
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MetaDataInfo.TAG.getFieldName(), voMap.values());
        conceptEntityApi.updateMetaData(kgName, entityId, metadata);
    }

    @Override
    public void deleteEntityTag(String kgName, Long entityId, List<String> tagNames) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName, entityId);
        List<EntityTagVO> beforeTags = details.getTags();
        if (Objects.isNull(beforeTags) || beforeTags.isEmpty()) {
            return;
        }
        Map<String, EntityTagVO> voMap = beforeTags.stream().collect(Collectors.toMap(EntityTagVO::getName,
                Function.identity()));
        tagNames.stream().filter(voMap::containsKey).forEach(voMap::remove);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MetaDataInfo.TAG.getFieldName(), voMap.values());
        conceptEntityApi.updateMetaData(kgName, entityId, metadata);
    }

    @Override
    public void addEntityLink(String kgName, Long entityId, List<EntityLinkVO> vos) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName, entityId);
        Set<EntityLinkVO> entityLinks = details.getEntityLinks();
        if (Objects.isNull(entityLinks) || entityLinks.isEmpty()) {
            entityLinks = new HashSet<>();
        }
        entityLinks.addAll(vos);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MetaDataInfo.ENTITY_LINK.getFieldName(), entityLinks);
        conceptEntityApi.updateMetaData(kgName, entityId, metadata);
    }

    @Override
    public void deleteEntityLink(String kgName, Long entityId, List<EntityLinkVO> vos) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName, entityId);
        Set<EntityLinkVO> entityLinks = details.getEntityLinks();
        if (Objects.isNull(entityLinks) || entityLinks.isEmpty()) {
            return;
        }
        entityLinks.removeAll(vos);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MetaDataInfo.ENTITY_LINK.getFieldName(), entityLinks);
        conceptEntityApi.updateMetaData(kgName, entityId, metadata);
    }

    @Override
    public void upsertNumericalAttrValue(String kgName, NumericalAttrValueReq numericalAttrValueReq) {
        AttributeValueFrom attributeValueFrom =
                ConvertUtils.convert(AttributeValueFrom.class).apply(numericalAttrValueReq);
        RestRespConverter.convertVoid(conceptEntityApi.addNumericAttrValue(kgName, attributeValueFrom));
    }

    @Override
    public void addObjectAttrValue(String kgName, ObjectAttrValueReq objectAttrValueReq) {
        ObjectAttributeValueFrom objectAttributeValueFrom =
                ConvertUtils.convert(ObjectAttributeValueFrom.class).apply(objectAttrValueReq);
        RestRespConverter.convertVoid(conceptEntityApi.addObjAttrValue(kgName, objectAttributeValueFrom));
    }

    @Override
    public void updateRelationMeta(String kgName, UpdateRelationMetaReq updateRelationMetaReq) {
        Map<String, Object> metaData = new HashMap<>();
        if (Objects.nonNull(updateRelationMetaReq.getScore())) {
            metaData.put(MetaDataInfo.SCORE.getFieldName(), updateRelationMetaReq.getScore());
        }
        if (StringUtils.hasText(updateRelationMetaReq.getSource())) {
            metaData.put(MetaDataInfo.SOURCE.getFieldName(), updateRelationMetaReq.getSource());
        }
        if (Objects.nonNull(updateRelationMetaReq.getReliability())) {
            metaData.put(MetaDataInfo.RELIABILITY.getFieldName(), updateRelationMetaReq.getReliability());
        }
        if (StringUtils.hasText(updateRelationMetaReq.getSourceReason())) {
            metaData.put(MetaDataInfo.SOURCE_REASON.getFieldName(), updateRelationMetaReq.getSourceReason());
        }
        updateRelationMetaReq.setMetaData(metaData);
        UpdateRelationFrom updateRelationFrom =
                ConvertUtils.convert(UpdateRelationFrom.class).apply(updateRelationMetaReq);
        RestRespConverter.convertVoid(conceptEntityApi.addObjAttrValue(kgName, updateRelationFrom));
    }

    @Override
    public void deleteObjAttrValue(String kgName, DeleteRelationReq deleteRelationReq) {
        DeleteRelationFrom deleteRelationFrom = ConvertUtils.convert(DeleteRelationFrom.class).apply(deleteRelationReq);
        RestRespConverter.convertVoid(conceptEntityApi.deleteObjAttrValue(kgName, deleteRelationFrom));
    }

    @Override
    public void addPrivateData(String kgName, PrivateAttrDataReq privateAttrDataReq) {
        AttributePrivateDataFrom privateDataFrom =
                ConvertUtils.convert(AttributePrivateDataFrom.class).apply(privateAttrDataReq);
        RestRespConverter.convertVoid(conceptEntityApi.addPrivateData(kgName, privateDataFrom));
    }

    @Override
    public void deletePrivateData(String kgName, DeletePrivateDataReq deletePrivateDataReq) {
        RestRespConverter.convertVoid(conceptEntityApi.deletePrivateData(kgName, deletePrivateDataReq.getType(),
                deletePrivateDataReq.getEntityId(), deletePrivateDataReq.getTripleIds()));
    }

    @Override
    public void addEdgeNumericAttrValue(String kgName, EdgeNumericAttrValueReq edgeNumericAttrValueReq) {
        EdgeValueFrom edgeValueFrom = ConvertUtils.convert(EdgeValueFrom.class).apply(edgeNumericAttrValueReq);
        RestRespConverter.convertVoid(conceptEntityApi.addEdgeNumericAttrValue(kgName, edgeValueFrom));
    }

    @Override
    public void addEdgeObjectAttrValue(String kgName, EdgeObjectAttrValueReq edgeObjectAttrValueReq) {
        Map<Integer, List<Long>> object = new HashMap<>(10);
        object.put(edgeObjectAttrValueReq.getSeqNo(), edgeObjectAttrValueReq.getRanges());
        edgeObjectAttrValueReq.setObject(object);
        UpdateRelationFrom updateRelationFrom =
                ConvertUtils.convert(UpdateRelationFrom.class).apply(edgeObjectAttrValueReq);
        RestRespConverter.convertVoid(conceptEntityApi.addObjAttrValue(kgName, updateRelationFrom));
    }

    @Override
    public void deleteEdgeObjectAttrValue(String kgName, DeleteEdgeObjectReq deleteEdgeObjectReq) {
        EdgeObjectValueFrom edgeObjectValueFrom =
                ConvertUtils.convert(EdgeObjectValueFrom.class).apply(deleteEdgeObjectReq);
        RestRespConverter.convertVoid(conceptEntityApi.deleteEdgeObjectAttrValue(kgName, edgeObjectValueFrom));
    }

    @Override
    public List<String> batchAddRelation(String kgName, BatchRelationReq batchRelationReq) {
        EntityRelationFrom entityRelationFrom = ConvertUtils.convert(EntityRelationFrom.class).apply(batchRelationReq);
        Optional<List<String>> optional = RestRespConverter.convert(conceptEntityApi.batchAddRelation(kgName,
                entityRelationFrom));
        return optional.orElse(new ArrayList<>());
    }

    @Override
    public List<String> batchAddPrivateRelation(String kgName, BatchPrivateRelationReq batchPrivateRelationReq) {
        EntityPrivateRelationFrom entityPrivateRelationFrom =
                ConvertUtils.convert(EntityPrivateRelationFrom.class).apply(batchPrivateRelationReq);
        Optional<List<String>> optional = RestRespConverter.convert(conceptEntityApi.batchAddRelation(kgName,
                entityPrivateRelationFrom));
        return optional.orElse(new ArrayList<>());
    }
}
