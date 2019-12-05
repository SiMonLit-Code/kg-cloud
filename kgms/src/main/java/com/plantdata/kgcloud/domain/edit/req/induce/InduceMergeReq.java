package com.plantdata.kgcloud.domain.edit.req.induce;

import ai.plantdata.kg.api.edit.validator.TypeRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 19:04
 * @Description:
 */
@Data
@ApiModel("属性合并模型")
public class InduceMergeReq {

    @ApiModelProperty(value = "合并目标属性id")
    private Integer attributeId;

    @ApiModelProperty(value = "属性类型， 0 数值属性，1 对象属性")
    @TypeRange
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "待合并的属性ids")
    private List<Integer> srcAttrIds;
}
