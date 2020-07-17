package ai.plantdata.kgcloud.domain.graph.attr.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.domain.edit.req.attr.AttrTemplateReq;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.persistence.AttributeConverter;
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
        return JacksonUtils.readValue(dbData, new TypeReference<List<AttrTemplateReq>>() {
        });
    }
}
