package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfReasoningRepository;
import com.plantdata.kgcloud.domain.graph.config.req.GraphConfReasoningReq;
import com.plantdata.kgcloud.domain.graph.config.rsp.GraphConfReasoningRsp;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfReasoningService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jdm on 2019/12/9 15:54.
 */
@Service
public class GraphConfReasoningServiceImpl implements GraphConfReasoningService{

    @Autowired
    private GraphConfReasoningRepository graphConfReasoningRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfReasoningRsp createReasoning(String kgName, GraphConfReasoningReq req) {
        GraphConfReasoning targe = new GraphConfReasoning();
        BeanUtils.copyProperties(req, targe);
        targe.setKgName(kgName);
        GraphConfReasoning result = graphConfReasoningRepository.save(targe);
        return ConvertUtils.convert(GraphConfReasoningRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReasoning(Long id) {
        GraphConfReasoning graphConfReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        graphConfReasoningRepository.delete(graphConfReasoning);
    }

    @Override
    public List<GraphConfReasoningRsp> findAll() {
        List<GraphConfReasoning> all = graphConfReasoningRepository.findAll();
        return all.stream().map(ConvertUtils.convert(GraphConfReasoningRsp.class)).collect(Collectors.toList());
    }

    @Override
    public GraphConfReasoningRsp findById(Long id) {
        GraphConfReasoning confReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_KGQL_NOT_EXISTS));
        return graphConfReasoningRepository.finById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfReasoningRsp updateReasoning(Long id, GraphConfReasoningReq req) {
        GraphConfReasoning graphConfReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_KGQL_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfReasoning);
        GraphConfReasoning result = graphConfReasoningRepository.save(graphConfReasoning);
        return ConvertUtils.convert(GraphConfReasoningRsp.class).apply(result);
    }
}
