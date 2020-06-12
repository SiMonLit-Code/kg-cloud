package com.plantdata.kgcloud.domain.graph.config.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.alibaba.fastjson.JSON;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfAlgorithmService;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReqList;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.AlgorithmStatisticeRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 图谱算法配置
 *
 * @author jiangdeming
 * @date 2019/11/29
 */
@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfAlgorithmController {
    @Autowired
    private GraphConfAlgorithmService graphConfAlgorithmService;

    @ApiOperation("图谱配置-算法-新建")
    @PostMapping("/algorithm/{kgName}")
    public ApiReturn<GraphConfAlgorithmRsp> save(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfAlgorithmReq req) {
        return ApiReturn.success(graphConfAlgorithmService.createAlgorithm(kgName, req));
    }

    @ApiOperation("图谱配置-算法-更新")
    @PutMapping("/algorithm/{id}")
    public ApiReturn<GraphConfAlgorithmRsp> update(@PathVariable("id") Long id, @RequestBody @Valid GraphConfAlgorithmReq req) {
        return ApiReturn.success(graphConfAlgorithmService.updateAlgorithm(id, req));
    }

    @ApiOperation("图谱配置-算法-删除")
    @DeleteMapping("/algorithm/{id}")
    public ApiReturn delete(@PathVariable("id") Long id) {
        graphConfAlgorithmService.deleteAlgorithm(id);
        return ApiReturn.success();
    }

    @ApiOperation("图谱配置-算法-获取")
    @GetMapping("/algorithm/{kgName}")
    public ApiReturn<Page<GraphConfAlgorithmRsp>> select(@PathVariable("kgName") String kgName, GraphConfAlgorithmReqList baseReq) {
        return ApiReturn.success(graphConfAlgorithmService.findByKgName(kgName, baseReq));
    }

    @ApiOperation("图谱配置-算法-详情")
    @GetMapping("/algorithm/detail/{id}")
    public ApiReturn<GraphConfAlgorithmRsp> detailAlgorithm(@PathVariable("id") Long id) {
        return ApiReturn.success(graphConfAlgorithmService.findById(id));
    }

    @ApiOperation("test1")
    @PostMapping("/test1")
    public RestResp<AlgorithmStatisticeRsp> test1() {


        int i = 0;
        int j = 1 / i;
        String str = "{\"xaxis\":[\"x1\",\"x2\",\"x3\"],\"chartTypes\":[\"line\",\"chart\"],\"series\":[{\"name\":\"名称\",\"data\":[3,5,1],\"ids\":[[\"id1\",\"id2\",\"id3\"],[\"id4\",\"id5\",\"id6\",\"id7\",\"id8\"],[\"id9\"]]}]}";
        AlgorithmStatisticeRsp rsp = JSON.parseObject(str,AlgorithmStatisticeRsp.class);
        return new RestResp(rsp);
    }
}
