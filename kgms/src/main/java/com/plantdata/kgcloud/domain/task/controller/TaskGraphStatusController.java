package com.plantdata.kgcloud.domain.task.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphStatusCheckRsp;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphStatusRsp;
import com.plantdata.kgcloud.domain.task.service.TaskGraphStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: LinHo
 * @Date: 2019/12/17 10:30
 * @Description:
 */
@Api(tags = "异步任务")
@RestController
@RequestMapping("/async")
public class TaskGraphStatusController {

    @Autowired
    private TaskGraphStatusService taskGraphStatusService;

    @ApiOperation("查询异步任务状态详情")
    @PostMapping("/task/{kgName}")
    public ApiReturn<TaskGraphStatusRsp> getDetails(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(taskGraphStatusService.getDetailsByKgName(kgName));
    }

    @ApiOperation("校验是否可以创建异步任务")
    @GetMapping("/task/{kgName}/check")
    public ApiReturn<TaskGraphStatusCheckRsp> checkTask(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(taskGraphStatusService.checkTask(kgName));
    }
}
