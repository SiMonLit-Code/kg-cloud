package com.plantdata.kgcloud.domain.graph.config;


import com.plantdata.kgcloud.domain.graph.config.service.GraphConfQaService;
import com.plantdata.kgcloud.sdk.req.GraphConfQaReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfQaRsp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jdm on 2019/12/13 15:26.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GraphConfQaTest {

    private static final String KG_NAME = "dh3773_9r96hk5ii5cfkk11";

    @Autowired
    private GraphConfQaService graphConfQaService;

    @Test
    public void saveQa(){
        GraphConfQaReq req = new GraphConfQaReq();
        req.setType(0);
        req.setPriority(1);
        req.setQuestion("这是一首简单的");
        List<GraphConfQaReq> list = new ArrayList<>();
        list.add(req);
        List<GraphConfQaRsp> graphConfQa = graphConfQaService.saveQa(KG_NAME,list);
    }


}
