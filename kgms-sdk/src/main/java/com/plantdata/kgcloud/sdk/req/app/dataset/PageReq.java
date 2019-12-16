package com.plantdata.kgcloud.sdk.req.app.dataset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 15:21
 */
@ApiModel
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageReq {
    @ApiParam("查询第几页,默认从1开始")
    protected Integer page;
    @ApiParam("每页记录数，默认10条")
    protected Integer size;

    @JsonIgnore
    @ApiParam(hidden = true)
    public int getOffset() {
        return (this.getPage() - 1) * this.getSize();
    }

    @JsonIgnore
    @ApiParam(hidden = true)
    public int getLimit() {
        return this.getSize();
    }

    public Integer getPage() {
        return this.page != null && this.page >= 0 ? this.page : 1;
    }

    public Integer getSize() {
        return this.size != null && this.size > 0 ? this.size : 10;
    }
}
