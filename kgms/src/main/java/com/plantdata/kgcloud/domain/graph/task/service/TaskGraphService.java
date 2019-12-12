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
}
