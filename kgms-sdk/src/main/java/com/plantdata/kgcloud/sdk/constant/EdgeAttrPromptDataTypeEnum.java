package com.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/31 19:04
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EdgeAttrPromptDataTypeEnum implements BaseEnum {
    /**
     * 展示类型
     */
    NUM_ATTR(1, "数值属性"),
    EDGE_ATTR(2, "边属性");
    private int value;
    private String desc;

    @Override
    public Integer fetchId() {
        return value;
    }
}
