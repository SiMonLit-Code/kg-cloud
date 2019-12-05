package com.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/11/13 15:32
 * @Description:
 */
@Getter
public enum MongoOperation {
    /**
     * 等于
     */
    EQUAL("$eq"),
    /**
     * 在范围内
     */
    IN("$in"),
    /**
     * 排序逆序
     */
    DESC("DESC"),
    /**
     * 排序顺序
     */
    ASC("ASC"),
    ;

    private final String type;

    MongoOperation(String type) {
        this.type = type;
    }

}
