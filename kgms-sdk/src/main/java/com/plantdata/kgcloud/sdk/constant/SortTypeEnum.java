package com.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * @author cjw
 */

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SortTypeEnum {
    /**
     * 排序通用
     */
    DESC(-1, "desc"),

    ASC(1, "asc");

    private Integer value;
    private String name;

    public static Optional<SortTypeEnum> parseByName(String name) {
        for (SortTypeEnum type : SortTypeEnum.values()) {
            if (type.getName().equals(name.toLowerCase())) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public static Optional<SortTypeEnum> parseByValue(Integer value) {
        for (SortTypeEnum type : SortTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
