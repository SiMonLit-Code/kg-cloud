package com.plantdata.kgcloud.sdk.req.app;

import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 14:28
 */
@Getter
@Setter
@ApiModel("时间筛选相关参数")
public class TimeFilterExploreReq {
    @ApiModelProperty("开始时间")
    private Date fromTime;
    @ApiModelProperty("结束时间")
    private Date toTime;
    @ApiModelProperty("排序方式")
    private String sort = SortTypeEnum.DESC.getName();
    @ApiModelProperty("时间筛选类型，0 不按时间不筛选, 1以节点的时间筛选,  2 以关系的时间筛选, 3 以关系与节点的时间筛选")
    private int timeFilterType = 0;
}
