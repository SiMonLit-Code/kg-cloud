package com.plantdata.kgcloud.domain.scene.service;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.AppClient;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.req.GraphPageReq;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class GraphServiceImpl implements GraphService{

    @Autowired
    private KgmsClient kgmsClient;

    @Autowired
    private AppClient appClient;

    @Override
    public ApiReturn<Page<GraphRsp>> getListPage(GraphPageReq graphPageReq) {
        return kgmsClient.graphFindAll(graphPageReq);
    }

    @Override
    public ApiReturn<SchemaRsp> getSchema(String kgName) {
        return appClient.querySchema(kgName);
    }
}
