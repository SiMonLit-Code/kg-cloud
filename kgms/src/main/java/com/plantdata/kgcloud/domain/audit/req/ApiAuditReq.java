package com.plantdata.kgcloud.domain.audit.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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


    private String from;

    private String to;


    private String kgName;

    private String page;


    private List<String> urls;


    @JsonIgnore
    private Date beginTime;

    @JsonIgnore
    private Date endTime;
}
