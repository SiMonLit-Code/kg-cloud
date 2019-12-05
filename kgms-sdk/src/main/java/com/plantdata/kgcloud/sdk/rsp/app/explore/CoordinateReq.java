package com.plantdata.kgcloud.sdk.rsp.app.explore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
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
public class CoordinateReq {
    @ApiModelProperty("x轴")
    private BigDecimal x;
    @ApiModelProperty("y轴")
    private BigDecimal y;
}
