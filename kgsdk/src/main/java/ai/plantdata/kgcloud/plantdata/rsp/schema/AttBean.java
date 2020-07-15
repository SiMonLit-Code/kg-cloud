package ai.plantdata.kgcloud.plantdata.rsp.schema;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class AttBean {
    /**
     * 属性ID
     */
    private Long k;
    private String v;
    private Long type;
    private String attrKey;
    private List<Long> range;
    private Long domain;
    private Integer dataType;
    private List<AttributeExtraInfoItem> extraInfos;
    private Integer direction;
    private Map<String, Object> additionalInfo;


}
