package com.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 18:43
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DimensionEnum {
    /**
     * 数据集显示类型
     */
    TWO(1, "二维"),
    THIRD(2, "三维");
    private int value;
    private String desc;

    public static DimensionEnum parseByValue(int value) {
        for (DimensionEnum statisticEnum : DimensionEnum.values()) {
            if (value == statisticEnum.value) {
                return statisticEnum;
            }
        }
        return null;
    }
}
