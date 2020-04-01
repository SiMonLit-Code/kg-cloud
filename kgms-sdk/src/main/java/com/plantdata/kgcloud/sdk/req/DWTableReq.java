package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel("数据表创建")

public class DWTableReq {

    @ApiModelProperty("数仓表名称")
    private String title;

    @ApiModelProperty("数仓数据库id")
    private Long dwDataBaseId;

    private String tableName;

    @ApiModelProperty("映射的模式id")
    private Integer modelId;

    private String ktr;

    @ApiModelProperty("数据schema")
    private List<DataSetSchema> schemas;

}
