package com.plantdata.kgcloud.domain.dataset.statistic;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.DataSetStatisticInterface;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author cjw 2019-11-04 14:49:07
 */
@RestController
@RequestMapping("v3/dataSet/statistic")
public class DataSetStatisticController implements DataSetStatisticInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation("统计数据二维(仅支持搜索数据集)")
    @PostMapping("2d/{dataName}")
    public ApiReturn<DataSetStatisticRsp> statistic2d(
            @ApiParam("数据集唯一标识") @PathVariable("dataName") String dataName,
            @RequestBody @Valid StatisticByDimensionalReq twoDimensional) {

        return appClient.statistic2d(dataName, twoDimensional);
    }

    @ApiOperation("统计数据三维(仅支持搜索数据集)")
    @PostMapping("3d/{dataName}")
    public ApiReturn<DataSetStatisticRsp> statistic3d(@ApiParam("数据集唯一标识") @PathVariable("dataName") String dataName,
                                                      @Valid @RequestBody StatisticByDimensionalReq twoDimensional) {
        return appClient.statistic3d(dataName, twoDimensional);
    }

    @ApiOperation("统计数据二维/按表统计")
    @PostMapping("2dByTable")
    public ApiReturn<DataSetStatisticRsp> statistic2dByTable(@Valid @RequestBody TableStatisticByDimensionalReq twoDimensional) {
        return appClient.statistic2dByTable(twoDimensional);
    }

    @ApiOperation("统计数据三维/按表统计")
    @PostMapping("3dByTable")
    public ApiReturn<DataSetStatisticRsp> statistic3dByTable(@Valid @RequestBody TableStatisticByDimensionalReq thirdDimensional) {
        return appClient.statistic3dByTable(thirdDimensional);
    }

}
