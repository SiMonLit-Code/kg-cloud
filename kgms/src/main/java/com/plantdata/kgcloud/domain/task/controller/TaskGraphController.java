package com.plantdata.kgcloud.domain.task.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphSnapshotRsp;
import com.plantdata.kgcloud.domain.task.rsp.TaskTemplateRsp;
import com.plantdata.kgcloud.domain.task.service.TaskGraphService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 10:28
 **/
@Api(tags = "任务相关")
@RestController
@RequestMapping("/task")
public class TaskGraphController {

    @Autowired
    private TaskGraphService taskGraphService;

    @ApiOperation("任务相关-快照-根据id删除")
    @GetMapping("/snapshot")
    public ApiReturn<Page<TaskGraphSnapshotRsp>> listByPage(TaskGraphSnapshotReq req) {
        return ApiReturn.success(taskGraphService.snapshotList(req));
    }

    @ApiOperation("任务相关-快照-根据id删除")
    @DeleteMapping("/snapshot/{id}")
    public ApiReturn delete(@PathVariable("id") Long id) {
        taskGraphService.snapshotDelete(id);
        return ApiReturn.success();
    }

    @GetMapping("template")
    @ApiOperation("任务相关-模板-组合任务模板")
    public ApiReturn<List<TaskTemplateRsp>> taskTemplateList() {
        return ApiReturn.success(taskGraphService.taskTemplateList());
    }
}
