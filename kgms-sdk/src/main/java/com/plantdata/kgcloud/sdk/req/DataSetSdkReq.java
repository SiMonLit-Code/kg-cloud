package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 16:56
 **/
@Data
public class DataSetSdkReq {

    @ApiModelProperty("数据库名称")
    private String database;

    @ApiModelProperty("表名称")
    private String table;

}
