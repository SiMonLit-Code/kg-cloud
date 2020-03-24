package com.plantdata.kgcloud.domain.data.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2020/3/24 16:07
 * @Description:
 */
@Setter
@Getter
@ApiModel("数仓数据返回结果")
public class DataStoreRsp {

    @ApiModelProperty("数据id")
    private String id;

    @ApiModelProperty("数据库名称")
    private String dbName;

    @ApiModelProperty("数据库表名称")
    private String dbTable;

    @ApiModelProperty("数据")
    private Map<String, Object> data;

    @ApiModelProperty("表字段")
    private String fields;

    @ApiModelProperty("数据状态")
    private String status;

    @ApiModelProperty("错误原因")
    private String errorReason;
}
