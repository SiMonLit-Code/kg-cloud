package com.plantdata.kgcloud.domain.d2r.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.d2r.entity.PreviewReq;
import com.plantdata.kgcloud.domain.d2r.entity.PreviewRsp;
import com.plantdata.kgcloud.domain.d2r.service.D2rService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author xiezhenxiang 2019/12/9
 */
@RestController
@RequestMapping("/d2r_transform")
@Api(tags = "D2R")
public class D2rController {

    @Autowired
    private D2rService d2rService;

    @PostMapping("/task/preview")
    @ApiOperation("预览")
    public ApiReturn<PreviewRsp> preview(@Valid @RequestBody PreviewReq previewReq) {

        PreviewRsp rep = d2rService.preview(previewReq);
        return ApiReturn.success(rep);
    }
}
