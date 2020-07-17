package ai.plantdata.kgcloud.domain.task.service;

import ai.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotNameReq;
import ai.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotReq;
import ai.plantdata.kgcloud.domain.task.rsp.TaskGraphSnapshotRsp;
import org.springframework.data.domain.Page;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 10:31
 **/
public interface TaskGraphService {
    /**
     * 快照列表
     *
     * @param req
     * @return
     */
    Page<TaskGraphSnapshotRsp> snapshotList(TaskGraphSnapshotReq req);

    /**
     * @param id
     */
    void snapshotDelete(Long id);

    /**
     * 新增备份记录
     * @param req
     * @return
     */
    TaskGraphSnapshotRsp add(TaskGraphSnapshotNameReq req);

}
