package com.plantdata.kgcloud.domain.task.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.task.req.KettleReq;
import com.plantdata.kgcloud.domain.task.service.KettleService;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 14:07
 **/
@Api(tags = "任务相关")
@RestController
@RequestMapping("/task")
public class KettleController {

    @Autowired
    private KettleService kettleService;

    @ApiOperation("etl配置保存")
    @PostMapping("/etl/save")
    public ApiReturn<String> saveEtl(@RequestBody KettleReq kettleReq) throws Exception {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(kettleService.kettleSave(userId, kettleReq));
    }

    @PostMapping("/etl/test")
    @ApiOperation("etl配置connect测试")
    public ApiReturn testEtl(@RequestBody KettleReq kettleReq) {
        return ApiReturn.success(kettleService.kettleService(kettleReq));
    }

    @ApiOperation("etl配置sql预览")
    @PostMapping("/etl/preview")
    public ApiReturn previewEtl(@RequestBody KettleReq kettleReq) {
        return ApiReturn.success(kettleService.kettlePreview(kettleReq));
    }
}
