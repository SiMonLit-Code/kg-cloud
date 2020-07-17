package ai.plantdata.kgcloud.sdk.exection.client;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.sdk.req.app.*;
import ai.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;
import ai.plantdata.kgcloud.sdk.req.app.explore.*;
import ai.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReqList;
import ai.plantdata.kgcloud.sdk.req.app.infobox.BatchMultiModalReqList;
import ai.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import ai.plantdata.kgcloud.sdk.req.app.infobox.InfoboxMultiModalReq;
import ai.plantdata.kgcloud.sdk.AppClient;
import ai.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import ai.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.*;
import ai.plantdata.kgcloud.sdk.rsp.app.main.*;
import ai.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.AlgorithmStatisticeRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-19 19:13
 **/
@Component
public class AppClientEx implements AppClient {
    @Override
    public ApiReturn<SchemaRsp> querySchema(String kgName) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<InfoBoxRsp>> listInfoBox(String kgName, BatchInfoBoxReqList batchInfoBoxReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<InfoBoxRsp> infoBox(String kgName, InfoBoxReq infoBoxReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<InfoboxMultiModelRsp>> listInfoBoxMultiModal(String kgName, BatchMultiModalReqList batchMultiModalReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<InfoboxMultiModelRsp> infoBoxMultiModal(String kgName, InfoboxMultiModalReq infoboxMultiModalReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<ComplexGraphVisualRsp> complexGraphVisual(String kgName, Long azkId, String type, int size) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<ObjectAttributeRsp>> knowledgeRecommend(String kgName, KnowledgeRecommendReqList recommendParam) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<ObjectAttributeRsp>> layerKnowledgeRecommend(String kgName, @Valid LayerKnowledgeRecommendReqList recommendParam) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<BasicConceptTreeRsp> visualModels(String kgName, long conceptId, boolean display) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<BasicInfoVO>> conceptTree(String kgName, Long conceptId, String conceptKey) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<PageRsp<ApkRsp>> getKgName(Integer page, Integer size) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<PromptEntityRsp>> prompt(String kgName, PromptReq promptReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<SeniorPromptRsp>> seniorPrompt(String kgName, SeniorPromptReq seniorPromptReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<List<EdgeAttributeRsp>> attrPrompt(String kgName, EdgeAttrPromptReq edgeAttrPromptReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<BusinessGraphRsp> executeAlgorithm(String kgName, long id, BusinessGraphRsp graphBean) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<AlgorithmStatisticeRsp> executeStatisticsAlgorithm(String kgName, long id, @Valid BusinessGraphRsp graphBean) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GraphInitRsp> initGraphExploration(String kgName, String type) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<CommonBasicGraphExploreRsp> exploreByKgQl(String kgName, ExploreByKgQlReq kgQlReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<CommonBasicGraphExploreRsp> commonGraphExploration(String kgName, CommonExploreReqList exploreParam) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<CommonBasicGraphExploreRsp> timingGraphExploration(String kgName, CommonTimingExploreReqList exploreParam) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<CommonBasicGraphExploreRsp> reasoningGraphExploration(String kgName, CommonReasoningExploreReqList exploreParam) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GisGraphExploreRsp> gisGraphExploration(String kgName, GisGraphExploreReq exploreParam) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<GisLocusAnalysisRsp> graphLocusGis(String kgName, GisLocusReq locusGisParam) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<PathAnalysisRsp> path(String kgName, PathAnalysisReqList analysisReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<PathAnalysisRsp> shortestPath(String kgName, PathAnalysisReqList analysisReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<PathAnalysisReasonRsp> pathRuleReason(String kgName, PathReasoningAnalysisReqList analysisReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<PathTimingAnalysisRsp> pathTimingAnalysis(String kgName, PathTimingAnalysisReqList analysisReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<RelationAnalysisRsp> relationAnalysis(String kgName, RelationReqAnalysisReqList analysisReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<RelationAnalysisRsp> relationDirect(String kgName, RelationReqAnalysisReqList analysisReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<RelationTimingAnalysisRsp> relationTimingAnalysis(String kgName, RelationTimingAnalysisReqList analysisReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<RelationReasoningAnalysisRsp> relationReasoningAnalysis(String kgName, RelationReasoningAnalysisReqList analysisReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn exportPng(String fileName, String data) throws IOException {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<DataSetStatisticRsp> statistic2d(String dataName, StatisticByDimensionalReq twoDimensional) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<DataSetStatisticRsp> statistic3d(String dataName, StatisticByDimensionalReq dimensionalReq) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<DataSetStatisticRsp> statistic2dByTable(TableStatisticByDimensionalReq twoDimensional) {
        return ApiReturn.fail(500,"请求超时");
    }

    @Override
    public ApiReturn<DataSetStatisticRsp> statistic3dByTable(@Valid TableStatisticByDimensionalReq thirdDimensional) {
        return ApiReturn.fail(500,"请求超时");
    }
}
