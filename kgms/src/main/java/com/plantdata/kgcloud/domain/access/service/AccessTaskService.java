package com.plantdata.kgcloud.domain.access.service;

import com.plantdata.kgcloud.domain.access.entity.DWTask;
import com.plantdata.kgcloud.domain.access.rsp.DWTaskRsp;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.sdk.rsp.DWTableRsp;
import com.plantdata.kgcloud.sdk.req.DataAccessTaskConfigReq;

import java.util.List;

public interface AccessTaskService {

    Integer run(String userId,List<DataAccessTaskConfigReq> reqs,Integer taskId);

    DWTaskRsp getTask(Integer id);

    DWTaskRsp getByTaskName(String taskName);

    void saveTask(DWTaskRsp taskRsp);

    void saveTask(DWTaskRsp taskRsp,Long timeout);

    String getKtrConfig(Long databaseId, String tableName,String isAllKey,Integer isScheduled,String target);

    String getTransferConfig(Boolean isGraph,Integer modelId,Long databaseId, String tableName,Integer isScheduled,String pdSingleFiel);

    String getDwConfig(Long databaseId, DWTable tableName,Integer isScheduled);

    String createKtrTask(String tableName,Long databaseId,String isAllKey,Integer isSchedue,String target);

    String createTransfer(Boolean isGraph,Integer modelId,String tableName,Long databaseId,List<String> outputs,List<String> distributeOriginalData,List<String> deleteOutputs,List<String> deleteDistributeOriginalData,String isAllKey);

    String createDwTask(String tableName,Long databaseId);

    void updateTableSchedulingConfig(DWDatabaseRsp database, DWTableRsp table, String cron, Integer isAll, String field);

    List<DWTask> getTableTask(Long dbId, String tableName);

    void addRerunTask(Long dbId, String tableName, List<String> resourceNames);

    void addDeleteTask(List<String> resourceNames);

    List<DWTask> getTransferTaskByResourceNames(List<String> transfTasks);

    void deleteTaskByDW(Long databaseId, String tableName);

    void deleteTaskByKG(String kgName);
}
