package com.plantdata.kgcloud.domain.common.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 10:35
 **/
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {
    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        return JacksonUtils.writeValueAsString(attribute);
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        return JacksonUtils.readValue(dbData, new TypeReference<List<Integer>>() {
        });
    }
}
