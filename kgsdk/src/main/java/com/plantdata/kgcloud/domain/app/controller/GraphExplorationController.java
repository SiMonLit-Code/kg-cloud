package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.constant.GraphInitEnum;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.domain.common.module.GraphApplicationInterface;
import com.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.bean.ApiReturn;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 16:45
 */
@RestController
@RequestMapping("app/graphExplore")
public class GraphExplorationController implements GraphApplicationInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation("初始化图探索数据")
    @PostMapping("init/{kgName}")
    public ApiReturn<GraphInitRsp> initGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                        @ApiParam(value = "图类型", required = true) GraphInitEnum type) {
        //todo 12-02
        return ApiReturn.success(null);
    }

    @ApiOperation("根据业务规则kgQl语句图探索")
    @PostMapping("byKgQl/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> exploreByKgQl(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                               @RequestBody @Valid ExploreByKgQlReq kgQlReq, @ApiIgnore BindingResult bindingResult) {
        return appClient.exploreByKgQl(kgName, kgQlReq);
    }

    @ApiOperation("普通图探索")
    @PostMapping("common/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> commonGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                        @RequestBody @Valid CommonExploreReq exploreParam, @ApiIgnore BindingResult bindingResult) {
        return appClient.commonGraphExploration(kgName, exploreParam);
    }

    @ApiOperation("时序图探索")
    @PostMapping("timing/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> timingGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                        @RequestBody @Valid CommonTimingExploreReq exploreParam, @ApiIgnore BindingResult bindingResult) {
        return appClient.timingGraphExploration(kgName, exploreParam);
    }

    @ApiOperation("gis图探索")
    @PostMapping("gis/{kgName}")
    public ApiReturn<GisGraphExploreRsp> gisGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                             @RequestBody @Valid GisGraphExploreReq exploreParam, @ApiIgnore BindingResult bindingResult) {
        return appClient.gisGraphExploration(kgName, exploreParam);
    }

    @ApiOperation("轨迹分析")
    @PostMapping("gisLocus/{kgName}")
    public ApiReturn<GisGraphExploreRsp> graphLocusGis(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                       @RequestBody @Valid GisLocusReq locusGisParam, BindingResult bindingResult) {
        return appClient.graphLocusGis(kgName, locusGisParam);
    }
}
