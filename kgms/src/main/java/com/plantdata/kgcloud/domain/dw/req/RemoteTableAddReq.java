package com.plantdata.kgcloud.domain.dw.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RemoteTableAddReq {

    @ApiModelProperty("表名称")
    private String tbName;

    @ApiModelProperty("映射已有表的名称")
    private String tableName;

    @ApiModelProperty("映射表的模式id")
    private Integer modelId;
}
