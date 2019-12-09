package com.plantdata.kgcloud.domain.d2r.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xiezhenxiang 2019/12/9
 */
@Data
@ApiModel("d2r预览参数模型")
public class PreviewReq {

    @NotBlank
    private String kgName;
    @NotBlank @ApiModelProperty(value = "任务配置")
    private String setting;
    @ApiModelProperty(value = "返回实体数量", example = "10")
    private Integer entSize;
    @ApiModelProperty(value = "返回同义词数量", example = "10")
    private Integer synonymSize;
    @ApiModelProperty(value = "返回实体属性数量", example = "10")
    private Integer attrSize;
    @ApiModelProperty(value = "返回实体关系数量", example = "10")
    private Integer relSize;
}
