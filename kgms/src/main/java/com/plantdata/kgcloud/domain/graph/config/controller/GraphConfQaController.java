package com.plantdata.kgcloud.domain.graph.config.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfQaService;
import com.plantdata.kgcloud.sdk.req.GraphConfQaReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfQaRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfQaStatusRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author jiangdeming
 * @date 2019/12/2
 */
@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfQaController {
    @Autowired
    private GraphConfQaService graphConfQaService;

    @ApiOperation("图谱配置-问答-新建")
    @PostMapping("/qa/{kgName}")
    public ApiReturn<List<GraphConfQaRsp>> save(@PathVariable("kgName") String kgName, @RequestBody @Valid List<GraphConfQaReq> req) {

        return ApiReturn.success(graphConfQaService.saveQa(kgName, req));
    }


    @ApiOperation("图谱配置-问答-获取")
    @GetMapping("/qa/{kgName}")
    public ApiReturn<List<GraphConfQaRsp>> selectQa(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(graphConfQaService.findByKgName(kgName));
    }

    @ApiOperation("图谱配置-问答-获取状态")
    @GetMapping("/qa/{kgName}/get/status")
    public ApiReturn<GraphConfQaStatusRsp> getStatus(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(graphConfQaService.getStatus(kgName));
    }

    @ApiOperation("图谱配置-问答-修改状态")
    @PostMapping("/qa/{kgName}/update/status/{status}")
    public ApiReturn getStatus(@PathVariable("kgName") String kgName,
                               @PathVariable("status") Integer status) {
        graphConfQaService.updateStatus(kgName, status);
        return ApiReturn.success();
    }
}
