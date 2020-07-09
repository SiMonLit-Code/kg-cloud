package com.plantdata.kgcloud.domain.prebuilder.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.sdk.rsp.ModelSchemaConfigRsp;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-09 19:11
 **/
public class MoldSchemaConfigConverter implements AttributeConverter<List<ModelSchemaConfigRsp>, String> {

    @Override
    public String convertToDatabaseColumn(List<ModelSchemaConfigRsp> config) {
        return JacksonUtils.writeValueAsString(config);
    }

    @Override
    public List<ModelSchemaConfigRsp> convertToEntityAttribute(String config) {
        return JacksonUtils.readValue(config, new TypeReference<List<ModelSchemaConfigRsp>>(){});
    }
}
