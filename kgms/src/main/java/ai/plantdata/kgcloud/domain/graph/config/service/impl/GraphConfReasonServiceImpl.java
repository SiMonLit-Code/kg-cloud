package ai.plantdata.kgcloud.domain.graph.config.service.impl;

import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.redis.util.KgKeyGenerator;
import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.app.util.JsonUtils;
import ai.plantdata.kgcloud.domain.graph.config.converter.GraphConfReasoningConverter;
import ai.plantdata.kgcloud.domain.graph.config.repository.GraphConfReasonRepository;
import com.fasterxml.jackson.databind.JsonNode;
import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfReasonService;
import ai.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
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
        if (targe.getRuleName().length()>50){
            throw BizException.of(KgmsErrorCodeEnum.CONF_RULENAME_ERROR);
        }
        String strRuleConfig = JacksonUtils.writeValueAsString(req.getRuleConfig());
        Optional<JsonNode> jsonNode = JsonUtils.parseJsonNode(strRuleConfig);
        if (!jsonNode.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.CONF_QUERYSETTING_ERROR);
        }
        targe.setRuleConfig(jsonNode.get());
        targe.setKgName(kgName);
        targe.setId(kgKeyGenerator.getNextId());
        GraphConfReasoning save = graphConfReasoningRepository.save(targe);
        GraphConfReasonRsp graphConfReasonRsp = GraphConfReasoningConverter.jsonNodeToMapConverter(save);
        return graphConfReasonRsp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReasoning(Long id) {
        GraphConfReasoning graphConfReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        graphConfReasoningRepository.delete(graphConfReasoning);
    }

    @Override
    public BasePage<GraphConfReasonRsp> getByKgName(String kgName, BaseReq baseReq) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize(), sort);
        Page<GraphConfReasoning> all = graphConfReasoningRepository.getByKgName(kgName, pageable);
        List<GraphConfReasonRsp> graphConfReasonRsps = BasicConverter.listConvert(
                all.getContent(), a -> GraphConfReasoningConverter.jsonNodeToMapConverter(a));
        BasePage<GraphConfReasonRsp> basePage = new BasePage<>();
        basePage.setContent(graphConfReasonRsps);
        basePage.setTotalElements(all.getTotalElements());
        return basePage;
    }

    @Override
    public GraphConfReasonRsp findById(Long id) {
        GraphConfReasoning confReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        GraphConfReasonRsp graphConfReasonRsp = GraphConfReasoningConverter.jsonNodeToMapConverter(confReasoning);
        return graphConfReasonRsp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfReasonRsp updateReasoning(Long id, GraphConfReasonReq req) {
        GraphConfReasoning graphConfReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfReasoning);
        String strRuleConfig = JacksonUtils.writeValueAsString(req.getRuleConfig());
        Optional<JsonNode> jsonNode = JsonUtils.parseJsonNode(strRuleConfig);
        if (!jsonNode.isPresent()){
            throw BizException.of(KgmsErrorCodeEnum.CONF_QUERYSETTING_ERROR);
        }
        graphConfReasoning.setRuleConfig(jsonNode.get());
        GraphConfReasoning save = graphConfReasoningRepository.save(graphConfReasoning);
        GraphConfReasonRsp graphConfReasonRsp = GraphConfReasoningConverter.jsonNodeToMapConverter(save);
        return graphConfReasonRsp;
    }
}
