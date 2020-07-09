package com.plantdata.kgcloud.domain.graph.config.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.cloud.web.util.ConvertUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;

import java.util.Map;

/**
 * @author jiangdeming
 * @date 2019/12/25  17:15
 */
public class GraphConfReasoningConverter {
    public static GraphConfReasonRsp jsonNodeToMapConverter(GraphConfReasoning graphConfReasoning){
        GraphConfReasonRsp rsp =
                ConvertUtils.convert(GraphConfReasonRsp.class).apply(graphConfReasoning);
        String strGraphConfReasoning = JacksonUtils.writeValueAsString(graphConfReasoning.getRuleConfig());

        Map<String,Object> objectMap = JacksonUtils.readValue(strGraphConfReasoning,new TypeReference<Map<String,Object>>() {
        } );
        rsp.setRuleConfig(objectMap);
        return rsp;
    }
}
