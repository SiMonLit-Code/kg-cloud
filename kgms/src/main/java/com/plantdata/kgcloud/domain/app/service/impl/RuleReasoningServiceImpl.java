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
import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.domain.app.bo.ReasoningBO;
import com.plantdata.kgcloud.domain.app.converter.EntityConverter;
import com.plantdata.kgcloud.domain.app.dto.RelationReasonRuleDTO;
import com.plantdata.kgcloud.domain.app.service.RuleReasoningService;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfReasoningRepository;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.explore.common.ReasoningReqInterface;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class RuleReasoningServiceImpl implements RuleReasoningService {

    @Autowired
    private ReasoningApi reasoningApi;
    @Autowired
    private GraphConfReasoningRepository graphConfReasoningRepository;
    @Autowired
    private EntityApi entityApi;

    @Override
    public GraphVO rebuildByRuleReason(String kgName, GraphVO graphVO, ReasoningReqInterface reasoningParam) {
        Map<Integer, JsonNode> configMap = reasoningParam.fetchReasonConfig();
        List<GraphConfReasoning> configList = graphConfReasoningRepository.findAllById(configMap.keySet().stream().map(Integer::longValue).collect(Collectors.toList()));
        ReasoningBO reasoning = new ReasoningBO(configList, configMap);
        reasoning.replaceRuleInfo();
        List<Long> analysisEntityIds = reasoningParam.fetchEntityIdList();
        ReasoningReq reasoningReq = reasoning.buildReasoningReq(analysisEntityIds);
        reasoningAndFill(kgName, graphVO, reasoningReq);
        if (reasoningParam.fetchDistance() != 1) {
            Set<Long> realDomains = reasoning.getReasonRuleList().stream().map(RelationReasonRuleDTO::getDomain).collect(Collectors.toSet());
            List<Long> realIdList = graphVO.getEntityList().stream().filter(s -> realDomains.contains(s.getConceptId()) && !analysisEntityIds.contains(s.getId())).map(SimpleEntity::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(realIdList)) {
                reasoningReq = reasoning.buildReasoningReq(realIdList);
                reasoningAndFill(kgName, graphVO, reasoningReq);
            }
        }
        return graphVO;
    }

    private void reasoningAndFill(String kgName, GraphVO graphVO, ReasoningReq reasoningReq) {
        Optional<ReasoningResultRsp> resultOpt = RestRespConverter.convert(reasoningApi.reasoning(kgName, reasoningReq));
        resultOpt.ifPresent(reasoningResultRsp -> this.fillReasonEntityAndRelation(kgName, graphVO, reasoningResultRsp, false));
    }

    private void fillReasonEntityAndRelation(String kgName, GraphVO graphBean, ReasoningResultRsp reasoningResultBean, Boolean newEntity) {
        List<SimpleEntity> entityList = graphBean.getEntityList();
        Set<Long> entityIdSet = new HashSet<>();
        for (SimpleEntity entityBean : entityList) {
            entityIdSet.add(entityBean.getId());
        }
        if (CollectionUtils.isEmpty(reasoningResultBean.getTripleList()) || reasoningResultBean.getCount() <= 0) {
            return;
        }
        List<TripleBean> tripleList = reasoningResultBean.getTripleList();
        Set<Long> idList = new HashSet<>();
        for (TripleBean tripleBean : tripleList) {
            NodeBean start = tripleBean.getStart();
            NodeBean end = tripleBean.getEnd();
            EdgeBean edge = tripleBean.getEdge();
            if (end.getType() == 0) {
                if (!entityIdSet.contains(end.getId())) {
                    idList.add(end.getId());
                }
                if (entityIdSet.contains(end.getId()) || newEntity) {
                    SimpleRelation relationBean = new SimpleRelation();
                    relationBean.setFrom(start.getId());
                    relationBean.setTo(end.getId());
                    if (edge.getId() != null) {
                        relationBean.setAttrId(-edge.getId());
                    }
                    relationBean.setAttrName(edge.getName());
                    graphBean.getRelationList().add(relationBean);
                }
            }
        }
        if (idList.isEmpty() || !newEntity) {
            return;
        }
        Optional<List<EntityVO>> entityBeans = RestRespConverter.convert(entityApi.serviceEntity(kgName, EntityConverter.buildIdsQuery(idList)));
        if (!entityBeans.isPresent() || CollectionUtils.isEmpty(entityBeans.get())) {
            return;
        }
        graphBean.getEntityList().addAll(entityBeans.get().stream().map(s -> {
            SimpleEntity entityBean = new SimpleEntity();
            entityBean.setId(s.getId());
            entityBean.setName(s.getName());
            entityBean.setConceptId(s.getConceptId());
            entityBean.setType(EntityTypeEnum.ENTITY.getValue());
            return entityBean;
        }).collect(Collectors.toList()));
    }
}
