package ai.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.web.util.ConvertUtils;
import ai.plantdata.cloud.web.util.SessionHolder;
import ai.plantdata.kg.api.edit.BatchApi;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.req.*;
import ai.plantdata.kg.api.edit.resp.EntityVO;
import ai.plantdata.kg.api.edit.resp.PromptVO;
import ai.plantdata.kg.api.edit.resp.RelationDetailVO;
import ai.plantdata.kg.api.edit.resp.SimpleBasic;
import ai.plantdata.kg.api.pub.CountApi;
import ai.plantdata.kg.api.pub.QlApi;
import ai.plantdata.kg.api.pub.StatisticsApi;
import ai.plantdata.kg.api.pub.req.statistics.ConceptStatisticsBean;
import ai.plantdata.kgcloud.constant.AttributeValueType;
import ai.plantdata.kgcloud.constant.BasicInfoType;
import ai.plantdata.kgcloud.constant.CountType;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.domain.edit.req.basic.*;
import ai.plantdata.kgcloud.domain.edit.rsp.EntityFileRsp;
import ai.plantdata.kgcloud.domain.edit.rsp.GraphStatisRsp;
import ai.plantdata.kgcloud.domain.edit.rsp.PromptRsp;
import ai.plantdata.kgcloud.domain.edit.rsp.RelationDetailRsp;
import ai.plantdata.kgcloud.sdk.UserClient;
import ai.plantdata.kgcloud.sdk.rsp.UserDetailRsp;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.graph.logging.core.ServiceEnum;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import ai.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import ai.plantdata.kgcloud.domain.edit.service.LogSender;
import ai.plantdata.kgcloud.domain.edit.util.MapperUtils;
import ai.plantdata.kgcloud.domain.edit.util.MetaDataUtils;
import ai.plantdata.kgcloud.domain.edit.util.ParserBeanUtils;
import ai.plantdata.kgcloud.domain.edit.vo.StatisticVO;
import ai.plantdata.kgcloud.domain.graph.attr.req.AttrGroupSearchReq;
import ai.plantdata.kgcloud.domain.graph.attr.service.GraphAttrGroupService;
import ai.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import ai.plantdata.kgcloud.sdk.req.edit.BasicInfoReq;
import ai.plantdata.kgcloud.sdk.req.edit.KgqlReq;
import ai.plantdata.kgcloud.sdk.rsp.BasicInfoRsp;
import ai.plantdata.kgcloud.sdk.rsp.GraphAttrGroupRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.KnowledgeIndexRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.MultiModalRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.SimpleBasicRsp;
import ai.plantdata.kgcloud.sdk.rsp.vo.EntityAttrValueVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
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
    private UserClient userClient;


    @Autowired
    private EntityFileRelationService entityFileRelationService;

    @Override
    public Long createBasicInfo(String kgName, BasicInfoReq basicInfoReq) {
        logSender.setActionId();
        if (BasicInfoType.isConcept(basicInfoReq.getType())) {
            logSender.sendLog(kgName, ServiceEnum.CONCEPT_DEFINE);
        } else {
            logSender.sendLog(kgName, ServiceEnum.ENTITY_EDIT);
        }
        BasicInfoFrom basicInfoFrom = ConvertUtils.convert(BasicInfoFrom.class).apply(basicInfoReq);
        if(BasicInfoType.isEntity(basicInfoReq.getType())){
            basicInfoFrom.setMetaData(MetaDataUtils.getDefaultSourceMetaData(null, SessionHolder.getUserId()));
        }
        RestResp<Long> restResp = conceptEntityApi.add(KGUtil.dbName(kgName), basicInfoFrom);
        logSender.remove();
        return RestRespConverter.convert(restResp).orElse(null);
    }

    @Override
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
        List<EntityAttrValueVO> attrValue = basicInfoRsp.getAttrValue();
        if (CollectionUtils.isEmpty(attrValue) || BasicInfoType.isConcept(basicInfoRsp.getType())) {
            return basicInfoRsp;
        } else {

            Map<String,String> usernameMap = new HashMap<>();
            attrValue.forEach(entityAttrValueVO -> {

                if(entityAttrValueVO.getSourceUser() != null && !entityAttrValueVO.getSourceUser().isEmpty()){
                    if(!usernameMap.containsKey(entityAttrValueVO.getSourceUser())){
                        UserDetailRsp userDetailRsp = userClient.getCurrentUserIdDetail(entityAttrValueVO.getSourceUser()).getData();
                        if(userDetailRsp != null){
                            usernameMap.put(userDetailRsp.getId(),userDetailRsp.getRealname());
                        }
                    }
                    entityAttrValueVO.setSourceUser(usernameMap.get(entityAttrValueVO.getSourceUser()));
                }
            });


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
    @Override
    public List<MultiModalRsp> listMultiModels(String kgName, Long entityId) {
        List<EntityFileRsp> relationList = entityFileRelationService.getRelationByKgNameAndEntityId(kgName, entityId);
        return relationList.stream().map(ConvertUtils.convert(MultiModalRsp.class)).collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<MultiModalRsp>> listMultiModels(String kgName, List<Long> entityIds) {
        List<EntityFileRsp> relationList = entityFileRelationService.getRelationByKgNameAndEntityIdIn(kgName, entityIds, 0);

        List<MultiModalRsp> list = relationList.stream().map(ConvertUtils.convert(MultiModalRsp.class)).collect(Collectors.toList());

        return list.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(MultiModalRsp::getEntityId));
    }

    @Override
    public Map<Long, List<KnowledgeIndexRsp>> listKnowledgeIndexs(String kgName, List<Long> entityIds) {
        List<EntityFileRsp> relationList = entityFileRelationService.getRelationByKgNameAndEntityIdIn(kgName, entityIds, 1);

        List<KnowledgeIndexRsp> list = relationList.stream().map(ConvertUtils.convert(KnowledgeIndexRsp.class)).collect(Collectors.toList());

        return list.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(KnowledgeIndexRsp::getEntityId));
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
        Set<String> names = synonymReq.getNames();
        if (CollectionUtils.isEmpty(names)) {
            return;
        }else{
            names = names.stream().filter(org.apache.commons.lang3.StringUtils::isNotBlank)
                    .filter(s -> s.length() < 50)
                    .collect(Collectors.toSet());
        }

        // 查询实体和概念的同义词信息
        EntityVO entityVO = conceptEntityApi.get(kgName, false, synonymReq.getId()).getData();
        if (entityVO == null) {
            entityVO = conceptEntityApi.get(kgName, true, synonymReq.getId()).getData();
        }
        if (entityVO == null) {
            throw BizException.of(KgmsErrorCodeEnum.BASIC_INFO_NOT_EXISTS);
        }
        List<String> synonym = entityVO.getSynonym();
        if (!CollectionUtils.isEmpty(synonym)) {
            // 去除空白和重复的同义词
            names = names.stream().filter(org.apache.commons.lang3.StringUtils::isNotBlank)
                    .filter(s -> !synonym.contains(s)).filter(s -> s.length() < 50)
                    .collect(Collectors.toSet());
        }
        synonymReq.setNames(names);

        if (synonymReq.getName() == null) {
            synonymReq.setName("");
        }
        if (CollectionUtils.isEmpty(names)) {
            return;
        }
        SynonymFrom synonymFrom = ConvertUtils.convert(SynonymFrom.class).apply(synonymReq);
        RestResp restResp = conceptEntityApi.addSynonym(KGUtil.dbName(kgName), synonymFrom);
        RestRespConverter.convertVoid(restResp);
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
                int e = query.indexOf("\"", s+1);
                String kgName = query.substring(s+1, e);
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

    @Override
    public RelationDetailRsp getRelationDetails(String kgName, String id) {
        RestResp<RelationDetailVO> restResp = conceptEntityApi.relationDetail(kgName, id);
        Optional<RelationDetailVO> optional = RestRespConverter.convert(restResp);
        if (!optional.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.TRIPLE_ID_NOT_EXISTS);
        }
        RelationDetailRsp rsp = new RelationDetailRsp();
        rsp.setExtraInfo(optional.get().getExtraInfo());
        rsp.setRelationDataValues(optional.get().getRelationDataValues());
        rsp.setRelationObjectValues(optional.get().getRelationObjectValues());
        rsp.setAttrTimeFrom(optional.get().getAttrTimeFrom());
        rsp.setAttrTimeTo(optional.get().getAttrTimeTo());
        if (optional.get().getMetaData().get("meta_data_3") != null) {
            rsp.setScore((Double) optional.get().getMetaData().get("meta_data_3"));
        }
        if (optional.get().getMetaData().get("meta_data_12") != null) {
            rsp.setReliability((Double) optional.get().getMetaData().get("meta_data_12"));
        }
        if (optional.get().getMetaData().get("meta_data_11") != null) {
            rsp.setSource((String) optional.get().getMetaData().get("meta_data_11"));
        }
        return rsp;
    }
}
