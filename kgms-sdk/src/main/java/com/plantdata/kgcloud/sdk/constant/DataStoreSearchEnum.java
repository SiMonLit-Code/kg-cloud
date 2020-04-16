package com.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @date 2020/4/15  13:44
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DataStoreSearchEnum {

    LIKE("like"),
    NOL_LIKE("noLike");
    @Getter
    private String name;

}
