package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonReasoningExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;

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
    CommonBasicGraphExploreRsp commonGraphExploration(String kgName, CommonExploreReq exploreParam);

    /**
     * 时序图探索
     *
     * @param kgName       图谱名称
     * @param exploreParam 图探索参数
     * @return 图探索结果
     */
    CommonBasicGraphExploreRsp timeGraphExploration(String kgName, CommonTimingExploreReq exploreParam);

    /**
     * 推理图探索
     * @param kgName 图谱名称
     * @param exploreReq 图探索参数
     * @return 图探索结果
     */
    CommonBasicGraphExploreRsp reasoningGraphExploration(String kgName, CommonReasoningExploreReq exploreReq);
}
