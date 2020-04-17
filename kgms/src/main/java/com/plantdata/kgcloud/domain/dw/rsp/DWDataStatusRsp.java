package com.plantdata.kgcloud.domain.dw.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "数仓信息日志状态")
public class DWDataStatusRsp {
    @ApiModelProperty("创建时间")
    private String logTimeStamp;
    @ApiModelProperty("状态")
    private String level;
    @ApiModelProperty("数据库名")
    private String tableName;
}
