package com.plantdata.kgcloud.domain.access.service;

import com.plantdata.kgcloud.domain.access.entity.DWTask;
import com.plantdata.kgcloud.domain.access.req.EtlConfigReq;
import com.plantdata.kgcloud.domain.access.req.KgConfigReq;
import com.plantdata.kgcloud.domain.access.rsp.DWTaskRsp;
import com.plantdata.kgcloud.sdk.req.DataAccessTaskConfigReq;

import java.util.List;

public interface AccessTaskService {
    String saveEtlTask(String userId, EtlConfigReq req);

    String saveKgTask(String userId, KgConfigReq req);

    Integer run(String userId,List<DataAccessTaskConfigReq> reqs,Integer taskId);

    DWTaskRsp getTask(Integer id);
}
