package com.plantdata.kgcloud.domain.data.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@ApiModel("数仓数据错误数据更改")
public class DataStoreReq {

    @ApiModelProperty("数据id")
    private String id;

    @ApiModelProperty("数据库名称")
    private String dbName;

    @ApiModelProperty("数据库表名称")
    private String dbTable;

    @ApiModelProperty("数据")
    private Map<String, Object> data;


}
