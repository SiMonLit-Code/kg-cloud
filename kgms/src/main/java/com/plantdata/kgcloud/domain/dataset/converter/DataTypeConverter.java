package com.plantdata.kgcloud.domain.dataset.converter;

import com.plantdata.kgcloud.sdk.constant.DataType;

import javax.persistence.AttributeConverter;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-09 19:11
 **/
public class DataTypeConverter implements AttributeConverter<DataType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DataType attribute) {
        return attribute.getDataType();
    }

    @Override
    public DataType convertToEntityAttribute(Integer dbData) {
        return DataType.findType(dbData);
    }
}
