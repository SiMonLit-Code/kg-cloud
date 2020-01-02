package com.plantdata.kgcloud.plantdata.bean.rule;

import com.plantdata.kgcloud.plantdata.rsp.MarkObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @author Administrator
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RuleBean implements MarkObject {
    private Integer ruleId;
    private String kgName;
    private String ruleName;
    private String ruleConfig;
    private String ruleSettings;
    private Date createTime;
    private Date updateTime;
}