package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-18 11:16
 **/
@Data
public class DateTimeScope {

    @ApiModelProperty("开始时间 时间戳")
    private Long begin;

    @ApiModelProperty("结束时间 时间戳")
    private Long end;
}
