package com.plantdata.kgcloud.sdk.rsp.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: LinHo
 * @Date: 2019/11/18 14:31
 * @Description:
 */
@Data
@ApiModel("gis查询结果模型")
public class GisVO {

    @ApiModelProperty(value = "是否开启gis")
    private Boolean isOpenGis;

    @ApiModelProperty(value = "经度")
    private Double lng;

    @ApiModelProperty(value = "纬度")
    private Double lat;

    @ApiModelProperty(value = "地点名称")
    private String address;

}
