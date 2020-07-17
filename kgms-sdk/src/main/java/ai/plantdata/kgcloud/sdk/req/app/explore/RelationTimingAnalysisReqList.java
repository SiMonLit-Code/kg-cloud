package ai.plantdata.kgcloud.sdk.req.app.explore;

import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import ai.plantdata.kgcloud.sdk.req.app.function.GraphRelationReqInterface;
import ai.plantdata.kgcloud.sdk.req.app.function.GraphTimingReqInterface;
import ai.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 16:41
 */
@Getter
@Setter
@ApiModel("时序关联分析-参数")
public class RelationTimingAnalysisReqList extends BasicGraphExploreReqList implements GraphRelationReqInterface, GraphTimingReqInterface {

    @NotNull
    @Valid
    @ApiModelProperty(value = "关联搜索参数", required = true)
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
