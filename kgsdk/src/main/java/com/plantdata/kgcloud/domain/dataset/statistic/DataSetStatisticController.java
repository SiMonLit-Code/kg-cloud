package com.plantdata.kgcloud.domain.dataset.statistic;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.DataSetStatisticInterface;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.ComponentStatisticClient;
import com.plantdata.kgcloud.sdk.req.DataSetCountReq;
import com.plantdata.kgcloud.sdk.req.DataSetStatisticReq;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.common.BasicValueRsp;
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
    @Autowired
    private ComponentStatisticClient componentStatisticClient;

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
    @PostMapping("2d/table")
    public ApiReturn<DataSetStatisticRsp> statistic2dByTable(@Valid @RequestBody TableStatisticByDimensionalReq twoDimensional) {
        return appClient.statistic2dByTable(twoDimensional);
    }

    @ApiOperation("统计数据三维/按表统计")
    @PostMapping("3d/table")
    public ApiReturn<DataSetStatisticRsp> statistic3dByTable(@Valid @RequestBody TableStatisticByDimensionalReq thirdDimensional) {
        return appClient.statistic3dByTable(thirdDimensional);
    }

    @ApiOperation("数据集统计")
    @PostMapping("dataSet/{dataSetId}")
    public ApiReturn<com.plantdata.kgcloud.sdk.rsp.DataSetStatisticRsp> dataSetStatistic(@PathVariable Long dataSetId, @RequestBody @Valid DataSetStatisticReq statisticReq) {
        return componentStatisticClient.dataSetStatistic(dataSetId, statisticReq);
    }

    @ApiOperation("数据集数字统计")
    @PostMapping("number/{dataSetId}")
    public ApiReturn<BasicValueRsp> dataSetNumberStatistic(@PathVariable Long dataSetId, @RequestBody @Valid DataSetCountReq countReq) {
        return componentStatisticClient.dataSetNumberStatistic(dataSetId, countReq);
    }

}
