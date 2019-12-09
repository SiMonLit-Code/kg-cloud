package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.constant.GraphInitBaseEnum;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.InfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReqAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisReasonRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathTimingAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationReasoningAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationTimingAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 14:03
 */
@FeignClient(value = "kgms", path = "app", contextId = "app")
public interface AppClient {

    /**
     * 获取当前图实体类型及属性类型的schema
     *
     * @param kgName kgName
     * @return List
     */
    @GetMapping("schema/{kgName}")
    ApiReturn<SchemaRsp> querySchema(@PathVariable("kgName") String kgName);

    /**
     * 知识卡片
     *
     * @param kgName     kgName
     * @param infoBoxReq param
     * @return 。。
     */
    @PostMapping("infoBox/{kgName}")
    ApiReturn<List<InfoBoxRsp>> infoBox(@PathVariable("kgName") String kgName,
                                        @RequestBody InfoBoxReq infoBoxReq);

    /**
     * 知识推荐
     *
     * @param kgName         kgName
     * @param recommendParam body
     * @return List
     */
    @PostMapping("knowledgeRecommend/{kgName}")
    ApiReturn<List<ObjectAttributeRsp>> knowledgeRecommend(@PathVariable("kgName") String kgName, @RequestBody KnowledgeRecommendReq recommendParam);

    /**
     * 获取模型可视化数据
     *
     * @param conceptId 概念id
     * @param kgName    kgName
     * @param display   true 显示属性(包含对象属性和数值属性)
     * @return object
     */
    @GetMapping("visualModels/{kgName}/{conceptId}")
    ApiReturn<BasicConceptTreeRsp> visualModels(@PathVariable("kgName") String kgName, @PathVariable("conceptId") long conceptId, @RequestParam("display") boolean display);

    /**
     * 获取概念树
     *
     * @param kgName     kgName
     * @param conceptId  概念id
     * @param conceptKey 概念唯一key
     * @return List
     */
    @GetMapping("concept/{kgName}")
    ApiReturn<List<BasicConceptRsp>> conceptTree(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                 @RequestParam("conceptId") Long conceptId,
                                                 @RequestParam("conceptKey") String conceptKey);

    /**
     * 获取所有图谱名称
     *
     * @param apk apk
     * @return ..
     */
    @GetMapping("kgName/all/{apk}")
    ApiReturn<List<ApkRsp>> getKgName(@PathVariable("apk") String apk);

    /**
     * 实体下拉提示
     *
     * @param kgName    kgName
     * @param promptReq req
     * @return 。。。
     */
    @PostMapping("prompt/{kgName}")
    ApiReturn<List<PromptEntityRsp>> prompt(@PathVariable("kgName") String kgName,
                                            @RequestBody PromptReq promptReq);

    /**
     * 高级搜索
     *
     * @param kgName          kgName
     * @param seniorPromptReq s
     * @return ...
     */
    @GetMapping("prompt/senior/{kgName}")
    ApiReturn<List<SeniorPromptRsp>> seniorPrompt(@PathVariable("kgName") String kgName,
                                                  SeniorPromptReq seniorPromptReq);

    /**
     * 边属性搜索
     *
     * @param kgName            kgName
     * @param edgeAttrPromptReq 边属性搜索参数
     * @return list
     */
    @PostMapping("attributes/{kgName}")
    ApiReturn<List<EdgeAttributeRsp>> attrPrompt(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                 EdgeAttrPromptReq edgeAttrPromptReq);

