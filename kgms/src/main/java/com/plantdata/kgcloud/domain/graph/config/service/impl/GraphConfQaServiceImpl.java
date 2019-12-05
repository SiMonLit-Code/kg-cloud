package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfQa;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfQaRepository;
import com.plantdata.kgcloud.sdk.req.GraphConfQaReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfQaRsp;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfQaService;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by plantdata-1007 on 2019/12/2.
 */
@Service
public class GraphConfQaServiceImpl implements GraphConfQaService {
    @Autowired
    private GraphConfQaRepository graphConfQaRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Override
    public List<GraphConfQaRsp> saveQa(String kgName, List<GraphConfQaReq> reqs) {
        graphConfQaRepository.deleteAll();
        List<GraphConfQa> list = new ArrayList<>();
        for (GraphConfQaReq req : reqs) {
            GraphConfQa targe = new GraphConfQa();
            BeanUtils.copyProperties(req, targe);
            targe.setId(kgKeyGenerator.getNextId());
            String s = targe.getQuestion();
            int count = 0;
            int index = s.indexOf("$entity");
            while (index > -1) {
                count ++;
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
        return all.stream().map(ConvertUtils.convert(GraphConfQaRsp.class)).collect(Collectors.toList());
    }
}
