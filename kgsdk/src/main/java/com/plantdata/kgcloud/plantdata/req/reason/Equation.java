package com.plantdata.kgcloud.plantdata.req.reason;


import com.plantdata.kgcloud.sdk.constant.SignEnum;
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
