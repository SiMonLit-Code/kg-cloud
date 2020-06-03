package com.plantdata.kgcloud.domain.graph.quality.service.impl;

import ai.plantdata.kg.common.bean.BasicInfo;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import com.plantdata.kgcloud.domain.graph.quality.entity.GraphAttrQuality;
import com.plantdata.kgcloud.domain.graph.quality.entity.GraphQuality;
import com.plantdata.kgcloud.domain.graph.quality.entity.GraphStatistics;
import com.plantdata.kgcloud.domain.graph.quality.repository.GraphAttrQualityRepository;
import com.plantdata.kgcloud.domain.graph.quality.repository.GraphQualityRepository;
import com.plantdata.kgcloud.domain.graph.quality.rsp.GraphAttrQualityRsp;
import com.plantdata.kgcloud.domain.graph.quality.rsp.GraphQualityRsp;
import com.plantdata.kgcloud.domain.graph.quality.service.GraphQualityService;
import com.plantdata.kgcloud.domain.graph.quality.util.ConceptUtils;
import com.plantdata.kgcloud.domain.graph.quality.util.InitFunc;
import com.plantdata.kgcloud.domain.graph.quality.util.SchemaUtils;
import com.plantdata.kgcloud.domain.graph.quality.vo.AttrQualityVO;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.XxlAdminClient;
import com.plantdata.kgcloud.sdk.bean.*;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 15:22
 * @Description:
 */
@Service
public class GraphQualityServiceImpl implements GraphQualityService {

    @Autowired
    private GraphQualityRepository graphQualityRepository;

    @Autowired
    private GraphAttrQualityRepository graphAttrQualityRepository;

    @Autowired
    private GraphRepository graphRepository;

    @Autowired
    private ConceptUtils conceptUtils;

    @Autowired
    private SchemaUtils schemaUtils;

    @Autowired
    private XxlAdminClient xxlAdminClient;

    @Override
    public List<GraphQualityRsp> listConceptQuality(String kgName) {
        GraphQuality graphQuality = new GraphQuality();
        graphQuality.setKgName(kgName);
        List<GraphQuality> graphQualities = graphQualityRepository.findAll(Example.of(graphQuality));
        return graphQualities.stream().map(ConvertUtils.convert(GraphQualityRsp.class)).collect(Collectors.toList());
    }


    @Override
    public List<GraphQualityRsp> sonConceptCount(String kgName, Long conceptId) {
        GraphQuality graphQuality = new GraphQuality();
        graphQuality.setKgName(kgName);
        graphQuality.setConceptId(conceptId);
        List<GraphQuality> graphQualities = graphQualityRepository.findAll(Example.of(graphQuality));
        GraphQuality quality = check(kgName, conceptId);
        if (quality.getId() != null) {
            graphQualities.add(quality);
        }

        // 实时查询概念实体和属性数量
        statisticsConcept(kgName, conceptId, graphQualities);

        return graphQualities.stream().map(ConvertUtils.convert(GraphQualityRsp.class)).collect(Collectors.toList());
    }

    private GraphQuality check(String kgName, Long selfId) {
        GraphQuality graphQuality = new GraphQuality();
        graphQuality.setKgName(kgName);
        graphQuality.setSelfId(selfId);
        Optional<GraphQuality> optional = graphQualityRepository.findOne(Example.of(graphQuality));
        return optional.orElseGet(() -> graphQuality);
    }

    @Override
    public GraphAttrQualityRsp detailByConceptId(String kgName, Long selfId) {
        GraphQuality quality = this.check(kgName, selfId);
        GraphAttrQuality graphAttrQuality = new GraphAttrQuality();
        graphAttrQuality.setKgName(kgName);
        graphAttrQuality.setSelfId(selfId);
        List<GraphAttrQuality> graphAttrQualities = graphAttrQualityRepository.findAll(Example.of(graphAttrQuality));
        List<AttrQualityVO> vos =
                graphAttrQualities.stream().map(ConvertUtils.convert(AttrQualityVO.class)).collect(Collectors.toList());
        GraphAttrQualityRsp graphAttrQualityRsp = ConvertUtils.convert(GraphAttrQualityRsp.class).apply(quality);
        graphAttrQualityRsp.setConceptName(quality.getName());
        graphAttrQualityRsp.setAttrQualities(vos);

        // 实时查询属性值数量
        statisticsAttr(kgName, selfId, graphAttrQualityRsp);

        return graphAttrQualityRsp;
    }

