package com.plantdata.kgcloud.domain.log.service;

import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.log.req.SyncLogReq;
import com.plantdata.kgcloud.domain.log.rsp.SyncLogRsp;

public interface SyncLogService {
    BasePage<SyncLogRsp> list(SyncLogReq req);
}
