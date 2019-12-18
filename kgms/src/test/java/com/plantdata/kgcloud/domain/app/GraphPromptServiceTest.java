package com.plantdata.kgcloud.domain.app;

import com.plantdata.kgcloud.domain.app.service.GraphPromptService;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 14:11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GraphPromptServiceTest {

    @Autowired
    private GraphPromptService graphPromptService;

    @Test
    public void edgeAttributeSearchTest(){
        EdgeAttrPromptReq promptReq = new EdgeAttrPromptReq();
        promptReq.setAttrId(2);
        promptReq.setReserved(0);
        promptReq.setSeqNo(1);
        graphPromptService.edgeAttributeSearch("dh3773_9r96hk5ii5cfkk11",promptReq);
    }

}
