package com.plantdata.kgcloud.domain.data.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.data.req.*;
import com.plantdata.kgcloud.domain.data.rsp.DataStoreRsp;
import com.plantdata.kgcloud.domain.data.rsp.DbAndTableRsp;
import com.plantdata.kgcloud.domain.data.service.DataStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2020/3/24 17:06
 * @Description:
 */
@Api(tags = "数仓")
@RestController
@RequestMapping("/data/store")
public class DataStoreController {

    @Autowired
    private DataStoreService dataStoreService;

    @ApiOperation("数据库与表列表")
    @GetMapping("/dt")
    public ApiReturn<List<DbAndTableRsp>> listDataStore(DtReq req) {
        return ApiReturn.success(dataStoreService.listAll(req));
    }

    @ApiOperation("数仓列表")
    @GetMapping("/list")
    public ApiReturn<BasePage<DataStoreRsp>> listDataStore(DataStoreScreenReq req) {
        return ApiReturn.success(dataStoreService.listDataStore(req));
    }

    @ApiOperation("修正数据")
    @PostMapping("/update")
    public ApiReturn updateData(@RequestBody DataStoreModifyReq modifyReq) {
        dataStoreService.updateData(modifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("删除数据")
    @DeleteMapping("/{id}/delete")
    public ApiReturn deleteData(@PathVariable("id") String id) {
        dataStoreService.deleteData(id);
        return ApiReturn.success();
    }

    @ApiOperation("批量发回数仓")
    @PostMapping("/send")
    public ApiReturn sendData(@RequestBody List<String> ids) {
        dataStoreService.sendData(ids);
        return ApiReturn.success();
    }

    @ApiOperation("更新数仓错误数据")
    @PostMapping("/err/update")
    public ApiReturn updateErrData(@RequestBody DataStoreReq req) {
        dataStoreService.updateErrData(req);
        return ApiReturn.success();
    }

    @ApiOperation("数仓错误数据修正列表")
    @GetMapping("/err/list")
    public ApiReturn<BasePage<DataStoreRsp>> listErrDataStore(DataStoreScreenReq req) {
        return ApiReturn.success(dataStoreService.listErrDataStore(req));
    }
}
