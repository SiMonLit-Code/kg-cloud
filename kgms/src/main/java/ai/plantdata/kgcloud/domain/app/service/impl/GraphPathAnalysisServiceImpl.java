package ai.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.pub.GraphApi;
import ai.plantdata.kg.api.pub.req.PathFrom;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kgcloud.domain.app.converter.graph.GraphReqConverter;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.domain.app.dto.GraphReasoningDTO;
import ai.plantdata.kgcloud.domain.app.dto.GraphRspDTO;
import ai.plantdata.kgcloud.domain.app.service.GraphHelperService;
import ai.plantdata.kgcloud.domain.app.service.GraphPathAnalysisService;
import ai.plantdata.kgcloud.domain.app.service.RuleReasoningService;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReqList;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisReasonRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.analysis.PathTimingAnalysisRsp;
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
    public PathAnalysisRsp path(String kgName, PathAnalysisReqList analysisReq) {

        analysisReq = graphHelperService.keyToId(kgName, analysisReq);
        PathFrom pathFrom = GraphReqConverter.pathReqProxy(analysisReq);
        //路径探索
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.path(KGUtil.dbName(kgName), pathFrom));
        PathAnalysisRsp analysisRsp = new PathAnalysisRsp();
        if (!graphOpt.isPresent()) {
            return analysisRsp;
        }
        //统计+组装结果
        return graphHelperService.buildExploreRspWithStatistic(kgName, analysisReq.getConfigList(), analysisRsp, new GraphRspDTO(graphOpt.get(), analysisReq));
    }

    @Override
    public PathAnalysisReasonRsp pathRuleReason(String kgName, PathReasoningAnalysisReqList reasonReq) {
        PathAnalysisReasonRsp analysisRsp = new PathAnalysisReasonRsp();
        reasonReq = graphHelperService.keyToId(kgName, reasonReq);
        PathFrom pathFrom = GraphReqConverter.pathReqProxy(reasonReq);
        //路径探索
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.path(KGUtil.dbName(kgName), pathFrom));
        if (!graphOpt.isPresent()) {
            return analysisRsp;
        }
        //推理
        GraphReasoningDTO reasoningDTO = ruleReasoningService.buildRuleReasonDto(kgName, graphOpt.get(), reasonReq);
        //统计+组装结果
        return graphHelperService.buildExploreRspWithStatistic(kgName, reasonReq.getConfigList(), analysisRsp, new GraphRspDTO(graphOpt.get(), reasonReq, reasoningDTO));
    }

    @Override
    public PathTimingAnalysisRsp pathTimingAnalysis(String kgName, PathTimingAnalysisReqList analysisReq) {
        PathTimingAnalysisRsp analysisRsp = new PathTimingAnalysisRsp();
        analysisReq = graphHelperService.keyToId(kgName, analysisReq);
        PathFrom pathFrom = GraphReqConverter.pathReqProxy(analysisReq);
        //路径探索
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.path(KGUtil.dbName(kgName), pathFrom));
        if (!graphOpt.isPresent()) {
            return analysisRsp;
        }
        //统计+组装结果
        return graphHelperService.buildExploreRspWithStatistic(kgName, analysisReq.getConfigList(), new PathTimingAnalysisRsp(), new GraphRspDTO(graphOpt.get(), analysisReq));
    }


}
