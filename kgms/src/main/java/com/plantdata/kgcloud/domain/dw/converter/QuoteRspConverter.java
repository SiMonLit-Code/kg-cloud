package com.plantdata.kgcloud.domain.dw.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.sdk.req.SchemaQuoteReq;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;
import java.util.List;

public class QuoteRspConverter implements AttributeConverter<List<SchemaQuoteReq>, String> {
    @Override
    public String convertToDatabaseColumn(List<SchemaQuoteReq> dataMapRspList) {
        return JacksonUtils.writeValueAsString(dataMapRspList);
    }

    @Override
    public List<SchemaQuoteReq> convertToEntityAttribute(String s) {
        return JacksonUtils.readValue(s, new TypeReference<List<SchemaQuoteReq>>() {
        });
    }
}
