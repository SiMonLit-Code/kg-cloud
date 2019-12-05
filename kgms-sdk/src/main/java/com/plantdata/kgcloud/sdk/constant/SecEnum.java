package com.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw 2019-11-12 14:52:43
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SecEnum {
    /**
     * todo
     */
    OR(0, "OR"), AND(1, "AND");

    private int index;
    private String expression;

    SecEnum(int index) {
        this.index = index;
    }

    public static SecEnum getSignByExpression(String expression) {
        for (SecEnum secEnum : SecEnum.values()) {
            if (secEnum.getExpression().equals(expression)) {
                return secEnum;
            }
        }
        return null;
    }
}
