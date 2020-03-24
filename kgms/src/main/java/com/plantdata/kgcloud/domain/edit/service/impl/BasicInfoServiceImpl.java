package com.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.req.BasicDetailFilter;
import ai.plantdata.kg.api.edit.req.BasicInfoFrom;
import ai.plantdata.kg.api.edit.req.BasicQuery;
import ai.plantdata.kg.api.edit.req.MetaDataFrom;
import ai.plantdata.kg.api.edit.req.PromptFrom;
import ai.plantdata.kg.api.edit.req.SynonymFrom;
import ai.plantdata.kg.api.edit.req.UpdateBasicInfoFrom;
import ai.plantdata.kg.api.edit.resp.EntityVO;
import ai.plantdata.kg.api.edit.resp.PromptVO;
import ai.plantdata.kg.api.edit.resp.SimpleBasic;
import ai.plantdata.kg.api.pub.CountApi;
import ai.plantdata.kg.api.pub.QlApi;
import ai.plantdata.kg.api.pub.StatisticsApi;
import ai.plantdata.kg.api.pub.req.statistics.ConceptStatisticsBean;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.plantdata.graph.logging.core.ServiceEnum;
import com.plantdata.kgcloud.constant.AttributeValueType;
import com.plantdata.kgcloud.constant.BasicInfoType;
import com.plantdata.kgcloud.constant.CountType;
import com.plantdata.kgcloud.constant.KgmsConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.req.basic.AbstractModifyReq;
import com.plantdata.kgcloud.domain.edit.req.basic.AdditionalReq;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicReq;
import com.plantdata.kgcloud.domain.edit.req.basic.ImageUrlReq;
import com.plantdata.kgcloud.domain.edit.req.basic.PromptReq;
import com.plantdata.kgcloud.domain.edit.req.basic.StatisticsReq;
import com.plantdata.kgcloud.domain.edit.req.basic.SynonymReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.GraphStatisRsp;
import com.plantdata.kgcloud.domain.edit.rsp.MultiModalRsp;
import com.plantdata.kgcloud.domain.edit.rsp.PromptRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.LogSender;
import com.plantdata.kgcloud.domain.edit.util.MapperUtils;
import com.plantdata.kgcloud.domain.edit.util.ParserBeanUtils;
import com.plantdata.kgcloud.domain.edit.vo.EntityAttrValueVO;
import com.plantdata.kgcloud.domain.edit.vo.StatisticVO;
import com.plantdata.kgcloud.domain.graph.attr.req.AttrGroupSearchReq;
import com.plantdata.kgcloud.domain.graph.attr.rsp.GraphAttrGroupRsp;
import com.plantdata.kgcloud.domain.graph.attr.service.GraphAttrGroupService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import com.plantdata.kgcloud.sdk.req.edit.KgqlReq;
import com.plantdata.kgcloud.sdk.rsp.edit.SimpleBasicRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.bson.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 11:45
 * @Description:
 */
@Service
public class BasicInfoServiceImpl implements BasicInfoService {

    @Autowired
    private ConceptEntityApi conceptEntityApi;

    @Autowired
    private GraphApi graphApi;

    @Autowired
    private CountApi countApi;

    @Autowired
    private StatisticsApi statisticsApi;

    @Autowired
    private BatchApi batchApi;

    @Autowired
    private QlApi qlApi;

    @Autowired
    private GraphAttrGroupService graphAttrGroupService;

    @Autowired
    private LogSender logSender;

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private DocumentConverter documentConverter;

    @Override
    public Long createBasicInfo(String kgName, BasicInfoReq basicInfoReq) {
        logSender.setActionId();
        if (BasicInfoType.isConcept(basicInfoReq.getType())) {
            logSender.sendLog(kgName, ServiceEnum.CONCEPT_DEFINE);
        } else {
            logSender.sendLog(kgName, ServiceEnum.ENTITY_EDIT);
        }
        BasicInfoFrom basicInfoFrom = ConvertUtils.convert(BasicInfoFrom.class).apply(basicInfoReq);
        RestResp<Long> restResp = conceptEntityApi.add(KGUtil.dbName(kgName), basicInfoFrom);
        logSender.remove();
        return RestRespConverter.convert(restResp).orElse(null);
    }

