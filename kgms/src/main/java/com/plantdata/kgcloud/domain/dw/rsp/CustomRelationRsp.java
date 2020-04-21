package com.plantdata.kgcloud.domain.dw.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-20 18:54
 **/
@Data
@ApiModel("自定义打标-关系")
public class CustomRelationRsp {

    @ApiModelProperty("关系名")
    private String name;

    @ApiModelProperty("定义域")
    private String domain;

    @ApiModelProperty("值域")
    private List<String> range;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("边属性")
    private List<CustomColumnRsp> relationAttrs;

}
