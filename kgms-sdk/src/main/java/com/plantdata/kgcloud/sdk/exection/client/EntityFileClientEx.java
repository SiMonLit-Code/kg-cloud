package com.plantdata.kgcloud.sdk.exection.client;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.EntityFileClient;
import com.plantdata.kgcloud.sdk.req.EntityFileRelationAddReq;
import com.plantdata.kgcloud.sdk.rsp.edit.EntityFileRelationRsp;
import org.springframework.stereotype.Component;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-19 19:17
 **/
@Component
public class EntityFileClientEx implements EntityFileClient {
    @Override
    public ApiReturn<EntityFileRelationRsp> add(String kgName, EntityFileRelationAddReq req) {
        return ApiReturn.fail(500,"请求超时");
    }
}
