package com.plantdata.kgcloud.domain.app;

import com.plantdata.kgcloud.domain.app.service.GraphExplorationService;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
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
        common.setKw("李岩");
        exploreReq.setCommon(common);
        CommonBasicGraphExploreRsp commonGraphExploreRsp = graphExplorationService.commonGraphExploration(KG_NAME, exploreReq);
        System.out.println(JacksonUtils.writeValueAsString(commonGraphExploreRsp));
    }


    /**
     * 业务规则
     */
    @Test
    public void CommonBasicGraphExploreRspTest() {
        ExploreByKgQlReq exploreByKgQlReq = new ExploreByKgQlReq();
        exploreByKgQlReq.setEntityId(13L);
        exploreByKgQlReq.setKgQl("concept('人').relation('任职机构')");
        exploreByKgQlReq.setRelationMerge(false);
        CommonBasicGraphExploreRsp exploreRsp = graphExplorationService.exploreByKgQl(KG_NAME, exploreByKgQlReq);
        System.out.println(JacksonUtils.writeValueAsString(exploreRsp));
    }

    /**
     * gis图探索
     */
    @Test
    public void gisGraphExplorationTest() {
        GisGraphExploreReq gisGraphExploreReq = new GisGraphExploreReq();
        gisGraphExploreReq.setIsInherit(true);
        GisGraphExploreRsp gisGraphExploreRsp = graphExplorationService.gisGraphExploration(KG_NAME, gisGraphExploreReq);
        System.out.println(JacksonUtils.writeValueAsString(gisGraphExploreRsp));
    }
}
