package com.plantdata.kgcloud.domain.repo.service.impl;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.repo.adapter.RequestAdapter;
import com.plantdata.kgcloud.domain.repo.enums.HandleType;
import com.plantdata.kgcloud.domain.repo.model.D2rRepository;
import com.plantdata.kgcloud.domain.repo.model.RepositoryHandler;
import com.plantdata.kgcloud.domain.repo.model.req.D2rReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.D2rRsp;
import com.plantdata.kgcloud.domain.repo.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private RequestAdapter requestAdapter;

    @Override
    public D2rRsp d2r(D2rReq d2rReq) {
        List<RepositoryHandler> handlers = Lists.newArrayList(
                new RepositoryHandler(1, HandleType.BEFORE, "d2r1", 1, requestAdapter::d2RPreRequest),
                new RepositoryHandler(2, HandleType.BEFORE, "d2r2", 2, requestAdapter::d2RPostRequest));
        return functionExecutor.execute(
                () -> new D2rRepository(discoveryClient, handlers,
                        requestAdapter::d2RBaSic,
                        new RequestAdapter.D2rDTO(d2rReq)),
                D2rRsp.class
        );
    }

}
