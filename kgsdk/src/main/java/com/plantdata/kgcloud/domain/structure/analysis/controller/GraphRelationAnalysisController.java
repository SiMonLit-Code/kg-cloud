package com.plantdata.kgcloud.domain.structure.analysis.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.GraphStructureAnalysisInterface;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationReasoningAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationTimingAnalysisRsp;
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
 * @date 2019/11/12 11:37
 */
@RestController
@RequestMapping("structure/relation")
public class GraphRelationAnalysisController implements GraphStructureAnalysisInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation("关联分析")
    @PostMapping("{kgName}")
    public ApiReturn<RelationAnalysisRsp> relationAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                           @RequestBody @Valid RelationAnalysisReq analysisReq) {
        return appClient.relationAnalysis(kgName, analysisReq);
    }

    @ApiOperation("时序关联分析")
    @PostMapping("timing/{kgName}")
    public ApiReturn<RelationTimingAnalysisRsp> relationTimingAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                                       @RequestBody @Valid RelationTimingAnalysisReq analysisReq) {
        return appClient.relationTimingAnalysis(kgName,analysisReq);
    }

    @ApiOperation("关联推理分析")
    @PostMapping("reasoning/{kgName}")
    public ApiReturn<RelationReasoningAnalysisRsp> relationReasoningAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                                             @RequestBody @Valid RelationReasoningAnalysisReq analysisReq) {
        return appClient.relationReasoningAnalysis(kgName,analysisReq);
    }
}