    public Long createBasicInfo(String kgName, BasicInfoReq basicInfoReq, ServiceEnum serviceEnum) {
        logSender.setActionId();
        logSender.sendLog(kgName, ServiceEnum.SCHEMA_REPO);
        BasicInfoFrom basicInfoFrom = ConvertUtils.convert(BasicInfoFrom.class).apply(basicInfoReq);
        RestResp<Long> restResp = conceptEntityApi.add(KGUtil.dbName(kgName), basicInfoFrom);
        logSender.remove();
        return RestRespConverter.convert(restResp).orElse(null);
    }

    @Override
    public void deleteBasicInfo(String kgName, Long id, Boolean force) {
        sendLog(kgName, id);
        RestRespConverter.convertVoid(conceptEntityApi.delete(KGUtil.dbName(kgName), id, force));
        logSender.remove();
    }

    @Override
    public void updateBasicInfo(String kgName, BasicInfoModifyReq basicInfoModifyReq) {
        this.sendLog(kgName, basicInfoModifyReq.getId());
        UpdateBasicInfoFrom updateBasicInfoFrom =
                ConvertUtils.convert(UpdateBasicInfoFrom.class).apply(basicInfoModifyReq);
        RestRespConverter.convertVoid(conceptEntityApi.update(KGUtil.dbName(kgName), updateBasicInfoFrom));
        logSender.remove();
    }

