package com.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 14:52
 * @Description: 属性优化类型
 */
@Getter
public enum InduceType {
    /**
     * 全部
     */
    ALL(0),
    /**
     * 对象化
     */
    OBJECT(1),
    /**
     * 公有化
     */
    PUBLIC(2),
    /**
     * 合并
     */
    MERGE(3),
    ;

    private Integer type;

    InduceType(Integer type) {
        this.type = type;
    }

    public static boolean isObject(Integer type) {
        return OBJECT.getType().equals(type);
    }

    public static boolean isPublic(Integer type) {
        return PUBLIC.getType().equals(type);
    }

    public static boolean isMerge(Integer type) {
        return MERGE.getType().equals(type);
    }
}
