package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.graph.config.converter.GraphConfStatisticalConverter;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfStatistical;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfStatisticalRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfStatisticalService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.req.UpdateGraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
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
        List<GraphConfStatisticalRsp> rspList = saveAll(Lists.newArrayList(req));
        return CollectionUtils.isEmpty(rspList) ? null : rspList.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GraphConfStatisticalRsp> saveAll(List<GraphConfStatisticalReq> listReq) {
        List<GraphConfStatistical> list = new ArrayList<>();
        for (GraphConfStatisticalReq req : listReq) {
            GraphConfStatistical targe = new GraphConfStatistical();
            BeanUtils.copyProperties(req, targe);
            targe.setId(kgKeyGenerator.getNextId());
            targe.setStatisType(req.getStatisticType());
            String strStatisRule = JacksonUtils.writeValueAsString(req.getStatisticRule());
            Optional<JsonNode> jsonNode = JsonUtils.parseJsonNode(strStatisRule);
            if (!jsonNode.isPresent()) {
                throw BizException.of(KgmsErrorCodeEnum.CONF_QUERYSETTING_ERROR);
            }
            targe.setStatisRule(jsonNode.get());
            list.add(targe);
        }
        List<GraphConfStatistical> list1 = graphConfStatisticalRepository.saveAll(list);
        return BasicConverter.listConvert(
                list1, GraphConfStatisticalConverter::jsonNodeToMapConverter);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfStatisticalRsp updateStatistical(Long id, GraphConfStatisticalReq req) {
        UpdateGraphConfStatisticalReq statisticalReq = new UpdateGraphConfStatisticalReq();
        BeanUtils.copyProperties(req, statisticalReq);
        statisticalReq.setId(id);
        List<GraphConfStatisticalRsp> rspList = updateAll(Lists.newArrayList(statisticalReq));
        return CollectionUtils.isEmpty(rspList) ? null : rspList.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GraphConfStatisticalRsp> updateAll(List<UpdateGraphConfStatisticalReq> reqs) {
        List<Long> list = new ArrayList<>();
        for (UpdateGraphConfStatisticalReq req : reqs) {
            graphConfStatisticalRepository.findById(req.getId())
                    .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_IDORIDS_NOT_EXISTS));
            list.add(req.getId());
        }
        List<GraphConfStatistical> confStatisticalList = graphConfStatisticalRepository.findAllById(list);
        List<GraphConfStatistical> statisticalArrayList = new ArrayList<>();
        Map<Long, GraphConfStatistical> confStatisticalMap = confStatisticalList.stream().collect(Collectors.toMap(GraphConfStatistical::getId, Function.identity()));
        for (UpdateGraphConfStatisticalReq req : reqs) {
            if (Objects.isNull(confStatisticalMap.get(req.getId()))) {
                throw BizException.of(KgmsErrorCodeEnum.CONF_STATISTICALID_NOT_EXISTS);
            }
            BeanUtils.copyProperties(req, confStatisticalMap.get(req.getId()));
            String strStatisRule = JacksonUtils.writeValueAsString(req.getStatisticRule());
            Optional<JsonNode> jsonNode = JsonUtils.parseJsonNode(strStatisRule);
            if (!jsonNode.isPresent()) {
                throw BizException.of(KgmsErrorCodeEnum.CONF_QUERYSETTING_ERROR);
            }
            confStatisticalMap.get(req.getId()).setStatisRule(jsonNode.get());
            statisticalArrayList.add(confStatisticalMap.get(req.getId()));
        }
        List<GraphConfStatistical> statisticalList = graphConfStatisticalRepository.saveAll(statisticalArrayList);
        return BasicConverter.listConvert(
                statisticalList, GraphConfStatisticalConverter::jsonNodeToMapConverter);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStatistical(Long id) {
        deleteInBatch(Lists.newArrayList(id));
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
        return BasicConverter.listConvert(all, GraphConfStatisticalConverter::jsonNodeToMapConverter);
    }

    @Override
    public BasePage<GraphConfStatisticalRsp> getByKgName(String kgName, BaseReq baseReq) {
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
        Page<GraphConfStatistical> all = graphConfStatisticalRepository.getByKgName(kgName, pageable);
        List<GraphConfStatisticalRsp> graphConfStatisticalRsps = BasicConverter.listConvert(
                all.getContent(), GraphConfStatisticalConverter::jsonNodeToMapConverter);
        BasePage<GraphConfStatisticalRsp> basePage = new BasePage<>();
        basePage.setContent(graphConfStatisticalRsps);
        basePage.setTotalElements(all.getTotalElements());
        return basePage;
    }
}
