package com.plantdata.kgcloud.sdk.req.edit;

import com.plantdata.kgcloud.sdk.validator.KeyCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
    @Min(value = 1, message = "不能修改图谱")
    private Long id;

    @ApiModelProperty(hidden = true, value = "0:概念,1:实体", allowableValues = "0,1")
    private Integer type = 0;

    @ApiModelProperty(required = true, value = "概念或实体名称")
    @NotEmpty
    @NotBlank
    @Length(max = 50, message = "概念或实体名称长度不能超过50")
    private String name;

    @ApiModelProperty(value = "消歧标识")
    @Length(max = 100, message = "消歧项长度不能超过100")
    private String meaningTag;

    @ApiModelProperty(value = "唯一标示")
    @KeyCheck
    @Length(max = 50, message = "长度不能超过50")
    private String key;
}
