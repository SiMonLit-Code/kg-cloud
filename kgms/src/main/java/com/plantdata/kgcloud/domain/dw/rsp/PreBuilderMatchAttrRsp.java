package com.plantdata.kgcloud.domain.dw.rsp;

import com.plantdata.kgcloud.sdk.constant.DataType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PreBuilderMatchAttrRsp {

    @ApiModelProperty("模式库属性id")
    private Integer id;

    @ApiModelProperty("属性名称")
    private String name;

    @ApiModelProperty("属性id")
    private Integer attrId;

    @ApiModelProperty("概念名称")
    private String conceptName;

    @ApiModelProperty("概念id")
    private Integer conceptId;

    @ApiModelProperty("属性类型")
    private Integer attrType;

    @ApiModelProperty("属性唯一标识")
    private String attrKey;

    @ApiModelProperty("数值属性类型")
    private Integer dataType;

    @ApiModelProperty("对象属性值域id")
    private Integer range;

    @ApiModelProperty("对象属性值域名称")
    private String rangeName;

    @ApiModelProperty("属性别名")
    private String alias;

    @ApiModelProperty("属性单位")
    private String unit;

    @ApiModelProperty("属性匹配状态")
    private String attrMatchStatus;

    @ApiModelProperty("边属性")
    private List<PreBuilderRelationAttrRsp> relationAttrs;

    @ApiModelProperty("模式id")
    private Integer modelId;

    @ApiModelProperty("属性匹配状态 0概念未引入 1已引入 2冲突 3可引入")
    private Integer matchStatus;


}
