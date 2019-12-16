package com.plantdata.kgcloud.domain.audit.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 11:23
 **/
@Data
public class ApiAuditReq {


    @ApiModelProperty("开始时间  格式 yyyy-MM-dd HH:mm:ss")
    private String from;

    @ApiModelProperty("结束时间  格式 yyyy-MM-dd HH:mm:ss")
    private String to;

    @ApiModelProperty("结束时间  格式 yyyy-MM-dd HH:mm:ss")
    private String kgName;


    @ApiModelProperty("结束时间  格式 yyyy-MM-dd HH:mm:ss")
    private String page;

    @ApiModelProperty("接口数组")
    private List<String> urls;


    @JsonIgnore
    private Date beginTime;

    @JsonIgnore
    private Date endTime;
}
