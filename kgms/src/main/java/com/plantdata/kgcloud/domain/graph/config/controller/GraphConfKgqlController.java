package com.plantdata.kgcloud.domain.graph.config.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfKgqlService;
import com.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 图谱业务配置
 *
 * @author jiangdeming
 * @date 2019/12/2
 */
@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfKgqlController {
    @Autowired
    private GraphConfKgqlService graphConfKgqlService;

    @ApiOperation("图谱配置-KGQL-新建")
    @PostMapping("/kgql/{kgName}")
    public ApiReturn<GraphConfKgqlRsp> saveKgql(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfKgqlReq req) {

        return ApiReturn.success(graphConfKgqlService.createKgql(kgName, req));
    }

    @ApiOperation("图谱配置-KGQL-更新")
    @PutMapping("/kgql/{id}")
    public ApiReturn<GraphConfKgqlRsp> updateKgql(@PathVariable("id") Long id, @RequestBody @Valid GraphConfKgqlReq req) {
        return ApiReturn.success(graphConfKgqlService.updateKgql(id, req));
    }

    @ApiOperation("图谱配置-KGQL-删除")
    @DeleteMapping("/kgql/{id}")
    public ApiReturn deleteKgql(@PathVariable("id") Long id) {
        graphConfKgqlService.deleteKgql(id);
        return ApiReturn.success();
    }

    @ApiOperation("图谱配置-KGQL-查询")
    @GetMapping("/kgql/{kgName}/{ruleType}")
    public ApiReturn<Page<GraphConfKgqlRsp>> selectKgql(@PathVariable("kgName") String kgName ,@PathVariable("ruleType") Integer ruleType, BaseReq baseReq) {
        return ApiReturn.success(graphConfKgqlService.findByKgNameAndRuleType(kgName,ruleType, baseReq));
    }

    @ApiOperation("图谱配置-KGQL-详情")
    @GetMapping("/kgql/detail/{id}")
    public ApiReturn<GraphConfKgqlRsp> detailKgql( @PathVariable("id") Long id) {
        return ApiReturn.success(graphConfKgqlService.findById(id));
    }
}
