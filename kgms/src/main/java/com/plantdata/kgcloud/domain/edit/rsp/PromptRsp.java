package com.plantdata.kgcloud.domain.edit.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 15:46
 * @Description:
 */
@Data
@ApiModel("概念实体同义提示结果模型")
public class PromptRsp {

    @ApiModelProperty(value = "概念或实体id")
    private Long id;

    @ApiModelProperty(value = "概念或实体名称")
    private String name;

    @ApiModelProperty(value = "同义词")
    private String synonym;

    @ApiModelProperty(value = "消歧标识")
    private String meaningTag;

    @ApiModelProperty(value = "父概念id")
    private Long conceptId;

    @ApiModelProperty(value = "概念、实体、同义")
    private Integer type;
}
