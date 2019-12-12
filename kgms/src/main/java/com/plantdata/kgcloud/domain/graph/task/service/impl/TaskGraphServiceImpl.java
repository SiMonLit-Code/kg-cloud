package com.plantdata.kgcloud.domain.graph.task.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetRepository;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.domain.graph.task.entity.TaskGraphSearch;
import com.plantdata.kgcloud.domain.graph.task.entity.TaskGraphSnapshot;
import com.plantdata.kgcloud.domain.graph.task.entity.TaskTemplate;
import com.plantdata.kgcloud.domain.graph.task.repository.TaskGraphSearchRepository;
import com.plantdata.kgcloud.domain.graph.task.repository.TaskGraphSnapshotRepository;
import com.plantdata.kgcloud.domain.graph.task.repository.TaskTemplateRepository;
import com.plantdata.kgcloud.domain.graph.task.req.TaskGraphSnapshotReq;
import com.plantdata.kgcloud.domain.graph.task.rsp.TaskGraphSnapshotRsp;
import com.plantdata.kgcloud.domain.graph.task.rsp.TaskTemplateRsp;
import com.plantdata.kgcloud.domain.graph.task.service.TaskGraphService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.XxlAdminClient;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 10:43
 **/
@Service
public class TaskGraphServiceImpl implements TaskGraphService {

    @Autowired
    private TaskGraphSnapshotRepository taskGraphSnapshotRepository;
    @Autowired
    private TaskTemplateRepository taskTemplateRepository;
    @Autowired
    private XxlAdminClient xxlAdminClient;
    @Autowired
    private DataSetService dataSetService;
    @Autowired
    private TaskGraphSearchRepository searchRepository;
    @Autowired
    private DataSetRepository dataSetRepository;

    @Override
    public Page<TaskGraphSnapshotRsp> snapshotList(TaskGraphSnapshotReq req) {
        PageRequest of = PageRequest.of(req.getPage() - 1, req.getSize());
        Page<TaskGraphSnapshot> page;
        if (StringUtils.hasText(req.getKgName())) {
            TaskGraphSnapshot snapshot = TaskGraphSnapshot
                    .builder()
                    .kgName(req.getKgName())
                    .build();
            page = taskGraphSnapshotRepository.findAll(Example.of(snapshot), of);
        } else {
            page = taskGraphSnapshotRepository.findAll(of);
        }
        return page.map(ConvertUtils.convert(TaskGraphSnapshotRsp.class));
    }

    @Override
    public void snapshotDelete(Long id) {
        taskGraphSnapshotRepository.deleteById(id);
    }

    @Override
    public List<TaskTemplateRsp> taskTemplateList() {
        List<TaskTemplate> all = taskTemplateRepository.findAll();
        return all.stream().map(ConvertUtils.convert(TaskTemplateRsp.class)).collect(Collectors.toList());
    }

    @Override
    public Integer searchStatus(String kgName) {

        Optional<TaskGraphSearch> optional = searchRepository.findById(kgName);
        return optional.isPresent() ? optional.get().getTaskId() : -1;
    }

    @Override
    public Integer openSearch(String kgName) {

        String userId = SessionHolder.getUserId();
        Optional<TaskGraphSearch> optional = searchRepository.findById(kgName);
        Integer taskId;
        if (optional.isPresent()) {
            TaskGraphSearch confSearch = optional.get();
            taskId = confSearch.getTaskId();
        } else {
            taskId = createSearchTaskAndRun(userId, kgName);
            TaskGraphSearch confSearch = new TaskGraphSearch();
            confSearch.setTaskId(taskId);
            confSearch.setKgName(kgName);
            confSearch.setCreateAt(new Date());
            confSearch.setUpdateAt(new Date());
            searchRepository.save(confSearch);
        }
        String cron = "0 0 0 ? * *";
        xxlAdminClient.taskSchedule(userId, taskId, cron, 0L);
        return taskId;
    }

    private Integer createSearchTaskAndRun(String userId, String kgName) {

        // create task
        JSONObject config = new JSONObject()
                .fluentPut("title", "拼音检索导出_" + System.currentTimeMillis())
                .fluentPut("dataName", "pinyin_" + System.currentTimeMillis())
                .fluentPut("kgName", kgName)
                .fluentPut("type", 1)
                .fluentPut("conceptIds", new ArrayList<>());
        JSONObject task = new JSONObject()
                .fluentPut("kgName", kgName)
                .fluentPut("name", "拼音检索导出_" + System.currentTimeMillis())
                .fluentPut("taskType", "kg_export")
                .fluentPut("userId", userId)
                .fluentPut("config", config.toJSONString());
        ApiReturn apiReturn = xxlAdminClient.taskAdd(userId, task.toJSONString());
        if (apiReturn.getErrCode() != 200) {
            throw new BizException(apiReturn.getErrCode(), apiReturn.getMessage());
        }

        // run task
        Integer taskId = (Integer) apiReturn.getData();
        apiReturn = xxlAdminClient.taskRun(userId, taskId, null, 0L, 0L);
        if (apiReturn.getErrCode() != 200) {
            throw new BizException(apiReturn.getErrCode(), apiReturn.getMessage());
        }
        return taskId;
    }

    @Override
    public void closeSearch(String kgName) {

        String userId = SessionHolder.getUserId();
        Optional<TaskGraphSearch> optional = searchRepository.findById(kgName);
        if (optional.isPresent()) {
            TaskGraphSearch confSearch = optional.get();
            xxlAdminClient.taskUnschedule(userId, confSearch.getTaskId());
            xxlAdminClient.taskDelete(userId, confSearch.getTaskId());
            searchRepository.deleteById(kgName);
            List<DataSet> ls = dataSetRepository.findByDataTypeAndCreateWayAndDataNameLike(2, "自动创建(图谱导出)", "pinyin_");
            ls.forEach(s -> {
                dataSetService.delete(userId, s.getId());
            });
        }
    }

    @Override
    public void flushSearch(String kgName) {

        Optional<TaskGraphSearch> optional = searchRepository.findById(kgName);
        String userId = SessionHolder.getUserId();
        if (optional.isPresent()) {
            ApiReturn apiReturn = xxlAdminClient.taskRun(userId, optional.get().getTaskId(), null, 0L, 0L);
            if (apiReturn.getErrCode() != 200) {
                throw new BizException(apiReturn.getMessage());
            }
        } else {
            throw new BizException("任务不存在");
        }
    }

}
