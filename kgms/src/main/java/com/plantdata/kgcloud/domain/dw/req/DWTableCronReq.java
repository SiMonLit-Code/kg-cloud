package com.plantdata.kgcloud.domain.dw.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("设置数仓表更新频率")
public class DWTableCronReq {

    private Long databaseId;

    private Long tableId;


    @ApiModelProperty("增量更新频率")
    private String cron;


    @ApiModelProperty("增量更新字段")
    private String field;


    @ApiModelProperty("是否全量更新 1全量 2增量")
    private Integer isAll;
}
