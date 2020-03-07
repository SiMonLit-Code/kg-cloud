package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttributesMapReq {

    @ApiModelProperty("引入的模式里属性名称")
    private String attrName;

    @ApiModelProperty("图谱中属性名")
    private String kgAttrName;

    @ApiModelProperty("图谱中属性id")
    private Integer kgAttrId;
}
