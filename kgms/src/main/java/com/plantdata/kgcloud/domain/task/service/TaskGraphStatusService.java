package com.plantdata.kgcloud.domain.task.service;

import com.plantdata.kgcloud.domain.task.entity.TaskGraphStatus;
import com.plantdata.kgcloud.domain.task.req.TaskGraphStatusReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphStatusCheckRsp;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphStatusRsp;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 17:45
 * @Description:
 */
public interface TaskGraphStatusService {

    /**
     * 添加异步任务
     *
     * @param taskGraphStatusReq
     * @return
     */
    TaskGraphStatus create(TaskGraphStatusReq taskGraphStatusReq);

    /**
     * 根据kgName 任务详情
     *
     * @param kgName
     * @return
     */
    TaskGraphStatusRsp getDetailsByKgName(String kgName);

    /**
     * 校验是否可以新建异步任务
     *
     * @param kgName
     * @return
     */
    TaskGraphStatusCheckRsp checkTask(String kgName);

    /**
     * 修改任务状态
     *
     * @param id
     * @param taskStatus
     */
    void updateTaskStatus(Long id, String taskStatus);
}
