package com.plantdata.kgcloud.domain.scene.service;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.GraphPageReq;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GraphService {
    ApiReturn<List<GraphRsp>> getListPage();

    ApiReturn<SchemaRsp> getSchema(String kgName);
}
