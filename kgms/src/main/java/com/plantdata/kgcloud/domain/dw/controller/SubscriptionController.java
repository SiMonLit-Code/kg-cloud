package com.plantdata.kgcloud.domain.dw.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dw.req.GraphMapReq;
import com.plantdata.kgcloud.domain.dw.rsp.GraphMapRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderSearchRsp;
import com.plantdata.kgcloud.domain.dw.service.GraphMapService;
import com.plantdata.kgcloud.sdk.req.PreBuilderSearchReq;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description: 订阅检测
 * @author: czj
 * @create: 2020-03-24 14:55
 **/

@Api(tags = "订阅检测")
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private GraphMapService graphMapService;

    @ApiOperation("订阅检测-列表")
    @PostMapping("/list")
    public ApiReturn<List<GraphMapRsp>> list(@RequestBody GraphMapReq graphMapReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphMapService.list(userId,graphMapReq));
    }

    @ApiOperation("订阅检测-当前图谱订阅的数据库")
    @GetMapping("/list/database/{kgName}")
    public ApiReturn<List<JSONObject>> listDatabase(@PathVariable("kgName")String kgName) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(graphMapService.listDatabase(userId,kgName));
    }

    @ApiOperation("订阅检测-开启/关闭订阅")
    @PatchMapping("/schedule/{id}/{status}")
    public ApiReturn scheduleSwitch(@PathVariable("id")Integer id,@PathVariable("status")Integer status) {
        graphMapService.scheduleSwitch(id,status);
        return ApiReturn.success();
    }

    @ApiOperation("订阅检测-批量开启/关闭订阅")
    @PostMapping("/schedule/batch/{status}")
    public ApiReturn batchScheduleSwitch(@RequestBody List<Integer> ids,@PathVariable("status")Integer status) {
        graphMapService.batchScheduleSwitch(ids,status);
        return ApiReturn.success();
    }


    @ApiOperation("订阅检测-按图谱全量开启/关闭订阅")
    @PatchMapping("/schedule/kg")
    public ApiReturn scheduleSwitch(@RequestBody GraphMapReq graphMapReq) {
        graphMapService.scheduleSwitchByKgName(graphMapReq);
        return ApiReturn.success();
    }

    @ApiOperation("订阅检测-删除订阅")
    @PatchMapping("/delete/schedule/{id}")
    public ApiReturn deleteSchedule(@PathVariable("id")Integer id) {
        graphMapService.deleteSchedule(id);
        return ApiReturn.success();
    }

    @ApiOperation("订阅检测-删除订阅")
    @PostMapping("/delete/schedule/batch")
    public ApiReturn deleteSchedule(@RequestBody List<Integer> ids) {
        graphMapService.batchDeleteSchedule(ids);
        return ApiReturn.success();
    }
}
