package com.plantdata.kgcloud.domain.access.controller;


import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.access.req.RunTaskReq;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.sdk.req.DataAccessTaskConfigReq;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "数据接入流程任务")
@RestController
@RequestMapping("/access")
public class AccessTaskController {

    @Autowired
    private AccessTaskService accessTaskService;


    @ApiOperation("运行任务")
    @PostMapping("/run/task")
    public ApiReturn runTask(@Valid @RequestBody List<DataAccessTaskConfigReq> reqs,Integer taskId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(accessTaskService.run(userId,reqs,taskId));
    }

    @ApiOperation("查看任务")
    @PatchMapping("/get/task/{id}")
    public ApiReturn getTask(@PathVariable("id")Integer id) {
        return ApiReturn.success(accessTaskService.getTask(id));
    }

    @ApiOperation("停止任务")
    @PostMapping("/stop/task")
    public ApiReturn stopTask(@Valid @RequestBody List<RunTaskReq> reqs) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success();
    }

}
