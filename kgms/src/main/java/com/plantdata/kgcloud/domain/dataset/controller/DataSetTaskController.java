package com.plantdata.kgcloud.domain.dataset.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dataset.req.EtlSaveRequest;
import com.plantdata.kgcloud.domain.dataset.service.KettleService;
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
@Api(tags = "数据集管理")
@RestController
@RequestMapping("/etl")
public class DataSetTaskController {

    @Autowired
    private KettleService kettleService;

    @PostMapping("/config/save")
    @ApiOperation("etl配置保存")
    public ApiReturn<String> saveEtl(@RequestBody EtlSaveRequest etlSaveRequest) throws Exception {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(kettleService.saveEtl(userId, etlSaveRequest));
    }

    @PostMapping("/config/test")
    @ApiOperation("etl配置connect测试")
    public ApiReturn testEtl(@RequestBody EtlSaveRequest etlSaveRequest) {
        return ApiReturn.success(kettleService.kettleService(etlSaveRequest));
    }

    @ApiOperation("etl配置sql预览")
    @PostMapping("/config/preview")
    public ApiReturn previewEtl(@RequestBody EtlSaveRequest etlSaveRequest) {
        return ApiReturn.success(kettleService.previewSqlEtl(etlSaveRequest));
    }
}
