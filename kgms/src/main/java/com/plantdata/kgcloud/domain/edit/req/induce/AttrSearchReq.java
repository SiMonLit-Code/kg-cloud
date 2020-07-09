package com.plantdata.kgcloud.domain.edit.req.induce;

import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kg.api.edit.validator.TypeRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 18:39
 * @Description:
 */
@Data
@ApiModel("规约属性查询模型")
public class AttrSearchReq extends BaseReq {

    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "属性名称,支持模糊查询")
    private String attrName;

    @ApiModelProperty(value = "属性类型,0:数值属性,1:对象属性")
    @TypeRange
    @NotNull
    private Integer type;
}
