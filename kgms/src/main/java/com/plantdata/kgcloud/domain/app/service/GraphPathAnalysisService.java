package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReqList;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReqList;
import com.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReqList;
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
    PathAnalysisRsp path(String kgName, PathAnalysisReqList analysisReq);

    /**
     * 路径+推理
     *
     * @param kgName    图谱名称
     * @param reasonReq req
     * @return 。。。
     */
    PathAnalysisReasonRsp pathRuleReason(String kgName, PathReasoningAnalysisReqList reasonReq);

    /**
     * 时序+路径+统计
     *
     * @param kgName          图谱名称
     * @param pathAnalysisReq 参数
     * @return 。。。
     */
    PathTimingAnalysisRsp pathTimingAnalysis(String kgName, PathTimingAnalysisReqList pathAnalysisReq);
}
