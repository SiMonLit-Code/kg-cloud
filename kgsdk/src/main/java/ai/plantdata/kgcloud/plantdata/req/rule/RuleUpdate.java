package ai.plantdata.kgcloud.plantdata.req.rule;

import ai.plantdata.kgcloud.plantdata.bean.rule.RuleBean;
import lombok.*;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RuleUpdate {
    private String kgName;
    private RuleBean bean;
    private Long id;
}
