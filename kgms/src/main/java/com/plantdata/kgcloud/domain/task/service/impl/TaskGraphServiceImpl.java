package com.plantdata.kgcloud.domain.task.service.impl;

import com.plantdata.kgcloud.domain.task.entity.TaskGraphSnapshot;
import com.plantdata.kgcloud.domain.task.entity.TaskTemplate;
import com.plantdata.kgcloud.domain.task.repository.TaskGraphSnapshotRepository;
import com.plantdata.kgcloud.domain.task.repository.TaskTemplateRepository;
import com.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphSnapshotRsp;
import com.plantdata.kgcloud.domain.task.rsp.TaskTemplateRsp;
import com.plantdata.kgcloud.domain.task.service.TaskGraphService;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
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


}
