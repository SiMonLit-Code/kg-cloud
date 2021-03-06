package ai.plantdata.kgcloud.domain.graph.config.converter;

import ai.plantdata.kgcloud.domain.graph.config.constant.FocusType;

import javax.persistence.AttributeConverter;

/**
 * @author
 * Created by plantdata-1007 on 2019/11/29.
 */
public class FocusTypeConverter implements AttributeConverter<FocusType, String> {
    @Override
    public String convertToDatabaseColumn(FocusType dataType) {
        return dataType.getCode();
    }

    @Override
    public FocusType convertToEntityAttribute(String type) {
        return FocusType.findType(type);
    }
}
