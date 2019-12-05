package com.plantdata.kgcloud.domain.graph.attr.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrTemplateReq;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/12/4 17:22
 * @Description:
 */
public class AttrTemplateConverter implements AttributeConverter<List<AttrTemplateReq>, String> {
    @Override
    public String convertToDatabaseColumn(List<AttrTemplateReq> attribute) {
        return JacksonUtils.writeValueAsString(attribute);
    }

    @Override
    public List<AttrTemplateReq> convertToEntityAttribute(String dbData) {
        if (StringUtils.hasText(dbData)) {
            return null;
        }
        ObjectMapper objectMapper = JacksonUtils.getInstance();
        try {

            return objectMapper.readValue(dbData, new TypeReference<List<AttrTemplateReq>>() {
            });
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.ATTR_TEMPLATE_ERROR);
        }
    }
}
