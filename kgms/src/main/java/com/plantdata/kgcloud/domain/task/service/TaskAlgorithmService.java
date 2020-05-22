package com.plantdata.kgcloud.domain.task.service;

import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.task.rsp.EntityIntimateRsp;
import com.plantdata.kgcloud.domain.task.rsp.EntityKeyRsp;

/**
 * @author xiezhenxiang 2020/5/21
 */
public interface TaskAlgorithmService {

    BasePage<EntityKeyRsp> entityKeyList(String kgName, String kw, BaseReq baseReq);

    BasePage<EntityIntimateRsp> entityIntimateList(String kgName, BaseReq baseReq);
}
