package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfAlgorithm;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfAlgorithmRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfAlgorithmService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
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
 * @author jiangdeming
 * @date 2019/11/29
 */
@Service
public class GraphConfAlgorithmServiceImpl implements GraphConfAlgorithmService {
    @Autowired
    private GraphConfAlgorithmRepository graphConfAlgorithmRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfAlgorithmRsp createAlgorithm(String kgName, GraphConfAlgorithmReq req) {
        GraphConfAlgorithm targe = new GraphConfAlgorithm();
        BeanUtils.copyProperties(req, targe);
        targe.setId(kgKeyGenerator.getNextId());
        targe.setKgName(kgName);
        GraphConfAlgorithm result = graphConfAlgorithmRepository.save(targe);
        return ConvertUtils.convert(GraphConfAlgorithmRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfAlgorithmRsp updateAlgorithm(Long id, GraphConfAlgorithmReq req) {
        GraphConfAlgorithm graphConfAlgorithm = graphConfAlgorithmRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_ALGORITHM_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfAlgorithm);
        GraphConfAlgorithm result = graphConfAlgorithmRepository.save(graphConfAlgorithm);
        return ConvertUtils.convert(GraphConfAlgorithmRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlgorithm(Long id) {
        GraphConfAlgorithm graphConfAlgorithm = graphConfAlgorithmRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_ALGORITHM_NOT_EXISTS));
        graphConfAlgorithmRepository.delete(graphConfAlgorithm);
    }

    @Override
    public Page<GraphConfAlgorithmRsp> findByKgName(String kgName, BaseReq baseReq) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize(), sort);
        Page<GraphConfAlgorithm> all = graphConfAlgorithmRepository.findByKgName(kgName, pageable);
        return all.map(ConvertUtils.convert(GraphConfAlgorithmRsp.class));
    }

    @Override
    public GraphConfAlgorithmRsp findById(Long id) {
        GraphConfAlgorithm confAlgorithm = graphConfAlgorithmRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_ALGORITHM_NOT_EXISTS));
        return ConvertUtils.convert(GraphConfAlgorithmRsp.class).apply(confAlgorithm);
    }
}
