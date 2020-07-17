package ai.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kg.api.pub.GisApi;
import ai.plantdata.kg.api.pub.GraphApi;
import ai.plantdata.kg.api.pub.req.GisLocusParam;
import ai.plantdata.kg.api.pub.req.GraphFrom;
import ai.plantdata.kg.api.pub.resp.GisLocusVO;
import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import ai.plantdata.kgcloud.domain.app.converter.GisConverter;
import ai.plantdata.kgcloud.domain.app.converter.graph.GraphReqConverter;
import ai.plantdata.kgcloud.domain.app.converter.graph.GraphRspConverter;
import ai.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import ai.plantdata.kgcloud.domain.app.dto.GraphReasoningDTO;
import ai.plantdata.kgcloud.domain.app.dto.GraphRspDTO;
import ai.plantdata.kgcloud.domain.app.service.GraphExplorationService;
import ai.plantdata.kgcloud.domain.app.service.GraphHelperService;
import ai.plantdata.kgcloud.domain.app.service.RuleReasoningService;
import ai.plantdata.kgcloud.domain.common.util.KGUtil;
import ai.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import ai.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import ai.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import ai.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.CommonReasoningExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.function.GraphReqAfterInterface;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/22 16:49
 */
@Service
@Slf4j
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
        log.error("kgql:{}", kgQlReq.getKgQl());
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.traversalRule(KGUtil.dbName(kgName), kgQlReq.getEntityId(), kgQlReq.getKgQl()));
        return graphOpt.map(graphVO -> this.buildExploreRspWithConcept(kgName, new GraphRspDTO(graphVO, kgQlReq))).orElse(CommonBasicGraphExploreRsp.EMPTY);
    }

    @Override
    public GisGraphExploreRsp gisGraphExploration(String kgName, GisGraphExploreReq exploreParam) {
        graphHelperService.replaceByConceptKey(kgName,exploreParam);
        Optional<List<BasicInfo>> basicInfoOpt = RestRespConverter.convert(gisApi.GisGeneralGraph(KGUtil.dbName(kgName), GisConverter.reqToGisFrom(exploreParam)));
        Map<Long, BasicInfo> conceptIdMap = graphHelperService.getConceptIdMap(kgName);
        return basicInfoOpt.map(a -> GisConverter.voToGisAnalysisRsp(a, conceptIdMap)).orElseGet(GisGraphExploreRsp::new);
    }

    @Override
    public GisLocusAnalysisRsp gisLocusAnalysis(String kgName, GisLocusReq locusReq) {
        GisConverter.check(locusReq);
        GisLocusParam gisLocusParam = GisConverter.reqToParam(locusReq);
        Optional<GisLocusVO> locusOpt = RestRespConverter.convert(gisApi.gisLocus(KGUtil.dbName(kgName), gisLocusParam));
        return locusOpt.map(GisConverter::voToGisLocusRsp).orElseGet(GisLocusAnalysisRsp::new);
    }

    @Override
    public CommonBasicGraphExploreRsp commonGraphExploration(String kgName, CommonExploreReqList exploreReq) {
        exploreReq = graphHelperService.keyToId(kgName, exploreReq);
        GraphFrom graphFrom = GraphReqConverter.commonReqProxy(exploreReq);
        return queryAndRebuildRsp(kgName, graphFrom, exploreReq);
    }

    @Override
    public CommonBasicGraphExploreRsp timeGraphExploration(String kgName, CommonTimingExploreReqList exploreReq) {
        exploreReq = graphHelperService.keyToId(kgName, exploreReq);
        GraphFrom graphFrom = GraphReqConverter.commonReqProxy(exploreReq);
        return queryAndRebuildRsp(kgName, graphFrom, exploreReq);
    }

    @Override
    public CommonBasicGraphExploreRsp reasoningGraphExploration(String kgName, CommonReasoningExploreReqList exploreReq) {
        exploreReq = graphHelperService.keyToId(kgName, exploreReq);
        GraphFrom graphFrom = GraphReqConverter.commonReqProxy(exploreReq);
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.graph(KGUtil.dbName(kgName), graphFrom));
        if (!graphOpt.isPresent()) {
            return CommonBasicGraphExploreRsp.EMPTY;
        }
        //推理
        GraphReasoningDTO reasoningDto = ruleReasoningService.buildRuleReasonDto(kgName, graphOpt.get(), exploreReq);
        return this.buildExploreRspWithConcept(kgName, new GraphRspDTO(graphOpt.get(), exploreReq, reasoningDto));
    }

    private CommonBasicGraphExploreRsp queryAndRebuildRsp(String kgName, GraphFrom graphFrom, GraphReqAfterInterface graphReqAfter) {
        System.out.println(JacksonUtils.writeValueAsString(graphFrom));
        Optional<GraphVO> graphOpt = RestRespConverter.convert(graphApi.graph(KGUtil.dbName(kgName), graphFrom));
        return graphOpt.map(graphVO -> this.buildExploreRspWithConcept(kgName, new GraphRspDTO(graphOpt.get(), graphReqAfter))).orElse(CommonBasicGraphExploreRsp.EMPTY);
    }

    private CommonBasicGraphExploreRsp buildExploreRspWithConcept(String kgName, GraphRspDTO afterDTO) {
        Map<Long, BasicInfo> conceptIdMap = graphHelperService.getConceptIdMap(kgName);
        return GraphRspConverter.fillEntityAndEntity(conceptIdMap, afterDTO);
    }
}
