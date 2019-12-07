package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.sdk.req.app.BatchEntityAttrDeleteReq;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 11:00
 */
public interface KgDataService {

    List<OpenEntityRsp> queryEntityList(String kgName, EntityQueryReq entityQueryReq);

    List<OpenBatchSaveEntityRsp> saveOrUpdate(String kgName, boolean update, List<OpenBatchSaveEntityRsp> batchEntity);

    void batchDeleteEntityAttr(String kgName, BatchEntityAttrDeleteReq deleteReq);
}
