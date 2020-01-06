package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/12/6 10:30
 * @Description:
 */
@Data
@ApiModel("三元组查询模型")
public class TripleReq {

    @NotNull
    @ApiModelProperty(value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "属性ids")
    private List<Integer> attrIds;

    @ApiModelProperty(value = "私有属性名称")
    private List<String> attrNames;

    @ApiModelProperty(value = "展示数量")
    private Integer limit = 10;
}
