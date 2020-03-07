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

    @ApiModelProperty("增量访问字段")
    private String queryField;

    @ApiModelProperty("访问周期")
    private String cron;

    @ApiModelProperty("数仓数据库id")
    private Long dwDatabaseId;

    @ApiModelProperty("数据schema")
    private List<DataSetSchema> schemas;


}
