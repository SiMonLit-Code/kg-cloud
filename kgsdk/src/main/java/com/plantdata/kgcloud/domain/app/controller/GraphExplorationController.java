package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphApplicationInterface;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReqList;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonReasoningExploreReqList;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReqList;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 16:45
 */
@RestController
@RequestMapping("v3/app/graphExplore")
public class GraphExplorationController implements GraphApplicationInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation(value = "图谱初始节点查询",notes = "知识图谱（或时序）的初始化节点，默认读取图谱中权重最高的实体。")
    @PostMapping("init/{kgName}")
    public ApiReturn<GraphInitRsp> initGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                        @ApiParam(value = "图类型", required = true) @RequestParam("type") String type) {
        return appClient.initGraphExploration(kgName, type);
    }

    @ApiOperation(value = "业务规则/kgQl语句图探索")
    @PostMapping("byKgQl/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> exploreByKgQl(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                               @RequestBody ExploreByKgQlReq kgQlReq) {
        return appClient.exploreByKgQl(kgName, kgQlReq);
    }

    @ApiOperation(value = "普通图探索",notes="图探索（非时序），读取知识图谱的时序关系数据。支持节点类型、边类型过滤。")
    @PostMapping("common/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> commonGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                        @RequestBody CommonExploreReqList exploreParam) {
        return appClient.commonGraphExploration(kgName, exploreParam);
    }

    @ApiOperation(value = "时序图探索",notes = "时序图探索，读取知识图谱的时序关系数据。支持节点类型、边类型及边时间区间的过滤。")
    @PostMapping("timing/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> timingGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                        @RequestBody CommonTimingExploreReqList exploreParam) {
        return appClient.timingGraphExploration(kgName, exploreParam);
    }

    @ApiOperation("推理图探索")
    @PostMapping("reasoning/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> reasoningGraphExploration(@PathVariable("kgName") String kgName, @RequestBody CommonReasoningExploreReqList exploreParam) {
        return appClient.reasoningGraphExploration(kgName, exploreParam);
    }

    @ApiOperation(value = "gis图探索",notes = "按照地理位置条件进行图探索")
    @PostMapping("gis/{kgName}")
    public ApiReturn<GisGraphExploreRsp> gisGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                             @RequestBody GisGraphExploreReq exploreParam) {
        return appClient.gisGraphExploration(kgName, exploreParam);
    }

    @ApiOperation(value = "轨迹分析",notes = "按照设置中的kgql规则和地理位置条件进行图探索")
    @PostMapping("gisLocus/{kgName}")
    public ApiReturn<GisLocusAnalysisRsp> graphLocusGis(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                        @RequestBody GisLocusReq locusGisParam) {
        return appClient.graphLocusGis(kgName, locusGisParam);
    }
}
