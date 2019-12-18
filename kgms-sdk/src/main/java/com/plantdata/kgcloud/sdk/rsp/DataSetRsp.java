package com.plantdata.kgcloud.sdk.rsp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:37
 **/
@Data
@JsonIgnoreProperties({"dbName","tbName","username","password","addr"})
public class DataSetRsp {
    private Long id;
    @ApiModelProperty(value = "文件夹Id")
    private Long folderId;
    private String userId;
    @ApiModelProperty(value = "数据集标识")
    private String dataName;
    @ApiModelProperty(value = "数据集标题")
    private String title;
    @ApiModelProperty(value = "是否私有")
    @JsonProperty("private")
    private Boolean privately;
    @ApiModelProperty(value = "是否可修改")
    @JsonProperty("allowEdit")
    private Boolean editable;
    @ApiModelProperty(value = "数据集类型")
    private Integer dataType;
    @ApiModelProperty(value = "数据集创建类型")
    private Integer createType;
    private String addr;
    private String username;
    private String password;
    private String dbName;
    private String tbName;
    @ApiModelProperty(value = "数据集创建途径")
    private String createWay;
    @ApiModelProperty(value = "列")
    private List<String> fields;
    @ApiModelProperty(value = "列配置")
    private List<DataSetSchema> schema;
    private String mapper;
    private String skillConfig;
    private Date createAt;
    private Date updateAt;
}
