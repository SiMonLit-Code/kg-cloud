package com.plantdata.kgcloud.sdk.req.app.explore;

import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReqList;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonRelationReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphRelationReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.ReasoningReqInterface;
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
 * @date 2019/12/4 14:30
 */
@Getter
@Setter
@ApiModel("关联推理分析-参数")
public class RelationReasoningAnalysisReqList extends BasicGraphExploreReqList implements ReasoningReqInterface, GraphRelationReqInterface {
    @ApiModelProperty(value = "关联搜索参数", required = true)
    @NotNull
    @Valid
    private CommonRelationReq relation;
    @ApiModelProperty("推理规则")
    private Map<Long, Object> reasoningRuleConfigs;
    @ApiModelProperty("统计配置")
    private List<BasicStatisticReq> configList;

    @Override
    public List<Long> fetchEntityIdList() {
        return relation.getIds();
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
    public CommonRelationReq fetchRelation() {
        return relation;
    }
}
