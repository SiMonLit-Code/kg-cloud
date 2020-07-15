package ai.plantdata.kgcloud.domain.app.service;

import ai.plantdata.kgcloud.sdk.req.app.explore.RelationReqAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReqList;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.RelationAnalysisRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.RelationReasoningAnalysisRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.RelationTimingAnalysisRsp;

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
    RelationAnalysisRsp relationAnalysis(String kgName, RelationReqAnalysisReqList analysisReq);

    /**
     * 时序关联分析
     *
     * @param kgName
     * @param analysisReq
     * @return
     */
    RelationTimingAnalysisRsp relationTimingAnalysis(String kgName, RelationTimingAnalysisReqList analysisReq);

    /**
     * 推理关联分析
     *
     * @param kgName
     * @param analysisReq
     * @return
     */
    RelationReasoningAnalysisRsp relationReasoningAnalysis(String kgName, RelationReasoningAnalysisReqList analysisReq);
}
