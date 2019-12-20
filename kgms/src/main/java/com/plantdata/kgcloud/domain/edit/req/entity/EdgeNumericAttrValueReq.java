package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/25 15:16
 * @Description:
 */
@Data
@ApiModel("边数值属性值添加或更新模型")
public class EdgeNumericAttrValueReq {

    @NotEmpty
    @ApiModelProperty(value = "关系id")
    private String tripleId;

    @NotNull
    @ApiModelProperty(value = "边id")
    private Integer seqNo;

    @NotNull
    @ApiModelProperty(value = "边数值属性值")
    private String attrValue;
}
