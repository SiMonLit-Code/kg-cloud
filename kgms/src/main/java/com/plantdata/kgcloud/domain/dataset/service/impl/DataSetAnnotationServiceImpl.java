package com.plantdata.kgcloud.domain.dataset.service.impl;

import com.plantdata.kgcloud.domain.dataset.service.DataSetAnnotationService;
import com.plantdata.kgcloud.sdk.req.AnnotationQueryReq;
import com.plantdata.kgcloud.sdk.req.AnnotationReq;
import com.plantdata.kgcloud.sdk.rsp.AnnotationRsp;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 21:13
 **/
@Service
public class DataSetAnnotationServiceImpl implements DataSetAnnotationService {
    @Override
    public Page<AnnotationRsp> findAll(String kgName, AnnotationQueryReq baseReq) {
        return null;
    }

    @Override
    public void delete(String kgName, Long id) {

    }

    @Override
    public AnnotationRsp findById(String kgName, Long id) {
        return null;
    }

    @Override
    public AnnotationRsp update(String kgName, Long id, AnnotationReq req) {
        return null;
    }

    @Override
    public AnnotationRsp add(String kgName, AnnotationReq req) {
        return null;
    }
}
