package com.plantdata.kgcloud.domain.graph.manage.service;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.manage.entity.Graph;
import com.plantdata.kgcloud.domain.graph.manage.entity.GraphPk;
import com.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.GraphPageReq;
import com.plantdata.kgcloud.sdk.req.GraphReq;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-08 10:03
 **/

@Service
public class GraphServiceImpl implements GraphService {

    @Autowired
    private GraphRepository graphRepository;


    private String genKgName(String userId) {
        return userId + "_" + Long.toHexString(System.currentTimeMillis());
    }

    @Override
    public List<GraphRsp> findAll(String userId) {
        Graph probe = Graph.builder()
                .userId(userId)
                .build();
        List<Graph> all = graphRepository.findAll(Example.of(probe));
        return all.stream()
                .map(ConvertUtils.convert(GraphRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public Page<GraphRsp> findAll(String userId, BaseReq baseReq) {
        Graph probe = Graph.builder()
                .userId(userId)
                .build();
        Page<Graph> all = graphRepository.findAll(Example.of(probe), PageRequest.of(baseReq.getPage() - 1,
                baseReq.getSize()));
        return all.map(ConvertUtils.convert(GraphRsp.class));
    }

    @Override
    public Page<GraphRsp> findAll(String userId, GraphPageReq req) {
        Page<Graph> all;
        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());
        if (StringUtils.hasText(req.getKw())) {
            all = graphRepository.findByUserIdAndTitleContaining(userId, req.getKw(), pageable);
        } else {
            Graph probe = Graph.builder().userId(userId).build();
            all = graphRepository.findAll(Example.of(probe), pageable);
        }
        return all.map(ConvertUtils.convert(GraphRsp.class));
    }

    @Override
    public GraphRsp findById(String userId, String kgName) {
        GraphPk graphPk = new GraphPk(userId, kgName);
        Optional<Graph> one = graphRepository.findById(graphPk);
        return one.map(ConvertUtils.convert(GraphRsp.class))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.GRAPH_NOT_EXISTS));
    }

    @Override
    public void delete(String userId, String kgName) {
        GraphPk graphPk = new GraphPk(userId, kgName);
        graphRepository.deleteById(graphPk);
        //TODO 图谱删除
    }

    @Override
    public GraphRsp insert(GraphReq graphReq) {
        return null;
    }

    @Override
    public GraphRsp insert(String userId, GraphReq req) {
        Graph target = new Graph();
        BeanUtils.copyProperties(req, target);
        String kgName = genKgName(userId);
        target.setKgName(kgName);
        target = graphRepository.save(target);

        //TODO 图谱创建


        return ConvertUtils.convert(GraphRsp.class).apply(target);
    }

    @Override
    public GraphRsp createDefault(String userId) {
        return null;
    }


    @Override
    public GraphRsp update(String userId, String kgName, GraphReq req) {
        GraphPk graphPk = new GraphPk(userId, kgName);
        Graph target = graphRepository.findById(graphPk).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.GRAPH_NOT_EXISTS));
        BeanUtils.copyProperties(req, target);
        target = graphRepository.save(target);
        return ConvertUtils.convert(GraphRsp.class).apply(target);
    }
}
