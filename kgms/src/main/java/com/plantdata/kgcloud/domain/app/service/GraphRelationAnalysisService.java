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

    RelationAnalysisRsp relationAnalysis(String kgName, RelationReqAnalysisReq analysisReq);

    RelationTimingAnalysisRsp relationTimingAnalysis(String kgName,RelationTimingAnalysisReq analysisReq);

    RelationReasoningAnalysisRsp relationReasoningAnalysis(String kgName, RelationReasoningAnalysisReq analysisReq);
}