    /**
     * 初始化图探索焦点
     *
     * @param kgName 图谱名称
     * @param type   图类型
     * @return 。。
     */
    @PostMapping("graphExplore/init/{kgName}")
    ApiReturn<GraphInitRsp> initGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                 @ApiParam(value = "图类型", required = true)@RequestBody String type);

    /**
     * kgQl查询
     *
     * @param kgName  kgName
     * @param kgQlReq kgQl
     * @return ..。
     */
    @PostMapping("graphExplore/byKgQl/{kgName}")
    ApiReturn<CommonBasicGraphExploreRsp> exploreByKgQl(@PathVariable("kgName") String kgName,
                                                        @RequestBody ExploreByKgQlReq kgQlReq);

    /**
     * 普通图探索
     *
     * @param kgName       kgName
     * @param exploreParam param
     * @return 。。。
     */
    @PostMapping("graphExplore/common/{kgName}")
    ApiReturn<CommonBasicGraphExploreRsp> commonGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                 @RequestBody CommonExploreReq exploreParam);

    /**
     * 时序图探索
     *
     * @param kgName       kgName
     * @param exploreParam param
     * @return 。。。
     */
    @PostMapping("graphExplore/timing/{kgName}")
    ApiReturn<CommonBasicGraphExploreRsp> timingGraphExploration(@ApiParam(value = "图谱名称", required = true) @PathVariable("kgName") String kgName,
                                                                 @RequestBody CommonTimingExploreReq exploreParam);

    /**
     * gis图探索
     *
     * @param kgName       图谱名称
     * @param exploreParam 个i是、图探索参数body
     * @return 。。。
     */
    @PostMapping("graphExplore/gis/{kgName}")
    ApiReturn<GisGraphExploreRsp> gisGraphExploration(@PathVariable("kgName") String kgName,
                                                      @RequestBody GisGraphExploreReq exploreParam);

    /**
     * gis轨迹分析
     *
     * @param kgName        kgName
     * @param locusGisParam param
     * @return 。。。
     */
    @PostMapping("graphExplore/gisLocus/{kgName}")
    ApiReturn<GisGraphExploreRsp> graphLocusGis(@PathVariable("kgName") String kgName,
                                                @RequestBody GisLocusReq locusGisParam);

    /**
     * 路径发现
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return 。。。
     */
    @PostMapping("graphExplore/path/{kgName}")
    ApiReturn<PathAnalysisRsp> path(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                    @RequestBody PathAnalysisReq analysisReq);

    /**
     * 最短路径发现
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return 。。。
     */
    @PostMapping("graphExplore/path/shortest/{kgName}")
    ApiReturn<PathAnalysisRsp> shortestPath(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                            @RequestBody PathAnalysisReq analysisReq);

    /**
     * 路径分析推理
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return 。。。
     */
    @PostMapping("graphExplore/path/reasoning/{kgName}")
    ApiReturn<PathAnalysisReasonRsp> pathRuleReason(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                    @RequestBody PathReasoningAnalysisReq analysisReq);

    /**
     * 时序路径分析
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return ...
     */
    @PostMapping("graphExplore/path/timing/{kgName}")
    ApiReturn<PathTimingAnalysisRsp> pathTimingAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                        @RequestBody PathTimingAnalysisReq analysisReq);

    /**
     * 关联分析
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return ...
     */
    @PostMapping("graphExplore/relation/{kgName}")
    ApiReturn<RelationAnalysisRsp> relationAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                    @RequestBody RelationReqAnalysisReq analysisReq);

    /**
     * 直接关联分析
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return ...
     */
    @PostMapping("graphExplore/relation/direct/{kgName}")
    ApiReturn<RelationAnalysisRsp> relationDirect(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                  @RequestBody RelationReqAnalysisReq analysisReq);

    /**
     * 时序关联分析
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return ...
     */
    @PostMapping("graphExplore/relation/timing/{kgName}")
    ApiReturn<RelationTimingAnalysisRsp> relationTimingAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                                @RequestBody RelationTimingAnalysisReq analysisReq);

    /**
     * 关联推理分析
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return ...
     */
    @PostMapping("graphExplore/relation/reasoning/{kgName}")
    ApiReturn<RelationReasoningAnalysisRsp> relationReasoningAnalysis(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                                      @RequestBody RelationReasoningAnalysisReq analysisReq);

}
