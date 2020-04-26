package com.plantdata.kgcloud.plantdata.controller;

import com.hiekn.basicnlptools.hanlp.HanLPService;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.DWClient;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.plantdata.kgcloud.sdk.rsp.DW2dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DW3dTableRsp;
import com.plantdata.kgcloud.sdk.req.SqlQueryReq;

import javax.validation.Valid;
import java.util.List;

/**
 * @author cx
 */
@RestController("DWController")
@RequestMapping("sdk/dw")
public class DWController implements SdkOldApiInterface {

    @Autowired
    public DWClient dwClient;

    @ApiOperation("统计数据仓库二维/按表统计")
    @PostMapping("statistic/by2dTable")
    public ApiReturn<DW2dTableRsp> statisticBy2dTable(@Valid @RequestBody SqlQueryReq req) {
        return dwClient.statisticBy2dTable(req);
    }

    @ApiOperation("统计数据仓库三维/按表统计")
    @PostMapping("statistic/by3dTable")
    public ApiReturn<DW3dTableRsp> statisticBy3dTable(@Valid @RequestBody SqlQueryReq req) {
        return dwClient.statisticBy3dTable(req);
    }

    @ApiOperation("数仓-查找所有数据库")
    @GetMapping("/database/all")
    public ApiReturn<List<DWDatabaseRsp>> findAll() {
        return dwClient.findAll();
    }

    @ApiOperation("数仓-查找所有数据库与表")
    @GetMapping("/database/table/list")
    public ApiReturn<List<DWDatabaseRsp>> databaseTableList() {
        return dwClient.databaseTableList();
    }
}
