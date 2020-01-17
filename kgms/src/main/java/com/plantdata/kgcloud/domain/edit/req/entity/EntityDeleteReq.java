package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 11:30
 * @Description:
 */
@Data
@ApiModel("条件删除实体模型")
public class EntityDeleteReq {

    @ApiModelProperty(value = "概念id")
    @NotNull
    private Long conceptId;

    @ApiModelProperty(value = "是否继承")
    private boolean inherit = false;

    @ApiModelProperty(hidden = true, value = "日志id")
    private String actionId;
}
