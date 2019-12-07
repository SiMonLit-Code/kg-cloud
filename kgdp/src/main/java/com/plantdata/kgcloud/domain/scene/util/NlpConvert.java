package com.plantdata.kgcloud.domain.scene.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.plantdata.kgcloud.domain.scene.rsp.NlpRsp;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter(autoApply = true)
public class NlpConvert implements AttributeConverter<List<NlpRsp>,String> {
    @Override
    public String convertToDatabaseColumn(List<NlpRsp> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<NlpRsp> convertToEntityAttribute(String dbData) {

        return new Gson().fromJson(dbData, new TypeToken<List<NlpRsp>>(){}.getType());
    }
}
