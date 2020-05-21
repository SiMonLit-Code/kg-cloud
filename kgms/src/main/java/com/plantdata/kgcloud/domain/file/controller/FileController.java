package com.plantdata.kgcloud.domain.file.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.file.req.FileDatabaseNameReq;
import com.plantdata.kgcloud.domain.file.rsq.FileDatabaseRsp;
import com.plantdata.kgcloud.domain.file.rsq.FileTableRsp;
import com.plantdata.kgcloud.domain.file.service.FileService;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lp
 * @date 2020/5/20 9:44
 */
@Api(tags = "文件系统管理")
@RestController
@RequestMapping("/file/system")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("文件系统管理-查找所有数据库")
    @GetMapping("/database/all")
    public ApiReturn<List<FileDatabaseRsp>> findAll() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileService.findAll(userId));
    }

    @ApiOperation("文件系统管理-查找所有数据库和表")
    @GetMapping("/database/table/list")
    public ApiReturn<List<FileDatabaseRsp>> databaseTableList() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileService.databaseTableList(userId));
    }

    @ApiOperation("文件系统管理-查询数据表")
    @GetMapping("/{databaseId}/table/all")
    public ApiReturn<List<FileTableRsp>> findTableAll(@PathVariable("databaseId") Long databaseId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileService.findTableAll(userId, databaseId));
    }

    @ApiOperation("文件系统管理-查找数据库详情")
    @GetMapping("/database/{databaseId}")
    public ApiReturn<FileDatabaseRsp> getDatabase(@PathVariable("databaseId") Long databaseId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileService.getDatabase(userId, databaseId));
    }

    @ApiOperation("文件系统管理-创建数据库")
    @PostMapping("/create/database")
    public ApiReturn<FileDatabaseRsp> createDatabase(@RequestParam("name") String name) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileService.createDatabase(userId, name));
    }

    @ApiOperation("文件系统管理-创建数据表")
    @PostMapping("/create/{databaseId}/table")
    public ApiReturn<FileTableRsp> createTable(@PathVariable("databaseId") Long databaseId,
                                               @RequestParam("name") String name) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(fileService.createTable(userId, databaseId, name));
    }

    @ApiOperation("文件系统管理-批量创建数据表")
    @PostMapping("/batch/create/{databaseId}/table")
    public ApiReturn batchCreateTable(@PathVariable("databaseId") Long databaseId,
                                      @RequestBody List<String> names) {
        String userId = SessionHolder.getUserId();
        fileService.batchCreateTable(userId, databaseId, names);
        return ApiReturn.success();
    }

    @ApiOperation("文件系统管理-更新数据库名")
    @PostMapping("/update/database/name")
    public ApiReturn updateDatabaseName(@RequestBody FileDatabaseNameReq req) {
        String userId = SessionHolder.getUserId();
        fileService.updateDatabaseName(userId, req);
        return ApiReturn.success();
    }

    @ApiOperation("文件系统管理-删除数据库")
    @DeleteMapping("/delete/database/{databaseId}")
    public ApiReturn deleteDatabase(@PathVariable("databaseId") Long databaseId) {
        String userId = SessionHolder.getUserId();
        fileService.deleteDatabase(userId, databaseId);
        return ApiReturn.success();
    }

    @ApiOperation("文件系统管理-删除数据表")
    @DeleteMapping("/delete/table/{databaseId}/{tableId}")
    public ApiReturn deleteTable(@PathVariable("databaseId") Long databaseId,
                                 @PathVariable("tableId") Long tableId) {
        String userId = SessionHolder.getUserId();
        fileService.deleteTable(userId, databaseId, tableId);
        return ApiReturn.success();
    }

    @ApiOperation("文件系统管理-清空表数据")
    @DeleteMapping("/delete/data/{databaseId}/{tableId}")
    public ApiReturn deleteData(@PathVariable("databaseId") Long databaseId,
                                @PathVariable("tableId") Long tableId) {
        String userId = SessionHolder.getUserId();
        fileService.deleteData(userId, databaseId, tableId);
        return ApiReturn.success();
    }
}
