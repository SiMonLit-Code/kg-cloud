package com.plantdata.kgcloud.domain.chanyelian.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author xiezhenxiang 2020/4/14
 */
@Getter
@Setter
@AllArgsConstructor
public class Pair {

    private Long source;
    private Long target;
    private String attrName;
    Map<String, Object> attr;

}
