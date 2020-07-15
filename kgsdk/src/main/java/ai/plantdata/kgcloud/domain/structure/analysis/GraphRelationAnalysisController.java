package ai.plantdata.kgcloud.domain.structure.analysis;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.common.module.GraphStructureAnalysisInterface;
import ai.plantdata.kgcloud.sdk.AppClient;
import ai.plantdata.kgcloud.sdk.req.app.explore.RelationReqAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReqList;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.RelationAnalysisRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.RelationReasoningAnalysisRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.RelationTimingAnalysisRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("v3/structure/relation")
public class GraphRelationAnalysisController implements GraphStructureAnalysisInterface {

    @Autowired
    private AppClient appClient;

    @ApiOperation(value = "关联关系分析",notes = "关联关系，查询知识图谱中多个实体间的路径。支持步数、方向、路径节点类型、路径边关系类型的筛选。")
    @PostMapping("{kgName}")
    public ApiReturn<RelationAnalysisRsp> relationAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                           @RequestBody @Validated RelationReqAnalysisReqList analysisReq) {
        return appClient.relationAnalysis(kgName, analysisReq);
    }

    @ApiOperation(value = "直接关联关系分析",notes = "直接关联关系，查询知识图谱中多个实体间直接（1步）路径。方向、路径节点类型、路径边关系类型的筛选。")
    @PostMapping("direct/{kgName}")
    public ApiReturn<RelationAnalysisRsp> directRelationAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                           @RequestBody @Validated RelationReqAnalysisReqList analysisReq) {
        return appClient.relationAnalysis(kgName, analysisReq);
    }

    @ApiOperation(value = "时序关联分析",notes = "时序关联关系，查询知识图谱中多个实体间路径。方向、路径节点类型、路径边关系类型及边上时间的筛选。")
    @PostMapping("timing/{kgName}")
    public ApiReturn<RelationTimingAnalysisRsp> relationTimingAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                                       @RequestBody @Valid RelationTimingAnalysisReqList analysisReq) {
        return appClient.relationTimingAnalysis(kgName,analysisReq);
    }

    @ApiOperation("关联推理分析")
    @PostMapping("reasoning/{kgName}")
    public ApiReturn<RelationReasoningAnalysisRsp> relationReasoningAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                                             @RequestBody @Valid RelationReasoningAnalysisReqList analysisReq) {
        return appClient.relationReasoningAnalysis(kgName,analysisReq);
    }
}
