package com.plantdata.kgcloud.sdk.req.edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author: LinHo
 * @Date: 2019/11/16 18:31
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("概念或实体创建模型")
public class BasicInfoReq {

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
