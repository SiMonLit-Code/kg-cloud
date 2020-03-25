package com.plantdata.kgcloud.domain.access.rsp;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DWTaskRsp {

    private Integer id;

    private String userId;
    private String name;
    private String taskType;
    private List<String> outputs;
    private List<String> distributeOriginalData;
    private String config;
    private Integer status;
    private Date createAt;
    private Date updateAt;

}
