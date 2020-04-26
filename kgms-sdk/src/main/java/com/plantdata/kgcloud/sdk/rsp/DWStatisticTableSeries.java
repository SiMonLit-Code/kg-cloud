package com.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @Author: cx
 * @Date: 2020/4/24 20:27
 * @Description:
 */
@Data
@ApiModel("数仓统计分组")
public class DWStatisticTableSeries {

    @ApiModelProperty(value = "分组名")
    private String name;

    @ApiModelProperty(value = "数据")
    private List<Object> data;
}
