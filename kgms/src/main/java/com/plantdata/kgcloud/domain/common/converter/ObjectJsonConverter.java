package com.plantdata.kgcloud.domain.common.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-12 10:35
 **/
public class ObjectJsonConverter<T> implements AttributeConverter<T, String> {

    @Override
    public String convertToDatabaseColumn(T checkConfig) {
        return JsonUtils.objToJson(checkConfig);
    }

    @Override
    public T convertToEntityAttribute(String s) {
        return JacksonUtils.readValue(s,new TypeReference<T>(){});
    }
}
