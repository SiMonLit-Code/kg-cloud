package com.plantdata.kgcloud.domain.task.dto.config;

import lombok.Data;

@Data
public class ConfigFilterRuleBean {
    /**
     * 1 count 2 sum
     */
    private Integer mode;
    private String attrId;
    private String function;
    private Object value;
    /**
     * or / and
     */
    private String relation;
    private String unit;
}
