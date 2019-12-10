package com.plantdata.kgcloud.domain.app.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.service.GraphRelationAnalysisService;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReqAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationReasoningAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationTimingAnalysisRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.math.NumberUtils;
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
 * @date 2019/11/12 11:37
 */
@Api(tags = "关联分析")
@RestController
@RequestMapping("v3/app/graphExplore/relation")
public class GraphRelationAnalysisController {

    @Autowired
    private GraphRelationAnalysisService graphRelationAnalysisService;

    @ApiOperation("关联分析")
    @PostMapping("{kgName}")
    public ApiReturn<RelationAnalysisRsp> relationAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                            @RequestBody @Valid RelationReqAnalysisReq analysisReq,
                                                            @ApiIgnore BindingResult bindingResult) {

        return ApiReturn.success(graphRelationAnalysisService.relationAnalysis(kgName, analysisReq));
    }

    @ApiOperation("直接关联关系")
    @PostMapping("direct/{kgName}")
    public ApiReturn<RelationAnalysisRsp> relationDirect(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                          @RequestBody @Valid RelationReqAnalysisReq analysisReq,
                                                          @ApiIgnore BindingResult bindingResult) {
        analysisReq.setDistance(NumberUtils.INTEGER_ONE);
        return ApiReturn.success(graphRelationAnalysisService.relationAnalysis(kgName, analysisReq));
    }

    @ApiOperation("时序关联分析")
    @PostMapping("timing/{kgName}")
    public ApiReturn<RelationTimingAnalysisRsp> relationTimingAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                                        @RequestBody @Valid RelationTimingAnalysisReq analysisReq,
                                                                        @ApiIgnore BindingResult bindingResult) {
        return ApiReturn.success(graphRelationAnalysisService.relationTimingAnalysis(kgName, analysisReq));
    }

    @ApiOperation("关联推理分析")
    @PostMapping("reasoning/{kgName}")
    public ApiReturn<RelationReasoningAnalysisRsp> relationReasoningAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                                              @RequestBody @Valid RelationReasoningAnalysisReq analysisReq,
                                                                              @ApiIgnore BindingResult bindingResult) {
        return ApiReturn.success(graphRelationAnalysisService.relationReasoningAnalysis(kgName, analysisReq));
    }

}
