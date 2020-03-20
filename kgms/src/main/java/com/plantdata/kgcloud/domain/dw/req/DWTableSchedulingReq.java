package com.plantdata.kgcloud.domain.dw.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("设置数仓表更新频率")
public class DWTableSchedulingReq {

    private Long databaseId;

    private Long tableId;


    @ApiModelProperty("调度开关 1开启 0关闭")
    private Integer schedulingSwitch;

}
