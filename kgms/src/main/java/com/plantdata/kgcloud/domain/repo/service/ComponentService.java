package com.plantdata.kgcloud.domain.repo.service;

import com.plantdata.kgcloud.domain.repo.model.req.D2rReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.D2rRsp;

/**
 * @author cjw
 * @date 2020/5/15  14:23
 */
public interface ComponentService {

    D2rRsp d2r(D2rReq d2rReq);
}
