package com.plantdata.kgcloud.sdk.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 14:25
 * @Description:
 */
@Getter
public enum DWDataFormat {

    /**
     * 行业标准
     */
    STANDARD(1),

    /**
     * pddocument
     */
    PDDOC(2),

    /**
     * 自定义
     */
    CUSTOM(3),

    RDF_OWL(4),

    FILE(5),

    PDD2R(6);

    private Integer type;

    DWDataFormat(Integer type) {
        this.type = type;
    }

    public static DWDataFormat findType(Integer dbData) {
        for (DWDataFormat value : DWDataFormat.values()) {
            if (value.type == dbData) {
                return value;
            }
        }
        return DWDataFormat.CUSTOM;
    }

    public static boolean isStandard(Integer type){
        return STANDARD.getType().equals(type);
    }

    public static boolean isPDdoc(Integer type){
        return PDDOC.getType().equals(type);
    }

    public static boolean isPDd2r(Integer type){
        return PDD2R.getType().equals(type);
    }




}
