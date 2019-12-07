package com.plantdata.kgcloud.domain.graph.manage.service;

import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.req.CopyGraphFrom;
import ai.plantdata.kg.api.edit.req.CreateGraphFrom;
import cn.hiboot.mcn.core.model.result.RestResp;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-08 10:03
 **/

@Service
public class GraphServiceImpl implements GraphService {

    private final static String GRAPH_PREFIX = "graph";
    private final static String JOIN = "_";
    @Autowired
    private GraphRepository graphRepository;
    @Autowired
    private GraphApi graphApi;

    private String genKgName(String userId) {
        return userId + JOIN + GRAPH_PREFIX + JOIN + Long.toHexString(System.currentTimeMillis());
    }

    @Override
    public List<GraphRsp> findAll(String userId) {
        Graph probe = Graph.builder()
                .userId(userId)
                .deleted(false)
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
                .deleted(false)
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
    @Transactional(rollbackFor = Exception.class)
    public void delete(String userId, String kgName) {
        GraphPk graphPk = new GraphPk(userId, kgName);
        Optional<Graph> one = graphRepository.findById(graphPk);
        if (one.isPresent()) {
            Graph entity = one.get();
            entity.setDeleted(true);
            graphRepository.save(entity);
            graphApi.delete(kgName);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphRsp insert(String userId, GraphReq req) {
        Graph target = new Graph();
        BeanUtils.copyProperties(req, target);
        String kgName = genKgName(userId);

        CreateGraphFrom createGraphFrom = new CreateGraphFrom();
        createGraphFrom.setKgName(kgName);
        createGraphFrom.setDisplayName(req.getTitle());
        RestResp restResp = graphApi.create(createGraphFrom);
        if (restResp.getActionStatus() == RestResp.ActionStatusMethod.FAIL) {
            throw BizException.of(KgmsErrorCodeEnum.GRAPH_CREATE_FAIL);
        }
        target.setKgName(kgName);
        target.setDeleted(false);
        target.setPrivately(true);
        target.setEditable(true);
        target = graphRepository.save(target);
        return ConvertUtils.convert(GraphRsp.class).apply(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphRsp createDefault(String userId) {
        String kgName = userId + JOIN + GRAPH_PREFIX + JOIN + "default";
        GraphPk graphPk = new GraphPk(userId, kgName);
        Optional<Graph> one = graphRepository.findById(graphPk);
        return one.map(ConvertUtils.convert(GraphRsp.class)).orElseGet(() -> {
                    CopyGraphFrom copyGraphFrom = new CopyGraphFrom();
                    copyGraphFrom.setSourceKgName("default_graph");
                    copyGraphFrom.setTargetKgName(kgName);
                    RestResp resp = graphApi.copy(copyGraphFrom);
                    if (resp.getActionStatus() == RestResp.ActionStatusMethod.FAIL
                            && !Objects.equals(resp.getErrorCode(), 100002)) {
                        throw BizException.of(KgmsErrorCodeEnum.GRAPH_CREATE_FAIL);
                    }
                    Graph target = new Graph();
                    target.setKgName(kgName);
                    target.setTitle("示例图谱");
                    target.setDeleted(false);
                    target.setPrivately(true);
                    target.setEditable(true);
                    target = graphRepository.save(target);
                    return ConvertUtils.convert(GraphRsp.class).apply(target);
                }
        );
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
