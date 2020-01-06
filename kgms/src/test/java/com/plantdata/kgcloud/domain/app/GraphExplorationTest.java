package com.plantdata.kgcloud.domain.app;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.service.GraphExplorationService;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonReasoningExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.NotNull;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/28 10:22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GraphExplorationTest {
    @Autowired
    private GraphExplorationService graphExplorationService;
    @Autowired
    private GraphHelperService graphHelperService;
    private static final String KG_NAME = "dh3773_9r96hk5ii5cfkk11";

    /**
     * 图探索
     */
    @Test
    public void commonGraphExplorationTest() {
        CommonExploreReq exploreReq = new CommonExploreReq();
        CommonFiltersReq common = new CommonFiltersReq();
        common.setId(1L);
        common.setHyponymyDistance(6);
        exploreReq.setCommon(common);
        CommonBasicGraphExploreRsp commonGraphExploreRsp = graphExplorationService.commonGraphExploration("bj73pdud_graph_16f1cc1439e", exploreReq);
        System.out.println(JacksonUtils.writeValueAsString(commonGraphExploreRsp));
    }


    /**
     * 业务规则
     */
    @Test
    public void CommonBasicGraphExploreRspTest() {
        ExploreByKgQlReq exploreByKgQlReq = new ExploreByKgQlReq();
//        exploreByKgQlReq.setEntityId(13L);
//        exploreByKgQlReq.setKgQl("concept('人').relation('任职机构')");

        exploreByKgQlReq.setEntityId(13L);
        exploreByKgQlReq.setKgQl("concept('投资机构').relation('投资')");
        exploreByKgQlReq.setRelationMerge(true);
        CommonBasicGraphExploreRsp exploreRsp = graphExplorationService.exploreByKgQl("bj73pb33_graph_default", exploreByKgQlReq);
        System.out.println(JacksonUtils.writeValueAsString(exploreRsp));
    }

    /**
     * gis图探索
     */
    @Test
    public void gisGraphExplorationTest() {
        GisGraphExploreReq gisGraphExploreReq = new GisGraphExploreReq();
        gisGraphExploreReq.setIsInherit(true);
        gisGraphExploreReq.setConceptIds(Lists.newArrayList(1L));
        GisGraphExploreRsp gisGraphExploreRsp = graphExplorationService.gisGraphExploration(KG_NAME, gisGraphExploreReq);
        System.out.println(JacksonUtils.writeValueAsString(gisGraphExploreRsp));
    }

    @Test
    public void gisLocusAnalysisTest() {
        GisLocusReq gisLocusReq = new GisLocusReq();
        GisLocusReq.GisRuleParam gisRuleParam = new GisLocusReq.GisRuleParam();
        gisRuleParam.setIds(Lists.newArrayList(3L));
        gisRuleParam.setRuleId(1L);
        gisRuleParam.setKql("concept('人').relation('任职机构')");
        gisLocusReq.setRules(Lists.newArrayList(gisRuleParam));
        GisLocusAnalysisRsp analysisRsp = graphExplorationService.gisLocusAnalysis(KG_NAME, gisLocusReq);
        System.out.println(JacksonUtils.writeValueAsString(analysisRsp));
    }


    @Test
    public void timeGraphExplorationTest() {
        CommonTimingExploreReq exploreReq = new CommonTimingExploreReq();
        CommonFiltersReq common = new CommonFiltersReq();
        common.setKw("李岩");
        exploreReq.setCommon(common);
        TimeFilterExploreReq timeFilterExploreReq = new TimeFilterExploreReq();
//        timeFilterExploreReq.setFromTime();
//        timeFilterExploreReq.setToTime();
//        timeFilterExploreReq.setTimeFilterType();
//        exploreReq.setTimeFilters();
        CommonBasicGraphExploreRsp exploreRsp = graphExplorationService.timeGraphExploration(KG_NAME, exploreReq);
        System.out.println(JacksonUtils.writeValueAsString(exploreRsp));

    }

    @Test
    public void reasoningGraphExplorationTest() {
        CommonReasoningExploreReq exploreReq = new CommonReasoningExploreReq();
        CommonFiltersReq common = new CommonFiltersReq();
        common.setKw("李岩");
        exploreReq.setCommon(common);
        CommonBasicGraphExploreRsp exploreRsp = graphExplorationService.reasoningGraphExploration(KG_NAME, exploreReq);
        System.out.println(JacksonUtils.writeValueAsString(exploreRsp));
    }

}
