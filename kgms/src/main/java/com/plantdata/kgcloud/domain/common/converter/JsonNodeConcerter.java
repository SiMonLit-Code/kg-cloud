package com.plantdata.kgcloud.domain.common.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;

/**
 * Created by plantdata-1007 on 2019/12/6.
 */
public class JsonNodeConcerter implements AttributeConverter<JsonNode,String> {
    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        return JacksonUtils.writeValueAsString(attribute);
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        return JacksonUtils.readValue(dbData, JsonNode.class);
    }
}
