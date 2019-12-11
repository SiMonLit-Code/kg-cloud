package com.plantdata.kgcloud.sdk.req.app.statistic;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 17:58
 */
@Getter
@Setter
public class EdgeStatisticByEntityIdReq {


    private Long entityId;

    private Boolean isDistinct = false;

    private List<IdsFilterReq<Integer>> allowAttrs;

    private List<IdsFilterReq<Long>> allowTypes;

}
