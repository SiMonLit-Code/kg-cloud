package com.plantdata.kgcloud.domain.prebuilder.controller;


import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.prebuilder.aop.PostHandler;
import com.plantdata.kgcloud.domain.prebuilder.req.ModelPushReq;
import com.plantdata.kgcloud.domain.prebuilder.req.PreBuilderCountReq;
import com.plantdata.kgcloud.domain.prebuilder.req.PreBuilderMatchAttrReq;
import com.plantdata.kgcloud.domain.prebuilder.req.PreBuilderSearchReq;
import com.plantdata.kgcloud.domain.prebuilder.rsp.PreBuilderMatchAttrRsp;
import com.plantdata.kgcloud.domain.prebuilder.rsp.PreBuilderSearchRsp;
import com.plantdata.kgcloud.domain.prebuilder.service.PreBuilderService;
import com.plantdata.kgcloud.sdk.req.PreBuilderGraphMapReq;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "预构建模式")
@RestController
@RequestMapping("/builder")
public class PreBuildController {

    @Autowired
    private PreBuilderService preBuilderService;

    @ApiOperation("预构建模式-查找所有")
    @PostMapping("/all")
    @PostHandler(id = 12021001)
    public ApiReturn findModel(@RequestBody PreBuilderSearchReq preBuilderSearchReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.findModel(userId, preBuilderSearchReq));
    }

    @ApiOperation("预构建模式-匹配属性统计")
    @PostMapping("/match/attr/count")
    @PostHandler(id = 12021002)
    public ApiReturn<PreBuilderCountReq> matchAttrCount(@RequestBody PreBuilderMatchAttrReq preBuilderMatchAttrReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.matchAttrCount(userId, preBuilderMatchAttrReq));
    }

    @ApiOperation("预构建模式-获取数据库模式")
    @PostMapping("/{databaseId}/detail")
    @PostHandler(id = 12021003)
    public ApiReturn<PreBuilderSearchRsp> databaseDetail(@PathVariable("databaseId") Long databaseId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.databaseDetail(userId, databaseId));
    }

    @ApiOperation("预构建模式-匹配属性")
    @PostMapping("/match/attr")
    @PostHandler(id = 12021004)
    public ApiReturn<Page<PreBuilderMatchAttrRsp>> matchAttr(@RequestBody PreBuilderMatchAttrReq preBuilderMatchAttrReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.matchAttr(userId, preBuilderMatchAttrReq));
    }

    @ApiOperation("预构建模式-引入模式配置保存")
    @PostMapping("/save/graph/map")
    @PostHandler(id = 12021005)
    public ApiReturn<JSONObject> saveGraphMap(@RequestBody PreBuilderGraphMapReq preBuilderGraphMapReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.saveGraphMap(userId, preBuilderGraphMapReq));
    }

    @ApiOperation("预构建模式-查询分类")
    @GetMapping("/get/types")
    @PostHandler(id = 12021006)
    public ApiReturn<List<String>> getTypes(Boolean isManage) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.getTypes(userId, isManage));
    }

    @ApiOperation("预构建模式-发布图谱模式")
    @PostMapping("/push/graph/model")
    @PostHandler(id = 12021007)
    public ApiReturn pushGraphModel(@RequestBody ModelPushReq req) {
        String userId = SessionHolder.getUserId();
        preBuilderService.pushGraphModel(userId, req);
        return ApiReturn.success();
    }

}
