package com.plantdata.kgcloud.domain.reasoning.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.reasoning.req.*;
import com.plantdata.kgcloud.domain.reasoning.service.ReasoningService;
import com.plantdata.kgcloud.sdk.req.ReasoningExecuteReq;
import com.plantdata.kgcloud.sdk.req.ReasoningQueryReq;
import com.plantdata.kgcloud.sdk.rsp.ReasoningRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-05-20 16:49
 **/
@Api(tags = "在线推理")
@RestController
@RequestMapping("/reasoning")
public class ReasoningController {

    @Autowired
    private ReasoningService reasoningService;

    @ApiOperation("在线推理-列表")
    @PostMapping("/list")
    public ApiReturn<Page<ReasoningRsp>> list(@Validated  @RequestBody ReasoningQueryReq reasoningQueryReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(reasoningService.list(userId, reasoningQueryReq));
    }

    @ApiOperation("在线推理-添加")
    @PostMapping("/add")
    public ApiReturn<ReasoningRsp> add(@Validated @RequestBody ReasoningAddReq reasoningAddReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(reasoningService.add(userId, reasoningAddReq));
    }

    @ApiOperation("在线推理-详情")
    @GetMapping("/get/{id}")
    public ApiReturn<ReasoningRsp> get(@PathVariable("id")Integer id) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(reasoningService.get(userId, id));
    }

    @ApiOperation("在线推理-更新")
    @PostMapping("/update")
    public ApiReturn<ReasoningRsp> update(@Validated  @RequestBody ReasoningUpdateReq reasoningUpdateReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(reasoningService.update(userId, reasoningUpdateReq));
    }

    @ApiOperation("在线推理-删除")
    @DeleteMapping("/delete/{id}")
    public ApiReturn delete(@PathVariable("id")Integer id) {
        String userId = SessionHolder.getUserId();
        reasoningService.delete(userId, id);
        return ApiReturn.success();
    }

    @ApiOperation("在线推理-验证")
    @PostMapping("/verification")
    public ApiReturn<CommonBasicGraphExploreRsp> verification(@RequestBody ReasoningVerifyReq reasoningVerifyReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(reasoningService.verification(userId, reasoningVerifyReq));
    }

    @ApiOperation("在线推理-执行")
    @PostMapping("/execute")
    public ApiReturn<CommonBasicGraphExploreRsp> execute(@RequestBody ReasoningExecuteReq reasoningExecuteReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(reasoningService.execute(userId, reasoningExecuteReq));
    }


}
