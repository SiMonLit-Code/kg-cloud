package com.plantdata.kgcloud.domain.edit.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.persistence.AttributeConverter;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 21:47
 * @Description:
 */
public class JpaMapConverter implements AttributeConverter<Map<String,Object>,String> {
    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        return JacksonUtils.writeValueAsString(attribute);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        return JacksonUtils.readValue(dbData,new TypeReference<Map<String,Object>>(){});
    }
}
