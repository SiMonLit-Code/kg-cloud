package com.plantdata.kgcloud.domain.dw.service;

import com.plantdata.kgcloud.domain.dw.req.StandardSearchReq;
import com.plantdata.kgcloud.domain.dw.rsp.StandardTemplateRsp;

import java.util.List;

public interface StandardTemplateService {
    List<StandardTemplateRsp> findAll(StandardSearchReq req);

    StandardTemplateRsp findOne(String userId, Integer standardTemplateId);
}
