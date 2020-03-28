package com.plantdata.kgcloud.domain.dw.rsp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-24 14:58
 **/
@Data
public class GraphMapRsp {

    private Integer id;

    private Date createAt;

    private Date updateAt;

    @ApiModelProperty("模式名称")
    private String modelName;

    @ApiModelProperty("模式概念名称")
    private String entityName;

    @ApiModelProperty("图谱概念名称")
    private String conceptName;

    @ApiModelProperty("图谱概念id")
    private Long conceptId;

    @ApiModelProperty("模式属性名称")
    private String modelAttrName;

    @ApiModelProperty("图谱属性id")
    private Integer attrId;

    @ApiModelProperty("图谱属性名称")
    private String attrName;

    @ApiModelProperty("图谱属性类型 0数值 1对象")
    private Integer attrType;

    @ApiModelProperty("订阅表名")
    private String tableName;

    @ApiModelProperty("订阅库名")
    private String databaseName;

    @ApiModelProperty("订阅库标识")
    private String dataName;

    @ApiModelProperty("订阅开关")
    private Integer schedulingSwitch;
}
