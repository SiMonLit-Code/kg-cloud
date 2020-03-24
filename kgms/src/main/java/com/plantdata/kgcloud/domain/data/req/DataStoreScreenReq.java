package com.plantdata.kgcloud.domain.data.req;

import com.plantdata.kgcloud.bean.BaseReq;
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
@ApiModel("数仓筛选")
public class DataStoreScreenReq extends BaseReq {

    @ApiModelProperty("数据库名称")
    private String dbName;

    @ApiModelProperty("数据库表名称")
    private String dbTable;
}
