package com.plantdata.kgcloud.domain.task.service;

import com.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphSnapshotRsp;
import com.plantdata.kgcloud.domain.task.rsp.TaskTemplateRsp;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 10:31
 **/
public interface TaskGraphService {
    /**
     * 快照列表
     * @param req
     * @return
     */
    Page<TaskGraphSnapshotRsp> snapshotList(TaskGraphSnapshotReq req);

    /**
     *
     * @param id
     */
    void snapshotDelete(Long id);


    List<TaskTemplateRsp> taskTemplateList();


    Integer searchStatus(String kgName);

    Integer openSearch(String kgName);

    void closeSearch(String kgName);

    void flushSearch(String kgName);
}
