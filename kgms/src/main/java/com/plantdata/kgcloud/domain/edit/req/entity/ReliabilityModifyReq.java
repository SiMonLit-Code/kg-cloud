package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 14:04
 * @Description:
 */
@Data
@ApiModel("置信度修改模型")
public class ReliabilityModifyReq {

    @ApiModelProperty(value = "实体置信度")
    @DecimalMax(value = "1.0")
    @DecimalMin(value = "0.0")
    private Double reliability;
}
