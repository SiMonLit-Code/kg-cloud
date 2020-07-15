package ai.plantdata.kgcloud.plantdata.req.explore.function;

import com.alibaba.fastjson.JSONObject;
import ai.plantdata.kgcloud.plantdata.req.reason.RelationReasoningBean;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface RuleGraphParameter {
    Map<Long, JSONObject> getReasoningRuleConfigs();

    List<RelationReasoningBean> getRule();

    void setRule(List<RelationReasoningBean> reasoningRuleConfigs);
}
