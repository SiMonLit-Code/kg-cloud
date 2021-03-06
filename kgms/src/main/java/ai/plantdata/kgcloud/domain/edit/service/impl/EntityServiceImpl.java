package ai.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.kafka.producer.KafkaMessageProducer;
import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.cloud.web.util.SessionHolder;
import ai.plantdata.kg.api.edit.AttributeApi;
import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.req.*;
import ai.plantdata.kg.api.edit.resp.*;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.req.EntityTagFrom;
import ai.plantdata.kg.api.pub.req.SearchByAttributeFrom;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kgcloud.constant.*;
import ai.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import ai.plantdata.kgcloud.domain.edit.checker.EntityChecker;
import ai.plantdata.kgcloud.domain.edit.converter.OpenEntityConverter;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.domain.edit.req.entity.*;
import ai.plantdata.kgcloud.domain.file.entity.FileData;
import ai.plantdata.kgcloud.domain.file.req.FileDataReq;
import ai.plantdata.kgcloud.domain.file.service.FileDataService;
import ai.plantdata.kgcloud.sdk.UserClient;
import ai.plantdata.kgcloud.sdk.rsp.UserDetailRsp;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.plantdata.graph.logging.core.GraphLog;
import com.plantdata.graph.logging.core.GraphLogOperation;
import com.plantdata.graph.logging.core.GraphLogScope;
import com.plantdata.graph.logging.core.ServiceEnum;
import com.plantdata.graph.logging.core.segment.EntityMultiDataSegment;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.app.converter.EntityConverter;
import ai.plantdata.kgcloud.domain.app.service.GraphHelperService;
import ai.plantdata.kgcloud.domain.common.converter.RestCopyConverter;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.domain.edit.entity.MultiModal;
import ai.plantdata.kgcloud.domain.edit.req.basic.BasicInfoListBodyReq;
import ai.plantdata.kgcloud.domain.edit.req.basic.BasicInfoListReq;
import ai.plantdata.kgcloud.domain.edit.req.basic.BasicReq;
import ai.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import ai.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import ai.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import ai.plantdata.kgcloud.domain.edit.service.EntityService;
import ai.plantdata.kgcloud.domain.edit.service.LogSender;
import ai.plantdata.kgcloud.domain.edit.util.MapperUtils;
import ai.plantdata.kgcloud.domain.edit.util.MetaDataUtils;
import ai.plantdata.kgcloud.domain.edit.util.ParserBeanUtils;
import ai.plantdata.kgcloud.domain.edit.util.ThreadLocalUtils;
import ai.plantdata.kgcloud.domain.edit.entity.TaskGraphStatus;
import ai.plantdata.kgcloud.domain.edit.req.task.TaskGraphStatusReq;
import ai.plantdata.kgcloud.domain.edit.service.TaskGraphStatusService;
import ai.plantdata.kgcloud.sdk.req.app.BatchEntityAttrDeleteReq;
import ai.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import ai.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import ai.plantdata.kgcloud.sdk.req.edit.BatchPrivateRelationReq;
import ai.plantdata.kgcloud.sdk.req.edit.PrivateAttrDataReq;
import ai.plantdata.kgcloud.sdk.rsp.BasicInfoRsp;
import ai.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import ai.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import ai.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import ai.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import ai.plantdata.kgcloud.sdk.rsp.vo.EntityAttrValueVO;
import ai.plantdata.kgcloud.sdk.rsp.vo.EntityTagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
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

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private DocumentConverter documentConverter;

    @Autowired
    private EntityFileRelationService entityFileRelationService;

    @Autowired
    private FileDataService fileDataService;

    @Autowired
    private UserClient userClient;
    @Autowired
    private AttributeApi attributeApi;

    @Override
    public void addMultipleConcept(String kgName, Long conceptId, Long entityId) {
        RestRespConverter.convertVoid(conceptEntityApi.addMultipleConcept(KGUtil.dbName(kgName), conceptId, entityId));
    }

    @Override
    public void deleteMultipleConcept(String kgName, Long conceptId, Long entityId) {
        RestRespConverter.convertVoid(conceptEntityApi.deleteMultipleConcept(KGUtil.dbName(kgName), conceptId,
                entityId));
    }

    private void sendMsg(String kgName, MultiModal multiModal, GraphLogOperation operation) {
        if (!logSender.isEnableLog()) {
            return;
        }
        String kgDbName = KGUtil.dbName(kgName);
        logSender.sendLog(kgName, ServiceEnum.ENTITY_EDIT);
        GraphLog graphLog = new GraphLog();
        graphLog.setBatch(ThreadLocalUtils.getBatchNo());
        graphLog.setScope(GraphLogScope.MULTI_DATA);
        if (GraphLogOperation.DELETE.equals(operation)) {
            graphLog.setOldValue(transform(multiModal));
        } else {
            graphLog.setNewValue(transform(multiModal));
        }
        graphLog.setOperation(operation);
        logSender.sendKgLog(kgDbName, graphLog);
    }

    private EntityMultiDataSegment transform(MultiModal modal) {
        EntityMultiDataSegment segment = new EntityMultiDataSegment();
        segment.setId(modal.getEntityId());
        segment.setDataHref(modal.getDataHref());
        segment.setName(modal.getName());
        segment.setType(modal.getType());
        segment.setThumpPath(modal.getThumbPath());
        return segment;
    }

    @Override
    public MultiModalRsp addMultiModal(String kgName, MultiModalReq multiModalReq) {
        logSender.setActionId();

        // if (entityFileRelationService.checkSize(kgName, multiModalReq.getEntityId())) {
        //     throw BizException.of(KgmsErrorCodeEnum.FILE_SIZE_OVER);
        // }

        FileData fileData = ConvertUtils.convert(FileData.class).apply(multiModalReq);
        if (multiModalReq.getUploadType() == 1) {
            FileData file = fileDataService.get(multiModalReq.getFileId());
            List<String> kgNames = file.getKgNames();
            if (kgNames == null) {
                kgNames = Lists.newArrayList(kgName);
            } else if (!kgNames.contains(kgName)) {
                kgNames.add(kgName);
            }
            file.setKgNames(kgNames);

            // 在文件中添加标引关系的图谱名称
            fileDataService.update(file);

            // 创建实体文件关联
            EntityFileRelationReq relation = ConvertUtils.convert(EntityFileRelationReq.class).apply(multiModalReq);
            relation.setIndexType(0);
            relation.setTitle(multiModalReq.getName() + "." + multiModalReq.getType());
            entityFileRelationService.createRelation(kgName, relation);
        } else if (multiModalReq.getFileSystemId() != null && multiModalReq.getFolderId() != null) {
            // 创建数仓文件记录
            FileDataReq fileDataReq = ConvertUtils.convert(FileDataReq.class).apply(multiModalReq);
            fileDataReq.setFileName(multiModalReq.getName() + "." + multiModalReq.getType());
            fileDataReq.setKgNames(Lists.newArrayList(kgName));
            FileData fileAdd = fileDataService.fileAdd(fileDataReq);
            // 创建实体文件关联
            EntityFileRelationReq relation = ConvertUtils.convert(EntityFileRelationReq.class).apply(multiModalReq);
            relation.setFileId(fileAdd.getId());
            relation.setIndexType(0);
            relation.setTitle(multiModalReq.getName() + "." + multiModalReq.getType());
            entityFileRelationService.createRelation(kgName, relation);
        }

        MultiModal multiModal = ConvertUtils.convert(MultiModal.class).apply(fileData);
        multiModal.setEntityId(multiModalReq.getEntityId());
        sendMsg(kgName, multiModal, GraphLogOperation.ADD);
        logSender.remove();
        return ConvertUtils.convert(MultiModalRsp.class).apply(fileData);
    }

    @Override
    public void batchAddMultiModal(String kgName, List<MultiModalReq> multiModalReqs) {
        logSender.setActionId();
        List<MultiModal> multiModals =
                multiModalReqs.stream().map(ConvertUtils.convert(MultiModal.class)).collect(Collectors.toList());

        for (MultiModalReq multiModalReq : multiModalReqs) {

            // if (entityFileRelationService.checkSize(kgName, multiModalReq.getEntityId())) {
            //     throw BizException.of(KgmsErrorCodeEnum.FILE_SIZE_OVER);
            // }
            // if (entityFileRelationService.checkExist(kgName, multiModalReq.getEntityId(), multiModalReq.getFileId())) {
            //     throw BizException.of(KgmsErrorCodeEnum.RELATION_IS_EXIST);
            // }

            if (multiModalReq.getUploadType() != null && 1 == multiModalReq.getUploadType()) {

                FileData file = fileDataService.get(multiModalReq.getFileId());
                List<String> kgNames = file.getKgNames();
                if (kgNames == null) {
                    kgNames = Lists.newArrayList(kgName);
                } else if (!kgNames.contains(kgName)) {
                    kgNames.add(kgName);
                }
                file.setKgNames(kgNames);

                // 在文件中添加标引关系的图谱名称
                fileDataService.update(file);

                // 创建实体文件关联
                EntityFileRelationReq relation = ConvertUtils.convert(EntityFileRelationReq.class).apply(multiModalReq);
                relation.setIndexType(0);
                relation.setTitle(multiModalReq.getName() + "." + multiModalReq.getType());
                entityFileRelationService.createRelation(kgName, relation);
            } else if (multiModalReq.getFileSystemId() != null && multiModalReq.getFolderId() != null) {
                // 创建数仓文件记录
                FileDataReq fileDataReq = ConvertUtils.convert(FileDataReq.class).apply(multiModalReq);
                fileDataReq.setFileName(multiModalReq.getName() + "." + multiModalReq.getType());
                fileDataReq.setKgNames(Lists.newArrayList(kgName));
                FileData fileAdd = fileDataService.fileAdd(fileDataReq);
                // 创建实体文件关联
                EntityFileRelationReq relation = ConvertUtils.convert(EntityFileRelationReq.class).apply(multiModalReq);
                relation.setFileId(fileAdd.getId());
                relation.setIndexType(0);
                relation.setTitle(multiModalReq.getName() + "." + multiModalReq.getType());
                entityFileRelationService.createRelation(kgName, relation);
            }
        }

        multiModals.forEach(modal -> sendMsg(kgName, modal, GraphLogOperation.ADD));
        logSender.remove();
    }

    @Override
    public void deleteMultiModal(String kgName, String relationId, Long entityId) {
        logSender.setActionId();
        // 删除实体文件关联
        MultiModal multiModal = entityFileRelationService.deleteMultiModalById(kgName, relationId, entityId);
        sendMsg(kgName, multiModal, GraphLogOperation.DELETE);
        logSender.remove();
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
        basicInfoListFrom.setLimit(size + 1);
        RestResp<List<EntityVO>> restResp = conceptEntityApi.list(KGUtil.dbName(kgName),
                basicInfoListFrom);
        Optional<List<EntityVO>> optional = RestRespConverter.convert(restResp);
        List<BasicInfoRsp> basicInfoRspList =
                optional.orElse(new ArrayList<>()).stream().map(ParserBeanUtils::parserEntityVO).collect(Collectors.toList());
        int count = basicInfoRspList.size();
        if (count > size) {
            basicInfoRspList.remove(size.intValue());
            count += page;
        }

        Map<String, String> usernameMap = new HashMap<>();
        basicInfoRspList.forEach(basicInfoRsp -> {

            if (basicInfoRsp.getSourceUser() != null && !basicInfoRsp.getSourceUser().isEmpty()) {
                if (!usernameMap.containsKey(basicInfoRsp.getSourceUser())) {
                    UserDetailRsp userDetailRsp = userClient.getCurrentUserIdDetail(basicInfoRsp.getSourceUser()).getData();
                    if (userDetailRsp != null) {
                        usernameMap.put(userDetailRsp.getId(), userDetailRsp.getRealname());
                    }
                }
                basicInfoRsp.setSourceUser(usernameMap.get(basicInfoRsp.getSourceUser()));
            }
        });
        return new PageImpl<>(basicInfoRspList, PageRequest.of(basicInfoListReq.getPage() - 1, size), count);
    }

    @Override
    public List<EntityAttrValueVO> listRelations(String kgName, EntityAttrReq entityAttrReq) {
        RelationListFrom relationListFrom = ConvertUtils.convert(RelationListFrom.class).apply(entityAttrReq);
        Integer size = entityAttrReq.getSize();
        Integer skip = (entityAttrReq.getPage() - 1) * size;
        relationListFrom.setSkip(skip);
        relationListFrom.setLimit(size + 1);
        RestResp<List<EntityAttributeValueVO>> restResp = conceptEntityApi.relationList(KGUtil.dbName(kgName),
                relationListFrom);
        Optional<List<EntityAttributeValueVO>> optional = RestRespConverter.convert(restResp);
        return optional.orElse(Collections.emptyList()).stream().map(vo -> ParserBeanUtils.parserEntityAttrValue(vo,
                size))
                .collect(Collectors.toList());

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
            filters.put(MetaDataInfo.METADATA + MetaDataInfo.RELIABILITY.getCode(), bodyReq.getReliability());
        }
        if (StringUtils.hasText(bodyReq.getSource())) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.EQUAL.getType(), bodyReq.getSource());
            filters.put(MetaDataInfo.METADATA + MetaDataInfo.SOURCE.getCode(), operation);
        }
        if (StringUtils.hasText(bodyReq.getSourceUser())) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.EQUAL.getType(), bodyReq.getSourceUser());
            filters.put(MetaDataInfo.METADATA + MetaDataInfo.SOURCE_USER.getCode(), operation);
        }
        if (StringUtils.hasText(bodyReq.getSourceAction())) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.EQUAL.getType(), bodyReq.getSourceAction());
            filters.put(MetaDataInfo.METADATA + MetaDataInfo.SOURCE_ACTION.getCode(), operation);
        }
        if (StringUtils.hasText(bodyReq.getBatchNo())) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.EQUAL.getType(), bodyReq.getBatchNo());
            filters.put(MetaDataInfo.METADATA + MetaDataInfo.BATCH_NO.getCode(), operation);
        }
        if (Objects.nonNull(bodyReq.getTags()) && !bodyReq.getTags().isEmpty()) {
            Map<String, Object> operation = new HashMap<>();
            operation.put(MongoOperation.IN.getType(), bodyReq.getTags());
            filters.put(MetaDataInfo.METADATA + MetaDataInfo.TAG.getCode() + ".name", operation);
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
    public List<DeleteResult> deleteByIds(String kgName, Boolean isTrace, List<Long> ids) {
        logSender.setActionId();
        if (isTrace) {
            logSender.sendLog(kgName, ServiceEnum.ENTITY_TRACE);
        } else {
            logSender.sendLog(kgName, ServiceEnum.ENTITY_EDIT);
        }
        Optional<List<BatchDeleteResult>> optional =
                RestRespConverter.convert(batchApi.deleteEntities(KGUtil.dbName(kgName), ids));

        logSender.remove();
        if (!optional.isPresent() || CollectionUtils.isEmpty(optional.get())) {
            return Collections.emptyList();
        }
        return optional.get().stream().map(a -> ConvertUtils.convert(DeleteResult.class).apply(a)).collect(Collectors.toList());
    }

    @Override
    public Long deleteByConceptId(String kgName, EntityDeleteReq entityDeleteReq) {
        logSender.setActionId();
        entityDeleteReq.setActionId(ThreadLocalUtils.getBatchNo());
        final String dbName = KGUtil.dbName(kgName);
        TaskGraphStatusReq taskGraphStatusReq = TaskGraphStatusReq.builder()
                .kgName(dbName)
                .status(TaskStatus.PROCESSING.getStatus())
                .type(TaskType.CLEAR_ENTITY.getType())
                .params(JacksonUtils.readValue(JacksonUtils.writeValueAsString(entityDeleteReq),
                        new TypeReference<Map<String, Object>>() {
                        }))
                .build();
        TaskGraphStatus taskGraphStatus = taskGraphStatusService.create(taskGraphStatusReq);
        kafkaMessageProducer.sendMessage(topicKgTask, taskGraphStatus);
        logSender.sendLog(kgName, ServiceEnum.ENTITY_EDIT);
        logSender.remove();
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
    public void updateScore(String kgName, Long entityId, ScoreModifyReq scoreModifyReq) {
        Map<String, Object> metadata = new HashMap<>();
        List<Integer> metaNo = new ArrayList<>(1);
        if (Objects.nonNull(scoreModifyReq.getScore())) {
            metadata.put(MetaDataInfo.SCORE.getFieldName(), scoreModifyReq.getScore());
        } else {
            metaNo.add(Integer.valueOf(MetaDataInfo.SCORE.getCode()));
        }
        if (!metaNo.isEmpty()) {
            conceptEntityApi.deleteMetaData(KGUtil.dbName(kgName), entityId, metaNo);
        }
        conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);
    }

    @Override
    public void updateSource(String kgName, Long entityId, SourceModifyReq sourceModifyReq) {
        Map<String, Object> metadata = new HashMap<>();
        List<Integer> metaNo = new ArrayList<>(1);
        if (Objects.nonNull(sourceModifyReq.getSource())) {
            metadata.put(MetaDataInfo.SOURCE.getFieldName(), sourceModifyReq.getSource());
        } else {
            metaNo.add(Integer.valueOf(MetaDataInfo.SOURCE.getCode()));
        }
        if (!metaNo.isEmpty()) {
            conceptEntityApi.deleteMetaData(KGUtil.dbName(kgName), entityId, metaNo);
        }
        conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);
    }

    @Override
    public void updateReliability(String kgName, Long entityId, ReliabilityModifyReq reliabilityModifyReq) {
        Map<String, Object> metadata = new HashMap<>();
        List<Integer> metaNo = new ArrayList<>(1);
        if (Objects.nonNull(reliabilityModifyReq.getReliability())) {
            metadata.put(MetaDataInfo.RELIABILITY.getFieldName(), reliabilityModifyReq.getReliability());
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
        Map<String, Object> metadata = new HashMap<>();
        String fromTime = entityTimeModifyReq.getFromTime();
        if (StringUtils.hasText(fromTime)) {
            metadata.put(MetaDataInfo.FROM_TIME.getFieldName(), fromTime);
        } else if ("".equals(fromTime)) {
            conceptEntityApi.deleteMetaData(KGUtil.dbName(kgName), entityId, Collections.singletonList(19));
        }

        String toTime = entityTimeModifyReq.getToTime();
        if (StringUtils.hasText(toTime)) {
            metadata.put(MetaDataInfo.TO_TIME.getFieldName(), toTime);
        } else if ("".equals(toTime)) {
            conceptEntityApi.deleteMetaData(KGUtil.dbName(kgName), entityId, Collections.singletonList(20));
        }
        BasicInfoRsp details = basicInfoService.getDetails(kgName, new BasicReq(entityId, true));
        fromTime = hasValue(fromTime, details.getFromTime());
        toTime = hasValue(toTime, details.getToTime());
        if (StringUtils.hasText(fromTime) && StringUtils.hasText(toTime) && fromTime.compareTo(toTime) > 0) {
            throw BizException.of(KgmsErrorCodeEnum.TIME_FORM_MORE_THAN_TO);
        }
        if (!CollectionUtils.isEmpty(metadata)) {
            conceptEntityApi.updateMetaData(KGUtil.dbName(kgName), entityId, metadata);
        }
    }

    private String hasValue(String oldTime, String nowTime) {
        if (StringUtils.hasText(oldTime)) {
            return oldTime;
        } else {
            return nowTime;
        }
    }

    @Override
    public void updateGisInfo(String kgName, Long entityId, GisInfoModifyReq gisInfoModifyReq) {
        Map<String, Object> metadata = new HashMap<>();
        //gis坐标
        List<Double> gisCoordinate = new ArrayList<>(2);
        try {
            if (StringUtils.isEmpty(gisInfoModifyReq.getLongitude())) {
                gisCoordinate.add(0, null);
            } else {
                Double longitude = Double.valueOf(gisInfoModifyReq.getLongitude());
                if (longitude.compareTo(-180d) < 0 || longitude.compareTo(180d) > 0) {
                    throw new Exception();
                }
                gisCoordinate.add(0, longitude);
            }
            if (StringUtils.isEmpty(gisInfoModifyReq.getLatitude())) {
                gisCoordinate.add(1, null);
            } else {
                Double latitude = Double.valueOf(gisInfoModifyReq.getLatitude());
                if (latitude.compareTo(-90d) < 0 || latitude.compareTo(90d) > 0) {
                    throw new Exception();
                }
                gisCoordinate.add(1, latitude);
            }
        } catch (Exception e) {
            throw BizException.of(AppErrorCodeEnum.GIS_INFO_ERROR);
        }
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

        attributeValueFrom.setMetaData(MetaDataUtils.getDefaultSourceMetaData(numericalAttrValueReq.getMetaData(), SessionHolder.getUserId()));
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

        String tripleId = RestRespConverter.convert(conceptEntityApi.addObjAttrValue(KGUtil.dbName(kgName),
                objectAttributeValueFrom)).orElse(null);

        UpdateRelationMetaReq updateRelationMetaReq = new UpdateRelationMetaReq();
        updateRelationMetaReq.setTripleId(tripleId);
        updateRelationMetaReq.setMetaData(MetaDataUtils.getDefaultSourceMetaData(null, SessionHolder.getUserId()));
        UpdateRelationFrom updateRelationFrom =
                ConvertUtils.convert(UpdateRelationFrom.class).apply(updateRelationMetaReq);
        RestRespConverter.convertVoid(conceptEntityApi.addObjAttrValue(KGUtil.dbName(kgName), updateRelationFrom));
        return tripleId;

    }

    @Override
    public void updateRelationMeta(String kgName, UpdateRelationMetaReq updateRelationMetaReq) {
        Map<String, Object> metaData = new HashMap<>();
        String score = updateRelationMetaReq.getScore();
        if (Objects.nonNull(score)) {
            metaData.put(MetaDataInfo.SCORE.getFieldName(),
                    "".equals(score) ? "" : Double.parseDouble(updateRelationMetaReq.getScore()));
        }
        if (Objects.nonNull(updateRelationMetaReq.getSource())) {
            metaData.put(MetaDataInfo.SOURCE.getFieldName(), updateRelationMetaReq.getSource());
        }
        String reliability = updateRelationMetaReq.getReliability();
        if (Objects.nonNull(reliability)) {
            metaData.put(MetaDataInfo.RELIABILITY.getFieldName(),
                    "".equals(reliability) ? "" : Double.parseDouble(reliability));
        }
        if (Objects.nonNull(updateRelationMetaReq.getSourceReason())) {
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
        logSender.setActionId();
        if (AttributeValueType.isNumeric(privateAttrDataReq.getType())) {
            logSender.sendLog(kgName, ServiceEnum.ENTITY_EDIT);
        } else {
            logSender.sendLog(kgName, ServiceEnum.RELATION_EDIT);
        }

        String attrId = privateAttrDataReq.getAttrId();
        if (!StringUtils.isEmpty(attrId)) {
            DeletePrivateDataReq req = ConvertUtils.convert(DeletePrivateDataReq.class).apply(privateAttrDataReq);
            req.setTripleIds(Lists.newArrayList(attrId));
            conceptEntityApi.deletePrivateData(KGUtil.dbName(kgName), req.getType(), req.getEntityId(), req.getTripleIds());
        }
        AttributePrivateDataFrom privateDataFrom =
                ConvertUtils.convert(AttributePrivateDataFrom.class).apply(privateAttrDataReq);
        String objId = RestRespConverter.convert(conceptEntityApi.addPrivateData(KGUtil.dbName(kgName),
                privateDataFrom))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.PRIVATE_RELATION_HAS_EXIST));
        logSender.remove();
        return objId;
    }

    @Override
    public void deletePrivateData(String kgName, DeletePrivateDataReq deletePrivateDataReq) {
        logSender.setActionId();
        if (AttributeValueType.isNumeric(deletePrivateDataReq.getType())) {
            logSender.sendLog(kgName, ServiceEnum.ENTITY_EDIT);
        } else {
            logSender.sendLog(kgName, ServiceEnum.RELATION_EDIT);
        }
        RestRespConverter.convertVoid(conceptEntityApi.deletePrivateData(KGUtil.dbName(kgName),
                deletePrivateDataReq.getType(),
                deletePrivateDataReq.getEntityId(), deletePrivateDataReq.getTripleIds()));
        logSender.remove();
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
        Function<List<Integer>, List<AttributeDefinition>> selectAttrDef =
                a -> RestRespConverter.convert(attributeApi.listByIds(KGUtil.dbName(kgName), a))
                        .orElse(Collections.emptyList());
        new EntityChecker(batchEntity, selectAttrDef).check();
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
