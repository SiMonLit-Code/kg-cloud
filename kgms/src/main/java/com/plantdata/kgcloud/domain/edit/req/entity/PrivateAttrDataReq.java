package com.plantdata.kgcloud.domain.edit.req.entity;

import ai.plantdata.kg.api.edit.validator.TypeRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @TypeRange
    private Integer type;

    @NotEmpty
    @ApiModelProperty(value = "私有属性名称")
    private String attrName;

    @NotEmpty
    @ApiModelProperty(value = "私有属性值")
    private String attrValue;
}
