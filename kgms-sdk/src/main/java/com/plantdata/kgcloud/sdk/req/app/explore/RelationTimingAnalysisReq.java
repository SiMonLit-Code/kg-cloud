package com.plantdata.kgcloud.sdk.req.app.explore;

import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.GraphRelationReqInterface;
import com.plantdata.kgcloud.sdk.req.app.explore.common.GraphTimingReqInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 16:41
 */
@Getter
@Setter
@ApiModel("时序关联分析视图")
public class RelationTimingAnalysisReq extends BasicGraphExploreReq implements GraphRelationReqInterface, GraphTimingReqInterface {

    @ApiModelProperty("关联搜索参数")
    private CommonRelationReq relation;
    @ApiModelProperty("时间参数过滤")
    private TimeFilterExploreReq timeFilters;
    @ApiModelProperty("统计配置")
    private List<BasicStatisticReq> configList;

    @Override
    public CommonRelationReq fetchRelation() {
        return relation;
    }

    @Override
    public TimeFilterExploreReq fetchTimeFilter() {
        return timeFilters;
    }
}
