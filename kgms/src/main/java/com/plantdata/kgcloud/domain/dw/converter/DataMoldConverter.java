package com.plantdata.kgcloud.domain.dw.converter;

import com.plantdata.kgcloud.sdk.constant.DWDataFormat;

import javax.persistence.AttributeConverter;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-09 19:11
 **/
public class DataMoldConverter implements AttributeConverter<DWDataFormat, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DWDataFormat attribute) {
        return attribute.getType();
    }

    @Override
    public DWDataFormat convertToEntityAttribute(Integer dbData) {
        return DWDataFormat.findType(dbData);
    }
}
