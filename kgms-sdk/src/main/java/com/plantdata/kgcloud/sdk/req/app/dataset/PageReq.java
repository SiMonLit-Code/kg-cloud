package com.plantdata.kgcloud.sdk.req.app.dataset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 15:21
 */
@ApiModel("sdk分页参数")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageReq {
    @ApiModelProperty("查询第几页,默认从1开始")
    @Min(1)
    protected Integer page;
    @ApiModelProperty("每页记录数，默认10条 page=1 size=-1 查询全部")
    protected Integer size;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public int getOffset() {
        return (this.getPage() - 1) * this.getSize();
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public int getLimit() {
        return this.getSize();
    }

    public Integer getPage() {
        return this.page != null && this.page >= 0 ? this.page : 1;
    }

    public Integer getSize() {
        if (this.size != null && this.size > 0) {
            return this.size;
        }
        if (this.size != null && this.size == -1) {
            return Integer.MAX_VALUE - 1;
        }
        return 10;
    }
}
