package com.plantdata.kgcloud.domain.graph.config;

import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfAlgorithm;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfAlgorithmRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfAlgorithmService;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jdm on 2019/12/12 10:31.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GraphConfAlgorithmTest {
    @Autowired
    private GraphConfAlgorithmService graphConfAlgorithmService;

    private static final String KG_NAME = "dh3773_9r96hk5ii5cfkk11";

    @Autowired
    private GraphConfAlgorithmRepository graphConfAlgorithmRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Test
    public void createAlgorithm(){
        GraphConfAlgorithmReq req = new GraphConfAlgorithmReq();
        req.setRemark("sdgf");
        req.setAlgorithmUrl("gh");
        req.setAlgorithmName("dd");
        GraphConfAlgorithmRsp graphConfAlgorithmRsp = graphConfAlgorithmService.createAlgorithm(KG_NAME,req);
        System.out.println(JacksonUtils.writeValueAsString(graphConfAlgorithmRsp));
    }
    @Test
    public void findByKgName(){
        BaseReq baseReq = new BaseReq();
        Pageable pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());

        Page<GraphConfAlgorithm> all = graphConfAlgorithmRepository.findByKgName(KG_NAME, pageable);

        System.out.println(all);
    }
}
