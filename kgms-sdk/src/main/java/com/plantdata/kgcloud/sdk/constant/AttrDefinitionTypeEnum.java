package com.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 17:15
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AttrDefinitionTypeEnum implements BaseEnum {

    /**
     *
     */
    DATA_VALUE(0, "数值属性"), OBJECT(1, "对象属性");

    private int id;
    private String desc;


    @Override
    public Integer fetchId() {
        return id;
    }
}
