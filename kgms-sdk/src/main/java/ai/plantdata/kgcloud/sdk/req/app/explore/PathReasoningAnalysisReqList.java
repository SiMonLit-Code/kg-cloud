package ai.plantdata.kgcloud.sdk.req.app.explore;

import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import ai.plantdata.kgcloud.sdk.req.app.function.GraphPathReqInterface;
import ai.plantdata.kgcloud.sdk.req.app.function.GraphTimingReqInterface;
import ai.plantdata.kgcloud.sdk.req.app.function.ReasoningReqInterface;
import ai.plantdata.kgcloud.sdk.req.app.function.SecondaryScreeningInterface;
import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 13:59
 */
@Getter
@Setter
@ApiModel("路径分析推理-参数")
public class PathReasoningAnalysisReqList extends BasicGraphExploreReqList implements ReasoningReqInterface, GraphPathReqInterface, GraphTimingReqInterface, SecondaryScreeningInterface {

    @NotNull
    @Valid
    @ApiModelProperty(value = "路径参数", required = true)
    private CommonPathReq path;
    @ApiModelProperty("时间过滤参数")
    private TimeFilterExploreReq timeFilters;
    @ApiModelProperty("推理规则")
    private Map<Long, Object> reasoningRuleConfigs;
    @ApiModelProperty("统计配置")
    private List<BasicStatisticReq> configList;

    @Override
    public List<Long> fetchEntityIdList() {
        return Lists.newArrayList(path.getStart(), path.getEnd());
    }

    @Override
    public Integer fetchDistance() {
        return getDistance();
    }

    @Override
    public Map<Long, Object> fetchReasonConfig() {
        return reasoningRuleConfigs;
    }

    @Override
    public CommonPathReq fetchPath() {
        return path;
    }

    @Override
    public TimeFilterExploreReq fetchTimeFilter() {
        return timeFilters;
    }
}
