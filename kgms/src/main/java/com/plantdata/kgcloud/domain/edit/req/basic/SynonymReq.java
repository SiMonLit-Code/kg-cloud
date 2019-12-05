package com.plantdata.kgcloud.domain.edit.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/22 17:21
 * @Description:
 */
@Data
@ApiModel("同义添加模型")
public class SynonymReq {

    @NotNull
    @ApiModelProperty(value = "概念或实体的ID", required = true)
    private Long id;

    @NotEmpty
    private String name;

}
