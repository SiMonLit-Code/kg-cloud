package com.plantdata.kgcloud.domain.graph.config.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfStatisticalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by plantdata-1007 on 2019/12/3.
 */
@Api(tags = "图谱统计配置")
@RestController
@RequestMapping("/config")
public class GraphConfStatisticalController {
    @Autowired
    private GraphConfStatisticalService graphConfStatisticalService;

    @ApiOperation("图谱新建统计")
    @PostMapping("/statistical/{kgName}")
    public ApiReturn<GraphConfStatisticalRsp> saveStatistical(@PathVariable("kgName") String kgName , @RequestBody @Valid GraphConfStatisticalReq req) {
        return ApiReturn.success(graphConfStatisticalService.createStatistical(kgName,req));
    }

    @ApiOperation("图谱更新统计")
    @PatchMapping("/statistical/{kgName}/{id}")
    public ApiReturn<GraphConfStatisticalRsp> updateStatistical(@PathVariable("id") Long id, @RequestBody @Valid GraphConfStatisticalReq req) {
        return ApiReturn.success(graphConfStatisticalService.updateStatistical(id, req));
    }

    @ApiOperation("图谱删除统计")
    @DeleteMapping("/statistical/{kgName}/{id}")
    public void deleteStatistical(@PathVariable("id") Long id) {
        graphConfStatisticalService.deleteStatistical(id);
    }

    @ApiOperation("图谱批量删除统计")
    @DeleteMapping("/statistical/{kgName}/{entities}")
    public void batchDeleteStatistical(@PathVariable("entities")Iterable entities) {
        graphConfStatisticalService.deleteInBatch(entities);
    }

    @ApiOperation("图谱查询统计")
    @GetMapping("/statistical/{kgName}")
    public ApiReturn<List<GraphConfStatisticalRsp>> selectStatistical(@PathVariable("kgName") String kgName) {
        return ApiReturn.success(graphConfStatisticalService.findByKgName(kgName));
    }

    @ApiOperation("图谱查询统计所有")
    @GetMapping("/statistical")
    public ApiReturn<List<GraphConfStatisticalRsp>> selectStatisticalAll() {
        return ApiReturn.success(graphConfStatisticalService.findAll());
    }
}
