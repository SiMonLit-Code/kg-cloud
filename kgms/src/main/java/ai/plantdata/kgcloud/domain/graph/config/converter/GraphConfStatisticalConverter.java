package ai.plantdata.kgcloud.domain.graph.config.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.cloud.web.util.ConvertUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import ai.plantdata.kgcloud.domain.graph.config.entity.GraphConfStatistical;
import ai.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;

import java.util.Map;

/**
 * @author jiangdeming
 * @date 2019/12/25  19:47
 */
public class GraphConfStatisticalConverter {

    public static GraphConfStatisticalRsp jsonNodeToMapConverter(GraphConfStatistical graphConfStatistical){
        GraphConfStatisticalRsp rsp =
                ConvertUtils.convert(GraphConfStatisticalRsp.class).apply(graphConfStatistical);
        String strGraphConfReasoning = JacksonUtils.writeValueAsString(graphConfStatistical.getStatisRule());

        Map<String,Object> objectMap = JacksonUtils.readValue(strGraphConfReasoning,new TypeReference<Map<String,Object>>() {
        } );
        rsp.setStatisRule(objectMap);
        return rsp;
    }
}
