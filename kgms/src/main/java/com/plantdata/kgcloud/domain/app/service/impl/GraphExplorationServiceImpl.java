package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.pub.GisApi;
import ai.plantdata.kg.api.pub.GraphApi;
import ai.plantdata.kg.api.pub.req.GisLocusParam;
import ai.plantdata.kg.api.pub.req.GraphFrom;
import ai.plantdata.kg.api.pub.resp.GisLocusVO;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.plantdata.kgcloud.domain.app.converter.GisConverter;
import com.plantdata.kgcloud.domain.app.converter.graph.GraphReqConverter;
import com.plantdata.kgcloud.domain.app.service.GraphExplorationService;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.domain.app.service.RuleReasoningService;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonReasoningExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/22 16:49
 */
@Service
public class GraphExplorationServiceImpl implements GraphExplorationService {

    @Autowired
    private GraphApi graphApi;
    @Autowired
    private GisApi gisApi;
    @Autowired
    private GraphHelperService graphHelperService;
    @Autowired
    private RuleReasoningService ruleReasoningService;

    @Override
    public CommonBasicGraphExploreRsp exploreByKgQl(String kgName, ExploreByKgQlReq kgQlReq) {
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.traversalRule(kgName, kgQlReq.getEntityId(), kgQlReq.getKgQl()));
        if (!graphOpt.isPresent()) {
            return CommonBasicGraphExploreRsp.EMPTY;
        }
        return graphHelperService.buildExploreRspWithConcept(kgName, graphOpt.get());
    }

    @Override
    public GisGraphExploreRsp gisGraphExploration(String kgName, GisGraphExploreReq exploreParam) {
        Optional<List<BasicInfo>> basicInfoOpt = RestRespConverter.convert(gisApi.GisGeneralGraph(kgName, GisConverter.reqToGisFrom(exploreParam)));
        return basicInfoOpt.map(GisConverter::voToGisAnalysisRsp).orElseGet(GisGraphExploreRsp::new);
    }

    @Override
    public GisLocusAnalysisRsp gisLocusAnalysis(String kgName, GisLocusReq locusReq) {
        GisConverter.check(locusReq);
        GisLocusParam gisLocusParam = GisConverter.reqToParam(locusReq);
        Optional<GisLocusVO> locusOpt = RestRespConverter.convert(gisApi.gisLocus(kgName, gisLocusParam));
        return locusOpt.map(GisConverter::voToGisLocusRsp).orElseGet(GisLocusAnalysisRsp::new);
    }

    @Override
    public CommonBasicGraphExploreRsp commonGraphExploration(String kgName, CommonExploreReq exploreReq) {
        exploreReq = graphHelperService.dealGraphReq(exploreReq);
        return queryAndRebuildRsp(kgName, GraphReqConverter.commonReqProxy(exploreReq));
    }

    @Override
    public CommonBasicGraphExploreRsp timeGraphExploration(String kgName, CommonTimingExploreReq exploreReq) {
        exploreReq = graphHelperService.dealGraphReq(exploreReq);
        return queryAndRebuildRsp(kgName, GraphReqConverter.commonReqProxy(exploreReq));
    }

    @Override
    public CommonBasicGraphExploreRsp reasoningGraphExploration(String kgName, CommonReasoningExploreReq exploreReq) {
        exploreReq = graphHelperService.dealGraphReq(exploreReq);
        GraphFrom graphFrom = GraphReqConverter.commonReqProxy(exploreReq);
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.graph(kgName, graphFrom));
        if (!graphOpt.isPresent()) {
            return CommonBasicGraphExploreRsp.EMPTY;
        }
        //推理
        GraphVO graphVO = ruleReasoningService.rebuildByRuleReason(kgName, graphOpt.get(), exploreReq);

        return graphHelperService.buildExploreRspWithConcept(kgName, graphVO);
    }


    private CommonBasicGraphExploreRsp queryAndRebuildRsp(String kgName, GraphFrom graphFrom) {
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.graph(kgName, graphFrom));
        if (!graphOpt.isPresent()) {
            return CommonBasicGraphExploreRsp.EMPTY;
        }
        return graphHelperService.buildExploreRspWithConcept(kgName, graphOpt.get());
    }

}
