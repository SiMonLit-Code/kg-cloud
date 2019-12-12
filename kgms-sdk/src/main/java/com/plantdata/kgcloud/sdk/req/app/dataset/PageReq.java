package com.plantdata.kgcloud.sdk.req.app.dataset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 15:21
 */
@ApiModel
@Getter
@Setter
public class PageReq {
    @ApiParam("查询第几页,默认从1开始")
    protected Integer page;
    @ApiParam("每页记录数，默认10条")
    protected Integer size;
}
