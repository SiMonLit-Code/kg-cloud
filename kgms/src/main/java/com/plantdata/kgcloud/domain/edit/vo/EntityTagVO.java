package com.plantdata.kgcloud.domain.edit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 14:31
 * @Description:
 */
@Data
@ApiModel("实体标签查询结果模型")
public class EntityTagVO {

    @ApiModelProperty(value = "标签名称")
    private String name;

    @ApiModelProperty(value = "标签来源")
    private String source;

    @ApiModelProperty(value = "标签创建时间")
    private String creationTime;

    @ApiModelProperty(value = "等级,1-5")
    private Integer grade;
}
