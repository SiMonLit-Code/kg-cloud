package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.util.DateUtils;
import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kg.api.edit.ConceptEntityApi;
import ai.plantdata.kg.api.pub.EntityApi;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kg.api.pub.resp.SimpleEntity;
import ai.plantdata.kg.api.pub.resp.SimpleRelation;
import ai.plantdata.kg.api.semantic.ReasoningApi;
import ai.plantdata.kg.api.semantic.req.ReasoningReq;
import ai.plantdata.kg.api.semantic.rsp.EdgeBean;
import ai.plantdata.kg.api.semantic.rsp.NodeBean;
import ai.plantdata.kg.api.semantic.rsp.ReasoningResultRsp;
import ai.plantdata.kg.api.semantic.rsp.TripleBean;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.bo.ReasoningBO;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.converter.ReasoningConverter;
import com.plantdata.kgcloud.domain.app.dto.GraphReasoningDTO;
import com.plantdata.kgcloud.domain.app.service.RuleReasoningService;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.util.ParserBeanUtils;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfReasonRepository;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.function.ReasoningReqInterface;
import com.plantdata.kgcloud.sdk.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/3 15:21
 */
@Service
@Slf4j
public class RuleReasoningServiceImpl implements RuleReasoningService {

    @Autowired
    private ReasoningApi reasoningApi;
    @Autowired
    private GraphConfReasonRepository graphConfReasoningRepository;
    @Autowired
    private EntityApi entityApi;
    @Autowired
    private ConceptEntityApi conceptEntityApi;

    @Override
    public List<RelationReasonRuleRsp> generateReasoningRule(Map<Long, Object> configMap) {
        if (CollectionUtils.isEmpty(configMap)) {
            return Collections.emptyList();
        }
        List<Long> ruleIds = configMap.keySet().stream().distinct().collect(Collectors.toList());
        List<GraphConfReasoning> configList = graphConfReasoningRepository.findAllById(ruleIds);
        ReasoningBO reasoning = new ReasoningBO(configList, configMap);
        reasoning.replaceRuleInfo();
        return reasoning.getReasonRuleList();
    }

    @Override
    public Optional<CommonBasicGraphExploreRsp> reasoningExecute(String kgName, ReasoningReq reasoningReq) {

        Optional<ReasoningResultRsp> opt = RestRespConverter.convert(reasoningApi.reasoning(KGUtil.dbName(kgName), reasoningReq));

        if(!opt.isPresent()){
            return Optional.empty();
        }
        CommonBasicGraphExploreRsp rsp = ReasoningConverter.reasoningResult2CommonBasicGraphExplore(opt.orElseGet(() -> new ReasoningResultRsp()));
        if(rsp.getEntityList() != null && !rsp.getEntityList().isEmpty()){
            for(CommonEntityRsp entityRsp : rsp.getEntityList()){
                BasicInfoRsp entity = getEntityDetail(kgName,entityRsp.getId());
                entityRsp.setName(entity.getName());
                entityRsp.setConceptId(entity.getConceptId());
                entityRsp.setType(entity.getType());
                entityRsp.setConceptName(entity.getParent().get(0).getName());
                entityRsp.setConceptIdList(entity.getParent().stream().map(BasicInfoVO::getId).collect(Collectors.toList()));
                entityRsp.setScore(entity.getScore());
                entityRsp.setImgUrl(entity.getImageUrl());
                entityRsp.setMeaningTag(entity.getMeaningTag());
                entityRsp.setStartTime(parseDate(entity.getFromTime()));
                entityRsp.setClassId(entity.getParent().get(0).getId());
                entityRsp.setEndTime(parseDate(entity.getToTime()));
            }
        }
        return Optional.of(rsp);
    }

