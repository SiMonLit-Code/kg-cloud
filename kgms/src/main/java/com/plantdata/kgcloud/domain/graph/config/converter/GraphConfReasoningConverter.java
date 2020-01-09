package com.plantdata.kgcloud.domain.graph.config.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;

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
