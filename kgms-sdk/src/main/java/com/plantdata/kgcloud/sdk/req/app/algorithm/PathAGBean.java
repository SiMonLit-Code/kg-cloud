package com.plantdata.kgcloud.sdk.req.app.algorithm;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Administrator
 */
@Getter
@Setter
public class PathAGBean {

    private Long start;
    private Long end;
    private List<String> edges;
    private List<Long> nodes;

}
