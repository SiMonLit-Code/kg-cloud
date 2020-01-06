package com.plantdata.kgcloud.sdk.rsp.edit;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 14:27
 * @Description:
 */
@Data
@ApiModel("概念或实体的查询结果模型")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicInfoVO {
    /**
     * 概念和实体的ID
     */
    @ApiModelProperty(value = "概念或实体id")
    private Long id;

    @ApiModelProperty(value = "父或概念id")
    private Long conceptId;

    @ApiModelProperty(value = "概念或实体名称")
    private String name;

    /**
     * 0:概念
     * 1:实体
     * 2:同义词
     */
    @ApiModelProperty(value = "0:概念,1:实体,2:同义词")
    private Integer type;

    @ApiModelProperty(value = "摘要")
    private String abs;

    @ApiModelProperty(value = "图片路径")
    private String imageUrl;

    @ApiModelProperty(value = "消歧标识")
    private String meaningTag;
    /**
     * 全局唯一
     */
    @ApiModelProperty(value = "唯一标示")
    private String key;
}
