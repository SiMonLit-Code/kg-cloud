package com.plantdata.kgcloud.domain.data.obtain.service;

import com.plantdata.kgcloud.domain.data.obtain.req.ConceptTreeReq;
import com.plantdata.kgcloud.domain.data.obtain.rsp.ConceptTreeRsp;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 17:47
 */
public interface ConceptOperationService {
    /**
     * 查询概念数
     *
     * @param conceptTreeReq 概念参数
     * @return 概念
     */
    List<ConceptTreeRsp> queryConceptTree(ConceptTreeReq conceptTreeReq);
}
