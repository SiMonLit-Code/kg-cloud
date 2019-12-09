package com.plantdata.kgcloud.domain.graph.config.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfKgql;
import com.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfKgqlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * 图谱业务配置
 * Created by plantdata-1007 on 2019/12/2.
 */
@Api(tags = "图谱业务配置")
@RestController
@RequestMapping("/config")
public class GraphConfKgqlController {
    @Autowired
    private GraphConfKgqlService graphConfKgqlService;

    @ApiOperation("图谱新建业务")
    @PostMapping("/kgql/{kgName}")
    public ApiReturn<GraphConfKgqlRsp> save(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfKgqlReq req) {

        return ApiReturn.success(graphConfKgqlService.createKgql(kgName, req));
    }

    @ApiOperation("图谱更新业务")
    @PatchMapping("/kgql/{id}")
    public ApiReturn<GraphConfKgqlRsp> update(@PathVariable("id") Long id, @RequestBody @Valid GraphConfKgqlReq req) {
        return ApiReturn.success(graphConfKgqlService.updateKgql(id, req));
    }

    @ApiOperation("图谱删除业务")
    @DeleteMapping("/kgql/{id}")
    public void deleteKgql(@PathVariable("id") Long id) {
        graphConfKgqlService.deleteKgql(id);
    }

    @ApiOperation("图谱查询业务")
    @GetMapping("/kgql/{kgName}")
    public ApiReturn<Page<GraphConfKgqlRsp>> select(@PathVariable("kgName") String kgName, @RequestParam("kgName") Integer ruleType, BaseReq baseReq) {
        return ApiReturn.success(graphConfKgqlService.findByKgName(kgName, baseReq, ruleType));
    }

    @ApiOperation("图谱业务详情")
    @GetMapping("/kgql/{id}")
    public ApiReturn<GraphConfKgqlRsp> detailKgql(@PathVariable("id") Long id) {
        return ApiReturn.success(graphConfKgqlService.findById(id));
    }
}
