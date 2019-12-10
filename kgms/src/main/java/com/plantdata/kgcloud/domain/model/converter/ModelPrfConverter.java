package com.plantdata.kgcloud.domain.model.converter;

import com.plantdata.kgcloud.sdk.req.ModelPrf;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 11:31
 **/
public class ModelPrfConverter implements AttributeConverter<ModelPrf,String> {
    @Override
    public String convertToDatabaseColumn(ModelPrf attribute) {
        return JacksonUtils.writeValueAsString(attribute);
    }

    @Override
    public ModelPrf convertToEntityAttribute(String dbData) {
        return JacksonUtils.readValue(dbData,ModelPrf.class);
    }
}
