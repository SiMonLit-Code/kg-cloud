package ai.plantdata.kgcloud.sdk.req.app.statistic;

import ai.plantdata.kgcloud.sdk.constant.StatisticConstants;
import ai.plantdata.kgcloud.sdk.req.app.function.AttrDefKeyReqInterface;
import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 13:52
 */
@Getter
@Setter
public class EdgeAttrStatisticByAttrValueReq implements AttrDefKeyReqInterface {

    private Integer attrDefId;
    private String attrDefKey;
    @NotNull
    private Integer seqNo;

    private List<Long> entityIds;

    private List<String> tripleIds;
    @ChooseCheck(value = "[0,1,-1]", name = "sort")
    private Integer sort = -1;
    private List<Object> allowValues;
    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    @Min(-1)
    @Max(StatisticConstants.STATISTIC_MAX_SIZE)
    private Integer size = 10;
    @NotNull
    private Boolean merge = false;

    private DateTypeReq dateType;
}
