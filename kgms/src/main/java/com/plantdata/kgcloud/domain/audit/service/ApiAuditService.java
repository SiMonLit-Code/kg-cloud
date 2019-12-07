package com.plantdata.kgcloud.domain.audit.service;

import com.plantdata.kgcloud.sdk.mq.ApiAuditMessage;

/**
 * @ClassName ApiAuditService
 * @Function TODO
 * @Author wanglong
 * @Date 2019/12/6 13:25
 * @Version 3.0.0
 **/
public interface ApiAuditService {
    void logApiAudit(ApiAuditMessage apiAuditMessage);
}
