package ai.plantdata.kgcloud.plantdata.req.rule;

import ai.plantdata.kgcloud.plantdata.req.common.PageModel;
import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.*;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GraphRuleListByPageParameter extends PageModel {
    private String kgName;
    @ChooseCheck(value = "[0,1]", name = "ruleType", isBlank = true)
    private Integer ruleType = 0;
}
