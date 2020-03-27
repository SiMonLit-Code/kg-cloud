package com.plantdata.kgcloud.domain.dw.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.sdk.req.DataMapReq;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;
import java.util.List;

public class DataMapRspConverter implements AttributeConverter<List<DataMapReq>, String> {
    @Override
    public String convertToDatabaseColumn(List<DataMapReq> dataMapRspList) {
        return JacksonUtils.writeValueAsString(dataMapRspList);
    }

    @Override
    public List<DataMapReq> convertToEntityAttribute(String s) {
        return JacksonUtils.readValue(s, new TypeReference<List<DataMapReq>>() {
        });
    }
}
