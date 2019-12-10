package com.plantdata.kgcloud.domain.j2r.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.domain.j2r.entity.Setting;
import com.plantdata.kgcloud.domain.j2r.service.J2rService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.JacksonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
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

    @GetMapping("/json/get")
    @ApiOperation("获取单条json")
    public ApiReturn<Page<Document>> getJson(@ApiParam( "数据集Id") @NotNull Integer dataSetId,
                                           @ApiParam(value = "序号", defaultValue = "1") Integer index) {

        Page<Document> rs = j2rService.jsonStr(dataSetId, index);
        return ApiReturn.success(rs);
    }

    @PostMapping("/config/check")
    @ApiOperation("检测配置格式是否正确")
    public ApiReturn<Boolean> checkSetting(@ApiParam("配置参数") @NotBlank String setting) {

        Setting j2rSetting = JacksonUtils.readValue(setting, Setting.class);
        boolean bool = j2rService.checkSetting(j2rSetting);
        return ApiReturn.success(bool);
    }

    @PostMapping("/path/preview")
    @ApiOperation("jsonPath配置预览")
    public ApiReturn<Object> pathReview(@ApiParam @NotNull String jsonStr,
                                              @ApiParam("jsonPath[]") String jsonPaths) {
        List<String> jsonPathLs;
        try {
            jsonPathLs = JacksonUtils.getInstance().readValue(jsonPaths, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            throw BizException.of(CommonErrorCode.BAD_REQUEST);
        }
        Object rs = j2rService.pathReview(jsonStr, jsonPathLs);
        return ApiReturn.success(rs);
    }

    @PostMapping("/config/preview")
    @ApiOperation("全局实体属性配置预览")
    public ApiReturn<Map> configPreview(@ApiParam("预览jsonId") @NotNull String jsonId,
                                        @ApiParam("配置参数") @NotBlank String setting) {

        Setting j2rSetting = JacksonUtils.readValue(setting, Setting.class);
        Map<String, Object> rsMap = j2rService.preview(jsonId, j2rSetting);
        return ApiReturn.success(rsMap);
    }
}
