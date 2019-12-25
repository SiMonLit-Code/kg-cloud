package com.plantdata.kgcloud.plantdata.bean.rule;

import com.plantdata.kgcloud.config.MarkObject;
import lombok.*;
import org.apache.commons.lang3.math.NumberUtils;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GraphmRuleMapBean implements MarkObject {

    private static final int KGQL_LENGTH = 255;
    private Integer ruleId;
    /**
     * ruleType 0 图探索 1 gis轨迹分析
     */
    private Integer ruleType = NumberUtils.INTEGER_ZERO;
    private String kgName;
    private String ruleKgql;
    private GraphRuleBean ruleSettings;
    private String ruleName;
    private Date createTime;
    private Date updateTime;

}