package com.plantdata.kgcloud.domain.graph.config.constant;

/**
 *
 * @author jiangdeming
 * @date 2019/11/29
 */
public enum FocusType {
    /**
     * 传输类型
     */
    graph("graph"),
    path("path"),
    relation("relation"),
    timing("timing"),
    pathtiming("pathtiming"),
    relationtiming("relationtiming"),
    explore("explore");

    private String code;

    FocusType(String code) {
        this.code=code;
    }

    public static FocusType findType(String dataType) {
        for (FocusType value : FocusType.values()) {
            if (value.getCode().equals(dataType)) {
                return value;
            }
        }
        return FocusType.graph;
    }

    public static boolean contains(String dataType) {
        for (FocusType value : FocusType.values()) {
            if (value.name().equals(dataType)) {
                return true;
            }
        }
        return false;
    }

    public String getCode() {
        return code;
    }
}
