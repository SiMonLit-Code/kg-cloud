package ai.plantdata.kgcloud.plantdata.req.reason;

import lombok.*;
import java.util.List;
/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RelationReasoningBean {
    private Integer attrId;
    private String name;
    /**
     * 0 relation 1 attribute
     */
    private int type;
    private Long domain;
    private List<Long> rangeList;
    private int literalDataType;
    private int pub;
    private String literalValue;
    private RuleCube nodeRule;
    private List<RuleSection> pathRuleList;
    private Equation compute;
}
