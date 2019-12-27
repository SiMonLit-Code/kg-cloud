package com.plantdata.kgcloud.domain.app.service;

import ai.plantdata.kg.api.pub.resp.GraphVO;
import com.plantdata.kgcloud.sdk.req.app.function.ReasoningReqInterface;
import com.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/3 15:18
 */
public interface RuleReasoningService {

    /**
     * 推理规则
     *
     * @param kgName         图谱名称
     * @param graphVO        GraphVO
     * @param reasoningParam ReasoningReqInterface
     * @return GraphVO
     */
    GraphVO rebuildByRuleReason(String kgName, GraphVO graphVO, ReasoningReqInterface reasoningParam);

    /**
     * 推理规则生成
     *
     * @param configMap k->id v->配置
     * @return List<RelationReasonRuleRsp>
     */
    List<RelationReasonRuleRsp> generateReasoningRule(Map<Long, Object> configMap);
}
