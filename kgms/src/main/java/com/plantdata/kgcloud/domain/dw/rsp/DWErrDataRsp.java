package com.plantdata.kgcloud.domain.dw.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "数仓错误信息展示")
public class DWErrDataRsp {
    @ApiModelProperty("创建时间")
    private String logTimeStamp;
    @ApiModelProperty("信息详情")
    private String resourceMsg;
    @ApiModelProperty("状态")
    private String level;
    @ApiModelProperty("数据库名")
    private String tableName;
}
