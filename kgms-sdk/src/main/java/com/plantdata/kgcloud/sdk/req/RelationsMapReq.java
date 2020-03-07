package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RelationsMapReq {

    @ApiModelProperty("引用的关系属性名")
    private String relationName;

    @ApiModelProperty("图谱中关系属性名称")
    private String kgRelationName;

    @ApiModelProperty("图谱中关系属性id")
    private Integer kgRelationId;

    @ApiModelProperty("边属性")
    private List<AttributesMapReq> attributes;
}
