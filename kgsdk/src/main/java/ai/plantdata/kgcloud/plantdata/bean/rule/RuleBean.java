package ai.plantdata.kgcloud.plantdata.bean.rule;

import ai.plantdata.kgcloud.plantdata.rsp.MarkObject;
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
    private Long ruleId;
    private String kgName;
    private String ruleName;
    private String ruleConfig;
    private String ruleSettings;
    private Date createTime;
    private Date updateTime;
}