package com.plantdata.kgcloud.domain.edit.req.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 14:16
 * @Description:
 */
@Data
@ApiModel("实体gis信息修改模型")
public class GisInfoModifyReq {

    @ApiModelProperty(value = "经度")
    @NotNull
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    @NotNull
    private Double latitude;

    @ApiModelProperty(value = "地址")
    @NotBlank
    private String address;
}
