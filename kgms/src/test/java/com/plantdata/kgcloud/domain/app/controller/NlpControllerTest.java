package com.plantdata.kgcloud.domain.app.controller;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/19 11:17
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NlpControllerTest {

    @Autowired
    private NlpController nlpController;
    private static final String KG_NAME = "dh3773_9r96hk5ii5cfkk11";

    @Test
    public void namedEntityRecognition() {
        NerReq nerReq = new NerReq();
        nerReq.setInput("李岩");
        //nerReq.setConfig();
        ApiReturn<List<NerResultRsp>> listApiReturn = nlpController.namedEntityRecognition(nerReq);
        System.out.println(JacksonUtils.writeValueAsString(listApiReturn.getData()));
    }

    @Test
    public void graphSegment() {
        SegmentReq segmentReq = new SegmentReq();
        segmentReq.setKw("李岩是个大人物阿你知道么");
        segmentReq.setUseAttr(true);
        segmentReq.setUseConcept(true);
        segmentReq.setUseEntity(true);
        ApiReturn<List<GraphSegmentRsp>> listApiReturn = nlpController.graphSegment(KG_NAME, segmentReq);
        System.out.println(JacksonUtils.writeValueAsString(listApiReturn.getData()));
    }

    @Test
    public void tagging() {
        EntityLinkingReq entityLinkingReq = new EntityLinkingReq();
        entityLinkingReq.setConceptIds(Lists.newArrayList(0L, 1L, 2L, 3L, 4L));
        entityLinkingReq.setText("李岩是个大人物阿你知道么");
        ApiReturn<List<TaggingItemRsp>> tagging = nlpController.tagging(KG_NAME, entityLinkingReq);
        System.out.println(JacksonUtils.writeValueAsString(tagging.getData()));
    }
}