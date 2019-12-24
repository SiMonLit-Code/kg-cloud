package com.plantdata.kgcloud.plantdata.req.explore.function;

import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.plantdata.req.reason.RelationReasoningBean;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface RuleGraphParameter {
    Map<Integer, JSONObject>  getReasoningRuleConfigs();

    void setReasoningRuleConfigs(Map<Integer, JSONObject>  reasoningRuleConfigs);


    List<RelationReasoningBean> getRule();

    void setRule(List<RelationReasoningBean> reasoningRuleConfigs);
}
