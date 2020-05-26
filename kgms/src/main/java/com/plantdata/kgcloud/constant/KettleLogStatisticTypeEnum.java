package com.plantdata.kgcloud.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Administrator
 * @Description
 * @data 2020-03-29 11:16
 **/
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum KettleLogStatisticTypeEnum {
    /**
     * 按日期统计
     */
    DAY("day"),
    /**
     * 按小时统计
     */
    HOUR("hour"),
    /**
     * 按月显示
     */
    MONTH("month");

    private String lowerCase;

}