    @Override
    public void run(String kgName) {
        String userId = SessionHolder.getUserId();
        // 查询质量统计脚本
        TaskListReq taskListReq = new TaskListReq();
        taskListReq.setUserId(userId);
        taskListReq.setKgName(kgName);
        taskListReq.setTaskType("data_quality");
        List<TaskBean> taskBeans = xxlAdminClient.list(taskListReq).getData().getContent();
        if (CollectionUtils.isEmpty(taskBeans)) {
            TaskBean taskBean = new TaskBean();
            taskBean.setUserId(userId);
            taskBean.setName("图谱质量统计");
            taskBean.setDesc("图谱知识质量统计");
            taskBean.setTaskType("data_quality");
            taskBean.setKgName(kgName);
            JSONObject config = new JSONObject();
            config.put("kgName", kgName);
            taskBean.setConfig(config.toString());
            ApiReturn<Integer> integerApiReturn = xxlAdminClient.taskAdd(taskBean);
            System.out.println(integerApiReturn);
            taskBeans = xxlAdminClient.list(taskListReq).getData().getContent();
        }

        TaskBean taskBean = taskBeans.get(0);

        // 查询最新执行记录
        ExecListReq req = ConvertUtils.convert(ExecListReq.class).apply(taskBean);
        req.setStatus(null);
        List<ExecBean> execBeans = xxlAdminClient.execlist(req).getData().getContent();
        if (!CollectionUtils.isEmpty(execBeans)) {
            ExecBean execBean = execBeans.get(0);
            // 脚本正在执行
            if ("WORK".equals(execBean.getStatus())) {
                throw BizException.of(KgmsErrorCodeEnum.SCRIPT_IS_RUNNING);
            }
        }

        RunTaskReq runTaskReq = ConvertUtils.convert(RunTaskReq.class).apply(taskBean);
        JSONObject config = new JSONObject();
        config.put("kgName", kgName);
        runTaskReq.setConfig(config.toString());
        runTaskReq.setStdDataOffset(0L);
        runTaskReq.setStdFireTime(0L);

        xxlAdminClient.taskRun(runTaskReq);
    }

    @Override
    public Long getTime(String kgName) {
        // 查询质量统计脚本
        TaskListReq taskListReq = new TaskListReq();
        taskListReq.setKgName(kgName);
        taskListReq.setUserId(SessionHolder.getUserId());
        taskListReq.setTaskType("data_quality");
        List<TaskBean> taskBeans = xxlAdminClient.list(taskListReq).getData().getContent();
        if (CollectionUtils.isEmpty(taskBeans)) {
            throw BizException.of(KgmsErrorCodeEnum.GRAPH_QUALITY_IS_NOT_RUN);
        }

        TaskBean taskBean = taskBeans.get(0);

        // 查询最新执行记录
        ExecListReq req = ConvertUtils.convert(ExecListReq.class).apply(taskBean);
        req.setStatus("200");
        List<ExecBean> execBeans = xxlAdminClient.execlist(req).getData().getContent();
        if (CollectionUtils.isEmpty(execBeans)) {
            throw BizException.of(KgmsErrorCodeEnum.GRAPH_QUALITY_IS_NOT_RUN);
        }
        return execBeans.get(0).getEndTime();
    }

