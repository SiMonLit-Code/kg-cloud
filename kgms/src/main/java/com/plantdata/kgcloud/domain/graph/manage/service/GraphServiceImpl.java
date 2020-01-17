package com.plantdata.kgcloud.domain.graph.manage.service;

import ai.plantdata.kg.api.edit.GraphApi;
import ai.plantdata.kg.api.edit.req.CopyGraphFrom;
import ai.plantdata.kg.api.edit.req.CreateGraphFrom;
import com.plantdata.kgcloud.config.CacheManagerReconfig;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.graph.manage.entity.Graph;
import com.plantdata.kgcloud.domain.graph.manage.entity.GraphPk;
import com.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.sdk.req.GraphPageReq;
import com.plantdata.kgcloud.sdk.req.GraphReq;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import com.plantdata.kgcloud.sdk.rsp.UserLimitRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-08 10:03
 **/

@Service
@CacheConfig(cacheNames = CacheManagerReconfig.CACHE_GRAPH_KGNAME)
public class GraphServiceImpl implements GraphService {

    private final static String GRAPH_PREFIX = "graph";
    private final static String JOIN = "_";
    @Autowired
    private GraphRepository graphRepository;
    @Autowired
    private GraphApi graphApi;

    @Autowired
    private UserClient userClient;

    //@Cacheable(key = "#kgName")
    public String getDbName(String kgName) {
        Graph probe = Graph.builder()
                .kgName(kgName)
                .deleted(false)
                .build();
        Optional<Graph> one = graphRepository.findOne(Example.of(probe));
        return one.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.GRAPH_NOT_EXISTS)).getDbName();
    }

    private String genKgName(String userId) {
        return userId + JOIN + GRAPH_PREFIX + JOIN + Long.toHexString(System.currentTimeMillis());
    }

    @Override
    public List<GraphRsp> findAll(String userId) {
        Graph probe = Graph.builder()
                .userId(userId)
                .deleted(false)
                .build();
        List<Graph> all = graphRepository.findAll(Example.of(probe), Sort.by(Sort.Direction.DESC, "createAt"));
        return all.stream()
                .map(ConvertUtils.convert(GraphRsp.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<GraphRsp> findAll(String userId, GraphPageReq req) {
        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize(), Sort.by(Sort.Direction.DESC, "createAt"));
        Specification<Graph> specification = (Specification<Graph>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            expressions.add(cb.equal(root.<String>get("userId"), userId));
            expressions.add(cb.equal(root.<Boolean>get("deleted"), false));
            if (StringUtils.hasText(req.getKw())) {
                expressions.add(cb.like(root.get("title"), "%" + req.getKw() + "%"));
            }
            return predicate;
        };
        Page<Graph> all = graphRepository.findAll(specification, pageable);
        return all.map(ConvertUtils.convert(GraphRsp.class));
    }

    @Override
    public GraphRsp findById(String userId, String kgName) {
        GraphPk graphPk = new GraphPk(userId, kgName);
        Optional<Graph> one = graphRepository.findById(graphPk);
        return one.map(ConvertUtils.convert(GraphRsp.class))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.GRAPH_NOT_EXISTS));
    }

    //@CacheEvict(key = "#kgName", beforeInvocation = true)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String userId, String kgName) {
        GraphPk graphPk = new GraphPk(userId, kgName);
        Optional<Graph> one = graphRepository.findById(graphPk);
        if (one.isPresent()) {
            Graph entity = one.get();
            String dbName = entity.getDbName();
            RestRespConverter.convertVoid(graphApi.delete(dbName));
            entity.setDeleted(true);
            graphRepository.save(entity);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphRsp insert(String userId, GraphReq req) {
        UserLimitRsp data = userClient.getCurrentUserLimitDetail().getData();
        if (data != null) {
            Graph probe = new Graph();
            probe.setUserId(userId);
            probe.setDeleted(false);
            long count = graphRepository.count(Example.of(probe));
            Integer graphCount = data.getGraphCount();
            if (graphCount != null && count >= graphCount) {
                throw BizException.of(KgmsErrorCodeEnum.GRAPH_OUT_LIMIT);
            }
        }
        Graph target = new Graph();
        BeanUtils.copyProperties(req, target);
        String kgName = genKgName(userId);
        String dbName = kgName;
        CreateGraphFrom createGraphFrom = new CreateGraphFrom();
        createGraphFrom.setKgName(dbName);
        createGraphFrom.setDisplayName(req.getTitle());
        RestRespConverter.convertVoid(graphApi.create(createGraphFrom));
        target.setKgName(kgName);
        target.setDbName(dbName);
        target.setUserId(userId);
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
        String dbName = kgName;
        GraphPk graphPk = new GraphPk(userId, kgName);
        Optional<Graph> one = graphRepository.findById(graphPk);
        return one.map(ConvertUtils.convert(GraphRsp.class)).orElseGet(() -> {
                    CopyGraphFrom copyGraphFrom = new CopyGraphFrom();
                    copyGraphFrom.setSourceKgName("default_graph");
                    copyGraphFrom.setTargetKgName(dbName);
                    RestRespConverter.convertVoid(graphApi.copy(copyGraphFrom));
                    Graph target = new Graph();
                    target.setKgName(kgName);
                    target.setDbName(dbName);
                    target.setUserId(userId);
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
        String dbName = target.getDbName();
        RestRespConverter.convertVoid(graphApi.update(dbName, req.getTitle()));
        target = graphRepository.save(target);
        return ConvertUtils.convert(GraphRsp.class).apply(target);
    }
}
