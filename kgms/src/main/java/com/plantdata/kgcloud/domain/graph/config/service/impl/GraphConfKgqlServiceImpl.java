package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfKgql;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfKgqlRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfKgqlService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 图谱业务配置
 * Created by plantdata-1007 on 2019/12/2.
 */
@Service
public class GraphConfKgqlServiceImpl implements GraphConfKgqlService {
    @Autowired
    private GraphConfKgqlRepository graphConfKgqlRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfKgqlRsp createKgql(String kgName,GraphConfKgqlReq req ) {
        GraphConfKgql targe = new GraphConfKgql();
        BeanUtils.copyProperties(req, targe);
        targe.setId(kgKeyGenerator.getNextId());
        targe.setKgName(kgName);
        GraphConfKgql result = graphConfKgqlRepository.save(targe);
        return ConvertUtils.convert(GraphConfKgqlRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfKgqlRsp updateKgql(Long id, GraphConfKgqlReq req) {
        GraphConfKgql graphConfKgql = graphConfKgqlRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_KGQL_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfKgql);
        GraphConfKgql result = graphConfKgqlRepository.save(graphConfKgql);
        return ConvertUtils.convert(GraphConfKgqlRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteKgql(Long id) {
        GraphConfKgql graphConfKgql = graphConfKgqlRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_KGQL_NOT_EXISTS));
        graphConfKgqlRepository.delete(graphConfKgql);
    }

    @Override
    public Page<GraphConfKgqlRsp> findByKgNameAndRuleType(String kgName, Integer ruleType, BaseReq baseReq) {
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
        Page<GraphConfKgql> all = graphConfKgqlRepository.findByKgNameAndRuleType(kgName,ruleType, pageable);
        return all.map(ConvertUtils.convert(GraphConfKgqlRsp.class));
    }

    @Override
    public GraphConfKgqlRsp findById(Long id) {
        GraphConfKgql graphConfKgql = graphConfKgqlRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_KGQL_NOT_EXISTS));
        return ConvertUtils.convert(GraphConfKgqlRsp.class).apply(graphConfKgql);
    }
}
