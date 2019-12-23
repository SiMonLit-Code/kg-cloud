package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfReasonRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfReasonService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
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
        targe.setKgName(kgName);
        targe.setId(kgKeyGenerator.getNextId());
        GraphConfReasoning result = graphConfReasoningRepository.save(targe);
        return ConvertUtils.convert(GraphConfReasonRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReasoning(Long id) {
        GraphConfReasoning graphConfReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        graphConfReasoningRepository.delete(graphConfReasoning);
    }

    @Override
    public Page<GraphConfReasonRsp> getByKgName(String kgName , BaseReq baseReq) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize(),sort);
        Page<GraphConfReasoning> all = graphConfReasoningRepository.getByKgName(kgName, pageable);
        return all.map(ConvertUtils.convert(GraphConfReasonRsp.class));
    }

    @Override
    public GraphConfReasonRsp findById(Long id) {
        GraphConfReasoning confReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        return ConvertUtils.convert(GraphConfReasonRsp.class).apply(confReasoning);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfReasonRsp updateReasoning(Long id, GraphConfReasonReq req) {
        GraphConfReasoning graphConfReasoning = graphConfReasoningRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_REASONING_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfReasoning);
        GraphConfReasoning result = graphConfReasoningRepository.save(graphConfReasoning);
        return ConvertUtils.convert(GraphConfReasonRsp.class).apply(result);
    }
}
