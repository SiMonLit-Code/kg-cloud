package com.plantdata.kgcloud.domain.graph.config.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.graph.config.req.GraphConfReasoningReq;
import com.plantdata.kgcloud.domain.graph.config.rsp.GraphConfReasoningRsp;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfReasoningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by jdm on 2019/12/9 16:10.
 */

@Api(tags = "图谱推理")
@RestController
@RequestMapping("/config")
public class GraphConfReasoningController {

    @Autowired
    private GraphConfReasoningService graphConfReasoningService;

    @ApiOperation("图谱新建推理")
    @PostMapping("/reason/{kgName}")
    public ApiReturn<GraphConfReasoningRsp> saveReasoning(@PathVariable("kgName") String kgName , @RequestBody @Valid GraphConfReasoningReq req) {
        return ApiReturn.success(graphConfReasoningService.createReasoning(kgName,req));
    }


    @ApiOperation("图谱查询推理所有")
    @GetMapping("/reason")
    public ApiReturn<List<GraphConfReasoningRsp>> selectReasoninglAll() {
        return ApiReturn.success(graphConfReasoningService.findAll());
    }

    @ApiOperation("图谱推理详情")
    @GetMapping("/reason/{id}")
    public ApiReturn<GraphConfReasoningRsp> detailReasoning(@PathVariable("id") Long id) {
        return ApiReturn.success(graphConfReasoningService.findById(id));
    }

    @ApiOperation("图谱删除推理")
    @DeleteMapping("/reason/{kgName}/{id}")
    public void deleteReasoning(@PathVariable("id") Long id) {
        graphConfReasoningService.deleteReasoning(id);
    }

    @ApiOperation("图谱更新推理")
    @PatchMapping("/reason/{kgName}/{id}")
    public ApiReturn<GraphConfReasoningRsp> updateReasoning(@PathVariable("id") Long id, @RequestBody @Valid GraphConfReasoningReq req) {
        return ApiReturn.success(graphConfReasoningService.updateReasoning(id, req));
    }


}
