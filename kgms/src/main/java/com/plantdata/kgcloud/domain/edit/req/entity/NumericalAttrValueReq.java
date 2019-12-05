package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 10:35
 * @Description:
 */
@Data
@ApiModel("数值属性值更新模型")
public class NumericalAttrValueReq {

    @NotNull
    @ApiModelProperty(value = "实体id")
    private Long entityId;

    @NotNull
    @ApiModelProperty(value = "属性id")
    private Integer attrId;

    @NotNull
    @ApiModelProperty(value = "属性值")
    private String attrValue;

}
