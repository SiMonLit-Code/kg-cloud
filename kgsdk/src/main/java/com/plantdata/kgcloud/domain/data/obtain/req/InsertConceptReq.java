package com.plantdata.kgcloud.domain.data.obtain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 17:43
 */
@Getter
@Setter
@ApiModel("添加概念参数")
public class InsertConceptReq {
    @ApiModelProperty("父概念id")
    @NotNull(message = "父概念不可为空")
    private Long parentId;
    @ApiModelProperty("概念名称")
    @NotBlank
    private String name;
    @ApiModelProperty("唯一标识")
    @Pattern(regexp = "^[a-zA-Z_]$", message = "唯一标识只能为字母和下划线")
    private String key;
    @ApiModelProperty("消歧标识")
    private String meaningTag;
}
