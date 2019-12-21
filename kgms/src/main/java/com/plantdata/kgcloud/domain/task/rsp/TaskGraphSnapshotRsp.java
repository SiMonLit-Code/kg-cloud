package com.plantdata.kgcloud.domain.task.rsp;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
public class TaskGraphSnapshotRsp {
    private Long id;

    private String userId;

    private String name;

    private String kgName;

    private String fileSize;

    private Integer status;

    private Date restoreAt;

    private Date createAt;

}
