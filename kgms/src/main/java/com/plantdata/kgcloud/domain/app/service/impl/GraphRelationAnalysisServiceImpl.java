package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.pub.GraphApi;
import ai.plantdata.kg.api.pub.req.RelationFrom;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphReqConverter;
import com.plantdata.kgcloud.domain.app.dto.GraphReasoningDTO;
import com.plantdata.kgcloud.domain.app.dto.GraphRspDTO;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.app.service.GraphRelationAnalysisService;
import com.plantdata.kgcloud.domain.app.service.RuleReasoningService;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReqAnalysisReqList;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReqList;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReqList;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationReasoningAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationTimingAnalysisRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 12:02
 */
@Service
@Slf4j
public class GraphRelationAnalysisServiceImpl implements GraphRelationAnalysisService {

    @Autowired
    private GraphHelperService graphHelperService;
    @Autowired
    private GraphApi graphApi;
    @Autowired
    private RuleReasoningService ruleReasoningService;

    @Override
    public RelationAnalysisRsp relationAnalysis(String kgName, RelationReqAnalysisReqList analysisReq) {

        analysisReq = graphHelperService.keyToId(kgName, analysisReq);
        RelationFrom relationFrom = GraphReqConverter.relationReqProxy(analysisReq);
        RelationAnalysisRsp analysisRsp = new RelationAnalysisRsp();
        //执行分析
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.relationFull(KGUtil.dbName(kgName), relationFrom));
        if (!graphOpt.isPresent()) {
            return analysisRsp;
        }
        //统计+组装结果
        return graphHelperService.buildExploreRspWithStatistic(kgName, analysisReq.getConfigList(), analysisRsp, new GraphRspDTO(graphOpt.get(), analysisReq));
    }

    @Override
    public RelationTimingAnalysisRsp relationTimingAnalysis(String kgName, RelationTimingAnalysisReqList analysisReq) {
        analysisReq = graphHelperService.keyToId(kgName, analysisReq);
        RelationFrom relationFrom = GraphReqConverter.relationReqProxy(analysisReq);
        RelationTimingAnalysisRsp analysisRsp = new RelationTimingAnalysisRsp();
        //执行分析
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.relationFull(KGUtil.dbName(kgName), relationFrom));
        if (!graphOpt.isPresent()) {
            return analysisRsp;
        }
        //统计+组装结果
        return graphHelperService.buildExploreRspWithStatistic(kgName, analysisReq.getConfigList(), analysisRsp, new GraphRspDTO(graphOpt.get(), analysisReq));
    }

    @Override
    public RelationReasoningAnalysisRsp relationReasoningAnalysis(String kgName, RelationReasoningAnalysisReqList analysisReq) {
        analysisReq = graphHelperService.keyToId(kgName, analysisReq);
        RelationFrom relationFrom = GraphReqConverter.relationReqProxy(analysisReq);
        RelationReasoningAnalysisRsp analysisRsp = new RelationReasoningAnalysisRsp();
        //执行分析
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.relationFull(KGUtil.dbName(kgName), relationFrom));
        if (!graphOpt.isPresent()) {
            return analysisRsp;
        }
        //推理
        GraphReasoningDTO reasoningDTO = ruleReasoningService.buildRuleReasonDto(kgName, graphOpt.get(), analysisReq);
        //统计+组装结果
        return graphHelperService.buildExploreRspWithStatistic(kgName, analysisReq.getConfigList(), analysisRsp, new GraphRspDTO(graphOpt.get(), analysisReq, reasoningDTO));
    }


}
