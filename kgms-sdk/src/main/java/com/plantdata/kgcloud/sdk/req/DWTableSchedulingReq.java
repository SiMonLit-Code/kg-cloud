package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("设置数仓表更新频率")
public class DWTableSchedulingReq {


    @ApiModelProperty("数仓库名")
    private Long databaseId;

    @ApiModelProperty("数仓表名")
    private Long tableId;

    @ApiModelProperty("es Index")
    private String target;

    @ApiModelProperty("搜索组件任务名称")
    private String resourceName;

    @ApiModelProperty("调度开关 1开启 0关闭")
    private Integer schedulingSwitch;

}
