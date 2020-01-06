package com.plantdata.kgcloud.domain.j2r.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.j2r.entity.PathPreviewReq;
import com.plantdata.kgcloud.domain.j2r.entity.Setting;
import com.plantdata.kgcloud.domain.j2r.service.J2rService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author xiezhenxiang 2019/12/9
 */
@RestController
@RequestMapping("/j2r")
@Api(tags = "J2R")
public class J2rController {

    @Autowired
    private J2rService j2rService;


    @PostMapping("/config/check")
    @ApiOperation("检测配置格式是否正确")
    public ApiReturn<Boolean> checkSetting(@ApiParam("配置参数") @RequestBody Setting setting) {

        boolean bool = j2rService.checkSetting(setting);
        return ApiReturn.success(bool);
    }

    @PostMapping("/path/preview")
    @ApiOperation("jsonPath配置预览")
    public ApiReturn<Object> pathReview(@RequestBody PathPreviewReq req) {

        Object rs = j2rService.pathReview(req.getJsonStr(), req.getJsonPaths());
        return ApiReturn.success(rs);
    }

    @PostMapping("/config/preview/{jsonId}")
    @ApiOperation("全局实体属性配置预览")
    public ApiReturn<Map> configPreview(@ApiParam("预览jsonId") @PathVariable String jsonId,
                                        @RequestBody Setting setting) {

        Map<String, Object> rsMap = j2rService.preview(jsonId, setting);
        return ApiReturn.success(rsMap);
    }
}
