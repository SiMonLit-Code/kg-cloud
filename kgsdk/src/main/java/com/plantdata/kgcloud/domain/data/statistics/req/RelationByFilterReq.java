package com.plantdata.kgcloud.domain.data.statistics.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Leslee
 */
@Getter
@Setter
public class RelationByFilterReq<T> {
    private Integer layer;
    private List<T> ids;
}
