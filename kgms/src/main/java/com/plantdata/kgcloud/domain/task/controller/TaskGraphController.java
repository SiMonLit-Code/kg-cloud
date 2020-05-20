package com.plantdata.kgcloud.domain.task.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphSnapshotRsp;
import com.plantdata.kgcloud.domain.task.service.TaskGraphService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @ApiOperation("任务相关-快照-快照列表")
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

    @PostMapping("/graph/export/conversion")
    @ApiOperation("概念转拼音")
    public ApiReturn<List<Map<String, Object>>> nameConversion(@RequestBody List<Map<String,Object>> maps) {
        return ApiReturn.success(taskGraphService.nameConversion(maps));
    }

}
