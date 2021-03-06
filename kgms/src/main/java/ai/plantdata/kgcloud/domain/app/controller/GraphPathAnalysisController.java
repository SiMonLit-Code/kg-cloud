package ai.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.app.controller.module.SdkOpenApiInterface;
import ai.plantdata.kgcloud.domain.app.service.GraphHelperService;
import ai.plantdata.kgcloud.domain.app.service.GraphPathAnalysisService;
import ai.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReqList;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisReasonRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.PathTimingAnalysisRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 16:53
 */
@RestController
@RequestMapping("app/graphExplore/path")
public class GraphPathAnalysisController implements SdkOpenApiInterface {

    @Autowired
    private GraphPathAnalysisService graphPathAnalysisService;
    @Autowired
    private GraphHelperService graphHelperService;

    @ApiOperation("路径发现")
    @PostMapping("{kgName}")
    public ApiReturn<PathAnalysisRsp> path(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                           @RequestBody @Valid PathAnalysisReqList analysisReq) {

        Optional<PathAnalysisRsp> rspOpt = graphHelperService.graphSearchBefore(kgName, analysisReq, new PathAnalysisRsp());
        return rspOpt.map(ApiReturn::success).orElseGet(() -> ApiReturn.success(graphPathAnalysisService.path(kgName, analysisReq)));
    }

    @ApiOperation("最短路径发现")
    @PostMapping("shortest/{kgName}")
    public ApiReturn<PathAnalysisRsp> shortestPath(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                   @RequestBody @Valid PathAnalysisReqList analysisReq) {
        analysisReq.getPath().setShortest(true);
        return ApiReturn.success(graphPathAnalysisService.path(kgName, analysisReq));
    }


    @ApiOperation("路径分析推理")
    @PostMapping("reasoning/{kgName}")
    public ApiReturn<PathAnalysisReasonRsp> pathRuleReason(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                           @RequestBody @Valid PathReasoningAnalysisReqList analysisReq) {
        Optional<PathAnalysisReasonRsp> rspOpt = graphHelperService.graphSearchBefore(kgName, analysisReq, new PathAnalysisReasonRsp());
        return rspOpt.map(ApiReturn::success).orElseGet(() -> ApiReturn.success(graphPathAnalysisService.pathRuleReason(kgName, analysisReq)));
    }

    @ApiOperation("时序路径分析")
    @PostMapping("timing/{kgName}")
    public ApiReturn<PathTimingAnalysisRsp> pathTimingAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                               @RequestBody @Valid PathTimingAnalysisReqList analysisReq) {
        Optional<PathTimingAnalysisRsp> rspOpt = graphHelperService.graphSearchBefore(kgName, analysisReq, new PathTimingAnalysisRsp());
        return rspOpt.map(ApiReturn::success).orElseGet(() -> ApiReturn.success(graphPathAnalysisService.pathTimingAnalysis(kgName, analysisReq)));
    }
}
