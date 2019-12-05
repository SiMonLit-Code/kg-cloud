package com.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 17:19
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DataSetStatisticEnum {

    /**
     * 数据集显示类型
     */
    E_CHART(0),
    KV(1);
    private int value;

    public static DataSetStatisticEnum parseByValue(int value) {
        for (DataSetStatisticEnum statisticEnum : DataSetStatisticEnum.values()) {
            if (value == statisticEnum.value) {
                return statisticEnum;
            }
        }
        return null;
    }

}
