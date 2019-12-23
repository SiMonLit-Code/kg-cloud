package com.plantdata.kgcloud.domain.app;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.service.GraphRelationAnalysisService;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReqAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationReasoningAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationTimingAnalysisRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/18 13:50
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GraphRelationAnalysisTest {

    private static final String KG_NAME = "dh3773_9r96hk5ii5cfkk11";
    @Autowired
    private GraphRelationAnalysisService graphRelationAnalysisService;

    CommonRelationReq commonRelationReq() {
        CommonRelationReq commonRelationReq = new CommonRelationReq();
        commonRelationReq.setIds(Lists.newArrayList(3L, 4L));
        return commonRelationReq;
    }


    @Test
    public void relationAnalysisTest() {
        RelationReqAnalysisReq analysisReq = new RelationReqAnalysisReq();
        analysisReq.setRelation(commonRelationReq());
        RelationAnalysisRsp analysisRsp = graphRelationAnalysisService.relationAnalysis(KG_NAME, analysisReq);
        System.out.println(JacksonUtils.writeValueAsString(analysisRsp));
    }

    @Test
    public void relationTimingAnalysisTest() {
        RelationTimingAnalysisReq analysisReq = new RelationTimingAnalysisReq();
        analysisReq.setRelation(commonRelationReq());
        RelationTimingAnalysisRsp analysisRsp = graphRelationAnalysisService.relationTimingAnalysis(KG_NAME, analysisReq);
        System.out.println(JacksonUtils.writeValueAsString(analysisRsp));
    }

    @Test
    public void  relationReasoningAnalysisTest(){
        RelationReasoningAnalysisReq analysisReq = new RelationReasoningAnalysisReq();
        analysisReq.setRelation(commonRelationReq());
        RelationReasoningAnalysisRsp analysisRsp = graphRelationAnalysisService.relationReasoningAnalysis(KG_NAME, analysisReq);
        System.out.println(JacksonUtils.writeValueAsString(analysisRsp));
    }

}
