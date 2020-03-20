package com.plantdata.kgcloud.domain.access.rsp;

import lombok.Data;

import java.util.Date;

@Data
public class DWTaskRsp {

    private Integer id;

    private String userId;
    private String name;
    private String taskType;
    private String config;
    private Date createAt;
    private Date updateAt;
}
