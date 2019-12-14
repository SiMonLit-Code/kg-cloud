package com.plantdata.kgcloud.domain.graph.config;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfKgql;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfKgqlRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfKgqlService;
import com.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * Created by jdm on 2019/12/13 14:07.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class GraphConfKgqlTest {
    private static final String KG_NAME = "dh3773_9r96hk5ii5cfkk11";

    @Autowired
    private GraphConfKgqlService graphConfKgqlService;

    @Autowired
    private GraphConfKgqlRepository graphConfKgqlRepository;




    @Test
    public void createKgql(){
        GraphConfKgqlReq req = new GraphConfKgqlReq();
        req.setKgql("alibabaniupi");
        req.setKgqlName("kgName");
        req.setRuleType(0);
        GraphConfKgqlRsp rsp = graphConfKgqlService.createKgql(KG_NAME,req);
        System.out.println(JacksonUtils.writeValueAsString(rsp));
    }

    @Test
    public void updateKgql(){
        GraphConfKgqlReq req = new GraphConfKgqlReq();
        req.setKgql("alibabaniupi");
        req.setKgqlName("kgName");
        req.setRuleType(0);
        GraphConfKgqlRsp rsp = graphConfKgqlService.updateKgql(100000000091L,req);
        System.out.println(rsp);
    }
    @Test
    public void deteleKgql(){
        graphConfKgqlService.deleteKgql(100000000091L);
    }

    @Test
    public void findByKgNameAndRuleType(){
        BaseReq baseReq = new BaseReq();
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
        Page<GraphConfKgql> all = graphConfKgqlRepository.findByKgNameAndRuleType(KG_NAME,0, pageable);
        System.out.println(all);
    }
    @Test
    public void findById(){
        Optional<GraphConfKgql> graphConfKgql = graphConfKgqlRepository.findById(100000000091L);
        System.out.println(graphConfKgql);
    }
}
