package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.sdk.req.app.explore.RelationReqAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationReasoningAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationTimingAnalysisRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 12:03
 */
public interface GraphRelationAnalysisService {

    /**
     * 关联分析
     *
     * @param kgName
     * @param analysisReq
     * @return
     */
    RelationAnalysisRsp relationAnalysis(String kgName, RelationReqAnalysisReq analysisReq);

    /**
     * 时序关联分析
     *
     * @param kgName
     * @param analysisReq
     * @return
     */
    RelationTimingAnalysisRsp relationTimingAnalysis(String kgName, RelationTimingAnalysisReq analysisReq);

    /**
     * 推理关联分析
     *
     * @param kgName
     * @param analysisReq
     * @return
     */
    RelationReasoningAnalysisRsp relationReasoningAnalysis(String kgName, RelationReasoningAnalysisReq analysisReq);
}
