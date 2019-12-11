package com.plantdata.kgcloud.domain.app;

import com.plantdata.kgcloud.domain.app.service.GraphExplorationService;
import com.plantdata.kgcloud.domain.app.service.GraphHelperService;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

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

    @Test
    public void commonGraphExplorationTest() {
        CommonExploreReq exploreReq = new CommonExploreReq();
//        exploreReq.setId(3L);
//        exploreReq.setDirection(0);
//        exploreReq.setHighLevelSize(10);
//        exploreReq.setDistance(4);
//        exploreReq.setInherit(true);
        CommonBasicGraphExploreRsp commonGraphExploreRsp = graphExplorationService.commonGraphExploration(KG_NAME, exploreReq);
        System.out.println(JacksonUtils.writeValueAsString(commonGraphExploreRsp));
    }

}
