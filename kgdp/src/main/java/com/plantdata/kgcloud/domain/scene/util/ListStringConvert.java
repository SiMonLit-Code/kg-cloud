package com.plantdata.kgcloud.domain.scene.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter(autoApply = true)
public class ListStringConvert implements AttributeConverter<List<String>,String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return new Gson().fromJson(dbData, new TypeToken<List<String>>(){}.getType());
    }
}
