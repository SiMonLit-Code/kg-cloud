package com.plantdata.kgcloud.sdk.req.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 11:12
 * @Description:
 */
@Data
@ApiModel("私有数值属性值添加模型")
public class PrivateAttrDataReq {

    @NotNull
    @ApiModelProperty(value = "实体id")
    private Long entityId;

    @ApiModelProperty(value = "0：数值，1：对象", allowableValues = "0,1")
    @NotNull
    @Min(value = 0, message = "数值属性类型")
    @Max(value = 1, message = "对象属性类型")
    private Integer type;

    @NotEmpty
    @ApiModelProperty(value = "私有属性名称")
    private String attrName;

    @NotEmpty
    @ApiModelProperty(value = "私有属性值")
    private String attrValue;
}
