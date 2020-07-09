package com.plantdata.kgcloud.domain.graph.config.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfIndexService;
import com.plantdata.kgcloud.sdk.rsp.GraphConfIndexStatusRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-13 17:37
 **/
@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfIndexController {

    @Autowired
    private GraphConfIndexService graphConfIndexService;


    @ApiOperation("图谱配置-索引-获取状态")
    @GetMapping("/index/{kgName}/get/status")
    public ApiReturn<GraphConfIndexStatusRsp> getStatus(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(graphConfIndexService.getStatus(kgName));
    }

    @ApiOperation("图谱配置-索引-开启")
    @PostMapping("/index/{kgName}/start")
    public ApiReturn start(@PathVariable("kgName") String kgName) {
        graphConfIndexService.updateStatus(kgName, 1);
        return ApiReturn.success();
    }
}
