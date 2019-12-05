package com.plantdata.kgcloud.domain.app.bo;

import ai.plantdata.kg.api.semantic.req.ReasoningReq;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.dto.RelationReasonRuleDTO;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private Map<Integer, JsonNode> configMap;
    @Getter
    private List<RelationReasonRuleDTO> reasonRuleList;

    public ReasoningBO(List<GraphConfReasoning> reasoningList, Map<Integer, JsonNode> configMap) {
        this.dbConfigMap = CollectionUtils.isEmpty(reasoningList) ? Collections.emptyMap() : reasoningList.stream().collect(Collectors.toMap(GraphConfReasoning::getId, Function.identity()));
        this.configMap = configMap;
    }

    public void replaceRuleInfo() {
        this.reasonRuleList = Lists.newArrayListWithCapacity(configMap.size());
        ObjectMapper instance = JacksonUtils.getInstance();
        for (Integer ruleId : configMap.keySet()) {
            JsonNode ruleConfigObject = configMap.get(ruleId);
            GraphConfReasoning ruleBean = dbConfigMap.get(Long.valueOf(ruleId));
            if (ruleBean == null) {
                continue;
            }
            String ruleConfig = ruleBean.getRuleConfig();

            Iterator<String> iterator = ruleConfigObject.fieldNames();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String pattern = "_" + key + "_";
                if (ruleConfig.contains(pattern)) {
                    ruleConfig = ruleConfig.replace(pattern, ruleConfigObject.get(key).toString());
                }
            }
            RelationReasonRuleDTO ruleObject = null;
            try {
                ruleObject = instance.readValue(ruleConfig, RelationReasonRuleDTO.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ruleObject == null) {
                continue;
            }
            ruleObject.setAttrId(ruleId);
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
