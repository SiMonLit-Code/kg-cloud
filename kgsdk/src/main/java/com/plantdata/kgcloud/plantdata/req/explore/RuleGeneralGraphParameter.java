package com.plantdata.kgcloud.plantdata.req.explore;

import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.plantdata.req.explore.function.RuleGraphParameter;
import com.plantdata.kgcloud.plantdata.req.reason.RelationReasoningBean;
import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * 普通图探索类
 *
 * @author Administrator
 */
@Data
public class RuleGeneralGraphParameter extends TimeGeneralGraphParameter implements RuleGraphParameter {

    private List<RelationReasoningBean> rule;
    private Map<Long, JSONObject> reasoningRuleConfigs;

}
