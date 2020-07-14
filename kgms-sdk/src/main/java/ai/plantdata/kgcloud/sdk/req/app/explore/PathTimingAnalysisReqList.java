package ai.plantdata.kgcloud.sdk.req.app.explore;

import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import ai.plantdata.kgcloud.sdk.req.app.function.GraphPathReqInterface;
import ai.plantdata.kgcloud.sdk.req.app.function.GraphTimingReqInterface;
import ai.plantdata.kgcloud.sdk.req.app.function.SecondaryScreeningInterface;
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
 * @date 2019/12/4 13:37
 */
@Getter
@Setter
@ApiModel("时序路径分析-参数")
public class PathTimingAnalysisReqList extends BasicGraphExploreReqList implements GraphPathReqInterface, GraphTimingReqInterface, SecondaryScreeningInterface {
    @ApiModelProperty("统计配置")
    private List<BasicStatisticReq> configList;
    @NotNull
    @Valid
    @ApiModelProperty(value = "路径", required = true)
    private CommonPathReq path;
    @ApiModelProperty("时间参数过滤")
    private TimeFilterExploreReq timeFilters;

    @Override
    public CommonPathReq fetchPath() {
        return path;
    }

    @Override
    public TimeFilterExploreReq fetchTimeFilter() {
        return timeFilters;
    }
}
