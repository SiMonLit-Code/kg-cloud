package com.plantdata.kgcloud.domain.task.service.impl;

import com.plantdata.kgcloud.domain.task.entity.TaskGraphSnapshot;
import com.plantdata.kgcloud.domain.task.repository.TaskGraphSnapshotRepository;
import com.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotNameReq;
import com.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphSnapshotRsp;
import com.plantdata.kgcloud.domain.task.service.TaskGraphService;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 10:43
 **/
@Service
public class TaskGraphServiceImpl implements TaskGraphService {

    @Autowired
    private TaskGraphSnapshotRepository taskGraphSnapshotRepository;

    @Override
    public Page<TaskGraphSnapshotRsp> snapshotList(TaskGraphSnapshotReq req) {

        Sort sort = Sort.by(Sort.Direction.DESC, "updateAt");
        PageRequest of = PageRequest.of(req.getPage() - 1, req.getSize(), sort);
        TaskGraphSnapshot query = new TaskGraphSnapshot();
        if (StringUtils.hasText(req.getKgName())) {
            query.setKgName(req.getKgName());
        } else if (StringUtils.hasText(req.getUserId())) {
            query.setUserId(req.getUserId());
        }
        Page<TaskGraphSnapshot> page = taskGraphSnapshotRepository.findAll(Example.of(query), of);
        return page.map(ConvertUtils.convert(TaskGraphSnapshotRsp.class));
    }

    @Override
    public void snapshotDelete(Long id) {
        taskGraphSnapshotRepository.deleteById(id);
    }

    @Override
    public TaskGraphSnapshotRsp add(TaskGraphSnapshotNameReq req) {
        TaskGraphSnapshot snapshot = ConvertUtils.convert(TaskGraphSnapshot.class).apply(req);
        snapshot.setName("图谱备份_");
        snapshot.setCatalogue(req.getName());
        return new TaskGraphSnapshotRsp();
    }


}
