package com.plantdata.kgcloud.sdk.req.app.explore;

import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphCommonReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.GraphTimingReqInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 11:02
 */
@ApiModel("时序图探索")
@Getter
@Setter
public class CommonTimingExploreReq extends BasicGraphExploreReq implements GraphCommonReqInterface, GraphTimingReqInterface {
    @NotNull
    @ApiModelProperty("普通参数")
    private CommonFiltersReq common;
    @ApiModelProperty("时间参数过滤")
    private TimeFilterExploreReq timeFilters;

    @Override
    public CommonFiltersReq fetchCommon() {
        return common;
    }

    @Override
    public TimeFilterExploreReq fetchTimeFilter() {
        return timeFilters;
    }
}
