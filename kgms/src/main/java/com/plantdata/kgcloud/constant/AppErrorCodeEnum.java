package com.plantdata.kgcloud.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/25 9:52
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AppErrorCodeEnum implements ErrorCode {
    /**
     *
     */
    NULL_GIS_RULE_ID(121001, "规则id不能为空"),
    NULL_GIS_ENTITY_ID(121002, "实体ids不能为空"),
    NULL_GIS_KG_QL(121003, "kgQl语句不能为空"),
    NULL_CONCEPT_ID_AND_KEY(121004, "概念id和key不能同时为空");
    private final int errorCode;

    private final String message;

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
