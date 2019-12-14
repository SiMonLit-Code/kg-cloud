package com.plantdata.kgcloud.plantdata.req.explore;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PathRule {
    private List<Long> conceptIds;
    private Integer attrId;
    private List<PathRule> nextNode;
}
