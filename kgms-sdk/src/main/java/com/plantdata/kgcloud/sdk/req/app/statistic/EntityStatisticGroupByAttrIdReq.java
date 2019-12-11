package com.plantdata.kgcloud.sdk.req.app.statistic;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/10 20:15
 */
@Getter
@Setter
public class EntityStatisticGroupByAttrIdReq {

    @NotNull
    private Integer attrId;
    private String attrKey;
    private List<Long> entityIds;
    //    @ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer direction = -1;
    private Integer sort = -1;
    private String allowValues;
    // @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    @Min(-1)
    @Max(10000)
    private Integer size = 10;

    private Boolean merge = false;

    private DateTypeReq dateType;


}
