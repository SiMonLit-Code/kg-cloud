package com.plantdata.kgcloud.domain.graph.config.constant;

import com.google.common.collect.Sets;

import java.util.Set;

/**
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
    private static  final Set<FocusType> TWO_FOCUS = Sets.newHashSet(relation,path,pathtiming,relationtiming) ;
    private String code;

    FocusType(String code) {
        this.code = code;
    }

    public static FocusType findType(String dataType) {
        for (FocusType value : FocusType.values()) {
            if (value.getCode().equals(dataType)) {
                return value;
            }
        }
        return FocusType.graph;
    }
    public static boolean containsTwo(String dataType) {
        for (FocusType value :TWO_FOCUS) {
            if (value.name().equals(dataType)) {
                return true;
            }
        }
        return false;
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
