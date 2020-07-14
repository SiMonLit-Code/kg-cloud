package ai.plantdata.kgcloud.plantdata.req.rule;

import ai.plantdata.kgcloud.plantdata.bean.rule.GraphmRuleMapBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GraphRuleUpdate {
    private String kgName;
    private GraphmRuleMapBean bean;
    private Long id;
}
