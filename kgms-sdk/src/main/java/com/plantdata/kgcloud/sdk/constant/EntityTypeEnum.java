package com.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/8 12:08
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EntityTypeEnum {
    /**
     * 展示类型
     */
    ENTITY(1, "实例"),
    CONCEPT(0, "概念");
    private int value;
    private String desc;

    public static EntityTypeEnum parseById(int entityType) {
        for (EntityTypeEnum dataTypeEnum : EntityTypeEnum.values()) {
            if (dataTypeEnum.value == entityType) {
                return dataTypeEnum;
            }
        }
        return null;
    }

}
