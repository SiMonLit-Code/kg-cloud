package com.plantdata.kgcloud.sdk.req;

import com.plantdata.kgcloud.sdk.constant.DataType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("数仓连接配置")
public class DWConnceReq {
    
    @ApiModelProperty("数仓数据库id")
    private Long dwDatabaseId;

    @ApiModelProperty("连接地址，如192.168.4.12:3306")
    private List<String> addr;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("数据库名")
    private String dbName;

    @ApiModelProperty("数据库类型")
    private DataType type;

}
