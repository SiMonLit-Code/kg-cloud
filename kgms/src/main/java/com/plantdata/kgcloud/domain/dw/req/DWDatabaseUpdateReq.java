package com.plantdata.kgcloud.domain.dw.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel("数仓表数据更改")
public class DWDatabaseUpdateReq {

    @ApiModelProperty("数仓id")
    private Long dataBaseId;

    @ApiModelProperty("表id")
    private Long tableId;
    @ApiModelProperty("数据id")
    private String id;

    @ApiModelProperty("数据")
    private Map<String, Object> data;


}
