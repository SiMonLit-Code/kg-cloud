package com.plantdata.kgcloud.sdk.constant;

import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 17:31
 */
@Getter
public enum GisFilterTypeEnum implements BaseEnum{
    /**
     * gis选取类型
     */
    BOX("$box"),
    CENTER_SPHERE("$centerSphere");
    String value;

    GisFilterTypeEnum(String value) {
        this.value = value;
    }


}
