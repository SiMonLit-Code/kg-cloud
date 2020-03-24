package com.plantdata.kgcloud.domain.access.service;

import com.plantdata.kgcloud.domain.access.rsp.DWTaskRsp;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.sdk.req.DataAccessTaskConfigReq;

import java.util.List;

public interface AccessTaskService {

    Integer run(String userId,List<DataAccessTaskConfigReq> reqs,Integer taskId);

    DWTaskRsp getTask(Integer id);

    DWTaskRsp getByTaskName(String taskName);

    void saveTask(DWTaskRsp taskRsp);

    void saveTask(DWTaskRsp taskRsp,Long timeout);

    String getKtrConfig(Long databaseId, String tableName);

    String getTransferConfig(Long databaseId, String tableName,Integer isScheduled);

    String getDwConfig(Long databaseId, DWTable tableName,Integer isScheduled);

    String createKtrTask(String tableName,Long databaseId,String isAllKey);

    String createTransfer(String tableName,Long databaseId,List<String> outputs,List<String> distributeOriginalData,String isAllKey);

    String createDwTask(String tableName,Long databaseId);
}
