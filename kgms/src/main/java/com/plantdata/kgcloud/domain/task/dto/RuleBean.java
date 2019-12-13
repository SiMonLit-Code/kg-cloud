package com.plantdata.kgcloud.domain.task.dto;


import lombok.Data;

@Data
public class RuleBean {
    private Integer ruleId;

    private String ruleName;

    private String ruleConfig;

    private String ruleSettings;

}