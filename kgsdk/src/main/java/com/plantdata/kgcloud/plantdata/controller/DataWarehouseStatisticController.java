package com.plantdata.kgcloud.plantdata.controller;

import com.google.common.collect.Lists;
import com.hiekn.basicnlptools.hanlp.HanLPService;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.kgcompute.stat.PdStatServiceibit;
import com.plantdata.kgcloud.sdk.kgcompute.stat.bean.PdStatBaseBean;
import com.plantdata.kgcloud.sdk.kgcompute.stat.bean.PdStatBean;
import com.plantdata.kgcloud.sdk.kgcompute.stat.bean.PdStatOrderBean;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.plantdata.kgcloud.sdk.rsp.DataWarehouse2dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DataWarehouse3dTableRsp;
import com.plantdata.kgcloud.sdk.req.SqlQueryReq;

import javax.validation.Valid;

/**
 * @author cx
 */
@RestController("dataWarehouseStatisticController")
@RequestMapping("sdk/dataWarehouse/statistic")
public class DataWarehouseStatisticController implements SdkOldApiInterface {

    @Autowired
    public AppClient appClient;
    private HanLPService hanLPService = new HanLPService();

    @ApiOperation("统计数据仓库二维/按表统计")
    @PostMapping("/by2dTable")
    public ApiReturn<DataWarehouse2dTableRsp> statisticBy2dTable(@Valid @RequestBody SqlQueryReq req) {
        return appClient.statisticBy2dTable(req);
    }

    @ApiOperation("统计数据仓库三维/按表统计")
    @PostMapping("/by3dTable")
    public ApiReturn<DataWarehouse3dTableRsp> statisticBy3dTable(@Valid @RequestBody SqlQueryReq req) {
        return appClient.statisticBy3dTable(req);
    }
}
