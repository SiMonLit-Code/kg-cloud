package ai.plantdata.kgcloud.sdk.req.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class FieldConfigBean {
    private String dependencySource;
    private String field;
    private String byType;
    private String typeValue;
    private String byAttr;
    private Integer group;
    private String valueType;
    private Map<String, Object> formatter;
}
