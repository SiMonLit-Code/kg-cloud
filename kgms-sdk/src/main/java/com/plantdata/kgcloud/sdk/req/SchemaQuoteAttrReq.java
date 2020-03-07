package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchemaQuoteAttrReq {

    @ApiModelProperty("属性id")
    private Integer attrId;

    @ApiModelProperty("属性名")
    private String name;

    @ApiModelProperty("属性唯一标识")
    private String attrKey;

    @ApiModelProperty("属性类型")
    private Integer attrType;

    @ApiModelProperty("数值属性类型")
    private Integer dataType;

    @ApiModelProperty("对象属性值域id")
    private Long range;

    @ApiModelProperty("对象属性值域名称")
    private String rangeName;

    @ApiModelProperty("属性别名")
    private String alias;

    @ApiModelProperty("属性单位")
    private String unit;

    @ApiModelProperty("边属性")
    private List<SchemaQuoteRelationAttrReq> relationAttrs;

}
