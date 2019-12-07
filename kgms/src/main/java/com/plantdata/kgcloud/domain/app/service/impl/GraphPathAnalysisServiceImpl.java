package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.pub.GraphApi;
import ai.plantdata.kg.api.pub.req.PathFrom;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphReqConverter;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.app.service.GraphPathAnalysisService;
import com.plantdata.kgcloud.domain.app.service.RuleReasoningService;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisReasonRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathTimingAnalysisRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:10
 */
@Service
public class GraphPathAnalysisServiceImpl implements GraphPathAnalysisService {

    @Autowired
    private GraphApi graphApi;
    @Autowired
    private RuleReasoningService ruleReasoningService;
    @Autowired
    private GraphHelperService graphHelperService;

    @Override
    public PathAnalysisRsp path(String kgName, PathAnalysisReq analysisReq) {
        analysisReq = graphHelperService.dealGraphReq(kgName,analysisReq);
        PathFrom pathFrom = GraphReqConverter.pathReqProxy(analysisReq);
        PathAnalysisRsp pathAnalysisRsp = new PathAnalysisRsp();
        //路径探索
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.path(kgName, pathFrom));
        if (!graphOpt.isPresent()) {
            return pathAnalysisRsp;
        }
        //统计+组装结果
        return graphHelperService.buildExploreRspWithStatistic(kgName, analysisReq.getConfigList(), graphOpt.get(), new PathAnalysisRsp());
    }

    @Override
    public PathAnalysisReasonRsp pathRuleReason(String kgName, PathReasoningAnalysisReq reasonReq) {
        reasonReq = graphHelperService.dealGraphReq(kgName,reasonReq);
        PathFrom pathFrom = GraphReqConverter.pathReqProxy(reasonReq);
        PathAnalysisReasonRsp analysisReasonRsp = new PathAnalysisReasonRsp();
        //路径探索
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.path(kgName, pathFrom));
        if (!graphOpt.isPresent()) {
            return analysisReasonRsp;
        }
        //推理
        GraphVO graphVO = ruleReasoningService.rebuildByRuleReason(kgName, graphOpt.get(), reasonReq);
        //统计+组装结果
        return graphHelperService.buildExploreRspWithStatistic(kgName, reasonReq.getConfigList(), graphVO, new PathAnalysisReasonRsp());
    }

    @Override
    public PathTimingAnalysisRsp pathTimingAnalysis(String kgName, PathTimingAnalysisReq analysisReq) {
        analysisReq = graphHelperService.dealGraphReq(kgName,analysisReq);
        PathFrom pathFrom = GraphReqConverter.pathReqProxy(analysisReq);
        PathTimingAnalysisRsp analysisRsp = new PathTimingAnalysisRsp();
        //路径探索
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.path(kgName, pathFrom));
        if (!graphOpt.isPresent()) {
            return analysisRsp;
        }
        //统计+组装结果
        return graphHelperService.buildExploreRspWithStatistic(kgName, analysisReq.getConfigList(), graphOpt.get(), new PathTimingAnalysisRsp());
    }


}
