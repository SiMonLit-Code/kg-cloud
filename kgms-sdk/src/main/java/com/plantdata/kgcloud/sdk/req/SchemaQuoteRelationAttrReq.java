package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchemaQuoteRelationAttrReq {

    @ApiModelProperty("边属性id")
    private Integer attrId;

    @ApiModelProperty("边属性名称")
    private String name;

    @ApiModelProperty("边属性类型")
    private Integer dataType;

    @ApiModelProperty("边属性单位")
    private String unit;
}
