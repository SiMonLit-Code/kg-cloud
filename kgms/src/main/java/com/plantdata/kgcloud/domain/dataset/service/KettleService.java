package com.plantdata.kgcloud.domain.dataset.service;


import com.plantdata.kgcloud.domain.dataset.req.EtlSaveRequest;

public interface KettleService {
    /**
     * kettle 测试
     * @param etlSaveRequest
     * @return
     */
    Object kettleService(EtlSaveRequest etlSaveRequest);

    /**
     * kettle sql预览
     * @param etlSaveRequest
     * @return
     */
    Object previewSqlEtl(EtlSaveRequest etlSaveRequest);

    /**
     * kettle 保存
     * @param etlSaveRequest
     * @return
     * @throws Exception
     */
    String saveEtl(String userId,EtlSaveRequest etlSaveRequest) throws Exception;
}
