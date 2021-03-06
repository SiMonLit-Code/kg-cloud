package ai.plantdata.kgcloud.domain.prebuilder.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import ai.plantdata.kgcloud.sdk.rsp.StandardTemplateSchemaRsp;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

public class ListStandardTemplateSchemaConverter implements AttributeConverter<List<StandardTemplateSchemaRsp>, String> {
    @Override
    public String convertToDatabaseColumn(List<StandardTemplateSchemaRsp> standardTemplateSchemas) {
        return JacksonUtils.writeValueAsString(standardTemplateSchemas);
    }

    @Override
    public List<StandardTemplateSchemaRsp> convertToEntityAttribute(String value) {
        if(value == null){
            return new ArrayList<>();
        }

        return JacksonUtils.readValue(value, new TypeReference<List<StandardTemplateSchemaRsp>>() {
        });
    }
}
