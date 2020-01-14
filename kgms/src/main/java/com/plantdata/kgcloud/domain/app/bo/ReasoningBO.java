package com.plantdata.kgcloud.domain.app.bo;

import ai.plantdata.kg.api.semantic.req.ReasoningReq;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.sdk.rsp.app.RelationReasonRuleRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/3 13:32
 */
@ToString
public class ReasoningBO {
    private static final int POS = 0;
    private static final int SIZE = 50;
    private Map<Long, GraphConfReasoning> dbConfigMap;
    private Map<Long, JsonNode> configMap;
    @Getter
    private Map<Integer, Long> ruleIdCatchMap;
    @Getter
    private List<RelationReasonRuleRsp> reasonRuleList;

    public ReasoningBO(List<GraphConfReasoning> reasoningList, Map<Long, Object> configMap) {
        this.dbConfigMap = CollectionUtils.isEmpty(reasoningList) ? Collections.emptyMap() : reasoningList.stream().collect(Collectors.toMap(GraphConfReasoning::getId, Function.identity()));
        this.configMap = Maps.newHashMap();
        this.ruleIdCatchMap = Maps.newHashMap();
        configMap.forEach((key, value) -> {
            Optional<JsonNode> jsonNodeOpt = JsonUtils.parseJsonNode(JacksonUtils.writeValueAsString(value));
            jsonNodeOpt.ifPresent(v -> this.configMap.put(key, v));
        });
    }

    public void replaceRuleInfo() {
        this.reasonRuleList = Lists.newArrayListWithCapacity(configMap.size());
        int i = 1;
        for (Long ruleId : configMap.keySet()) {
            JsonNode ruleConfigObject = configMap.get(ruleId);
            GraphConfReasoning ruleBean = dbConfigMap.get(ruleId);
            if (ruleBean == null) {
                continue;
            }
            String ruleConfig = ruleBean.getRuleConfig().toString();

            Iterator<String> iterator = ruleConfigObject.fieldNames();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String pattern = "_" + key + "_";
                if (ruleConfig.contains(pattern)) {
                    ruleConfig = ruleConfig.replace(pattern, ruleConfigObject.get(key).toString());
                }
            }
            RelationReasonRuleRsp ruleObject = JsonUtils.parseObj(ruleConfig, RelationReasonRuleRsp.class);
            if (ruleObject == null) {
                continue;
            }
            //生成临时序号避免规则重复
            ruleObject.setAttrId(i++);
            this.ruleIdCatchMap.put(ruleObject.getAttrId(), ruleId);
            ruleObject.setName(ruleBean.getRuleName());
            reasonRuleList.add(ruleObject);
        }
    }


    public ReasoningReq buildReasoningReq(List<Long> entityIdList) {
        ReasoningReq reasoningReq = new ReasoningReq();
        reasoningReq.setIds(entityIdList);
        reasoningReq.setPos(POS);
        reasoningReq.setSize(SIZE);
        reasoningReq.setRuleConfig(JacksonUtils.writeValueAsString(reasonRuleList));
        return reasoningReq;
    }

}
