package com.plantdata.kgcloud.domain.app;

import com.plantdata.kgcloud.domain.app.service.GraphPathAnalysisService;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisReasonRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathTimingAnalysisRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/18 12:13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GraphPathAnalysisTest {

    @Autowired
    private GraphPathAnalysisService graphPathAnalysisService;

    private static final String KG_NAME = "dh3773_9r96hk5ii5cfkk11";

    private CommonPathReq commonPathReq() {
        CommonPathReq commonPathReq = new CommonPathReq();
        commonPathReq.setStart(3L);
        commonPathReq.setEnd(4L);
        return commonPathReq;
    }

    @Test
    public void pathTest() {
        PathAnalysisReq pathAnalysisReq = new PathAnalysisReq();
        pathAnalysisReq.setPath(commonPathReq());
        PathAnalysisRsp path = graphPathAnalysisService.path(KG_NAME, pathAnalysisReq);
        System.out.println(JacksonUtils.writeValueAsString(path));
    }

    @Test
    public void pathRuleReasonTest() {
        PathReasoningAnalysisReq analysisReq = new PathReasoningAnalysisReq();
        analysisReq.setPath(commonPathReq());
        PathAnalysisReasonRsp path = graphPathAnalysisService.pathRuleReason(KG_NAME, analysisReq);
        System.out.println(JacksonUtils.writeValueAsString(path));
    }

    @Test
    public void pathTimingAnalysisTest() {
        PathTimingAnalysisReq pathTimingAnalysisReq = new PathTimingAnalysisReq();
        pathTimingAnalysisReq.setPath(commonPathReq());
        PathTimingAnalysisRsp path = graphPathAnalysisService.pathTimingAnalysis(KG_NAME, pathTimingAnalysisReq);
        System.out.println(JacksonUtils.writeValueAsString(path));
    }

}
