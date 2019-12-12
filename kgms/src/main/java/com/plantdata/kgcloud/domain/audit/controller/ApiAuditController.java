package com.plantdata.kgcloud.domain.audit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.audit.req.ApiAuditReq;
import com.plantdata.kgcloud.domain.audit.req.ApiAuditTopReq;
import com.plantdata.kgcloud.domain.audit.req.ApiAuditUrlReq;
import com.plantdata.kgcloud.domain.audit.rsp.AuditKgNameRsp;
import com.plantdata.kgcloud.domain.audit.service.ApiAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 10:48
 **/
@Api(tags = "接口日志")
@RestController
@RequestMapping("/audit")
public class ApiAuditController {

    @Autowired
    private ApiAuditService apiAuditService;

    @ApiOperation("查找所有图谱名称")
    @GetMapping("/kgname")
    public ApiReturn<List<AuditKgNameRsp>> findAll() {
        return ApiReturn.success(apiAuditService.findAllKgName());
    }

    @ApiOperation("kgname分组")
    @PostMapping("/group/kgname")
    public ApiReturn groupByKgName(@RequestBody ApiAuditReq req) {
        return ApiReturn.success();
    }

    @ApiOperation("url分组")
    @PostMapping("/group/url")
    public ApiReturn groupByUrl(@RequestBody ApiAuditUrlReq req) {
        return ApiReturn.success();
    }

    @ApiOperation("top分组")
    @PostMapping("/group/top")
    public ApiReturn groupByTop(@RequestBody ApiAuditTopReq req) {
        return ApiReturn.success();
    }

    @ApiOperation("page分组")
    @PostMapping("/group/page")
    public ApiReturn groupByPage(@RequestBody ApiAuditReq req) {
        return ApiReturn.success();
    }
}
