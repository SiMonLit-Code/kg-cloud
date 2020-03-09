package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2020/3/7 23:55
 * @Description:
 */
@Data
@ApiModel("实体关系列表查询")
public class EntityAttrReq {
    @NotNull
    private Long conceptId;
    @NotNull
    private Long entityId;

    @ApiModelProperty("关系属性id")
    private Integer attrId;

    @ApiModelProperty("关系属性私有属性名称")
    private String attrName;

    private Integer page = 1;

    private Integer size = 10;
}
