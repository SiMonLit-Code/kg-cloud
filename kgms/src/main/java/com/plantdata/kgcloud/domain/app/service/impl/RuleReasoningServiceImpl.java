package com.plantdata.kgcloud.domain.app.service.impl;

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
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.domain.app.bo.ReasoningBO;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.dto.GraphReasoningDTO;
import com.plantdata.kgcloud.domain.app.service.RuleReasoningService;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfReasonRepository;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.function.ReasoningReqInterface;
import com.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
