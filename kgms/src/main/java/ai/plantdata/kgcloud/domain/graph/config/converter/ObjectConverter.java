package ai.plantdata.kgcloud.domain.graph.config.converter;

import ai.plantdata.cloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-11 10:06
 **/
public class ObjectConverter implements AttributeConverter<Object, String> {

    @Override
    public String convertToDatabaseColumn(Object o) {
        return JacksonUtils.writeValueAsString(o);
    }

    @Override
    public Object convertToEntityAttribute(String s) {
        return JacksonUtils.readValue(s, Object.class);
    }
}
