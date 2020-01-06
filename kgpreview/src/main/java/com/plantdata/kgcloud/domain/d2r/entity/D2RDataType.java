package com.plantdata.kgcloud.domain.d2r.entity;

/**
 * D2R数据类型
 * Created by bin on 2018/6/6.
 */
public enum D2RDataType {

    /**
     * D2R数据类型
     */
    CONCEPT("concept"), ATTR_DEF("attr_def"), ENTITY("entity"), SYNONYM("synonym"), ATTR("attr"), RELATION("rel");
    private final String type;

    private D2RDataType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return this.type;
    }
}
