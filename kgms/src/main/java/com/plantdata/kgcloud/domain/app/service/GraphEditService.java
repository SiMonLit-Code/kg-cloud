package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/20 10:01
 */
public interface GraphEditService {
    /**
     * 创建概念
     *
     * @param kgName
     * @param conceptAddReq req
     * @return .
     */
    Long createConcept(String kgName, ConceptAddReq conceptAddReq);
}
