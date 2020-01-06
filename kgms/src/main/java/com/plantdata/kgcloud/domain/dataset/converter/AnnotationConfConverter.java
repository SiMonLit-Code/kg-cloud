package com.plantdata.kgcloud.domain.dataset.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.sdk.req.AnnotationConf;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-09 21:58
 **/
public class AnnotationConfConverter implements AttributeConverter<List<AnnotationConf>, String> {
    @Override
    public String convertToDatabaseColumn(List<AnnotationConf> attribute) {
        return JacksonUtils.writeValueAsString(attribute);
    }

    @Override
    public List<AnnotationConf> convertToEntityAttribute(String dbData) {
        return JacksonUtils.readValue(dbData,new TypeReference<List<AnnotationConf>>(){});
    }
}
