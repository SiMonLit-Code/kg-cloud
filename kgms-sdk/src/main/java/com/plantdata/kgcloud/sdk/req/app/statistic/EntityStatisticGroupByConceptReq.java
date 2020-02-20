package com.plantdata.kgcloud.sdk.req.app.statistic;

import com.plantdata.kgcloud.sdk.constant.StatisticConstants;
import com.plantdata.kgcloud.sdk.req.app.function.ConceptKeyListReqInterface;
import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
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
public class EntityStatisticGroupByConceptReq  implements ConceptKeyListReqInterface {
    private List<Long> entityIds;
    @ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer sort = -1;
    private List<Long> allowConcepts;
    private List<String> allowConceptsKey;
    private Integer returnType = 0;
    @Max(StatisticConstants.STATISTIC_MAX_SIZE)
    @Min(1)
    private Integer size = 10;
}
