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
    DESC(-1),

    ASC(1);

    private Integer value;
}
