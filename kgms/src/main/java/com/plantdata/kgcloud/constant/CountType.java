package com.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/12/3 18:01
 * @Description:
 */
@Getter
public enum CountType {
    /**
     * 概念
     */
    CONCEPT("concept", 0),
    /**
     * 实体
     */
    ENTITY("entity", 1),
    /**
     * 数值属性
     */
    NUMERICAL_ATTR("number", 2),
    /**
     * 私有数值属性
     */
    PRIVATE_NUMERICAL_ATTR("privateNumber", 3),
    /**
     * 对象属性
     */
    OBJECT_ATTR("object", 4),
    /**
     * 私有对象属性
     */
    PRIVATE_OBJECT_ATTR("privateObject", 5),
    ;

    private String type;

    private int code;

    CountType(String type, int code) {
        this.type = type;
        this.code = code;
    }
}

