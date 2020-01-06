package com.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/12/9 19:00
 * @Description:
 */
@Data
@ApiModel("修改父概念模型")
public class ConceptReplaceReq {
    @ApiModelProperty(required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "父概念id", required = true)
    @NotNull
    private Long conceptId;
}
