package com.plantdata.kgcloud.sdk.req.app.statistic;

import com.plantdata.kgcloud.sdk.constant.StatisticConstants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    private Integer sort = -1;
    private List<Long> allowTypes;
    private List<String> allowTypesKey;
    private Integer returnType = 0;
    @Max(StatisticConstants.STATISTIC_MAX_SIZE)
    @Min(-1)
    private Integer size = 10;
}
