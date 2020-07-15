package ai.plantdata.kgcloud.plantdata.req.rule;

import ai.plantdata.kgcloud.plantdata.bean.rule.RuleBean;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RuleAdd {
    @NotBlank(groups = Integer.class)
    private String kgName;
    @NotNull(groups = Integer.class)
    private RuleBean bean;
}
