package com.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author cjw
 * @version 1.0
 * @date 2020/1/9 10:09
 */
@Getter
@Setter
@ApiModel("实体名称和消歧标识")
public class EntityQueryWithConditionReq {
    @ApiModelProperty(value = "实体名称", required = true)
    @NotBlank
    private String name;
    @ApiModelProperty(value = "消歧标识", required = true)
    @NotBlank
    private String meaningTag;
}
