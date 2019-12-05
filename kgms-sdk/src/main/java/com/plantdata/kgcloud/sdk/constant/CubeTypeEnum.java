package com.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw 2019-11-12 14:52:43
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CubeTypeEnum {
    /**
     * todo
     */
    NODE(0, "NODE"), EDGE(1, "EDGE");

    private int index;
    private String expression;

    CubeTypeEnum(int index) {
        this.index = index;
    }

    public static CubeTypeEnum getSignByExpression(String expression) {
        for (CubeTypeEnum cubeTypeEnum : CubeTypeEnum.values()) {
            if (cubeTypeEnum.getExpression().equals(expression)) {
                return cubeTypeEnum;
            }
        }
        return null;
    }

}
