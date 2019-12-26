package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.graph.config.converter.GraphConfReasoningConverter;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfReasonRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfReasonService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author jdm
 * @date 2019/12/9 15:54
 */
@Service
public class GraphConfReasonServiceImpl implements GraphConfReasonService {

    @Autowired
    private GraphConfReasonRepository graphConfReasoningRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfReasonRsp createReasoning(String kgName, GraphConfReasonReq req) {
        GraphConfReasoning targe = new GraphConfReasoning();
        BeanUtils.copyProperties(req, targe);
        String strRuleConfig = JacksonUtils.writeValueAsString(req.getRuleConfig());
        Optional<JsonNode> jsonNode = JsonUtils.parseJsonNode(strRuleConfig);
        targe.setRuleConfig(jsonNode.get());
        targe.setKgName(kgName);
        targe.setId(kgKeyGenerator.getNextId());
        GraphConfReasoning save = graphConfReasoningRepository.save(targe);
        GraphConfReasonRsp graphConfReasonRsp = GraphConfReasoningConverter.JsonNodeToMapConverter(save);
        return graphConfReasonRsp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReasoning(Long id) {
        GraphConfReasoning graphConfReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        graphConfReasoningRepository.delete(graphConfReasoning);
    }

    @Override
    public BasePage<GraphConfReasonRsp> getByKgName(String kgName, BaseReq baseReq) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize(), sort);
        Page<GraphConfReasoning> all = graphConfReasoningRepository.getByKgName(kgName, pageable);
        List<GraphConfReasonRsp> graphConfReasonRsps = BasicConverter.listConvert(
                all.getContent(), a -> GraphConfReasoningConverter.JsonNodeToMapConverter(a));
        BasePage<GraphConfReasonRsp> basePage = new BasePage<>();
        basePage.setContent(graphConfReasonRsps);
        basePage.setTotalElements(all.getTotalElements());
        return basePage;
    }

    @Override
    public GraphConfReasonRsp findById(Long id) {
        GraphConfReasoning confReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        GraphConfReasonRsp graphConfReasonRsp = GraphConfReasoningConverter.JsonNodeToMapConverter(confReasoning);
        return graphConfReasonRsp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfReasonRsp updateReasoning(Long id, GraphConfReasonReq req) {
        GraphConfReasoning graphConfReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfReasoning);
        String strRuleConfig = JacksonUtils.writeValueAsString(req.getRuleConfig());
        Optional<JsonNode> jsonNode = JsonUtils.parseJsonNode(strRuleConfig);
        graphConfReasoning.setRuleConfig(jsonNode.get());
        GraphConfReasoning save = graphConfReasoningRepository.save(graphConfReasoning);
        GraphConfReasonRsp graphConfReasonRsp = GraphConfReasoningConverter.JsonNodeToMapConverter(save);
        return graphConfReasonRsp;
    }
}
