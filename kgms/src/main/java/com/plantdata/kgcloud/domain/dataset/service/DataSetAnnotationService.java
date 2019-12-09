package com.plantdata.kgcloud.domain.dataset.service;

import com.plantdata.kgcloud.sdk.req.AnnotationQueryReq;
import com.plantdata.kgcloud.sdk.req.AnnotationReq;
import com.plantdata.kgcloud.sdk.rsp.AnnotationRsp;
import org.springframework.data.domain.Page;

public interface DataSetAnnotationService {
//    void updateDataById(String userId, AnnotationRequest annotationRequest);

    Page<AnnotationRsp> findAll(String kgName, AnnotationQueryReq baseReq);

    void delete(String kgName, Long id);


    AnnotationRsp findById(String kgName,Long id);
    AnnotationRsp update(String kgName, Long id, AnnotationReq req);

    AnnotationRsp add(String kgName, AnnotationReq req);
}