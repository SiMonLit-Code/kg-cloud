package com.plantdata.kgcloud.domain.common.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;
import java.util.List;

public class StringListConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return JacksonUtils.writeValueAsString(strings);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return JacksonUtils.readValue(s, new TypeReference<List<String>>() {
        });
    }
}
