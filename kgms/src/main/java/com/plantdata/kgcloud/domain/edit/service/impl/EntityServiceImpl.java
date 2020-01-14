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
import ai.plantdata.kg.api.edit.resp.BatchDeleteAttrValueVO;
import ai.plantdata.kg.api.edit.resp.BatchDeleteResult;
import ai.plantdata.kg.api.edit.resp.BatchEntityVO;
import ai.plantdata.kg.api.edit.resp.BatchResult;
import ai.plantdata.kg.api.edit.resp.EntityVO;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.req.EntityTagFrom;
import ai.plantdata.kg.api.pub.req.SearchByAttributeFrom;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.plantdata.graph.logging.core.ServiceEnum;
import com.plantdata.kgcloud.constant.AttributeValueType;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.constant.MongoOperation;
import com.plantdata.kgcloud.constant.TaskStatus;
import com.plantdata.kgcloud.constant.TaskType;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.common.converter.RestCopyConverter;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.OpenEntityConverter;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicInfoListBodyReq;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicInfoListReq;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicReq;
import com.plantdata.kgcloud.domain.edit.req.entity.BatchRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeleteEdgeObjectReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeletePrivateDataReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeleteRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EdgeNumericAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EdgeObjectAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityDeleteReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityMetaDeleteReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityTagSearchReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityTimeModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.GisInfoModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.NumericalAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.ObjectAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.SsrModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.UpdateRelationMetaReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.EntityService;
import com.plantdata.kgcloud.domain.edit.service.LogSender;
import com.plantdata.kgcloud.domain.edit.util.MapperUtils;
import com.plantdata.kgcloud.domain.edit.util.ParserBeanUtils;
import com.plantdata.kgcloud.domain.edit.vo.EntityTagVO;
import com.plantdata.kgcloud.domain.task.entity.TaskGraphStatus;
import com.plantdata.kgcloud.domain.task.req.TaskGraphStatusReq;
import com.plantdata.kgcloud.domain.task.service.TaskGraphStatusService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.producer.KafkaMessageProducer;
import com.plantdata.kgcloud.sdk.req.app.BatchEntityAttrDeleteReq;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.req.edit.BatchPrivateRelationReq;
import com.plantdata.kgcloud.sdk.req.edit.PrivateAttrDataReq;
import com.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    private EntityApi entityApi;
    @Autowired
    private BasicInfoService basicInfoService;
    @Autowired
    private GraphHelperService graphHelperService;

    @Value("${topic.kg.task}")
    private String topicKgTask;

    @Autowired
    private LogSender logSender;

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    @Autowired
    private TaskGraphStatusService taskGraphStatusService;

    @Override
    public void addMultipleConcept(String kgName, Long conceptId, Long entityId) {
        RestRespConverter.convertVoid(conceptEntityApi.addMultipleConcept(KGUtil.dbName(kgName), conceptId, entityId));
    }

    @Override
    public void deleteMultipleConcept(String kgName, Long conceptId, Long entityId) {
        RestRespConverter.convertVoid(conceptEntityApi.deleteMultipleConcept(KGUtil.dbName(kgName), conceptId,
                entityId));
    }

    @Override
    public Page<BasicInfoRsp> listEntities(String kgName, BasicInfoListReq basicInfoListReq,
                                           BasicInfoListBodyReq bodyReq) {
        BasicInfoListFrom basicInfoListFrom = MapperUtils.map(basicInfoListReq, BasicInfoListFrom.class);
        basicInfoListFrom.setMetaData(parserFilterMetadata(basicInfoListReq, bodyReq));
        basicInfoListFrom.setSort(ParserBeanUtils.parserSortMetadata(basicInfoListReq.getSorts()));
        Integer size = basicInfoListReq.getSize();
        Integer page = (basicInfoListReq.getPage() - 1) * size;
        basicInfoListFrom.setSkip(page);
        basicInfoListFrom.setLimit(size);
        RestResp<List<EntityVO>> restResp = conceptEntityApi.list(KGUtil.dbName(kgName),
                basicInfoListFrom);
        Optional<List<EntityVO>> optional = RestRespConverter.convert(restResp);
        Integer count = RestRespConverter.convertCount(restResp).orElse(0);
        List<BasicInfoRsp> basicInfoRspList =
                optional.orElse(new ArrayList<>()).stream().map(ParserBeanUtils::parserEntityVO).collect(Collectors.toList());
        return new PageImpl<>(basicInfoRspList, PageRequest.of(basicInfoListReq.getPage() - 1, size), count);
    }


    /**
     * 解析来源,置信度,批次号,标签,过滤
     *
     * @param basicInfoListReq
     * @return
     */
    private Map<String, Object> parserFilterMetadata(BasicInfoListReq basicInfoListReq, BasicInfoListBodyReq bodyReq) {
        Map<String, Object> filters = new HashMap<>();
        if (Objects.nonNull(bodyReq.getReliability()) && !bodyReq.getReliability().isEmpty()) {
            filters.put(MetaDataInfo.RELIABILITY.getCode(), bodyReq.getReliability());
        }
        if (StringUtils.hasText(bodyReq.getSource())) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.EQUAL.getType(), bodyReq.getSource());
            filters.put(MetaDataInfo.SOURCE.getCode(), operation);
        }
        if (StringUtils.hasText(bodyReq.getBatchNo())) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.EQUAL.getType(), bodyReq.getBatchNo());
            filters.put(MetaDataInfo.BATCH_NO.getCode(), operation);
        }
        if (Objects.nonNull(bodyReq.getTags()) && !bodyReq.getTags().isEmpty()) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.IN.getType(), bodyReq.getTags());
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
    public List<DeleteResult> deleteByIds(String kgName, List<Long> ids) {
        Optional<List<BatchDeleteResult>> optional =
                RestRespConverter.convert(batchApi.deleteEntities(KGUtil.dbName(kgName), ids));
        if (!optional.isPresent() || CollectionUtils.isEmpty(optional.get())) {
            return Collections.emptyList();
        }
        return optional.get().stream().map(a -> ConvertUtils.convert(DeleteResult.class).apply(a)).collect(Collectors.toList());
    }

    @Override
    public Long deleteByConceptId(String kgName, EntityDeleteReq entityDeleteReq) {
        TaskGraphStatusReq taskGraphStatusReq = TaskGraphStatusReq.builder()
                .kgName(kgName)
                .status(TaskStatus.PROCESSING.getStatus())
                .type(TaskType.CLEAR_ENTITY.getType())
                .params(JacksonUtils.readValue(JacksonUtils.writeValueAsString(entityDeleteReq),
                        new TypeReference<Map<String, Object>>() {
                        }))
                .build();
        TaskGraphStatus taskGraphStatus = taskGraphStatusService.create(taskGraphStatusReq);
        kafkaMessageProducer.sendMessage(topicKgTask, taskGraphStatus);
        return taskGraphStatus.getId();
    }

    @Override
    public void updateScoreSourceReliability(String kgName, Long entityId, SsrModifyReq ssrModifyReq) {
        Map<String, Object> metadata = new HashMap<>();
        List<Integer> metaNo = new ArrayList<>(3);
        if (Objects.nonNull(ssrModifyReq.getScore())) {
            metadata.put(MetaDataInfo.SCORE.getFieldName(), ssrModifyReq.getScore());
        } else {
            metaNo.add(Integer.valueOf(MetaDataInfo.SCORE.getCode()));
        }
        if (Objects.nonNull(ssrModifyReq.getSource())) {
            metadata.put(MetaDataInfo.SOURCE.getFieldName(), ssrModifyReq.getSource());
        } else {
            metaNo.add(Integer.valueOf(MetaDataInfo.SOURCE.getCode()));
        }
        if (Objects.nonNull(ssrModifyReq.getReliability())) {
            metadata.put(MetaDataInfo.RELIABILITY.getFieldName(), ssrModifyReq.getReliability());
        } else {
            metaNo.add(Integer.valueOf(MetaDataInfo.RELIABILITY.getCode()));
        }
        if (!metaNo.isEmpty()) {
            conceptEntityApi.deleteMetaData(KGUtil.dbName(kgName), entityId, metaNo);
        }
        conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);
    }

    @Override
    public void updateEntityTime(String kgName, Long entityId, EntityTimeModifyReq entityTimeModifyReq) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName, new BasicReq(entityId, true));

        Map<String, Object> metadata = new HashMap<>();
        String fromTime = entityTimeModifyReq.getFromTime();
        if (StringUtils.hasText(fromTime)) {
            metadata.put(MetaDataInfo.FROM_TIME.getFieldName(), fromTime);
        } else {
            fromTime = details.getFromTime();
        }

        String toTime = entityTimeModifyReq.getToTime();
        if (StringUtils.hasText(toTime)) {
            metadata.put(MetaDataInfo.TO_TIME.getFieldName(), toTime);
        } else {
            toTime = details.getToTime();
        }
        if (StringUtils.hasText(fromTime) && StringUtils.hasText(toTime) && fromTime.compareTo(toTime) > 0) {
            throw BizException.of(KgmsErrorCodeEnum.TIME_FORM_MORE_THAN_TO);
        }
        conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);
    }

    @Override
    public void updateGisInfo(String kgName, Long entityId, GisInfoModifyReq gisInfoModifyReq) {
        Map<String, Object> metadata = new HashMap<>();
        //gis坐标
        List<Double> gisCoordinate = new ArrayList<>(2);
        gisCoordinate.add(0, gisInfoModifyReq.getLongitude());
        gisCoordinate.add(1, gisInfoModifyReq.getLatitude());
        metadata.put(MetaDataInfo.GIS_COORDINATE.getFieldName(), gisCoordinate);
        if (Objects.nonNull(gisInfoModifyReq.getAddress())) {
            metadata.put(MetaDataInfo.GIS_ADDRESS.getFieldName(), gisInfoModifyReq.getAddress());
        }
        conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);
    }

    @Override
    public void deleteByMeta(String kgName, EntityMetaDeleteReq entityMetaDeleteReq) {
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
        MetaDataOptionFrom metaDataOptionFrom = new MetaDataOptionFrom();
        metaDataOptionFrom.setFilters(filters);
        conceptEntityApi.deleteEntityByMetaData(KGUtil.dbName(kgName), 1, metaDataOptionFrom);
    }

    @Override
    public void addEntityTag(String kgName, Long entityId, List<EntityTagVO> vos) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName,
                BasicReq.builder().id(entityId).isEntity(true).build());
        List<EntityTagVO> beforeTags = details.getTags();
        if (Objects.isNull(beforeTags) || beforeTags.isEmpty()) {
            beforeTags = new ArrayList<>();
        }

        List<String> oldNames = beforeTags.stream().map(EntityTagVO::getName).collect(Collectors.toList());
        for (EntityTagVO entityTagVO : vos) {
            if (oldNames.contains(entityTagVO.getName())) {
                throw BizException.of(KgmsErrorCodeEnum.TAG_HAVE_EXISTED);
            }
            beforeTags.add(entityTagVO);
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MetaDataInfo.TAG.getFieldName(), beforeTags);
        conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);

    }

    @Override
    public void updateEntityTag(String kgName, Long entityId, List<EntityTagVO> vos) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName,
                BasicReq.builder().id(entityId).isEntity(true).build());
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
        conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);
    }

    @Override
    public void deleteEntityTag(String kgName, Long entityId, List<String> tagNames) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName,
                BasicReq.builder().id(entityId).isEntity(true).build());
        List<EntityTagVO> beforeTags = details.getTags();
        if (Objects.isNull(beforeTags) || beforeTags.isEmpty()) {
            return;
        }

        Map<String, EntityTagVO> voMap = beforeTags.stream().collect(Collectors.toMap(EntityTagVO::getName,
                Function.identity(), (k1, k2) -> k1));
        tagNames.stream().filter(voMap::containsKey).forEach(voMap::remove);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MetaDataInfo.TAG.getFieldName(), voMap.values());
        conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);
    }

    @Override
    public void addEntityLink(String kgName, Long entityId, List<EntityLinkVO> vos) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName,
                BasicReq.builder().id(entityId).isEntity(true).build());
        Set<EntityLinkVO> entityLinks = details.getEntityLinks();
        if (Objects.isNull(entityLinks) || entityLinks.isEmpty()) {
            entityLinks = new HashSet<>();
        }
        entityLinks.addAll(vos);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MetaDataInfo.ENTITY_LINK.getFieldName(), entityLinks);
        conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);
    }

    @Override
    public void deleteEntityLink(String kgName, Long entityId, List<EntityLinkVO> vos) {
        BasicInfoRsp details = basicInfoService.getDetails(kgName,
                BasicReq.builder().id(entityId).isEntity(true).build());
        Set<EntityLinkVO> entityLinks = details.getEntityLinks();
        if (Objects.isNull(entityLinks) || entityLinks.isEmpty()) {
            return;
        }
        entityLinks.removeAll(vos);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MetaDataInfo.ENTITY_LINK.getFieldName(), entityLinks);
        conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);
    }

    @Override
    public void upsertNumericalAttrValue(String kgName, NumericalAttrValueReq numericalAttrValueReq) {
        AttributeValueFrom attributeValueFrom =
                ConvertUtils.convert(AttributeValueFrom.class).apply(numericalAttrValueReq);
        NumericalAttrValueReq.UrlAttrValue urlAttrValue = numericalAttrValueReq.getUrlAttrValue();
        if (Objects.isNull(numericalAttrValueReq.getAttrValue()) && Objects.nonNull(urlAttrValue)) {
            attributeValueFrom.setAttrValue(JacksonUtils.writeValueAsString(urlAttrValue));
        }
        RestRespConverter.convertVoid(conceptEntityApi.addNumericAttrValue(KGUtil.dbName(kgName), attributeValueFrom));
    }


    @Override
    public String addObjectAttrValue(String kgName, ObjectAttrValueReq objectAttrValueReq) {

        if (StringUtils.hasText(objectAttrValueReq.getAttrTimeFrom()) && StringUtils.hasText(objectAttrValueReq.getAttrTimeTo())
                && objectAttrValueReq.getAttrTimeFrom().compareTo(objectAttrValueReq.getAttrTimeTo()) > 0) {
            throw BizException.of(KgmsErrorCodeEnum.TIME_FORM_MORE_THAN_TO);
        }
        ObjectAttributeValueFrom objectAttributeValueFrom =
                ConvertUtils.convert(ObjectAttributeValueFrom.class).apply(objectAttrValueReq);
        return RestRespConverter.convert(conceptEntityApi.addObjAttrValue(KGUtil.dbName(kgName),
                objectAttributeValueFrom)).orElse(null);

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
        if (StringUtils.hasText(updateRelationMetaReq.getAttrTimeFrom()) && StringUtils.hasText(updateRelationMetaReq.getAttrTimeTo())
                && updateRelationMetaReq.getAttrTimeFrom().compareTo(updateRelationMetaReq.getAttrTimeTo()) > 0) {
            throw BizException.of(KgmsErrorCodeEnum.TIME_FORM_MORE_THAN_TO);
        }
        updateRelationMetaReq.setMetaData(metaData);
        UpdateRelationFrom updateRelationFrom =
                ConvertUtils.convert(UpdateRelationFrom.class).apply(updateRelationMetaReq);
        RestRespConverter.convertVoid(conceptEntityApi.addObjAttrValue(KGUtil.dbName(kgName), updateRelationFrom));
    }

    @Override
    public void deleteObjAttrValue(String kgName, DeleteRelationReq deleteRelationReq) {
        DeleteRelationFrom deleteRelationFrom = ConvertUtils.convert(DeleteRelationFrom.class).apply(deleteRelationReq);
        RestRespConverter.convertVoid(conceptEntityApi.deleteObjAttrValue(KGUtil.dbName(kgName), deleteRelationFrom));
    }

    @Override
    public String addPrivateData(String kgName, PrivateAttrDataReq privateAttrDataReq) {
        if (AttributeValueType.isNumeric(privateAttrDataReq.getType())) {
            logSender.sendLog(kgName, ServiceEnum.ENTITY_EDIT);
        } else {
            logSender.sendLog(kgName, ServiceEnum.RELATION_EDIT);
        }
        AttributePrivateDataFrom privateDataFrom =
                ConvertUtils.convert(AttributePrivateDataFrom.class).apply(privateAttrDataReq);
        return RestRespConverter.convert(conceptEntityApi.addPrivateData(KGUtil.dbName(kgName), privateDataFrom))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.PRIVATE_RELATION_HAS_EXIST));
    }

    @Override
    public void deletePrivateData(String kgName, DeletePrivateDataReq deletePrivateDataReq) {
        if (AttributeValueType.isNumeric(deletePrivateDataReq.getType())) {
            logSender.sendLog(kgName, ServiceEnum.ENTITY_EDIT);
        } else {
            logSender.sendLog(kgName, ServiceEnum.RELATION_EDIT);
        }
        RestRespConverter.convertVoid(conceptEntityApi.deletePrivateData(KGUtil.dbName(kgName),
                deletePrivateDataReq.getType(),
                deletePrivateDataReq.getEntityId(), deletePrivateDataReq.getTripleIds()));
    }

    @Override
    public void addEdgeNumericAttrValue(String kgName, EdgeNumericAttrValueReq edgeNumericAttrValueReq) {
        EdgeValueFrom edgeValueFrom = ConvertUtils.convert(EdgeValueFrom.class).apply(edgeNumericAttrValueReq);
        edgeValueFrom.setObjId(edgeNumericAttrValueReq.getTripleId());
        RestRespConverter.convertVoid(conceptEntityApi.addEdgeNumericAttrValue(KGUtil.dbName(kgName), edgeValueFrom));
    }

    @Override
    public void addEdgeObjectAttrValue(String kgName, EdgeObjectAttrValueReq edgeObjectAttrValueReq) {
        EdgeObjectValueFrom edgeObjectValueFrom =
                ConvertUtils.convert(EdgeObjectValueFrom.class).apply(edgeObjectAttrValueReq);
        edgeObjectValueFrom.setObjId(edgeObjectAttrValueReq.getTripleId());
        RestRespConverter.convertVoid(conceptEntityApi.addEdgeObjectAttrValue(KGUtil.dbName(kgName),
                edgeObjectValueFrom));
    }

    @Override
    public void deleteEdgeObjectAttrValue(String kgName, DeleteEdgeObjectReq deleteEdgeObjectReq) {
        EdgeObjectValueFrom edgeObjectValueFrom =
                ConvertUtils.convert(EdgeObjectValueFrom.class).apply(deleteEdgeObjectReq);
        RestRespConverter.convertVoid(conceptEntityApi.deleteEdgeObjectAttrValue(KGUtil.dbName(kgName),
                edgeObjectValueFrom));
    }

    @Override
    public List<String> batchAddRelation(String kgName, BatchRelationReq batchRelationReq) {
        EntityRelationFrom entityRelationFrom = ConvertUtils.convert(EntityRelationFrom.class).apply(batchRelationReq);
        Optional<List<String>> optional =
                RestRespConverter.convert(conceptEntityApi.batchAddRelation(KGUtil.dbName(kgName),
                        entityRelationFrom));
        return optional.orElse(new ArrayList<>());
    }

    @Override
    public List<String> batchAddPrivateRelation(String kgName, BatchPrivateRelationReq batchPrivateRelationReq) {
        EntityPrivateRelationFrom entityPrivateRelationFrom =
                ConvertUtils.convert(EntityPrivateRelationFrom.class).apply(batchPrivateRelationReq);
        Optional<List<String>> optional =
                RestRespConverter.convert(conceptEntityApi.batchAddRelation(KGUtil.dbName(kgName),
                        entityPrivateRelationFrom));
        return optional.orElse(new ArrayList<>());
    }

    @Override
    public List<String> tagSearch(String kgName, EntityTagSearchReq entityTagSearchReq) {
        Optional<List<String>> optional = RestRespConverter.convert(entityApi.tagList(KGUtil.dbName(kgName),
                MapperUtils.map(entityTagSearchReq, EntityTagFrom.class)));
        return optional.orElse(new ArrayList<>());
    }

    @Override
    public List<OpenEntityRsp> queryEntityList(String kgName, EntityQueryReq entityQueryReq) {
        if (entityQueryReq.getConceptId() == null && !StringUtils.isEmpty(entityQueryReq.getConceptKey())) {
            List<Long> longList = graphHelperService.queryConceptByKey(kgName,
                    Lists.newArrayList(entityQueryReq.getConceptKey()));
            BasicConverter.consumerIfNoNull(longList, a -> entityQueryReq.setConceptId(a.get(0)));
        }
        SearchByAttributeFrom attributeFrom = EntityConverter.entityQueryReqToSearchByAttributeFrom(entityQueryReq);
        Optional<List<ai.plantdata.kg.api.pub.resp.EntityVO>> entityOpt =
                RestRespConverter.convert(entityApi.searchByAttribute(KGUtil.dbName(kgName), attributeFrom));
        return entityOpt.orElse(new ArrayList<>()).stream().map(EntityConverter::voToOpenEntityRsp)
                .collect(Collectors.toList());
    }

    @Override
    public OpenBatchResult<OpenBatchSaveEntityRsp> saveOrUpdate(String kgName, boolean add,
                                                                List<OpenBatchSaveEntityRsp> batchEntity) {
        List<BatchEntityVO> entityList = BasicConverter.listToRsp(batchEntity,
                OpenEntityConverter::openBatchSaveEntityRspToVo);

        Optional<BatchResult<BatchEntityVO>> editOpt =
                RestRespConverter.convert(batchApi.addEntities(KGUtil.dbName(kgName),
                        add, entityList));
        return editOpt.map(result -> RestCopyConverter.copyToBatchResult(result, OpenBatchSaveEntityRsp.class))
                .orElseGet(OpenBatchResult::empty);
    }

    @Override
    public void batchDeleteEntityAttr(String kgName, BatchEntityAttrDeleteReq deleteReq) {
        BatchDeleteAttrValueVO deleteAttrValueVO = new BatchDeleteAttrValueVO();
        deleteAttrValueVO.setAttributeIds(deleteReq.getAttributeIds());
        deleteAttrValueVO.setAttrNames(deleteReq.getAttrNames());
        deleteAttrValueVO.setEntityIds(deleteReq.getEntityIds());
        RestRespConverter.convertVoid(batchApi.deleteEntities(KGUtil.dbName(kgName), deleteAttrValueVO));
    }
}
