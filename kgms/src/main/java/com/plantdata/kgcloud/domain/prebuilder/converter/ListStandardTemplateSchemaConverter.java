package com.plantdata.kgcloud.domain.prebuilder.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.domain.prebuilder.rsp.StandardTemplateSchemaRsp;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

public class ListStandardTemplateSchemaConverter implements AttributeConverter<List<StandardTemplateSchemaRsp>, String> {
    @Override
    public String convertToDatabaseColumn(List<StandardTemplateSchemaRsp> standardTemplateSchemas) {
        return JacksonUtils.writeValueAsString(standardTemplateSchemas);
    }

    @Override
    public List<StandardTemplateSchemaRsp> convertToEntityAttribute(String value) {
        if(value == null){
            return new ArrayList<>();
        }

        return JacksonUtils.readValue(value, new TypeReference<List<StandardTemplateSchemaRsp>>() {
        });
    }
}
