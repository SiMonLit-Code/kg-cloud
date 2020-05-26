package com.plantdata.kgcloud.domain.task.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.task.rsp.EntityIntimateRsp;
import com.plantdata.kgcloud.domain.task.rsp.EntityKeyRsp;
import com.plantdata.kgcloud.domain.task.service.TaskAlgorithmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiezhenxiang 2020/5/21
 */
@Api(tags = "算法任务相关")
@RestController
@RequestMapping("/task/algo")
public class TaskAlgorithmController {

    @Autowired
    TaskAlgorithmService algorithmService;

    @GetMapping("{kgName}/entity/key")
    @ApiOperation("关键节点-列表")
    public ApiReturn<BasePage<EntityKeyRsp>> entityKey(@PathVariable("kgName") String kgName,
                                                     String kw,
                                                     BaseReq baseReq) {
        return ApiReturn.success(algorithmService.entityKeyList(kgName, kw, baseReq));
    }

    @GetMapping("{kgName}/entity/intimate")
    @ApiOperation("亲密关系-列表")
    public ApiReturn<BasePage<EntityIntimateRsp>> entityIntimate(@PathVariable("kgName") String kgName,
                                                                 BaseReq baseReq) {
        return ApiReturn.success(algorithmService.entityIntimateList(kgName, baseReq));
    }
}