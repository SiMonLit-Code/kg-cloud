package com.plantdata.kgcloud.domain.model.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiezhenxiang 2019/7/5
 */
@Data
public class AttrConfig {

    /** 字段名 */
    private String fieldName;
    /** 属性名 */
    private String attrName;
    /** 属性类型 0数值属性 1关系属性 */
    private Integer type;
    /** 数据类型 */
    private Integer dataType;
    /** 关联表 */
    private List<ReferTable> referTable;

    /** 建模后的属性ID */
    private Integer attrId;

    public List<ReferTable> getReferTable() {
        return referTable == null ? new ArrayList<>() : referTable;
    }
}
