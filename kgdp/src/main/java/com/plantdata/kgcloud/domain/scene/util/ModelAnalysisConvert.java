package com.plantdata.kgcloud.domain.scene.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.plantdata.kgcloud.domain.scene.rsp.ModelAnalysisRsp;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter(autoApply = true)
public class ModelAnalysisConvert implements AttributeConverter<List<ModelAnalysisRsp>,String> {
    @Override
    public String convertToDatabaseColumn(List<ModelAnalysisRsp> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<ModelAnalysisRsp> convertToEntityAttribute(String dbData) {
        return new Gson().fromJson(dbData, new TypeToken<List<ModelAnalysisRsp>>(){}.getType());
    }
}
