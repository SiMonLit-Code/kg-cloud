package com.plantdata.kgcloud.domain.graph.task.service;

import com.plantdata.kgcloud.domain.graph.task.req.TaskGraphSnapshotReq;
import com.plantdata.kgcloud.domain.graph.task.rsp.TaskGraphSnapshotRsp;
import com.plantdata.kgcloud.domain.graph.task.rsp.TaskTemplateRsp;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 10:31
 **/
public interface TaskGraphService {
    Page<TaskGraphSnapshotRsp> snapshotList(TaskGraphSnapshotReq req);

    void snapshotDelete(Long id);


    List<TaskTemplateRsp> taskTemplateList();

    Integer searchStatus(String kgName);

    Integer openSearch(String kgName);

    void closeSearch(String kgName);

    void flushSearch(String kgName);
}
