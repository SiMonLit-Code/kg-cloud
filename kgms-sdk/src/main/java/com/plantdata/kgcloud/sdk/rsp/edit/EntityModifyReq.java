package com.plantdata.kgcloud.sdk.rsp.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author: LinHo
 * @Date: 2019/11/16 18:31
 * @Description:
 */
@Data
@ApiModel("实体修改模型")
public class EntityModifyReq {

    @ApiModelProperty(required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "所属概念ID", required = true)
    @NotNull
    private Long conceptId;

    @ApiModelProperty(value = "0:概念,1:实体", allowableValues = "0,1", required = true)
    @NotNull
    private Integer type;

    @ApiModelProperty(required = true, value = "概念或实体名称")
    @NotEmpty
    @Length(max = 50)
    private String name;

    @ApiModelProperty(value = "消歧标识")
    private String meaningTag;

}
