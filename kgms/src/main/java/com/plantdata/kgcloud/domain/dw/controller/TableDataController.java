package com.plantdata.kgcloud.domain.dw.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dw.service.TableDataService;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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


    @ApiOperation("数据集-数据-分页条件查询")
    @PatchMapping("/list/{databaseId}/{tableId}")
    public ApiReturn<Page<Map<String, Object>>> findAll(
            @PathVariable("tableId") Long tableId,
            @PathVariable("databaseId") Long databaseId,
            DataOptQueryReq baseReq
    ) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(tableDataService.getData(userId,databaseId, tableId, baseReq));
    }

    @ApiOperation("数据集-数据-根据Id查询")
    @PatchMapping("/{databaseId}/{tableId}/{dataId}")
    public ApiReturn<Map<String, Object>> findById(
            @PathVariable("tableId") Long tableId,
            @PathVariable("databaseId") Long databaseId,
            @PathVariable("dataId") String dataId
    ) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(tableDataService.getDataById(userId,databaseId, tableId, dataId));
    }


}
