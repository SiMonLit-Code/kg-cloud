package ai.plantdata.kgcloud.plantdata.req.reason;


import ai.plantdata.kgcloud.sdk.constant.SignEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
public class Equation {
    private String leftExpression;
    private String rightExpression;
    private SignEnum sign;
}
