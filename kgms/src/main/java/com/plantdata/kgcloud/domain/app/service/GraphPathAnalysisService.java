package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisReasonRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathTimingAnalysisRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:10
 */
public interface GraphPathAnalysisService {

    /**
     * 路径+统计
     *
     * @param kgName      图谱名称
     * @param analysisReq req
     * @return 。。。
     */
    PathAnalysisRsp path(String kgName, PathAnalysisReq analysisReq);

    /**
     * 路径+推理
     *
     * @param kgName    图谱名称
     * @param reasonReq req
     * @return 。。。
     */
    PathAnalysisReasonRsp pathRuleReason(String kgName, PathReasoningAnalysisReq reasonReq);

    /**
     * 时序+路径+统计
     *
     * @param kgName          图谱名称
     * @param pathAnalysisReq 参数
     * @return 。。。
     */
    PathTimingAnalysisRsp pathTimingAnalysis(String kgName, PathTimingAnalysisReq pathAnalysisReq);
}
