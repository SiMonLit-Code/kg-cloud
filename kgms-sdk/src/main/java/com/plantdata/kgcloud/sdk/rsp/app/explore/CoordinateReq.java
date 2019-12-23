package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 16:23
 */
@Getter
@Setter
@ApiModel("坐标信息")
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateReq {
    @ApiModelProperty("x轴")
    private Double x;
    @ApiModelProperty("y轴")
    private Double y;
}
