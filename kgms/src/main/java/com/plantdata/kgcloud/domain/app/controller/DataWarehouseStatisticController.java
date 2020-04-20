package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import com.plantdata.kgcloud.domain.app.service.DataSetStatisticService;
import com.plantdata.kgcloud.domain.app.service.DataWarehouseStatisticService;
import com.plantdata.kgcloud.sdk.constant.DimensionEnum;
import com.plantdata.kgcloud.sdk.req.SqlQueryReq;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import com.plantdata.kgcloud.sdk.rsp.DataWarehouse2dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DataWarehouse3dTableRsp;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author cx
 * @version 1.0
 * @date 2020/04/17 17:32
 */
@RestController
@RequestMapping("app/dataWarehouse/statistic")
public class DataWarehouseStatisticController implements SdkOpenApiInterface {

    @Resource
    private DataWarehouseStatisticService dataWarehouseStatisticService;

    @ApiOperation("统计数据二维/按表统计")
    @PostMapping("by2dTable")
    public ApiReturn<DataWarehouse2dTableRsp> statistic2dByTable(@Valid @RequestBody SqlQueryReq req) {
        return ApiReturn.success(dataWarehouseStatisticService.statisticBy2DTable(req));
    }

    @ApiOperation("统计数据三维/按表统计")
    @PostMapping("by3dTable")
    public ApiReturn<DataWarehouse3dTableRsp> statistic3dByTable(@Valid @RequestBody SqlQueryReq req) {
        return ApiReturn.success(dataWarehouseStatisticService.statisticBy3DTable(req));
    }
}
