package com.plantdata.kgcloud.domain.edit.req.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 11:47
 * @Description:
 */
@Data
@ApiModel("领域词典创建模型")
public class DictReq {
    @ApiModelProperty(value = "词性")
    private String nature;

    @NotEmpty(message = "领域词不能为空")
    @NotBlank(message = "领域词不能为空")
    @ApiModelProperty(required = true, value = "领域词")
    @Length(max = 50, message = "领域词长度不能超过50")
    private String name;

    @NotNull(message = "概念id不能为空")
    @ApiModelProperty(required = true, value = "概念id")
    private Long conceptId;

    @ApiModelProperty(value = "实体id")
    private Long entityId;

    @ApiModelProperty(value = "词频")
    private Double frequency;
}
