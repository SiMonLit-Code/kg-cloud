package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.config.constant.FocusType;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfFocus;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfStatistical;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfStatisticalRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfStatisticalService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.GraphConfFocusReq;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfFocusRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * Created by plantdata-1007 on 2019/12/3.
 *
 */
@Service
public class GraphConfStatisticalServiceImpl implements GraphConfStatisticalService {
    @Autowired
    private GraphConfStatisticalRepository graphConfStatisticalRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfStatisticalRsp createStatistical(String kgName, GraphConfStatisticalReq req) {
        GraphConfStatistical targe = new GraphConfStatistical();
        BeanUtils.copyProperties(req, targe);
        targe.setId(kgKeyGenerator.getNextId());
        targe.setKgName(kgName);
        GraphConfStatistical result = graphConfStatisticalRepository.save(targe);
        return ConvertUtils.convert(GraphConfStatisticalRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GraphConfStatisticalRsp> saveAll(List<GraphConfStatisticalReq> listReq) {
        List<GraphConfStatistical> list = new ArrayList<>();
        for (GraphConfStatisticalReq req : listReq){
            GraphConfStatistical targe = new GraphConfStatistical();
            BeanUtils.copyProperties(req, targe);
            targe.setId(kgKeyGenerator.getNextId());
            list.add(targe);
        }
        List<GraphConfStatistical> list1 = graphConfStatisticalRepository.saveAll(list);
        return list1.stream().map(ConvertUtils.convert(GraphConfStatisticalRsp.class)).collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfStatisticalRsp updateStatistical(Long id, GraphConfStatisticalReq req) {
        GraphConfStatistical graphConfStatistical = graphConfStatisticalRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_STATISTICAL_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfStatistical);
        GraphConfStatistical result = graphConfStatisticalRepository.save(graphConfStatistical);
        return ConvertUtils.convert(GraphConfStatisticalRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GraphConfStatisticalRsp> updateAll( List<GraphConfStatisticalReq> reqs) {
        List<Long> list = new ArrayList<>();

        for (GraphConfStatisticalReq req : reqs){
            GraphConfStatistical targe = new GraphConfStatistical();
            Long id = targe.getId();
            list.add(id);
        }
        List<GraphConfStatistical> list1 = graphConfStatisticalRepository.findAllById(list);
        for (GraphConfStatisticalReq req : reqs){
            GraphConfStatistical targe = new GraphConfStatistical();
            BeanUtils.copyProperties(req, targe);
            list1.add(targe);
        }
        List<GraphConfStatistical> list2 = graphConfStatisticalRepository.saveAll(list1);
        return list1.stream().map(ConvertUtils.convert(GraphConfStatisticalRsp.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStatistical(Long id) {
        GraphConfStatistical graphConfStatistical = graphConfStatisticalRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_STATISTICAL_NOT_EXISTS));
        graphConfStatisticalRepository.delete(graphConfStatistical);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInBatch( List<Long> ids) {
        List<GraphConfStatistical> allById = graphConfStatisticalRepository.findAllById(ids);
        graphConfStatisticalRepository.deleteInBatch(allById);
    }

    @Override
    public List<GraphConfStatisticalRsp> findByKgName(String kgName) {
        List<GraphConfStatistical> all = graphConfStatisticalRepository.findByKgName(kgName);
        return all.stream().map(ConvertUtils.convert(GraphConfStatisticalRsp.class)).collect(Collectors.toList());
    }

    @Override
    public Page<GraphConfStatisticalRsp> getByKgName(String kgName , BaseReq baseReq) {
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
        Page<GraphConfStatistical> all = graphConfStatisticalRepository.getByKgName(kgName, pageable);
        return all.map(ConvertUtils.convert(GraphConfStatisticalRsp.class));
    }
}
