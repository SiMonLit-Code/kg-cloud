package com.plantdata.kgcloud.domain.dw.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-24 17:49
 **/
@Data
public class GraphMapReq {

    @ApiModelProperty("图谱名")
    private String kgName;

    @ApiModelProperty("订阅表名")
    private String tableName;

    @ApiModelProperty("订阅库id")
    private Long databaseId;

    @ApiModelProperty("图谱概念id")
    private Long conceptId;

    @ApiModelProperty("图谱属性id")
    private Integer attrId;

    @ApiModelProperty("订阅状态")
    private Integer status;


}
