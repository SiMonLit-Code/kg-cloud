package com.plantdata.kgcloud.domain.task.dto.config;


import lombok.Data;

@Data
public class ConfigRuleBean {
    private Integer ruleId;

    private String kgName;

    private String ruleConfig;

}