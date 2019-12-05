package com.plantdata.kgcloud.domain.common.converter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-11 10:10
 **/
public class ArrayNodeConcerter implements AttributeConverter<ArrayNode, String> {
    @Override
    public String convertToDatabaseColumn(ArrayNode attribute) {
        return JacksonUtils.writeValueAsString(attribute);
    }

    @Override
    public ArrayNode convertToEntityAttribute(String dbData) {
        return JacksonUtils.readValue(dbData, ArrayNode.class);
    }
}
