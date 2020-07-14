package ai.plantdata.kgcloud.domain.app.service;

import ai.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import ai.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import ai.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import ai.plantdata.kgcloud.sdk.req.app.explore.CommonReasoningExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReqList;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/22 16:49
 */
public interface GraphExplorationService {
    /**
     * 普通图探索
     *
     * @param kgName  kgName
     * @param kgQlReq kgQl
     * @return ...
     */
    CommonBasicGraphExploreRsp exploreByKgQl(String kgName, ExploreByKgQlReq kgQlReq);

    /**
     * gis 图探索
     *
     * @param kgName       图谱名称
     * @param exploreParam 图探索参数
     * @return 图探索结果
     */
    GisGraphExploreRsp gisGraphExploration(String kgName, GisGraphExploreReq exploreParam);
    /**
     * gis轨迹分析
     *
     * @param kgName     图谱名称
     * @param locusParam gis轨迹探索参数
     * @return gis 实体和关系
     */
    GisLocusAnalysisRsp gisLocusAnalysis(String kgName, GisLocusReq locusParam);

    /**
     * 普通图探索
     *
     * @param kgName       图谱名称
     * @param exploreParam 图探索参数
     * @return 图探索结果
     */
    CommonBasicGraphExploreRsp commonGraphExploration(String kgName, CommonExploreReqList exploreParam);

    /**
     * 时序图探索
     *
     * @param kgName       图谱名称
     * @param exploreParam 图探索参数
     * @return 图探索结果
     */
    CommonBasicGraphExploreRsp timeGraphExploration(String kgName, CommonTimingExploreReqList exploreParam);

    /**
     * 推理图探索
     * @param kgName 图谱名称
     * @param exploreReq 图探索参数
     * @return 图探索结果
     */
    CommonBasicGraphExploreRsp reasoningGraphExploration(String kgName, CommonReasoningExploreReqList exploreReq);
}
