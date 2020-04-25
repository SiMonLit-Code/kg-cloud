package com.plantdata.kgcloud.domain.dw.controller;

import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.req.*;
import com.plantdata.kgcloud.domain.dw.rsp.CustomTableRsp;
import com.plantdata.kgcloud.domain.dw.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.rsp.DWTableRsp;
import com.plantdata.kgcloud.domain.dw.rsp.ModelSchemaConfigRsp;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.domain.edit.rsp.FilePathRsp;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.sdk.req.DWConnceReq;
import com.plantdata.kgcloud.sdk.req.DWDatabaseReq;
import com.plantdata.kgcloud.sdk.req.DWTableReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.req.*;
import com.plantdata.kgcloud.sdk.rsp.DW2dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DW3dTableRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    @ApiOperation("数仓-查询行业数据库需要映射的表")
    @PatchMapping("/get/{databaseId}/mapping/table")
    public ApiReturn<List<JSONObject>> getDatabaseMappingTable(@PathVariable("databaseId") Long databaseId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.getDatabaseMappingTable(userId, databaseId));
    }

    @PostMapping("/test/connect")
    @ApiOperation("数仓-connect测试")
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
    public ApiReturn setTableCron(@Valid @RequestBody List<DWTableCronReq> reqs) {
        String userId = SessionHolder.getUserId();
        dwServince.setTableCron(userId, reqs);
        return ApiReturn.success();
    }


    @ApiOperation("数仓-统一调度")
    @PostMapping("/unified/scheduling")
    public ApiReturn unifiedScheduling(@Valid @RequestBody DWTableCronReq req) {
        String userId = SessionHolder.getUserId();
        dwServince.unifiedScheduling(userId, req);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-设置表调度开关")
    @PostMapping("/set/table/scheduling")
    public ApiReturn setTableScheduling(@Valid @RequestBody DWTableSchedulingReq req) {
        String userId = SessionHolder.getUserId();
        dwServince.setTableScheduling(userId, req);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-删除数仓")
    @DeleteMapping("/delete/database/{id}")
    public ApiReturn deleteDatabase(@PathVariable("id") Long id) {
        String userId = SessionHolder.getUserId();
        dwServince.deteleDatabase(userId, id);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-删除表")
    @DeleteMapping("/delete/table/{databaseId}/{tableId}")
    public ApiReturn deleteTable(@PathVariable("databaseId") Long databaseId,
                                 @PathVariable("tableId") Long tableId) {
        String userId = SessionHolder.getUserId();
        dwServince.deleteTable(userId, databaseId, tableId);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-清空表数据")
    @DeleteMapping("/delete/data/{databaseId}/{tableId}")
    public ApiReturn deleteData(@PathVariable("databaseId") Long databaseId,
                                @PathVariable("tableId") Long tableId) {
        String userId = SessionHolder.getUserId();
        dwServince.deleteData(userId, databaseId, tableId);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-读取远程数据库表信息")
    @GetMapping("/get/remote/{databaseId}/tables")
    public ApiReturn<List<JSONObject>> getRemoteTables(@PathVariable("databaseId") Long databaseId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.getRemoteTables(userId, databaseId));
    }

    @ApiOperation("数仓-添加远程库数据表")
    @PostMapping("/add/remote/{databaseId}/table")
    public ApiReturn addRemoteTables(@PathVariable("databaseId") Long databaseId,
                                     @RequestBody List<RemoteTableAddReq> reqList) {
        String userId = SessionHolder.getUserId();
        dwServince.addRemoteTables(userId, databaseId, reqList);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-更新数仓名")
    @PostMapping("/update/database/name")
    public ApiReturn updateDatabaseName(@RequestBody DWDatabaseNameReq req) {
        String userId = SessionHolder.getUserId();
        dwServince.updateDatabaseName(userId, req);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-查找所有数据库")
    @GetMapping("/database/all")
    public ApiReturn<List<DWDatabaseRsp>> findAll() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.findAll(userId));
    }

    @ApiOperation("数仓-查找所有数据库与表")
    @GetMapping("/database/table/list")
    public ApiReturn<List<DWDatabaseRsp>> databaseTableList() {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.databaseTableList(userId));
    }

    @ApiOperation("数仓-查找数仓详情")
    @GetMapping("/database/{id}")
    public ApiReturn<DWDatabaseRsp> getDatabase(@PathVariable("id") Long id) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.getDatabase(userId, id));
    }


    @ApiOperation("数仓-查询指定类型的数据库")
    @PostMapping("/database/list")
    public ApiReturn<Page<DWDatabaseRsp>> list(@RequestBody DWDatabaseQueryReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.list(userId, req));
    }

    @ApiOperation("数仓-查询数据库表")
    @GetMapping("/{databaseId}/table/all")
    public ApiReturn<List<DWTableRsp>> findTableAll(@PathVariable("databaseId") Long databaseId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.findTableAll(userId, databaseId));
    }

    @ApiOperation("数仓-模式上传")
    @PostMapping("/{databaseId}/model/upload")
    public ApiReturn<FilePathRsp> tagUpload(
            @PathVariable("databaseId") Long databaseId,
            @RequestParam(value = "file") MultipartFile file) {

        long size = file.getSize();
        if (size > 1024 * 1024) {
            return ApiReturn.fail(KgmsErrorCodeEnum.FILE_OUT_LIMIT);
        }

        dwServince.modelUpload(databaseId, file);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-更新pd类型打标")
    @PostMapping("/{databaseId}/update/tagjson")
    public ApiReturn updateTagJson(
            @PathVariable("databaseId") Long databaseId,
            @RequestBody List<TagJsonReq> tagJsonReqs) {

        dwServince.updateTagJson(databaseId, tagJsonReqs);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-获取pd类型打标")
    @PatchMapping("/{databaseId}/get/tagjson")
    public ApiReturn<List<ModelSchemaConfigRsp>> getTagJson(
            @PathVariable("databaseId") Long databaseId) {

        return ApiReturn.success(dwServince.getTagJson(databaseId));
    }

    @ApiOperation("数仓-获取自定义类型打标 isDefault默认false，传true获取自动生成信息")
    @GetMapping("/get/custom/label")
    public ApiReturn<List<CustomTableRsp>> getCustomLabel(Long databaseId, Boolean isDefault) {

        return ApiReturn.success(dwServince.getCustomLabel(databaseId,isDefault));
    }

    @ApiOperation("数仓-获取表字段枚举值")
    @GetMapping("/get/table/field/enum")
    public ApiReturn<List<String>> getTableFieldEnum(Long databaseId,
            String tableName,String field) {

        return ApiReturn.success(dwServince.getTableFieldEnum(databaseId,tableName,field));
    }

    @ApiOperation("数仓-更新自定义类型打标")
    @PostMapping("/{databaseId}/update/custom/label")
    public ApiReturn updateCustomLabel(
            @PathVariable("databaseId") Long databaseId,
            @RequestBody List<CustomTableRsp> tableLabels) {
        dwServince.updateCustomLabel(databaseId,tableLabels);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-模式发布")
    @PostMapping("/model/push")
    public ApiReturn push(@RequestBody ModelPushReq req) {

        String userId = SessionHolder.getUserId();
        dwServince.push(userId, req);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-查看模式")
    @PatchMapping("/model/get/{id}")
    public ApiReturn<ModelSchemaConfigRsp> getModel(@PathVariable("id") Long id) {

        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.getModel(userId, id));
    }

    @ApiOperation("数仓-创建表")
    @PostMapping("/create/table")
    public ApiReturn<DWTableRsp> createTable(@Valid @RequestBody DWTableReq req) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(dwServince.createTable(userId, req));
    }

    @ApiOperation("数仓-批量创建表")
    @PostMapping("/batch/create/table")
    public ApiReturn batchCreateTable(@Valid @RequestBody List<DWTableReq> reqs) {
        String userId = SessionHolder.getUserId();
        dwServince.batchCreateTable(userId, reqs);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-文件上传")
    @PostMapping("/{databaseId}/upload")
    public ApiReturn upload(
            @PathVariable("databaseId") Long databaseId,
            Long tableId,
            @RequestParam(value = "file") MultipartFile file) {
        long size = file.getSize();
        if (size > 10 * 1024 * 1024) {
            return ApiReturn.fail(KgmsErrorCodeEnum.FILE_OUT_LIMIT);
        }

        String userId = SessionHolder.getUserId();
        dwServince.upload(userId, databaseId, tableId, file);
        return ApiReturn.success();
    }

    @ApiOperation("数仓-示例文件下载")
    @GetMapping("/download/{databaseId}")
    public void exampleDownload(
            @PathVariable("databaseId") Long databaseId,
            HttpServletResponse response) {
        try {
            String userId = SessionHolder.getUserId();
            dwServince.exampleDownload(userId, databaseId, response);
            return;
        } catch (Exception e) {
            return;
        }
    }

    @ApiOperation("数仓-schema-识别")
    @PostMapping("/schema")
    public ApiReturn<List<DataSetSchema>> resolve(@RequestParam(value = "file") MultipartFile file) {
        return ApiReturn.success(dwServince.schemaResolve(file, null));
    }

    @ApiOperation("数仓-日志状态列表")
    @GetMapping("/database/status/{databaseId}")
    public ApiReturn DataStatusList(@PathVariable("databaseId") Long databaseId) {
        return ApiReturn.success(dwServince.DataStatusList(databaseId));
    }

    @ApiOperation("数仓-数据库日志记录列表")
    @GetMapping("/database/tableLogList")
    public ApiReturn tableLogList(@Valid DWTableLogReq dwTableLogReq) {
        return ApiReturn.success(dwServince.tableLogList(dwTableLogReq));
    }

    @ApiOperation("统计数据二维/按表统计")
    @PostMapping("statistic/by2dTable")
    public ApiReturn<DW2dTableRsp> statistic2dByTable(@Valid @RequestBody SqlQueryReq req) {
        String userId = SessionHolder.getUserId();
        req.setDbName(dwServince.findById(userId,req.getDbId()).getDataName());
        return ApiReturn.success(dwServince.statisticBy2DTable(req));
    }

    @ApiOperation("统计数据三维/按表统计")
    @PostMapping("statistic/by3dTable")
    public ApiReturn<DW3dTableRsp> statistic3dByTable(@Valid @RequestBody SqlQueryReq req) {
        String userId = SessionHolder.getUserId();
        req.setDbName(dwServince.findById(userId,req.getDbId()).getDataName());
        return ApiReturn.success(dwServince.statisticBy3DTable(req));
    }
}
