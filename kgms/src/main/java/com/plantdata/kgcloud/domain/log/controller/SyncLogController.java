package com.plantdata.kgcloud.domain.log.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.log.req.SyncLogReq;
import com.plantdata.kgcloud.domain.log.rsp.SyncLogRsp;
import com.plantdata.kgcloud.domain.log.service.SyncLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-13 21:57
 **/
@Api(tags = "同步日志")
@RestController
@RequestMapping("/sync/log")
public class SyncLogController {

    @Autowired
    private SyncLogService syncLogService;

    @ApiOperation("读取日志")
    @PostMapping("/list")
    public ApiReturn<BasePage<SyncLogRsp>> list(@Validated @RequestBody SyncLogReq req) {
        return ApiReturn.success(syncLogService.list(req));
    }
}
