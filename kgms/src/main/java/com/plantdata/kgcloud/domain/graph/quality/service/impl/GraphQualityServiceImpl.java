package com.plantdata.kgcloud.domain.graph.quality.service.impl;

import ai.plantdata.kg.common.bean.BasicInfo;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
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
import org.bson.Document;
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
    private InitFunc initFunc;

    @Autowired
    private MongoClient mongoClient;

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
        graphQualities.add(check(kgName, conceptId));

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
     * 实时查询概念实体和属性数量
     *
     * @param kgName
     * @param conceptId
     * @param graphQualities
     */
    private void statisticsConcept(String kgName, Long conceptId, List<GraphQuality> graphQualities) {
        String kgDbName = graphRepository.findByKgNameAndUserId(kgName, SessionHolder.getUserId()).getDbName();
        Map<Long, GraphStatistics> dataMap = new HashMap<>();
        // 初始化数据
        initFunc.init(kgDbName);

        Set<Long> sonAndSelfConceptIds = InitFunc.sonAndSelfConceptIds.get(conceptId);
        for (Long id : sonAndSelfConceptIds) {
            // 获取当前概念下的实体数量(不包含子概念)
            Long count = conceptUtils.countEntityByOneConceptId(kgDbName, id);

            // 获取当前概念下的属性数量(包含父概念属性)
            Long attrCount = conceptUtils.countAttrDefinSonParent(kgDbName, id);

            GraphStatistics statistics = GraphStatistics.builder()
                    .entityCount(count).attrDefinitionCount(attrCount.intValue())
                    .build();
            dataMap.put(id, statistics);
        }

        for (Long id : sonAndSelfConceptIds) {
            // 获取当前概念下的所有实体数量(包含子概念)
            Long countAll = 0L;
            Set<Long> sonAndSelfConceptId = InitFunc.sonAndSelfConceptIds.get(id);
            for (Long sonId : sonAndSelfConceptId) {
                countAll += dataMap.get(sonId).getEntityCount();
            }

            dataMap.get(id).setEntityTotal(countAll);
        }

        for (GraphQuality graphQuality : graphQualities) {
            Long selfId = graphQuality.getSelfId();
            GraphStatistics statistics = dataMap.get(selfId);
            graphQuality.setEntityCount(statistics.getEntityCount());
            graphQuality.setEntityTotal(statistics.getEntityTotal());
            graphQuality.setAttrDefinitionCount(statistics.getAttrDefinitionCount());
        }

        // 缺少的概念
        Set<Long> set = new HashSet<>();
        List<Long> collect = graphQualities.stream().map(GraphQuality::getSelfId).collect(Collectors.toList());
        Set<Long> keySet = dataMap.keySet();
        for (Long id : keySet) {
            if (!collect.contains(id)) {
                set.add(id);
            }
        }

        List<BasicInfo> list = conceptUtils.getBasicInfoByConceptId(kgDbName, set);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (BasicInfo basicInfo : list) {
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
     * 实时查询属性值数量
     *
     * @param kgName
     * @param conceptId
     * @param graphAttrQualityRsp
     */
    private void statisticsAttr(String kgName, Long conceptId, GraphAttrQualityRsp graphAttrQualityRsp) {
        List<AttrQualityVO> attrQualityVOS = graphAttrQualityRsp.getAttrQualities();
        String kgDbName = graphRepository.findByKgNameAndUserId(kgName, SessionHolder.getUserId()).getDbName();
        // 初始化数据
        initFunc.init(kgDbName);

        // 获取当前概念的实体数
        Long count = conceptUtils.countEntityByOneConceptId(kgDbName, conceptId);
        graphAttrQualityRsp.setEntityCount(count);

        // 该概念自己的属性ID和类型（对象 基本）
        Map<Integer, Integer> attrType = schemaUtils.getSelfAttrTypeByConceptId(kgDbName, conceptId);

        // 该概念自己独有的属性
        List<Integer> selfAttrIds = schemaUtils.getSelfAttrIdsByConceptId(kgDbName, conceptId);

        Map<Integer, Long> attrValueCount = new HashMap<>();
        for (Integer selfAttrId : selfAttrIds) {

            // 属性类型
            Integer type = attrType.get(selfAttrId);

            // 属性值数量
            Long attrValues = 0L;

            if (type == 1) {
                // 对象属性
                Document queryObj = new Document();
                queryObj.append("attr_id", selfAttrId);
                queryObj.append("entity_type", conceptId);
                // 该概念下的一个属性的属性值总数
                attrValues = mongoClient.getDatabase(kgDbName).getCollection("attribute_object").countDocuments(queryObj);
            } else if (type == 0) {
                // 基本属性
                // 概念自己属性值数量
                attrValues = schemaUtils.getValueCountByAttrId(kgDbName, selfAttrId);
                if (attrValues == null) {
                    attrValues = 0L;
                }
            } else {
                attrValues = 0L;
            }
            attrValueCount.put(selfAttrId, attrValues);
        }

        Map<String, Integer> attrNameMap = InitFunc.attrNameMap;
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

        Map<Integer, String> attrIdMap = InitFunc.attrIdMap;
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