    /**
     * 实时查询当前概念及子概念的实体和属性数量
     *
     * @param kgName
     * @param conceptId
     * @param graphQualities
     */
    private void statisticsConcept(String kgName, Long conceptId, List<GraphQuality> graphQualities) {

        String kgDbName = graphRepository.findByKgNameAndUserId(kgName, SessionHolder.getUserId()).getDbName();

        InitFunc initFunc = new InitFunc();
        // 获取所有概念信息，父概念集合，子概念集合
        schemaUtils.getConceptMap(kgDbName, initFunc);

        Map<Long, GraphStatistics> dataMap = new HashMap<>();

        // 当前概念的子概念（不包含自己）
        Set<Long> sonConceptIds = initFunc.getSonConceptIdMap().get(conceptId);
        // 当前概念的子概念（包含自己）
        Set<Long> sonAndSelfConceptIds = Sets.newHashSet(sonConceptIds);
        sonAndSelfConceptIds.add(conceptId);

        // 当前概念的父概念（不包含自己）
        Set<Long> parentConceptIds = initFunc.getParentConceptIdMap().get(conceptId);
        // 当前概念的父概念（包含自己）
        Set<Long> parentAndSelfConceptIds = Sets.newHashSet(parentConceptIds);
        parentAndSelfConceptIds.add(conceptId);

        // 获取当前概念下所有子概念的实体数量(不包含子概念)
        Map<Long, Long> entityCounts = conceptUtils.countEntityByConceptIds(kgDbName, sonAndSelfConceptIds);
        // 获取当前概念下所有子概念的实体数量(包含子概念)
        Map<Long, Long> entityTotals = Maps.newHashMap();
        for (Long key : sonAndSelfConceptIds) {
            long count = entityCounts.getOrDefault(key, 0L);
            Set<Long> ids = initFunc.getSonConceptIdMap().get(key);
            long sum = ids.stream().mapToLong(s -> entityCounts.getOrDefault(s, 0L)).sum();
            entityTotals.put(key, count + sum);
        }

        // 获取当前概念下所有相关概念(父概念和子概念)的属性数量(不包含父概念属性)
        HashSet<Long> allConceptIds = Sets.newHashSet(parentAndSelfConceptIds);
        allConceptIds.addAll(sonAndSelfConceptIds);
        Map<Long, Long> attrCounts = conceptUtils.countAttrByConceptIds(kgDbName, allConceptIds);
        // 获取当前概念下所有子概念的属性数量(包含父概念属性)
        Map<Long, Long> attrTotals = Maps.newHashMap();
        for (Long key : sonAndSelfConceptIds) {
            long count = attrCounts.getOrDefault(key, 0L);
            Set<Long> ids = initFunc.getParentConceptIdMap().get(key);
            long sum = ids.stream().mapToLong(s -> attrCounts.getOrDefault(s, 0L)).sum();
            attrTotals.put(key, count + sum);
        }

        for (Long id : sonAndSelfConceptIds) {
            // 获取当前概念下的实体数量(不包含子概念)
            long entityCount = entityCounts.getOrDefault(id, 0L);

            // 获取当前概念下的实体数量(包含子概念)
            long entityTotal = entityTotals.getOrDefault(id, 0L);

            // 获取当前概念下的属性数量(包含父概念属性)
            int attrCount = attrTotals.getOrDefault(id, 0L).intValue();

            GraphStatistics statistics = GraphStatistics.builder()
                    .entityCount(entityCount).entityTotal(entityTotal).attrDefinitionCount(attrCount)
                    .build();
            dataMap.put(id, statistics);
        }

        for (GraphQuality graphQuality : graphQualities) {
            Long selfId = graphQuality.getSelfId();
            GraphStatistics statistics = dataMap.get(selfId);
            graphQuality.setEntityCount(statistics.getEntityCount());
            graphQuality.setEntityTotal(statistics.getEntityTotal());
            graphQuality.setAttrDefinitionCount(statistics.getAttrDefinitionCount());
        }

        List<Long> collect = graphQualities.stream().map(GraphQuality::getSelfId).collect(Collectors.toList());
        // 缺少的概念
        Set<Long> set = dataMap.keySet().stream().filter(s -> !collect.contains(s)).collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(set)) {
            return;
        }
        for (Long key : set) {
            BasicInfo basicInfo = initFunc.getConceptMap().get(key);
            GraphQuality graphQuality = new GraphQuality();
            Long id = basicInfo.getId();
            graphQuality.setSelfId(id);
            graphQuality.setConceptId(basicInfo.getConceptId());
            graphQuality.setName(basicInfo.getName());
            graphQuality.setEntityCount(dataMap.get(id).getEntityCount());
            graphQuality.setEntityTotal(dataMap.get(id).getEntityTotal());
            graphQuality.setAttrDefinitionCount(dataMap.get(id).getAttrDefinitionCount());
            graphQuality.setSchemaIntegrity(0d);
            graphQuality.setReliability(0d);
            graphQualities.add(graphQuality);
        }
    }

    /**
     * 实时查询当前概念实体和属性值数量
     *
     * @param kgName
     * @param conceptId
     * @param graphAttrQualityRsp
     */
    private void statisticsAttr(String kgName, Long conceptId, GraphAttrQualityRsp graphAttrQualityRsp) {

        List<AttrQualityVO> attrQualityVOS = graphAttrQualityRsp.getAttrQualities();
        String kgDbName = graphRepository.findByKgNameAndUserId(kgName, SessionHolder.getUserId()).getDbName();

        InitFunc initFunc = new InitFunc();
        // 获取所有属性信息
        schemaUtils.getAttrIdAndName(kgDbName, initFunc.getAttrIdMap(), initFunc.getAttrNameMap());

        // 获取当前概念的实体数
        Long count = conceptUtils.countEntityByConceptId(kgDbName, conceptId);
        graphAttrQualityRsp.setEntityCount(count);

        // 对象属性ID
        Set<Integer> objectAttrId = Sets.newHashSet();
        // 基本属性ID
        Set<Integer> baseAttrId = Sets.newHashSet();
        // 当前概念自己的基本属性ID和对象属性ID
        schemaUtils.getSelfAttrTypeByConceptId(kgDbName, conceptId, objectAttrId, baseAttrId);

        Map<Integer, Long> attrValueCount = new HashMap<>();

        // 查询对象属性的属性值总数
        Map<Integer, Long> objectAttrValueCount = conceptUtils.countObjectAttrValueByAttrIds(kgDbName, objectAttrId);

        // 查询对象属性的属性值总数
        Map<Integer, Long> baseAttrValueCount = conceptUtils.countBaseAttrValueByAttrIds(kgDbName, baseAttrId);

        attrValueCount.putAll(objectAttrValueCount);
        attrValueCount.putAll(baseAttrValueCount);

        Map<String, Integer> attrNameMap = initFunc.getAttrNameMap();
        for (AttrQualityVO attrQualityVO : attrQualityVOS) {
            Integer selfId = attrNameMap.get(attrQualityVO.getAttrName());
            Long attrCount = attrValueCount.get(selfId);
            attrQualityVO.setAttrCount(attrCount);
        }

        // 缺少的属性ID
        Set<Integer> set = new HashSet<>();
        // 脚本获取的属性ID
        List<String> collect = attrQualityVOS.stream().map(AttrQualityVO::getAttrName).collect(Collectors.toList());
        // 实时查询的属性ID
        Set<Integer> keySet = attrValueCount.keySet();

        Map<Integer, String> attrIdMap = initFunc.getAttrIdMap();
        for (Integer id : keySet) {
            if (!collect.contains(attrIdMap.get(id))) {
                set.add(id);
            }
        }

        if (CollectionUtils.isEmpty(set)) {
            return;
        }
        for (Integer attrId : set) {
            AttrQualityVO attrQualityVO = new AttrQualityVO();
            attrQualityVO.setAttrName(attrIdMap.get(attrId));
            attrQualityVO.setAttrCount(attrValueCount.get(attrId));
            attrQualityVO.setAttrIntegrity(0d);
            attrQualityVO.setAttrReliability(0d);
            attrQualityVOS.add(attrQualityVO);
        }
    }

}