    @Override
    public BasicInfoRsp getDetails(String kgName, BasicReq basicReq) {
        RestResp<EntityVO> restResp = conceptEntityApi.get(KGUtil.dbName(kgName), basicReq.getIsEntity(),
                basicReq.getId());
        Optional<EntityVO> optional = RestRespConverter.convert(restResp);
        if (!optional.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.BASIC_INFO_NOT_EXISTS);
        }
        BasicInfoRsp basicInfoRsp = ParserBeanUtils.parserEntityVO(optional.get());
        if (basicReq.getIsEntity()) {
            basicInfoRsp.setMultiModals(this.listMultiModels(kgName, basicReq.getId()));
        }
        MongoDatabase mongoDatabase = mongoClient.getDatabase(KGUtil.dbName(kgName));
        MongoCollection<Document> collection = mongoDatabase.getCollection(KgmsConstants.MULTI_MODAL);
        List<EntityAttrValueVO> attrValue = basicInfoRsp.getAttrValue();
        if (CollectionUtils.isEmpty(attrValue) || BasicInfoType.isConcept(basicInfoRsp.getType())) {
            return basicInfoRsp;
        } else {
            List<GraphAttrGroupRsp> groupRsps = graphAttrGroupService.listAttrGroups(KGUtil.dbName(kgName),
                    new AttrGroupSearchReq());
            if (CollectionUtils.isEmpty(groupRsps)) {
                return basicInfoRsp;
            }
            List<Integer> attrIds =
                    attrValue.stream().filter(attrVO -> AttributeValueType.isNumeric(attrVO.getType()))
                            .map(EntityAttrValueVO::getId).collect(Collectors.toList());
            List<GraphAttrGroupRsp> attrGroupRsps = groupRsps.stream().filter(graphAttrGroupRsp -> {
                List<Integer> rspAttrIds = graphAttrGroupRsp.getAttrIds();
                if (CollectionUtils.isEmpty(rspAttrIds)) {
                    return false;
                }
                rspAttrIds.retainAll(attrIds);
                return !rspAttrIds.isEmpty();
            }).collect(Collectors.toList());
            basicInfoRsp.setAttrGroup(attrGroupRsps);
            return basicInfoRsp;
        }
    }

    /**
     * 获取多模态数据信息
     *
     * @param kgName
     * @param entityId
     * @return
     */
    private List<MultiModalRsp> listMultiModels(String kgName, Long entityId) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(KGUtil.dbName(kgName));
        MongoCollection<Document> collection = mongoDatabase.getCollection(KgmsConstants.MULTI_MODAL);
        FindIterable<Document> findIterable = collection.find(Filters.eq("entity_id", entityId));
        return documentConverter.toBeans(findIterable, MultiModalRsp.class);
    }

    @Override
    public void updateAbstract(String kgName, AbstractModifyReq abstractModifyReq) {
        this.sendLog(kgName, abstractModifyReq.getId());
        UpdateBasicInfoFrom updateBasicInfoFrom =
                ConvertUtils.convert(UpdateBasicInfoFrom.class).apply(abstractModifyReq);
        RestRespConverter.convertVoid(conceptEntityApi.update(KGUtil.dbName(kgName), updateBasicInfoFrom));
        logSender.remove();
    }

    @Override
    public List<BasicInfoRsp> listByIds(String kgName, List<Long> ids) {
        BasicDetailFilter basicDetailFilter = new BasicDetailFilter();
        basicDetailFilter.setIds(ids);
        basicDetailFilter.setEntity(true);
        basicDetailFilter.setReadObj(true);
        basicDetailFilter.setReadReverseObj(false);
        RestResp<List<EntityVO>> restResp = conceptEntityApi.listByIds(KGUtil.dbName(kgName),
                basicDetailFilter);
        Optional<List<EntityVO>> optional = RestRespConverter.convert(restResp);
        return optional.orElse(new ArrayList<>()).stream().map(ParserBeanUtils::parserEntityVO).collect(Collectors.toList());
    }

    @Override
    public void addSynonym(String kgName, SynonymReq synonymReq) {
        SynonymFrom synonymFrom = ConvertUtils.convert(SynonymFrom.class).apply(synonymReq);
        RestRespConverter.convertVoid(conceptEntityApi.addSynonym(KGUtil.dbName(kgName), synonymFrom));
    }

    @Override
    public void deleteSynonym(String kgName, SynonymReq synonymReq) {
        SynonymFrom synonymFrom = ConvertUtils.convert(SynonymFrom.class).apply(synonymReq);
        RestRespConverter.convertVoid(conceptEntityApi.deleteSynonym(KGUtil.dbName(kgName), synonymFrom));
    }

    @Override
    public void saveImageUrl(String kgName, ImageUrlReq imageUrlReq) {
        sendLog(kgName, imageUrlReq.getId());
        UpdateBasicInfoFrom updateBasicInfoFrom = ConvertUtils.convert(UpdateBasicInfoFrom.class).apply(imageUrlReq);
        RestRespConverter.convertVoid(conceptEntityApi.update(KGUtil.dbName(kgName), updateBasicInfoFrom));
        logSender.remove();
    }

    private void sendLog(String kgName, Long id) {
        logSender.setActionId();
        Optional<Integer> optional = RestRespConverter.convert(conceptEntityApi.listBatch(KGUtil.dbName(kgName), id));
        if (BasicInfoType.isConcept(optional.get())) {
            logSender.sendLog(kgName, ServiceEnum.CONCEPT_DEFINE);
        } else {
            logSender.sendLog(kgName, ServiceEnum.ENTITY_EDIT);
        }
    }

    @Override
    public List<PromptRsp> prompt(String kgName, PromptReq promptReq) {
        PromptFrom promptFrom = MapperUtils.map(promptReq, PromptFrom.class);
        //第一页
        promptFrom.setSkip(0);
        promptFrom.setLimit(promptReq.getSize());
        Optional<List<PromptVO>> optional = RestRespConverter.convert(graphApi.prompt(KGUtil.dbName(kgName),
                promptFrom));
        return optional.orElse(new ArrayList<>()).stream().map(promptVO -> {
            PromptRsp promptRsp = new PromptRsp();
            BeanUtils.copyProperties(promptVO, promptRsp);
            promptRsp.setConceptId(promptVO.getClassId());
            return promptRsp;
        }).collect(Collectors.toList());
    }

    @Override
    public GraphStatisRsp graphStatis(String kgName) {
        long defaultValue = 0L;
        StatisticVO.StatisticVOBuilder builder = StatisticVO.builder();
        Optional<Long> concept = RestRespConverter.convert(countApi.countElement(KGUtil.dbName(kgName),
                CountType.CONCEPT.getCode()));
        Long conceptCount = concept.orElse(defaultValue);
        Optional<Long> entity = RestRespConverter.convert(countApi.countElement(KGUtil.dbName(kgName),
                CountType.ENTITY.getCode()));
        Long entityCount = entity.orElse(defaultValue);
        Optional<Long> number = RestRespConverter.convert(countApi.countElement(KGUtil.dbName(kgName),
                CountType.NUMERICAL_ATTR.getCode()));
        Long numberCount = number.orElse(defaultValue);
        Optional<Long> privateNumber = RestRespConverter.convert(countApi.countElement(KGUtil.dbName(kgName),
                CountType.PRIVATE_NUMERICAL_ATTR.getCode()));
        Long privateNumberCount = privateNumber.orElse(defaultValue);
        Optional<Long> object = RestRespConverter.convert(countApi.countElement(KGUtil.dbName(kgName),
                CountType.OBJECT_ATTR.getCode()));
        Long objectCount = object.orElse(defaultValue);
        Optional<Long> privateObject = RestRespConverter.convert(countApi.countElement(KGUtil.dbName(kgName),
                CountType.PRIVATE_OBJECT_ATTR.getCode()));
        Long privateObjectCount = privateObject.orElse(defaultValue);
        int baseValue = 10000;
        if (conceptCount >= baseValue) {
            double n = (double) (conceptCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.conceptTotal(result);
        } else {
            builder.conceptTotal(conceptCount);
        }

        if (entityCount >= baseValue) {
            double n = (double) (entityCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.entityTotal(result);
        } else {
            builder.entityTotal(entityCount);
        }

        if (numberCount >= baseValue) {
            double n = (double) (numberCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.numericalAttrTotal(result);
        } else {
            builder.numericalAttrTotal(numberCount);
        }

        if (privateNumberCount >= baseValue) {
            double n = (double) (privateNumberCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.privateNumericalAttrTotal(result);
        } else {
            builder.privateNumericalAttrTotal(privateNumberCount);
        }

        if (objectCount >= baseValue) {
            double n = (double) (objectCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.objectAttrTotal(result);
        } else {
            builder.objectAttrTotal(objectCount);
        }

        if (privateObjectCount >= baseValue) {
            double n = (double) (privateObjectCount) / baseValue;
            String result = String.format("%.2f", n) + "万";
            builder.privateObjectAttrTotal(result);
        } else {
            builder.privateObjectAttrTotal(privateObjectCount);
        }
        StatisticVO statisticVO = builder.build();
        ConceptStatisticsBean statisticsBean =
                ConvertUtils.convert(ConceptStatisticsBean.class).apply(new StatisticsReq());
        Optional<List<Map<String, Object>>> optional =
                RestRespConverter.convert(statisticsApi.conceptStatistics(KGUtil.dbName(kgName), statisticsBean));
        return GraphStatisRsp.builder().statistics(statisticVO).conceptDetails(optional.orElse(new ArrayList<>())).build();
    }

    @Override
    public void batchAddMetaData(String kgName, AdditionalReq additionalReq) {
        MetaDataFrom metaDataFrom = ConvertUtils.convert(MetaDataFrom.class).apply(additionalReq);
        RestRespConverter.convertVoid(batchApi.addMetaData(KGUtil.dbName(kgName), metaDataFrom));
    }

    @Override
    public void clearMetaData(String kgName) {
        MetaDataFrom metaDataFrom = new MetaDataFrom();
        //默认清除meta 14
        metaDataFrom.setId(14);
        RestRespConverter.convertVoid(batchApi.deleteMetaData(KGUtil.dbName(kgName), metaDataFrom));
    }

    @Override
    public Object executeQl(KgqlReq kgqlReq) {
        String query = kgqlReq.getQuery();
        if (StringUtils.hasText(query)) {
            try {
                int s = query.indexOf("\"");
                int e = query.indexOf("\"", s);
                String kgName = query.substring(s, e);
                query = query.replace(kgName, KGUtil.dbName(kgName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RestRespConverter.convert(qlApi.executeQl(query));
    }

    @Override
    public List<SimpleBasicRsp> listNames(String kgName, List<String> names) {
        BasicQuery basicQuery = new BasicQuery();
        basicQuery.setNames(names);
        Optional<List<SimpleBasic>> optional =
                RestRespConverter.convert(conceptEntityApi.listBatch(KGUtil.dbName(kgName), basicQuery));

        return optional.orElse(new ArrayList<>()).stream().map(ConvertUtils.convert(SimpleBasicRsp.class)).collect(Collectors.toList());
    }
}
