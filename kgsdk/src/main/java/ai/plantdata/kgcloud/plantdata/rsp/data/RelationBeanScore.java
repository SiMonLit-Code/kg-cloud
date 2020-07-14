package ai.plantdata.kgcloud.plantdata.rsp.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RelationBeanScore implements java.io.Serializable {

    private String tripleId;
    /**
     * 起始节点ID
     */
    private Long entityId;
    /**
     * 起始节点名称
     */
    private String entityName;
    /**
     * 起始节点消歧标识
     */
    private String entityMeaningTag;
    /**
     * 起始节点概念ID
     */
    private Long entityConcept;
    /**
     * 属性ID
     */
    private Integer attrId;
    /**
     * 结束节点ID
     */
    private Long attrValueId;
    /**
     * 结束节点名称
     */
    private String attrValueName;
    /**
     * 结束节点消歧标识
     */
    private String attrValueMeaningTag;
    /**
     * 结束节点概念ID
     */
    private Long attrValueConcept;
    /**
     * 关系边附加起始时间
     */
    private String attrTimeFrom;
    /**
     * 关系边附加结束时间
     */
    private String attrTimeTo;
    /**
     * 边附加属性
     */
    Map<String, Object> extraInfoMap;

    /**
     * 权重
     */
    private String score = "0";

    /**
     * 来源
     */
    private String source;
    /**
     * 可信度
     */
    private String reliability = "0";

}
