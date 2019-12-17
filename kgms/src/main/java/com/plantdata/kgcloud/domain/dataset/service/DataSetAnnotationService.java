package com.plantdata.kgcloud.domain.dataset.service;

import com.plantdata.kgcloud.sdk.req.AnnotationCreateReq;
import com.plantdata.kgcloud.sdk.req.AnnotationDataReq;
import com.plantdata.kgcloud.sdk.req.AnnotationQueryReq;
import com.plantdata.kgcloud.sdk.req.AnnotationReq;
import com.plantdata.kgcloud.sdk.rsp.AnnotationRsp;
import org.springframework.data.domain.Page;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface DataSetAnnotationService {

    /**
     * 查找全部
     *
     * @param kgName
     * @param baseReq
     * @return
     */
    Page<AnnotationRsp> findAll(String kgName, AnnotationQueryReq baseReq);

    /**
     * 删除
     *
     * @param kgName
     * @param id
     */
    void delete(String kgName, Long id);

    /**
     * 根据id查找
     *
     * @param kgName
     * @param id
     * @return
     */
    AnnotationRsp findById(String kgName, Long id);

    /**
     * 更新标引
     *
     * @param kgName
     * @param id
     * @param req
     * @return
     */
    AnnotationRsp update(String kgName, Long id, AnnotationReq req);

    /**
     * 添加标引
     *
     * @param kgName
     * @param req
     * @return
     */
    AnnotationRsp add(String userId,String kgName, AnnotationCreateReq req);

    /**
     * 标引
     *
     * @param kgName
     * @param id
     * @param req
     */
    void annotation(String userId,String kgName, Long id, AnnotationDataReq req);
}