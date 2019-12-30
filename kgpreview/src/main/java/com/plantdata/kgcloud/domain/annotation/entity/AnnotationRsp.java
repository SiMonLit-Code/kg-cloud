package com.plantdata.kgcloud.domain.annotation.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xiezhenxiang 2019/12/28
 */
@ApiModel
@Data
public class AnnotationRsp {

    @ApiModelProperty("实体ID")
    private Long entityId;
    @ApiModelProperty("实体名称")
    private String entityName;
    private Integer type;
    @ApiModelProperty("权重")
    private Double score;
}
