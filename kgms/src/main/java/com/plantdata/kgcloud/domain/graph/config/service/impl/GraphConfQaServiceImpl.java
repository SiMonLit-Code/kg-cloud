package com.plantdata.kgcloud.domain.graph.config.service.impl;

import ai.plantdata.cloud.redis.util.KgKeyGenerator;
import ai.plantdata.cloud.web.util.ConvertUtils;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfQa;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfQaStatus;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfQaRepository;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfQaStatusRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfQaService;
import com.plantdata.kgcloud.sdk.req.GraphConfQaReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfQaRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfQaStatusRsp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangdeming
 * @date 2019/12/2
 */
@Service
public class GraphConfQaServiceImpl implements GraphConfQaService {
    @Autowired
    private GraphConfQaRepository graphConfQaRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Autowired
    private GraphConfQaStatusRepository graphConfQaStatusRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GraphConfQaRsp> saveQa(String kgName, List<GraphConfQaReq> reqs) {
        graphConfQaRepository.deleteByKgName(kgName);
        List<GraphConfQa> list = new ArrayList<>();
        for (GraphConfQaReq req : reqs) {
            GraphConfQa targe = new GraphConfQa();
            BeanUtils.copyProperties(req, targe);
            targe.setId(kgKeyGenerator.getNextId());
            String s = targe.getQuestion();
            int count = 0;
            int index = s.indexOf("$entity");
            while (index > -1) {
                count++;
                s = s.substring(index + 1);
                index = s.indexOf("$entity");
            }
            targe.setCount(count);
            targe.setKgName(kgName);
            list.add(targe);
        }
        List<GraphConfQa> result = graphConfQaRepository.saveAll(list);
        return result.stream().map(ConvertUtils.convert(GraphConfQaRsp.class)).collect(Collectors.toList());

    }


    @Override
    public List<GraphConfQaRsp> findByKgName(String kgName) {
        List<GraphConfQa> all = graphConfQaRepository.findAll();
        List<GraphConfQa> newList = new ArrayList<>();
        if (all != null) {
            for (GraphConfQa qa : all) {
                if (qa.getKgName().equals(kgName)) {
                    newList.add(qa);
                }
            }
        }
        return newList.stream().map(ConvertUtils.convert(GraphConfQaRsp.class)).collect(Collectors.toList());
    }

    @Override
    public GraphConfQaStatusRsp getStatus(String kgName) {
        GraphConfQaStatus graphConfQaStatus = new GraphConfQaStatus();
        graphConfQaStatus.setKgName(kgName);
        Optional<GraphConfQaStatus> optional = graphConfQaStatusRepository.findOne(Example.of(graphConfQaStatus));
        if (!optional.isPresent()) {
            graphConfQaStatus.setStatus(0);
            GraphConfQaStatus save = graphConfQaStatusRepository.save(graphConfQaStatus);
            return ConvertUtils.convert(GraphConfQaStatusRsp.class).apply(save);
        }
        return ConvertUtils.convert(GraphConfQaStatusRsp.class).apply(optional.get());
    }

    @Override
    public void updateStatus(String kgName, Integer status) {
        GraphConfQaStatus graphConfQaStatus = new GraphConfQaStatus();
        graphConfQaStatus.setKgName(kgName);
        Optional<GraphConfQaStatus> optional = graphConfQaStatusRepository.findOne(Example.of(graphConfQaStatus));
        if (!optional.isPresent()) {
            graphConfQaStatus.setStatus(status);
            graphConfQaStatusRepository.save(graphConfQaStatus);
        }
        GraphConfQaStatus newGraphConfQaStatus = optional.get();
        newGraphConfQaStatus.setStatus(status);
        graphConfQaStatusRepository.save(newGraphConfQaStatus);
    }
}
