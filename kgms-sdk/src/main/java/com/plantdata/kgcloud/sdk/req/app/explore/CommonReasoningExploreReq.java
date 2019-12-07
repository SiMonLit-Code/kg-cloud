package com.plantdata.kgcloud.sdk.req.app.explore;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.GraphCommonReqInterface;
import com.plantdata.kgcloud.sdk.req.app.explore.common.ReasoningReqInterface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 15:50
 */
@ApiModel("普通图探索参数")
@Getter
@Setter
public class CommonReasoningExploreReq extends BasicGraphExploreReq implements ReasoningReqInterface, GraphCommonReqInterface {

    @ApiModelProperty("通用参数")
    private CommonFiltersReq common;
    @ApiModelProperty("推理规则")
    private Map<Integer, JsonNode> reasoningRuleConfigs;

    @Override
    public List<Long> fetchEntityIdList() {
        return Lists.newArrayList(common.getId());
    }

    @Override
    public Integer fetchDistance() {
        return getDistance();
    }

    @Override
    public Map<Integer, JsonNode> fetchReasonConfig() {
        return reasoningRuleConfigs;
    }

    @Override
    public CommonFiltersReq fetchCommon() {
        return common;
    }
}
