package com.plantdata.kgcloud.domain.audit.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ApiModelProperty("根据图谱名称统计")
    private String kgName;


    @ApiModelProperty("根据调用方式统计")
    private String page;

    @ApiModelProperty("接口数组")
    private List<String> urls;


    @JsonIgnore
    private Date beginTime;

    @JsonIgnore
    private Date endTime;
}
