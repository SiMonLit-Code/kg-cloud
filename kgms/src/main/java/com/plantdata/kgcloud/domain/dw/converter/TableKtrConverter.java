package com.plantdata.kgcloud.domain.dw.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.domain.dw.rsp.TableKtrRsp;
import com.plantdata.kgcloud.util.JacksonUtils;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description: 结构转换
 * @author: czj
 * @create: 2020-03-13 16:31
 **/
public class TableKtrConverter implements AttributeConverter<List<TableKtrRsp>, String> {
    @Override
    public String convertToDatabaseColumn(List<TableKtrRsp> tableKtrRsps) {
        return JacksonUtils.writeValueAsString(tableKtrRsps);
    }

    @Override
    public List<TableKtrRsp> convertToEntityAttribute(String s) {
        return JacksonUtils.readValue(s, new TypeReference<List<TableKtrRsp>>(){});
    }
}
