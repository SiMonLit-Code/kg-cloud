package ai.plantdata.kgcloud.domain.common.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 10:35
 **/
public class JsonObjectConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> checkConfig) {
        if (checkConfig == null) {
            return JacksonUtils.writeValueAsString(Collections.emptyMap());
        }
        return JsonUtils.objToJson(checkConfig);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String s) {
        if (!StringUtils.hasText(s)) {
            return new HashMap<>();
        }
        return JacksonUtils.readValue(s, new TypeReference<Map<String, Object>>() {
        });
    }
}
