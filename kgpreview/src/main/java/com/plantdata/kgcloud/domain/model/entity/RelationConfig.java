package com.plantdata.kgcloud.domain.model.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiezhenxiang 2019/7/30
 */
@Data
public class RelationConfig {

    private String tableName;
    private String domainTable;
    private String domainConcept;
    private String domainField;
    private String attrName;
    private String rangeTable;
    private String rangeConcept;
    private String rangeField;
    /** 0正向 1反向 */
    private Integer direct = 0;
    /** 边属性配置 */
    private List<AttrConfig> sideAttrConfig;

    /** 建模后定义域概念ID */
    private Long domainConceptId;
    /** 建模后关系属性ID */
    private Integer attrId;
    /** 建模后值域概念ID */
    private Long rangeConceptId;

    public List<AttrConfig> getSideAttrConfig() {
        return sideAttrConfig == null ? new ArrayList<>() : sideAttrConfig;
    }
}
