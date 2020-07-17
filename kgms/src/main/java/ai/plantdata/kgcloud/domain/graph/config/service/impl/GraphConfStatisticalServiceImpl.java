package ai.plantdata.kgcloud.domain.graph.config.service.impl;

import ai.plantdata.cloud.bean.BasePage;
import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.redis.util.KgKeyGenerator;
import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.domain.app.converter.BasicConverter;
import ai.plantdata.kgcloud.domain.app.util.JsonUtils;
import ai.plantdata.kgcloud.domain.graph.config.converter.GraphConfStatisticalConverter;
import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfStatistical;
import ai.plantdata.kgcloud.domain.graph.config.repository.GraphConfStatisticalRepository;
import ai.plantdata.kgcloud.domain.graph.config.service.GraphConfStatisticalService;
import ai.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import ai.plantdata.kgcloud.sdk.req.UpdateGraphConfStatisticalReq;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        GraphConfStatistical targe = new GraphConfStatistical();
        BeanUtils.copyProperties(req, targe);
        targe.setStatisType(req.getStatisticType());
        String strStatisRule = JacksonUtils.writeValueAsString(req.getStatisticRule());
        Optional<JsonNode> jsonNode = JsonUtils.parseJsonNode(strStatisRule);
        if (!jsonNode.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.CONF_QUERYSETTING_ERROR);
        }
        targe.setStatisRule(jsonNode.get());
        targe.setId(kgKeyGenerator.getNextId());
        targe.setKgName(kgName);
        GraphConfStatistical statistical = graphConfStatisticalRepository.save(targe);
        GraphConfStatisticalRsp graphConfStatisticalRsp = GraphConfStatisticalConverter.jsonNodeToMapConverter(statistical);

        return graphConfStatisticalRsp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GraphConfStatisticalRsp> saveAll(List<GraphConfStatisticalReq> listReq) {
        List<GraphConfStatistical> list = new ArrayList<>();
        for (GraphConfStatisticalReq req : listReq) {
            GraphConfStatistical targe = new GraphConfStatistical();
            BeanUtils.copyProperties(req, targe);
            targe.setStatisType(req.getStatisticType());
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
        GraphConfStatistical graphConfStatistical = graphConfStatisticalRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_STATISTICAL_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfStatistical);
        graphConfStatistical.setStatisType(req.getStatisticType());
        String strStatisRule = JacksonUtils.writeValueAsString(req.getStatisticRule());
        Optional<JsonNode> jsonNode = JsonUtils.parseJsonNode(strStatisRule);
        if (!jsonNode.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.CONF_QUERYSETTING_ERROR);
        }
        graphConfStatistical.setStatisRule(jsonNode.get());
        GraphConfStatistical save = graphConfStatisticalRepository.save(graphConfStatistical);
        GraphConfStatisticalRsp graphConfStatisticalRsp = GraphConfStatisticalConverter.jsonNodeToMapConverter(save);
        return graphConfStatisticalRsp;
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
            confStatisticalMap.get(req.getId()).setStatisType(req.getStatisticType());
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
