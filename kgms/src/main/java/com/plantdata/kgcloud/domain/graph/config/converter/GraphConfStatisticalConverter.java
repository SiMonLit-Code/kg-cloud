package com.plantdata.kgcloud.domain.graph.config.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfReasoning;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfStatistical;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;

import java.util.Map;

/**
 * @author jiangdeming
 * @date 2019/12/25  19:47
 */
public class GraphConfStatisticalConverter {

    public static GraphConfStatisticalRsp JsonNodeToMapConverter(GraphConfStatistical graphConfStatistical){
        GraphConfStatisticalRsp rsp =
                ConvertUtils.convert(GraphConfStatisticalRsp.class).apply(graphConfStatistical);
        String strGraphConfReasoning = JacksonUtils.writeValueAsString(graphConfStatistical.getStatisRule());

        Map<String,Object> objectMap = JacksonUtils.readValue(strGraphConfReasoning,new TypeReference<Map<String,Object>>() {
        } );
        rsp.setStatisRule(objectMap);
        return rsp;
    }
}
