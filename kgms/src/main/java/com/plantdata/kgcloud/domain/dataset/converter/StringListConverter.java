package com.plantdata.kgcloud.domain.dataset.converter;

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
        return JacksonUtils.readValue(s, List.class);
    }
}
