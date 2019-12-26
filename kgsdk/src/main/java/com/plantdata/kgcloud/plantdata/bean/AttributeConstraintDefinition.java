package com.plantdata.kgcloud.plantdata.bean;
import lombok.*;

import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AttributeConstraintDefinition extends AttributeDefinition {

    /**
     * 枚举值约束
     */
    private Set<Object> enumList;
    /**
     * 范围值约束，key有 gt gte lt lte
     */
    private Map<String,Object> scopeMap;

    /**
     * 约束属性的结果集
     */
    private Map<String,Object> constraints;
}
