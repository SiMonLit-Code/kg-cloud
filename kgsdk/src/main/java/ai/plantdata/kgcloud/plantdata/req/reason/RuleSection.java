package ai.plantdata.kgcloud.plantdata.req.reason;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Administrator
 */
@Getter
@Setter
public class RuleSection {
    private SecEnum sec;
    /**
     * 0 满足 1 不满足
     */
    private int mode;
    private List<RuleCube> cubeList;
    private List<Equation> equationList;
}
