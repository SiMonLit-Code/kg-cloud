package com.plantdata.kgcloud.sdk.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 13:49
 */
@Getter
@Setter
@ApiModel("按照table统计数据集参数")
public class TableStatisticByDimensionalReq extends StatisticByDimensionalReq {

    private List<DataBaseReq> dataBaseList;

    @Getter
    @Setter
    public static class DataBaseReq {
        @ApiModelProperty("数据集库名对应es中的index")
        private String database;
        @ApiModelProperty("数据集表名对应es中的type")
        private String table;
    }
}
