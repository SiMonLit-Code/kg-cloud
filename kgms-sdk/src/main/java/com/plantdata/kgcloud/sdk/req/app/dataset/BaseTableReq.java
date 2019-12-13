package com.plantdata.kgcloud.sdk.req.app.dataset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 14:20
 */
@Getter
@Setter
public class BaseTableReq extends PageReq {
    @ApiModelProperty("es query条件")
    private String query;
    @ApiModelProperty("要查询的字段")
    private List<String> fields;
    @ApiModelProperty("mongodb 语法")
    private String sort;
}
