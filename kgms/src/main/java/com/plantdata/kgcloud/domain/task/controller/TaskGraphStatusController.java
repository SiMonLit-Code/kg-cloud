package com.plantdata.kgcloud.domain.task.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphStatusRsp;
import com.plantdata.kgcloud.domain.task.service.TaskGraphStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/task/{id}")
    public ApiReturn<TaskGraphStatusRsp> getDetails(@PathVariable("id")Long id) {
        return ApiReturn.success(taskGraphStatusService.getDetails(id));
    }
}
