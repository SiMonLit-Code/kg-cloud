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
import com.plantdata.kgcloud.sdk.rsp.common.KgStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.common.MultiMeasureStatisticResultRsp;
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
@RequestMapping("v3/dataset/statistic")
public class DataSetStatisticController implements DataSetStatisticInterface {

    @Autowired
    private AppClient appClient;
    @Autowired
    private ComponentStatisticClient componentStatisticClient;

    @ApiOperation(value = "搜索数据集统计(二维)",notes = "对搜索数据集进行统计，仅支持二维统计，返回KV格式或兼容Echarts的格式。")
    @PostMapping("2d/{dataName}")
    public ApiReturn<DataSetStatisticRsp> statistic2d(
            @ApiParam("数据集唯一标识") @PathVariable("dataName") String dataName,
            @RequestBody @Valid StatisticByDimensionalReq twoDimensional) {

        return appClient.statistic2d(dataName, twoDimensional);
    }

    @ApiOperation(value = "搜索数据集统计(三维)",notes = "对搜索数据集进行统计，仅支持三维统计，返回KV格式或兼容Echarts的格式。")
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

//    @ApiOperation(value = "数据集统计",notes = "按照数据集指定字段进行统计支持筛选和模糊搜索")
//    @PostMapping("data/{dataSetId}")
//    public     ApiReturn<KgStatisticRsp<MultiMeasureStatisticResultRsp>>  dataSetStatistic(@PathVariable Long dataSetId, @RequestBody @Valid DataSetStatisticReq statisticReq) {
//        return componentStatisticClient.dataSetStatistic(dataSetId, statisticReq);
//    }
//
//    @ApiOperation(value = "数据集数字统计",notes = "对数据集某个字段的值进行计数或求和")
//    @PostMapping("number/{dataSetId}")
//    public ApiReturn<BasicValueRsp> dataSetNumberStatistic(@PathVariable Long dataSetId, @RequestBody @Valid DataSetCountReq countReq) {
//        return componentStatisticClient.dataSetNumberStatistic(dataSetId, countReq);
//    }

}
