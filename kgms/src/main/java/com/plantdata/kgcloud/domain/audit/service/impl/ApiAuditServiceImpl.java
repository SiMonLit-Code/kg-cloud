package com.plantdata.kgcloud.domain.audit.service.impl;

import com.plantdata.kgcloud.domain.audit.entity.ApiAudit;
import com.plantdata.kgcloud.domain.audit.repository.ApiAuditRepository;
import com.plantdata.kgcloud.domain.audit.service.ApiAuditService;
import com.plantdata.kgcloud.sdk.mq.ApiAuditMessage;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName ApiAuditServiceImpl
 * @Function TODO
 * @Author wanglong
 * @Date 2019/12/6 13:26
 * @Version 3.0.0
 **/
@Service
public class ApiAuditServiceImpl implements ApiAuditService {
    @Autowired
    private ApiAuditRepository apiAuditRepository;
    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logApiAudit(ApiAuditMessage apiAuditMessage) {
        ApiAudit apiAudit = ConvertUtils.convert(ApiAudit.class).apply(apiAuditMessage);
        apiAudit.setId(this.kgKeyGenerator.getNextId());
        this.apiAuditRepository.save(apiAudit);
    }
}
