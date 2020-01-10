package com.plantdata.kgcloud.plantdata.req.rule;

import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
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
