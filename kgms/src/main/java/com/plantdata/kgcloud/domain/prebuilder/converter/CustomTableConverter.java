package com.plantdata.kgcloud.domain.prebuilder.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.sdk.rsp.CustomTableRsp;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * @description:
 * @author:
 * @create: 2019-11-09 19:11
 **/
public class CustomTableConverter implements AttributeConverter<List<CustomTableRsp>, String> {

    @Override
    public String convertToDatabaseColumn(List<CustomTableRsp> config) {
        return JacksonUtils.writeValueAsString(config);
    }

    @Override
    public List<CustomTableRsp> convertToEntityAttribute(String config) {
        return JacksonUtils.readValue(config, new TypeReference<List<CustomTableRsp>>(){});
    }
}
