package com.plantdata.kgcloud.domain.graph.config.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfAlgorithmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 图谱算法配置
 * Created by plantdata-1007 on 2019/11/29.
 */
@Api(tags = "图谱算法配置")
@RestController
@RequestMapping("/config")
public class GraphConfAlgorithmController {
    @Autowired
    private GraphConfAlgorithmService graphConfAlgorithmService;

    @ApiOperation("图谱新建算法")
    @PostMapping("/algorithm/{kgName}")
    public ApiReturn<GraphConfAlgorithmRsp> save(@PathVariable("kgName") String kgName ,@RequestBody @Valid GraphConfAlgorithmReq req) {
        return ApiReturn.success(graphConfAlgorithmService.createAlgorithm(kgName,req));
    }

    @ApiOperation("图谱更新算法")
    @PatchMapping("/algorithm/{kgName}/{id}")
    public ApiReturn<GraphConfAlgorithmRsp> update(@PathVariable("id") Long id, @RequestBody @Valid GraphConfAlgorithmReq req) {
        return ApiReturn.success(graphConfAlgorithmService.updateAlgorithm(id, req));
    }

    @ApiOperation("图谱删除算法")
    @DeleteMapping("/algorithm/{kgName}/{id}")
    public void delete(@PathVariable("id") Long id) {
        graphConfAlgorithmService.deleteAlgorithm(id);
    }

    @ApiOperation("图谱查询算法")
    @GetMapping("/algorithm/{kgName}")
    public ApiReturn<Page<GraphConfAlgorithmRsp>> select(@PathVariable("kgName") String kgName , BaseReq baseReq) {
        return ApiReturn.success(graphConfAlgorithmService.findByKgName(kgName ,baseReq));
    }
}
