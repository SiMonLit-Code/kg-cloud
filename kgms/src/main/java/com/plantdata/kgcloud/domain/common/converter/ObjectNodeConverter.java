package com.plantdata.kgcloud.domain.common.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.persistence.AttributeConverter;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-11 10:06
 **/
public class ObjectNodeConverter implements AttributeConverter<ObjectNode, String> {
    @Override
    public String convertToDatabaseColumn(ObjectNode attribute) {
        return JacksonUtils.writeValueAsString(attribute);
    }

    @Override
    public ObjectNode convertToEntityAttribute(String dbData) {
        return JacksonUtils.readValue(dbData, ObjectNode.class);
    }
}
