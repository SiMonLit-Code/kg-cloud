package com.plantdata.kgcloud.domain.graph.quality.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.graph.quality.rsp.GraphAttrQualityRsp;
import com.plantdata.kgcloud.domain.graph.quality.rsp.GraphQualityRsp;
import com.plantdata.kgcloud.domain.graph.quality.service.GraphQualityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2020/3/21 16:03
 * @Description:
 */
@Api(tags = "图谱质量")
@RestController
@RequestMapping("quality")
public class GraphQualityController {

    @Autowired
    private GraphQualityService graphQualityService;

    @ApiOperation("当前图谱下的概念统计列表")
    @GetMapping("/{kgName}")
    public ApiReturn<List<GraphQualityRsp>> listConceptQuality(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(graphQualityService.listConceptQuality(kgName));
    }

    @ApiOperation("当前概念子概念质量统计信息")
    @GetMapping("/{kgName}/{conceptId}/son")
    public ApiReturn<List<GraphQualityRsp>> selfConceptDetail(@PathVariable("kgName") String kgName,
                                                              @PathVariable("conceptId") Long conceptId) {
        return ApiReturn.success(graphQualityService.sonConceptCount(kgName, conceptId));
    }

    @ApiOperation("概念本身质量统计信息")
    @GetMapping("/{kgName}/{selfId}/self")
    public ApiReturn<GraphAttrQualityRsp> detailByConceptId(@PathVariable("kgName") String kgName,
                                                            @PathVariable("selfId") Long selfId) {
        return ApiReturn.success(graphQualityService.detailByConceptId(kgName, selfId));
    }

    @ApiOperation("执行质量统计脚本")
    @GetMapping("/{kgName}/run")
    public ApiReturn run(@PathVariable("kgName") String kgName) {
        graphQualityService.run(kgName);
        return ApiReturn.success();
    }

    @ApiOperation("查询最新质量统计执行时间")
    @GetMapping("/{kgName}/time")
    public ApiReturn<Long> getTime(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(graphQualityService.getTime(kgName));
    }
}
