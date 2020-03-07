package com.plantdata.kgcloud.domain.access.controller;


import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.access.req.EtlConfigReq;
import com.plantdata.kgcloud.domain.access.req.KgConfigReq;
import com.plantdata.kgcloud.domain.access.req.RunTaskReq;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.sdk.req.DataAccessTaskConfigReq;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "数据接入流程任务")
@RestController
@RequestMapping("/access")
public class AccessTaskController {

    @Autowired
    private AccessTaskService accessTaskService;

    @ApiOperation("保存接入任务配置")
    @PostMapping("/save/etl/task")
    public ApiReturn<String> saveTask(@Valid @RequestBody EtlConfigReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(accessTaskService.saveEtlTask(userId, req));
    }

    @ApiOperation("保存入图任务配置")
    @PostMapping("/save/kg/task")
    public ApiReturn<String> saveTask(@Valid @RequestBody KgConfigReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(accessTaskService.saveKgTask(userId, req));
    }

    @ApiOperation("运行任务")

    @PostMapping("/run/task")
    public ApiReturn runTask(@Valid @RequestBody List<DataAccessTaskConfigReq> reqs) {
        String userId = SessionHolder.getUserId();
        accessTaskService.run(userId,reqs);
        return ApiReturn.success();
    }


    @ApiOperation("停止任务")
    @PostMapping("/stop/task")
    public ApiReturn stopTask(@Valid @RequestBody List<RunTaskReq> reqs) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success();
    }

}
