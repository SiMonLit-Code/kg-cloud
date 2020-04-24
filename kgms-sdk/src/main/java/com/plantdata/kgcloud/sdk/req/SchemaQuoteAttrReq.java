package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.sdk.rsp.ModelRangeRsp;
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

    @ApiModelProperty("原模式属性id")
    private Integer modelAttrId;

    @ApiModelProperty("属性id")
    private Integer attrId;

    @ApiModelProperty("属性名")
    private String attrName;

    @ApiModelProperty("属性唯一标识")
    private String attrKey;

    @ApiModelProperty("属性类型")
    private Integer attrType;

    @ApiModelProperty("数值属性类型")
    private Integer dataType;

    @ApiModelProperty("原模式对象属性值域")
    private List<Integer> modelRange;

    @ApiModelProperty("对象属性值域")
    private List<ModelRangeRsp> range;

    @ApiModelProperty("属性别名")
    private String alias;

    @ApiModelProperty("属性单位")
    private String unit;

    @ApiModelProperty("边属性")
    private List<SchemaQuoteRelationAttrReq> relationAttrs;


    @ApiModelProperty("映射的表名")
    private List<String> tables;

    @ApiModelProperty("映射的模式id")
    private Integer modelId;

}
