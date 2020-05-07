package com.plantdata.kgcloud.domain.data.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2020/3/24 16:09
 * @Description:
 */
@Setter
@Getter
@ApiModel("数仓数据库与表")
public class DtReq {

    @ApiModelProperty("数据库名称")
    private String dbName;

    @ApiModelProperty("数据库表名称")
    private String dbTable;


    @ApiModelProperty("状态标识 1=已修改 0=错误")
    private int status;


}
