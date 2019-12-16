package com.plantdata.kgcloud.domain.structure.analysis;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphStructureAnalysisInterface;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisReasonRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathTimingAnalysisRsp;
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
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 16:53
 */
@RestController
@RequestMapping("v3/structure/path")
public class GraphPathAnalysisController implements GraphStructureAnalysisInterface {


    @Autowired
    private AppClient appClient;

    @ApiOperation("路径分析")
    @PostMapping("{kgName}")
    public ApiReturn<PathAnalysisRsp> path(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                           @RequestBody @Valid PathAnalysisReq analysisReq) {
        return appClient.path(kgName, analysisReq);
    }

    @ApiOperation("路径分析推理")
    @PostMapping("reasoning/{kgName}")
    public ApiReturn<PathAnalysisReasonRsp> pathRuleReason(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                           @RequestBody @Valid PathReasoningAnalysisReq analysisReq) {
        return appClient.pathRuleReason(kgName, analysisReq);
    }

    @ApiOperation("时序路径分析")
    @PostMapping("timing/{kgName}")
    public ApiReturn<PathTimingAnalysisRsp> pathTimingAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                               @RequestBody @Valid PathTimingAnalysisReq analysisReq) {
        return appClient.pathTimingAnalysis(kgName, analysisReq);
    }
}
