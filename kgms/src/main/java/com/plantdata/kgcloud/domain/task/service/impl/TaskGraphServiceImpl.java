package com.plantdata.kgcloud.domain.task.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetRepository;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.domain.task.entity.TaskGraphSearch;
import com.plantdata.kgcloud.domain.task.entity.TaskGraphSnapshot;
import com.plantdata.kgcloud.domain.task.entity.TaskTemplate;
import com.plantdata.kgcloud.domain.task.repository.TaskGraphSearchRepository;
import com.plantdata.kgcloud.domain.task.repository.TaskGraphSnapshotRepository;
import com.plantdata.kgcloud.domain.task.repository.TaskTemplateRepository;
import com.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphSnapshotRsp;
import com.plantdata.kgcloud.domain.task.rsp.TaskTemplateRsp;
import com.plantdata.kgcloud.domain.task.service.TaskGraphService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.XxlAdminClient;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        ObjectNode config = JacksonUtils.getInstance().createObjectNode()
                .put("title", "拼音检索导出_" + System.currentTimeMillis())
                .put("dataName", "pinyin_" + System.currentTimeMillis())
                .put("kgName", kgName)
                .put("type", 1)
                .putPOJO("conceptIds", new ArrayList<>());
        ObjectNode task = JacksonUtils.getInstance().createObjectNode()
                .put("kgName", kgName)
                .put("name", "拼音检索导出_" + System.currentTimeMillis())
                .put("taskType", "kg_export")
                .put("userId", userId)
                .put("config", config.toString());
        ApiReturn apiReturn = xxlAdminClient.taskAdd(userId, task.toString());
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

    @Override
    public List<Map<String, Object>> nameConversion(List<Map<String, Object>> maps) {
        try {
            for (Map<String, Object> map : maps) {
                String mapperName = map.get("mapperName").toString();
                String id = map.get("id").toString();
                String after = PinyinHelper.convertToPinyinString(mapperName, "", PinyinFormat.WITHOUT_TONE) + "_" + id;
                map.put("mapperName", after);
            }
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return maps;
    }

}
