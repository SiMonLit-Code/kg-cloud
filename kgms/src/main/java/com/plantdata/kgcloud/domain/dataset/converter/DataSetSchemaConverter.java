package com.plantdata.kgcloud.domain.dataset.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;
import java.util.List;

public class DataSetSchemaConverter implements AttributeConverter<List<DataSetSchema>, String> {
    @Override
    public String convertToDatabaseColumn(List<DataSetSchema> dataSetSchemas) {
        return JacksonUtils.writeValueAsString(dataSetSchemas);
    }

    @Override
    public List<DataSetSchema> convertToEntityAttribute(String s) {
        return JacksonUtils.readValue(s, new TypeReference<List<DataSetSchema>>() {
        });
    }
}
