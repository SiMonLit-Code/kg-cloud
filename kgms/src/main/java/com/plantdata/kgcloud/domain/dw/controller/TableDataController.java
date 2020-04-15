package com.plantdata.kgcloud.domain.dw.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableBatchReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableUpdateReq;
import com.plantdata.kgcloud.sdk.req.DwTableDataStatisticReq;
import com.plantdata.kgcloud.domain.dw.rsp.DWFileTableRsp;
import com.plantdata.kgcloud.domain.dw.service.TableDataService;
import com.plantdata.kgcloud.domain.edit.rsp.FilePathRsp;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @program: kg-cloud-kgms
 * @description: 数仓表
 * @author: czj
 * @create: 2020-03-30 16:30
 **/

@Api(tags = "数仓表")
@RestController
@RequestMapping("/table/data")
public class TableDataController {

    @Autowired
    private TableDataService tableDataService;


    @ApiOperation("数仓数据-分页条件查询")
    @PatchMapping("/list/{databaseId}/{tableId}")
    public ApiReturn<Page<Map<String, Object>>> getData(
            @PathVariable("tableId") Long tableId,
            @PathVariable("databaseId") Long databaseId,
            DataOptQueryReq baseReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(tableDataService.getData(userId, databaseId, tableId, baseReq));
    }

    @ApiOperation("数仓数据-根据Id查询")
    @PatchMapping("/{databaseId}/{tableId}/{dataId}")
    public ApiReturn<Map<String, Object>> findById(
            @PathVariable("tableId") Long tableId,
            @PathVariable("databaseId") Long databaseId,
            @PathVariable("dataId") String dataId
    ) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(tableDataService.getDataById(userId, databaseId, tableId, dataId));
    }

    @ApiOperation("文件数仓-文件上传")
    @PostMapping("/file/add")
    public ApiReturn<FilePathRsp> fileAdd(
            @RequestBody DWFileTableReq fileTableReq) {

        tableDataService.fileAdd(fileTableReq);
        return ApiReturn.success();
    }

    @ApiOperation("文件数仓-文件批量上传")
    @PostMapping("/file/add/batch")
    public ApiReturn<FilePathRsp> fileAddBatch(
            DWFileTableBatchReq fileTableReq,
            MultipartFile[] files) {

        tableDataService.fileAddBatch(fileTableReq, files);
        return ApiReturn.success();
    }

    @ApiOperation("文件数仓-编辑")
    @PostMapping("/file/update")
    public ApiReturn<FilePathRsp> fileUpdate(
            @RequestBody DWFileTableUpdateReq fileTableReq) {

        tableDataService.fileUpdate(fileTableReq);
        return ApiReturn.success();
    }

    @ApiOperation("文件数仓-删除")
    @PatchMapping("/file/delete/{id}")
    public ApiReturn<FilePathRsp> fileDelete(
            @PathVariable("id") Integer id) {

        tableDataService.fileDelete(id);
        return ApiReturn.success();
    }


    @ApiOperation("文件数仓-分页条件查询")
    @PatchMapping("/file/list/{dataBaseId}/{tableId}")
    public ApiReturn<Page<DWFileTableRsp>> getFileData(
            @PathVariable("tableId") Long tableId,
            @PathVariable("dataBaseId") Long dataBaseId,
            DataOptQueryReq baseReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(tableDataService.getFileData(userId, dataBaseId, tableId, baseReq));
    }



    @ApiOperation("数仓表统计")
    @PostMapping("statistic/{dataStoreId}/{tableId}")
    public ApiReturn<List<Map<String, Object>>> dataSoreStatistic(@PathVariable("dataStoreId") long dataStoreId,
                                                                  @PathVariable("tableId") long tableId,
                                                                  @RequestBody DwTableDataStatisticReq statisticReq)  {
        System.out.println(JsonUtils.objToJson(statisticReq));
        return ApiReturn.success(tableDataService.statistic(SessionHolder.getUserId(), dataStoreId, tableId, statisticReq));
    }

}
