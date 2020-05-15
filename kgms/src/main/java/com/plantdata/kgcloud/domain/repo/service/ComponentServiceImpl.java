package com.plantdata.kgcloud.domain.repo.service;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.repo.enums.HandleType;
import com.plantdata.kgcloud.domain.repo.model.D2rRepository;
import com.plantdata.kgcloud.domain.repo.model.RepositoryHandler;
import com.plantdata.kgcloud.domain.repo.model.req.D2rReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.D2rRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

/**
 * @author cjw
 * @date 2020/5/15  14:23
 */
@Service
public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private FunctionExecutorImpl functionExecutor;

    @Override
    public D2rRsp d2r(D2rReq d2rReq) {
        List<RepositoryHandler> handlers = Lists.newArrayList(
                new RepositoryHandler(1, HandleType.BEFORE, "d2r1", 1),
                new RepositoryHandler(2, HandleType.BEFORE, "d2r2", 2),
                new RepositoryHandler(3, HandleType.AFTER, "d2r3", 3));
        Function<String, String> basicTest = this::basicTest;
        return functionExecutor.execute(
                () -> new D2rRepository<>(discoveryClient, handlers, basicTest, "调用基础版本方法"),
                D2rRsp.class
        );
    }


    public String basicTest(String config) {
        System.out.println("config:" + config);
        return config;
    }


}
