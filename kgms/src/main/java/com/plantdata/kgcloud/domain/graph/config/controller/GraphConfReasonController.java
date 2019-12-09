package com.plantdata.kgcloud.domain.graph.config.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.graph.config.req.GraphConfReasonReq;
import com.plantdata.kgcloud.domain.graph.config.rsp.GraphConfReasonRsp;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfReasonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by jdm on 2019/12/9 16:10.
 */

@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfReasonController {

    @Autowired
    private GraphConfReasonService graphConfReasoningService;

    @ApiOperation("图谱配置-推理-新增")
    @PostMapping("/reason/{kgName}")
    public ApiReturn<GraphConfReasonRsp> saveReasoning(@PathVariable("kgName") String kgName , @RequestBody @Valid GraphConfReasonReq req) {
        return ApiReturn.success(graphConfReasoningService.createReasoning(kgName,req));
    }


    @ApiOperation("图谱配置-推理-查询所有")
    @GetMapping("/reason")
    public ApiReturn<List<GraphConfReasonRsp>> selectReasoninglAll() {
        return ApiReturn.success(graphConfReasoningService.findAll());
    }

    @ApiOperation("图谱配置-推理-详情")
    @GetMapping("/reason/{id}")
    public ApiReturn<GraphConfReasonRsp> detailReasoning(@PathVariable("id") Long id) {
        return ApiReturn.success(graphConfReasoningService.findById(id));
    }

    @ApiOperation("图谱配置-推理-删除")
    @DeleteMapping("/reason/{kgName}/{id}")
    public ApiReturn deleteReasoning(@PathVariable("id") Long id) {
        graphConfReasoningService.deleteReasoning(id);
        return ApiReturn.success();
    }

    @ApiOperation("图谱配置-推理-更新")
    @PatchMapping("/reason/{kgName}/{id}")
    public ApiReturn<GraphConfReasonRsp> updateReasoning(@PathVariable("id") Long id, @RequestBody @Valid GraphConfReasonReq req) {
        return ApiReturn.success(graphConfReasoningService.updateReasoning(id, req));
    }


}
