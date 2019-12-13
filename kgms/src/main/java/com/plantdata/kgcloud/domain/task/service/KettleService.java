package com.plantdata.kgcloud.domain.task.service;


import com.plantdata.kgcloud.domain.task.req.KettleReq;

public interface KettleService {
    /**
     * kettle 测试
     *
     * @param etlSaveRequest
     * @return
     */
    Object kettleService(KettleReq etlSaveRequest);

    /**
     * kettle sql预览
     *
     * @param etlSaveRequest
     * @return
     */
    Object kettlePreview(KettleReq etlSaveRequest);

    /**
     * kettle 保存
     *
     * @param userId
     * @param kettleReq
     * @return
     * @throws Exception
     */
    String kettleSave(String userId, KettleReq kettleReq) throws Exception;
}
