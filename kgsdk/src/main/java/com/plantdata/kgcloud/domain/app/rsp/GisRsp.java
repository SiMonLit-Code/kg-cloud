package com.plantdata.kgcloud.domain.app.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/4 10:31
 */
@ToString
@Getter
@Setter
@ApiModel("gis信息")
public class GisRsp {
    @ApiModelProperty("是否开启地理位置信息 true开启 false 未开启")
    private Boolean openGis;
    @ApiModelProperty("经度")
    private Double lng;
    @ApiModelProperty("纬度度")
    private Double lat;
    @ApiModelProperty("地址名称")
    private String address;
}
