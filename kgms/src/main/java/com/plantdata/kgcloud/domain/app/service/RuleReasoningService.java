package com.plantdata.kgcloud.domain.app.service;

import ai.plantdata.kg.api.pub.resp.GraphVO;
import com.plantdata.kgcloud.sdk.req.app.explore.common.ReasoningReqInterface;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/3 15:18
 */
public interface RuleReasoningService {

    GraphVO rebuildByRuleReason(String kgName, GraphVO graphVO,  ReasoningReqInterface reasoningParam);
}
