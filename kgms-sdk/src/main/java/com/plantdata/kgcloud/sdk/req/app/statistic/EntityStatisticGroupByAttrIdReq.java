package com.plantdata.kgcloud.sdk.req.app.statistic;

import com.plantdata.kgcloud.sdk.constant.StatisticConstants;
import com.plantdata.kgcloud.sdk.req.app.function.AttrDefKeyReqInterface;
import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 20:15
 */
@Getter
@Setter
@ApiModel("实体数值属性统计-参数")
public class EntityStatisticGroupByAttrIdReq implements AttrDefKeyReqInterface {


    private Integer attrDefId;
    private String attrDefKey;
    private List<Long> entityIds;
    @ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer sort = -1;
    private List<Object> allowValues;
    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    @Min(-1)
    @Max(StatisticConstants.STATISTIC_MAX_SIZE)
    @ApiModelProperty("数量 min:1 max:10000")
    private Integer size = 10;
    private Boolean merge = false;

    private DateTypeReq dataType;

}
