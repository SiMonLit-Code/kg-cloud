package com.plantdata.kgcloud.plantdata.req.explore.common;

import com.plantdata.kgcloud.config.MarkObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PathRule implements MarkObject {
    private List<Long> conceptIds;
    private Integer attrId;
    private List<PathRule> nextNode;
}
