package com.plantdata.kgcloud.sdk.req.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 13:47
 * @Description:
 */
@Data
@ApiModel("概念或实体修改模型")
public class BasicInfoModifyReq {

    @ApiModelProperty(required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(hidden = true, value = "0:概念,1:实体", allowableValues = "0,1")
    private Integer type = 0;

    @ApiModelProperty(required = true, value = "概念或实体名称")
    @NotEmpty
    @Length(max = 50)
    private String name;

    @ApiModelProperty(value = "消歧标识")
    private String meaningTag;

    @ApiModelProperty(value = "唯一标示")
    private String key;
}
