package com.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 13:47
 * @Description:
 */
@Data
@ApiModel("摘要更新模型")
public class AbstractModifyReq {

    @ApiModelProperty(required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "摘要")
    private String abs;
}
