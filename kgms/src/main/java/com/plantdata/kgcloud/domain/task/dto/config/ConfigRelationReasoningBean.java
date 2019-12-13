package com.plantdata.kgcloud.domain.task.dto.config;

import lombok.Data;

import java.util.List;

@Data
public class ConfigRelationReasoningBean {
    private Integer attrId;
    /**
     * 0 private 1 public
     */
    private int mode;
    private String name;
    private Long domain;
    private List<Long> rangeList;
    private List<ConfigPathEdgeRuleBean> pathRuleList;
}
