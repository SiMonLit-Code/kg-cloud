package com.plantdata.kgcloud.domain.app;

import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/17 16:15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GraphAppTest {

    @Autowired
    private GraphApplicationService graphApplicationService;

    /**
     * schema 测试
     */
    @Test
    public void querySchemaTest() {
        SchemaRsp schema = graphApplicationService.querySchema("bj73pb33_graph_16f12e3186b");
        System.out.println(JacksonUtils.writeValueAsString(schema));
    }

    /**
     * 知识推荐测试
     */
    @Test
    public void knowledgeRecommendTest() {
        KnowledgeRecommendReq knowledgeRecommendReq = new KnowledgeRecommendReq();
//        knowledgeRecommendReq.setAllowAttrs();
//        knowledgeRecommendReq.setAllowAttrsKey();
//        knowledgeRecommendReq.setDirection();
//
//        knowledgeRecommendReq.setPage();
//        knowledgeRecommendReq.setSize();
//        knowledgeRecommendReq
        knowledgeRecommendReq.setEntityId(3L);
        knowledgeRecommendReq.setAllowAttrs(null);
        List<ObjectAttributeRsp> attributeRspList = graphApplicationService.knowledgeRecommend("dh3773_9r96hk5ii5cfkk11", knowledgeRecommendReq);
        System.out.println(JacksonUtils.writeValueAsString(attributeRspList));
    }
}
