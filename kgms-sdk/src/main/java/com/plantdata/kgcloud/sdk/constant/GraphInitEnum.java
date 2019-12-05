package com.plantdata.kgcloud.sdk.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/10/31 15:24
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public enum GraphInitEnum {
    /**
     *
     */
    GRAPH("graph"),
    PATH("path"),
    RELATION("relation"),
    TIMING("timing"),
    PATH_TIMING("pathtiming"),
    RELATION_TIMING("relationtiming"),
    EXPLORE("explore");
    private String value;
}
