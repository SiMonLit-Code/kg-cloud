package com.plantdata.kgcloud.sdk.rsp;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:37
 **/
@Data
public class DataSetRsp {
    private Long id;
    private Long folderId;
    private String userId;
    private String dataName;
    private String title;
    private Boolean privately;
    private Boolean editable;
    private Integer dataType;
    private Integer createType;
    private String addr;
    private String username;
    private String password;
    private String dbName;
    private String tbName;
    private String createWay;
    private String fields;
    private String schemaConfig;
    private String mapper;
    private String skillConfig;
    private Date createAt;
    private Date updateAt;
}
