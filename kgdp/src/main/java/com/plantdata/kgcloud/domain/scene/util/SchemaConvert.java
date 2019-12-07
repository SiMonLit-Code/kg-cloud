package com.plantdata.kgcloud.domain.scene.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SchemaConvert implements AttributeConverter<SchemaRsp,String> {


    @Override
    public String convertToDatabaseColumn(SchemaRsp attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public SchemaRsp convertToEntityAttribute(String dbData) {

        return new Gson().fromJson(dbData, SchemaRsp.class);
    }
}
