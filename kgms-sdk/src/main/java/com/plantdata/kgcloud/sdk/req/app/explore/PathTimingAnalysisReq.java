package com.plantdata.kgcloud.sdk.req.app.explore;

import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 13:37
 */
@Getter
@Setter
@ApiModel("时序路径分析参数")
public class PathTimingAnalysisReq extends BasicGraphExploreReq {
    @ApiModelProperty("统计配置")
    private List<BasicStatisticReq> configList;
    @ApiModelProperty("路径")
    private CommonPathReq path;
    @ApiModelProperty("时间参数过滤")
    private TimeFilterExploreReq timeFilters;
}
