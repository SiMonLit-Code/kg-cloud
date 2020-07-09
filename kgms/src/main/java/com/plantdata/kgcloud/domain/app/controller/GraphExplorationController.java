package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.exception.BizException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.domain.app.service.GraphExplorationService;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.common.util.EnumUtils;
import com.plantdata.kgcloud.sdk.constant.GraphInitBaseEnum;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReqList;
import com.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
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

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 10:44
 */
@RestController
@RequestMapping("app/graphExplore")
public class GraphExplorationController implements SdkOpenApiInterface {

    @Autowired
    private GraphExplorationService graphExplorationService;
    @Autowired
    private GraphApplicationService graphApplicationService;
    @Autowired
    private GraphHelperService graphHelperService;

    @ApiOperation("初始化图探索数据")
    @PostMapping("init/{kgName}")
    public ApiReturn<GraphInitRsp> initGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                        @ApiParam(value = "图类型 graph,path,relation,timing,pathtiming,relationtiming,explore", required = true)
                                                        @RequestParam("type") String type) throws JsonProcessingException {
        Optional<GraphInitBaseEnum> enumObject = EnumUtils.getEnumObject(GraphInitBaseEnum.class, type);
        if (!enumObject.isPresent()) {
            throw BizException.of(AppErrorCodeEnum.GRAPH_TYPE_ERROR);
        }
        return ApiReturn.success(graphApplicationService.initGraphExploration(kgName, enumObject.get()));
    }

    @ApiOperation("根据业务规则kgQl语句图探索")
    @PostMapping("byKgQl/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> exploreByKgQl(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                               @RequestBody @Valid ExploreByKgQlReq kgQlReq) {
        return ApiReturn.success(graphExplorationService.exploreByKgQl(kgName, kgQlReq));
    }

    @ApiOperation("gis图探索")
    @PostMapping("gis/{kgName}")
    public ApiReturn<GisGraphExploreRsp> gisGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                             @RequestBody @Valid GisGraphExploreReq exploreParam) {

        return ApiReturn.success(graphExplorationService.gisGraphExploration(kgName, exploreParam));
    }

    @ApiOperation("轨迹分析")
    @PostMapping("gisLocus/{kgName}")
    public ApiReturn<GisLocusAnalysisRsp> graphLocusGis(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                        @RequestBody @Valid GisLocusReq locusGisParam) {
        return ApiReturn.success(graphExplorationService.gisLocusAnalysis(kgName, locusGisParam));
    }

    @ApiOperation("普通图探索")
    @PostMapping("common/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> commonGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                        @RequestBody @Valid CommonExploreReqList exploreParam) {
        Optional<CommonBasicGraphExploreRsp> rspOpt = graphHelperService.graphSearchBefore(kgName, exploreParam, new CommonBasicGraphExploreRsp());
        return rspOpt.map(ApiReturn::success).orElseGet(() -> ApiReturn.success(graphExplorationService.commonGraphExploration(kgName, exploreParam)));
    }

    @ApiOperation("时序图探索")
    @PostMapping("timing/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> timingGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                        @RequestBody @Valid CommonTimingExploreReqList exploreParam) {
        Optional<CommonBasicGraphExploreRsp> rspOpt = graphHelperService.graphSearchBefore(kgName, exploreParam, new CommonBasicGraphExploreRsp());
        return rspOpt.map(ApiReturn::success).orElseGet(() -> ApiReturn.success(graphExplorationService.timeGraphExploration(kgName, exploreParam)));
    }

    @ApiOperation("推理图探索")
    @PostMapping("reasoning/{kgName}")
    public ApiReturn<CommonBasicGraphExploreRsp> reasoningGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                           @RequestBody @Valid CommonReasoningExploreReqList exploreParam) {
        Optional<CommonBasicGraphExploreRsp> rspOpt = graphHelperService.graphSearchBefore(kgName, exploreParam, new CommonBasicGraphExploreRsp());
        return rspOpt.map(ApiReturn::success).orElseGet(() -> ApiReturn.success(graphExplorationService.reasoningGraphExploration(kgName, exploreParam)));
    }

}