    public static Date parseDate(String str) {
        try {
            Date date = DateUtils.parseDate(str, "yyyy-MM-dd hh:mm:ss");
            if(date == null){
                date = DateUtils.parseDate(str, "yyyy-MM-dd");
            }
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public BasicInfoRsp getEntityDetail(String kgName,Long entityId){
        RestResp<ai.plantdata.kg.api.edit.resp.EntityVO> restResp = conceptEntityApi.get(KGUtil.dbName(kgName), true,
                entityId);
        Optional<ai.plantdata.kg.api.edit.resp.EntityVO> optional = RestRespConverter.convert(restResp);
        if (!optional.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.BASIC_INFO_NOT_EXISTS);
        }
        return ParserBeanUtils.parserEntityVO(optional.get());
    }

    @Override
    public GraphReasoningDTO buildRuleReasonDto(String kgName, GraphVO graphVO, ReasoningReqInterface reasoningParam) {
        Map<Long, Object> configMap = reasoningParam.fetchReasonConfig();
        if (MapUtils.isEmpty(configMap)) {
            return null;
        }
        List<GraphConfReasoning> configList = graphConfReasoningRepository.findAllById(new ArrayList<>(configMap.keySet()));
        ReasoningBO reasoning = new ReasoningBO(configList, configMap);
        reasoning.replaceRuleInfo();
        //一次推理
        List<Long> analysisEntityIds = reasoningParam.fetchEntityIdList();
        ReasoningReq reasoningReq = reasoning.buildReasoningReq(analysisEntityIds);
        GraphReasoningDTO reasoningDTO = new GraphReasoningDTO(reasoning.getRuleIdCatchMap());
        reasoningAndFill(kgName, graphVO, reasoningReq, reasoningDTO);
        if (reasoningParam.fetchDistance() != null && reasoningParam.fetchDistance() == 1) {
            return reasoningDTO;
        }
        //二次推理
        Set<Long> realDomains = reasoning.getReasonRuleList().stream().map(RelationReasonRuleRsp::getDomain).collect(Collectors.toSet());
        List<Long> realIdList = graphVO.getEntityList().stream().filter(s -> realDomains.contains(s.getConceptId()) && !analysisEntityIds.contains(s.getId())).map(SimpleEntity::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(realIdList)) {
            reasoningReq = reasoning.buildReasoningReq(realIdList);
            reasoningAndFill(kgName, graphVO, reasoningReq, reasoningDTO);
        }
        return reasoningDTO;
    }

    private void reasoningAndFill(String kgName, GraphVO graphVO, ReasoningReq reasoningReq, GraphReasoningDTO reasoningDTO) {
        Optional<ReasoningResultRsp> resultOpt = RestRespConverter.convert(reasoningApi.reasoning(KGUtil.dbName(kgName), reasoningReq));
        log.error("推理reasoningReq:{}", JacksonUtils.writeValueAsString(reasoningReq));
        resultOpt.ifPresent(reasoningResultRsp -> this.fillReasonEntityAndRelation(kgName, graphVO, reasoningResultRsp, false, reasoningDTO));
    }

    private void fillReasonEntityAndRelation(String kgName, GraphVO graphBean, ReasoningResultRsp reasoningResultBean, Boolean newEntity, GraphReasoningDTO reasoningDTO) {

        Set<Long> entityIdSet = BasicConverter.listToSetNoNull(BasicConverter.listToRsp(graphBean.getEntityList(), SimpleEntity::getId), Sets::newHashSet);

        if (CollectionUtils.isEmpty(reasoningResultBean.getTripleList()) || reasoningResultBean.getCount() <= 0) {
            return;
        }
        List<TripleBean> tripleList = reasoningResultBean.getTripleList();
        Set<Long> idList = new HashSet<>();
        for (TripleBean tripleBean : tripleList) {
            NodeBean start = tripleBean.getStart();
            NodeBean end = tripleBean.getEnd();
            EdgeBean edge = tripleBean.getEdge();
            if (end.getType() != 0) {
                continue;
            }
            if (!entityIdSet.contains(end.getId())) {
                idList.add(end.getId());
            }
            if (entityIdSet.contains(end.getId()) || newEntity) {
                SimpleRelation relationBean = new SimpleRelation();
                relationBean.setFrom(start.getId());
                relationBean.setTo(end.getId());
                relationBean.setAttrId(edge.getId());
                relationBean.setAttrName(edge.getName());
                reasoningDTO.getRelationList().add(relationBean);
            }
        }
        if (idList.isEmpty() || !newEntity) {
            return;
        }
        Optional<List<EntityVO>> entityBeans = RestRespConverter.convert(entityApi.serviceEntity(KGUtil.dbName(kgName), EntityConverter.buildIdsQuery(idList,true)));
        if (!entityBeans.isPresent() || CollectionUtils.isEmpty(entityBeans.get())) {
            return;
        }
        List<SimpleEntity> simpleEntities = entityBeans.get().stream().map(s -> {
            SimpleEntity entityBean = new SimpleEntity();
            entityBean.setId(s.getId());
            entityBean.setName(s.getName());
            entityBean.setConceptId(s.getConceptId());
            entityBean.setType(EntityTypeEnum.ENTITY.getValue());
            return entityBean;
        }).collect(Collectors.toList());
        BasicConverter.consumerIfNoNull(simpleEntities, reasoningDTO.getEntityList()::addAll);
    }


}
