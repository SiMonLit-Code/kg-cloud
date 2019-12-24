package com.plantdata.kgcloud.plantdata.constant;

import com.plantdata.kgcloud.sdk.constant.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Administrator
 */

@AllArgsConstructor
@Getter
public enum SortEnum implements BaseEnum {
    /**
     *
     */
    DESC(-1, "desc"),

    ASC(1, "asc");

    private Integer value;
    private String desc;
}
