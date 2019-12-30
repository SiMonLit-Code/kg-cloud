package com.plantdata.kgcloud.domain.annotation.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.annotation.entity.AnnotationRsp;
import com.plantdata.kgcloud.domain.annotation.entity.SettingReq;
import com.plantdata.kgcloud.domain.annotation.service.AnnotationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标引预览
 * @author xiezhenxiang 2019/12/28
 */
@RestController
@RequestMapping("/annotation")
@Api(tags = "自动标引")
public class AnnotationController {

    @Autowired
    private AnnotationService annotationService;

    @PostMapping("/preview")
    @ApiOperation("预览")
    public ApiReturn<BasePage<AnnotationRsp>> preview(@RequestBody SettingReq setting) {
        return ApiReturn.success(annotationService.preview(setting));
    }
}
