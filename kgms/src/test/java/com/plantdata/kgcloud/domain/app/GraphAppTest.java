package com.plantdata.kgcloud.domain.app;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.service.GraphApplicationService;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
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
//        knowledgeRecommendReq.setAllowAttrsKey();
//        knowledgeRecommendReq.setDirection();
//
//        knowledgeRecommendReq.setPage();
//        knowledgeRecommendReq.setSize();
//        knowledgeRecommendReq
        knowledgeRecommendReq.setEntityId(3L);
        knowledgeRecommendReq.setAllowAttrs(Lists.newArrayList(1, 2));
        List<ObjectAttributeRsp> attributeRspList = graphApplicationService.knowledgeRecommend("dh3773_9r96hk5ii5cfkk11", knowledgeRecommendReq);
        System.out.println(JacksonUtils.writeValueAsString(attributeRspList));
    }

    /**
     * 概念树
     */
    @Test
    public void conceptTreeTest() {
        List<BasicInfoVO> concept = graphApplicationService.conceptTree("dh3773_9r96hk5ii5cfkk11", null, null);
        System.out.println(JacksonUtils.writeValueAsString(concept));
    }

    /**
     * 知识卡片
     */
    @Test
    public void infoBoxTest() throws IOException {
        InfoBoxReq infoBoxReq = new InfoBoxReq();
        infoBoxReq.setId(3L);
        InfoBoxRsp infoBoxRsp = graphApplicationService.infoBox("dh3773_9r96hk5ii5cfkk11", "bj73pcfp", infoBoxReq);
        System.out.println(JacksonUtils.writeValueAsString(infoBoxRsp));
    }

    /**
     * 知识卡片
     */
    @Test
    public void batchInfoBoxTest() {
        BatchInfoBoxReq infoBoxReq = new BatchInfoBoxReq();
        infoBoxReq.setEntityIdList(Lists.newArrayList(3L));
        List<InfoBoxRsp> infoBoxRspList = graphApplicationService.infoBox("dh3773_9r96hk5ii5cfkk11", infoBoxReq);
        System.out.println(JacksonUtils.writeValueAsString(infoBoxRspList));
    }

    /**
     * 可视化模型
     */
    @Test
    public void visualModelsTest() {
        BasicConceptTreeRsp treeRsp = graphApplicationService.visualModels("dh3773_9r96hk5ii5cfkk11", true, 0L);
        System.out.println(JacksonUtils.writeValueAsString(treeRsp));
    }

}
