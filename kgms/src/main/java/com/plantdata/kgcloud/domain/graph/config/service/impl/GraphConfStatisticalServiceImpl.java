package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfStatistical;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfStatisticalRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfStatisticalService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.req.UpdateGraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
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
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author jiangdeming
 * @date 2019/12/3
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
        String strStatisRule = JacksonUtils.writeValueAsString(req.getStatisRule());
        Optional<JsonNode> jsonNode = JsonUtils.parseJsonNode(strStatisRule);
        targe.setStatisRule(jsonNode.get());
        targe.setId(kgKeyGenerator.getNextId());
        targe.setKgName(kgName);
        GraphConfStatistical result = graphConfStatisticalRepository.save(targe);
        return ConvertUtils.convert(GraphConfStatisticalRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GraphConfStatisticalRsp> saveAll(List<GraphConfStatisticalReq> listReq) {
        List<GraphConfStatistical> list = new ArrayList<>();
        for (GraphConfStatisticalReq req : listReq) {
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
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_STATISTICAL_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfStatistical);
        GraphConfStatistical result = graphConfStatisticalRepository.save(graphConfStatistical);
        return ConvertUtils.convert(GraphConfStatisticalRsp.class).apply(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GraphConfStatisticalRsp> updateAll(List<UpdateGraphConfStatisticalReq> reqs) {
        List<Long> list = new ArrayList<>();
        for (UpdateGraphConfStatisticalReq req : reqs) {
            GraphConfStatistical graphConfStatistical = graphConfStatisticalRepository.findById(req.getId())
                    .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_IDORIDS_NOT_EXISTS));
            list.add(req.getId());
        }
        List<GraphConfStatistical> confStatisticalList = graphConfStatisticalRepository.findAllById(list);
        List<GraphConfStatistical> statisticalArrayList = new ArrayList<>();
        Map<Long, GraphConfStatistical> confStatisticalMap = confStatisticalList.stream().collect(Collectors.toMap(GraphConfStatistical::getId, Function.identity()));
        for (UpdateGraphConfStatisticalReq req : reqs) {
            if (null == confStatisticalMap.get(req.getId())) {
                throw BizException.of(AppErrorCodeEnum.CONF_STATISTICALID_NOT_EXISTS);
            }
            BeanUtils.copyProperties(req, confStatisticalMap.get(req.getId()));
            statisticalArrayList.add(confStatisticalMap.get(req.getId()));
        }
        List<GraphConfStatistical> list2 = graphConfStatisticalRepository.saveAll(statisticalArrayList);
        return list2.stream().map(ConvertUtils.convert(GraphConfStatisticalRsp.class)).collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStatistical(Long id) {
        GraphConfStatistical graphConfStatistical = graphConfStatisticalRepository.findById(id)
                .orElseThrow(() -> BizException.of(AppErrorCodeEnum.CONF_STATISTICAL_NOT_EXISTS));
        graphConfStatisticalRepository.delete(graphConfStatistical);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInBatch(List<Long> ids) {
        List<GraphConfStatistical> allById = graphConfStatisticalRepository.findAllById(ids);
        graphConfStatisticalRepository.deleteInBatch(allById);
    }

    @Override
    public List<GraphConfStatisticalRsp> findByKgName(String kgName) {
        List<GraphConfStatistical> all = graphConfStatisticalRepository.findByKgName(kgName);
        return all.stream().map(ConvertUtils.convert(GraphConfStatisticalRsp.class)).collect(Collectors.toList());
    }

    @Override
    public Page<GraphConfStatisticalRsp> getByKgName(String kgName, BaseReq baseReq) {
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
        Page<GraphConfStatistical> all = graphConfStatisticalRepository.getByKgName(kgName, pageable);
        return all.map(ConvertUtils.convert(GraphConfStatisticalRsp.class));
    }
}
