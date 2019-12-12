package com.plantdata.kgcloud.domain.task.dto.config;

import lombok.Data;

import java.util.List;

@Data
public class ConfigPathEdgeRuleBean {
    private Integer attrId;
    private int direction;
    private Long range;
    private List<ConfigFilterRuleBean> filterList;
    private List<ConfigFilterRuleBean> aggregateFilterList;
    private int isComputed;

}
