package com.plantdata.kgcloud.domain.dw.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.req.DWTableCronReq;
import com.plantdata.kgcloud.domain.dw.req.RemoteTableAddReq;
import com.plantdata.kgcloud.domain.dw.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.rsp.DWTableRsp;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.sdk.req.DWConnceReq;
import com.plantdata.kgcloud.sdk.req.DWDatabaseReq;
import com.plantdata.kgcloud.sdk.req.DWTableReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "数仓")
@RestController
@RequestMapping("/dw")
public class DWController {

    @Autowired
    private DWService dwServince;

    @ApiOperation("数仓-创建数据库")
    @PostMapping("/create/database")
    public ApiReturn<DWDatabaseRsp> createDatabase(@Valid @RequestBody DWDatabaseReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.createDatabase(userId, req));
    }

    @PostMapping("/test/connect")
    @ApiOperation("connect测试")
    public ApiReturn testConnect(@RequestBody DWConnceReq req) {
        return ApiReturn.success(dwServince.testConnect(req));
    }


    @ApiOperation("数仓-设置连接信息")
    @PostMapping("/set/connect")
    public ApiReturn<DWDatabaseRsp> setConn(@Valid @RequestBody DWConnceReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.setConn(userId, req));
    }


    @ApiOperation("数仓-设置表更新频率")
    @PostMapping("/set/table/cron")
    public ApiReturn setTableCron(@Valid @RequestBody DWTableCronReq req) {
        String userId = SessionHolder.getUserId();
        dwServince.setTableCron(userId, req);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-读取远程数据库表信息")
    @GetMapping("/get/remote/{databaseId}/tables")
    public ApiReturn<List<String>> getRemoteTables(@PathVariable("databaseId") Long databaseId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.getRemoteTables(userId, databaseId));
    }

    @ApiOperation("数仓-添加远程库数据表")
    @PostMapping("/add/remote/{databaseId}/table")
    public ApiReturn<List<Long>> addRemoteTables(@PathVariable("databaseId") Long databaseId,
                                     @RequestBody List<RemoteTableAddReq> reqList) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.addRemoteTables(userId, databaseId,reqList));
    }

    @ApiOperation("数仓-查找所有数据库")
    @GetMapping("/database/all")
    public ApiReturn<List<DWDatabaseRsp>> findAll() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.findAll(userId));
    }

    @ApiOperation("数仓-查询数据库表")
    @GetMapping("/{databaseId}/table/all")
    public ApiReturn<List<DWTableRsp>> findTableAll(@PathVariable("databaseId") Long databaseId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.findTableAll(userId,databaseId));
    }

    @ApiOperation("数仓-yaml上传")
    @PostMapping("/{databaseId}/yaml/upload")
    public ApiReturn yamlUpload(
            @PathVariable("databaseId") Long databaseId,
            @RequestParam(value = "file") MultipartFile file) {
        long size = file.getSize();
        if (size > 1024 * 1024) {
            return ApiReturn.fail(KgmsErrorCodeEnum.FILE_OUT_LIMIT);
        }
        try {
            String userId = SessionHolder.getUserId();
            dwServince.yamlUpload(userId, databaseId, file);
            return ApiReturn.success();
        } catch (Exception e) {
            return ApiReturn.fail(KgmsErrorCodeEnum.DATASET_IMPORT_FAIL);
        }

    }

    @ApiOperation("数仓-创建表")
    @PostMapping("/create/table")
    public ApiReturn<DWTableRsp> createTable(@Valid @RequestBody DWTableReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.createTable(userId, req));
    }

    @ApiOperation("数仓-文件上传")
    @PostMapping("/{databaseId}/{tableId}/upload")
    public ApiReturn D(
            @PathVariable("databaseId") Long databaseId,
            @PathVariable("tableId") Long tableId,
            @RequestParam(value = "file") MultipartFile file) {
        long size = file.getSize();
        if (size > 1024 * 1024) {
            return ApiReturn.fail(KgmsErrorCodeEnum.FILE_OUT_LIMIT);
        }
        try {
            String userId = SessionHolder.getUserId();
            dwServince.upload(userId, databaseId, tableId, file);
            return ApiReturn.success();
        } catch (Exception e) {
            return ApiReturn.fail(KgmsErrorCodeEnum.DATASET_IMPORT_FAIL);
        }

    }

    @ApiOperation("数仓-schema-识别")
    @PostMapping("/schema")
    public ApiReturn<List<DataSetSchema>> resolve(@RequestParam(value = "file") MultipartFile file) {
        return ApiReturn.success(dwServince.schemaResolve(file));
    }
}
