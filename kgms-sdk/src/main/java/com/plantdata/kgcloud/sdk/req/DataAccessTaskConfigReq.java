package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("数据接入任务配置")
public class DataAccessTaskConfigReq {


    private String id;

    private String type;

    private String config;

    private List<String> outputs;
}
