package com.plantdata.kgcloud.domain.task.req;

import ai.plantdata.cloud.bean.BaseReq;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 10:38
 **/
@Data
public class TaskGraphSnapshotReq extends BaseReq {
    private String kgName;
    private String userId;
}
