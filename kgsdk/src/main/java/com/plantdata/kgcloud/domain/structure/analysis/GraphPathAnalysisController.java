package com.plantdata.kgcloud.domain.structure.analysis;

import ai.plantdata.cloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphStructureAnalysisInterface;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReqList;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReqList;
import com.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReqList;
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

    @ApiOperation(value = "路径发现", notes = "路径发现，查询知识图谱中两个实体间的路径。支持步数、方向、路径节点类型、路径边关系类型的筛选。")
    @PostMapping("{kgName}")
    public ApiReturn<PathAnalysisRsp> path(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                           @RequestBody @Valid PathAnalysisReqList analysisReq) {
        return appClient.path(kgName, analysisReq);
    }

    @ApiOperation(value = "最短路径发现", notes = "路径发现，查询知识图谱中两个实体间的最短路径。支持步数、方向、路径节点类型、路径边关系类型的筛选。")
    @PostMapping("shortest{kgName}")
    public ApiReturn<PathAnalysisRsp> shortestPath(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                   @RequestBody @Valid PathAnalysisReqList analysisReq) {
        analysisReq.getPath().setShortest(true);
        return appClient.path(kgName, analysisReq);
    }

    @ApiOperation("路径分析推理")
    @PostMapping("reasoning/{kgName}")
    public ApiReturn<PathAnalysisReasonRsp> pathRuleReason(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                           @RequestBody @Valid PathReasoningAnalysisReqList analysisReq) {
        return appClient.pathRuleReason(kgName, analysisReq);
    }

    @ApiOperation(value = "时序路径分析",notes = "路径发现，查询知识图谱中两个实体间的路径。支持步数、方向、路径节点类型、路径边关系类型及边上时间的筛选。")
    @PostMapping("timing/{kgName}")
    public ApiReturn<PathTimingAnalysisRsp> pathTimingAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                               @RequestBody @Valid PathTimingAnalysisReqList analysisReq) {
        return appClient.pathTimingAnalysis(kgName, analysisReq);
    }
}
