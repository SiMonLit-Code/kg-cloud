package com.plantdata.kgcloud.domain.task.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.task.entity.TaskGraphStatus;
import com.plantdata.kgcloud.domain.task.repository.TaskGraphStatusRepository;
import com.plantdata.kgcloud.domain.task.req.TaskGraphStatusReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphStatusRsp;
import com.plantdata.kgcloud.domain.task.service.TaskGraphService;
import com.plantdata.kgcloud.domain.task.service.TaskGraphStatusService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 19:33
 * @Description:
 */
@Service
public class TaskGraphStatusServiceImpl implements TaskGraphStatusService {

    @Autowired
    private TaskGraphStatusRepository taskGraphStatusRepository;

    @Autowired
    private KgKeyGenerator  kgKeyGenerator;



    @Override
    public TaskGraphStatus create(TaskGraphStatusReq taskGraphStatusReq) {
        TaskGraphStatus taskGraphStatus = ConvertUtils.convert(TaskGraphStatus.class).apply(taskGraphStatusReq);
        taskGraphStatus.setId(kgKeyGenerator.getNextId());
        taskGraphStatus = taskGraphStatusRepository.save(taskGraphStatus);
        System.out.println(taskGraphStatus);
        return taskGraphStatus;
    }

    @Override
    public TaskGraphStatusRsp getDetails(Long id) {
        Optional<TaskGraphStatus> optional = taskGraphStatusRepository.findById(id);
        TaskGraphStatus taskGraphStatus =
                optional.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.TASK_STATUS_NOT_EXISTS));
        return ConvertUtils.convert(TaskGraphStatusRsp.class).apply(taskGraphStatus);
    }

    @Override
    public void updateTaskStatus(Long id, String taskStatus) {
        Optional<TaskGraphStatus> optional = taskGraphStatusRepository.findById(id);
        TaskGraphStatus taskGraphStatus =
                optional.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.TASK_STATUS_NOT_EXISTS));
        taskGraphStatus.setStatus(taskStatus);
        taskGraphStatusRepository.save(taskGraphStatus);
    }
}
