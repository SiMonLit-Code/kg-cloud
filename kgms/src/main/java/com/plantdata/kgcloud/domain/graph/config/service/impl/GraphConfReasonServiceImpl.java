package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfAlgorithm;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfReasonRepository;
import com.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfReasonService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jdm on 2019/12/9 15:54.
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
        targe.setKgName(kgName);
        targe.setId(kgKeyGenerator.getNextId());
        GraphConfReasoning result = graphConfReasoningRepository.save(targe);
        return ConvertUtils.convert(GraphConfReasonRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReasoning(Long id) {
        GraphConfReasoning graphConfReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        graphConfReasoningRepository.delete(graphConfReasoning);
    }

    @Override
    public Page<GraphConfReasonRsp> findByKgName(String kgName , BaseReq baseReq) {
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
        Page<GraphConfReasoning> all = graphConfReasoningRepository.findByKgName(kgName, pageable);
        return all.map(ConvertUtils.convert(GraphConfReasonRsp.class));
    }

    @Override
    public GraphConfReasonRsp findById(Long id) {
        GraphConfReasoning confReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_KGQL_NOT_EXISTS));
        return ConvertUtils.convert(GraphConfReasonRsp.class).apply(confReasoning);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfReasonRsp updateReasoning(Long id, GraphConfReasonReq req) {
        GraphConfReasoning graphConfReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_KGQL_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfReasoning);
        GraphConfReasoning result = graphConfReasoningRepository.save(graphConfReasoning);
        return ConvertUtils.convert(GraphConfReasonRsp.class).apply(result);
    }
}
