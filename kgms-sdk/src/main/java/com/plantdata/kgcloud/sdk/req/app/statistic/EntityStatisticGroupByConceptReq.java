package com.plantdata.kgcloud.sdk.req.app.statistic;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 18:33
 */
@Getter
@Setter
public class EntityStatisticGroupByConceptReq {
    private List<Long> entityIds;
    private Integer direction = -1;
    private List<Long> allowTypes;
    private List<String> allowTypesKey;
    private Integer returnType = 0;
    private Integer size = 10;
}
