package com.plantdata.kgcloud.domain.chanyelian.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xiezhenxiang 2020/4/14
 */
@Getter
@Setter
public class ViewRsp {

    private Node node;
    private Integer deep;
    private List<Pair> relation;
}
