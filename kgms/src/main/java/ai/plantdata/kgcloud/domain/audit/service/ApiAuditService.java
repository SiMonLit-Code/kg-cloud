package ai.plantdata.kgcloud.domain.audit.service;

import ai.plantdata.kgcloud.domain.audit.req.ApiAuditReq;
import ai.plantdata.kgcloud.domain.audit.req.ApiAuditTopReq;
import ai.plantdata.kgcloud.domain.audit.req.ApiAuditUrlReq;
import ai.plantdata.kgcloud.domain.audit.rsp.ApiAuditRsp;
import ai.plantdata.kgcloud.domain.audit.rsp.ApiAuditTopRsp;
import ai.plantdata.kgcloud.domain.audit.rsp.ApiAuditUrlRsp;
import ai.plantdata.kgcloud.domain.audit.rsp.AuditKgNameRsp;
import ai.plantdata.kgcloud.sdk.mq.ApiAuditMessage;

import java.util.List;

/**
 * @ClassName ApiAuditService
 * @Function TODO
 * @Author wanglong
 * @Date 2019/12/6 13:25
 * @Version 3.0.0
 **/
public interface ApiAuditService {
    void logApiAudit(ApiAuditMessage apiAuditMessage);

    List<AuditKgNameRsp> findAllKgName();

    List<String> findAllString();

    List<ApiAuditRsp> groupByKgName(ApiAuditReq req);

    ApiAuditUrlRsp groupByUrl(ApiAuditUrlReq req);

    List<ApiAuditTopRsp> groupByTop(ApiAuditTopReq req);

    List<ApiAuditRsp> groupByPage(ApiAuditReq req);
}
