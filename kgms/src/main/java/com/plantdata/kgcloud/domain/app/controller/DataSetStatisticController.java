package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.service.DataSetStatisticService;
import com.plantdata.kgcloud.sdk.constant.DimensionEnum;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/5 15:28
 */
@RestController
@Api(tags = "图谱应用")
@RequestMapping("app/dataset/statistic")
public class DataSetStatisticController {

    @Resource
    private DataSetStatisticService dataSetStatisticService;

    @ApiOperation("统计数据二维")
    @PostMapping("2d/{dataSetKey}")
    public ApiReturn<DataSetStatisticRsp> statistic2d(
            @ApiParam("数据集唯一标识") @PathVariable("dataSetKey") String dataSetKey,
            @RequestBody @Valid StatisticByDimensionalReq twoDimensional) {
        return ApiReturn.success(dataSetStatisticService.statisticByDimension(twoDimensional, dataSetKey, DimensionEnum.TWO));
    }

    @ApiOperation("统计数据三维")
    @PostMapping("3d/{dataSetKey}")
    public ApiReturn<DataSetStatisticRsp> statistic3d(@ApiParam("数据集唯一标识") @PathVariable("dataSetKey") String dataSetKey,
                                                      @Valid StatisticByDimensionalReq twoDimensional) {
        return ApiReturn.success(dataSetStatisticService.statisticByDimension(twoDimensional, dataSetKey, DimensionEnum.THIRD));
    }

    @ApiOperation("统计数据二维/按表统计")
    @PostMapping("2dByTable")
    public ApiReturn<DataSetStatisticRsp> statistic2dByTable(@Valid TableStatisticByDimensionalReq twoDimensional) {
        return ApiReturn.success(dataSetStatisticService.statisticByDimensionAndTable(twoDimensional, DimensionEnum.TWO));
    }

    @ApiOperation("统计数据三维/按表统计")
    @PostMapping("3dByTable")
    public ApiReturn<DataSetStatisticRsp> statistic3dByTable(@Valid TableStatisticByDimensionalReq thirdDimensional) {
        return ApiReturn.success(dataSetStatisticService.statisticByDimensionAndTable(thirdDimensional, DimensionEnum.THIRD));
    }
}
