package com.plantdata.kgcloud.sdk.req.app.explore;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonPathReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.ReasoningReqInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
@ApiModel("路径分析推理参数")
public class PathReasoningAnalysisReq extends BasicGraphExploreReq implements ReasoningReqInterface {

    @NotNull
    @ApiModelProperty(value = "路径参数", required = true)
    private CommonPathReq path;
    @ApiModelProperty("时间过滤参数")
    private List<TimeFilterExploreReq> timeFilters;
    @ApiModelProperty("推理规则")
    private Map<Integer, JsonNode> reasoningRuleConfigs;
    @ApiModelProperty("统计配置")
    private List<BasicStatisticReq> configList;

    @Override
    public List<Long> fetchEntityIdList() {
        return Lists.newArrayList(path.getStart(), path.getEnd());
    }

    @Override
    public Integer fetchDistance() {
        return path.getDistance();
    }

    @Override
    public Map<Integer, JsonNode> fetchReasonConfig() {
        return reasoningRuleConfigs;
    }
}
