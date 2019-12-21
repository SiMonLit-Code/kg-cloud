package com.plantdata.kgcloud.sdk.req.app.statistic;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 13:52
 */
@Getter
@Setter
public class EdgeAttrStatisticByAttrValueReq {
    @NotBlank
    private String kgName;
    private Integer attrId;
    private String attrKey;
    @NonNull
    private Integer seqNo;

    private Set<Long> entityIds;

    private List<String> tripleIds;
    //@ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer sort = 1;

    private List<Object> allowValues;
    // @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    @Min(-1)
    @Max(1000)
    private Integer size = 10;


    private Boolean merge = false;

    private DateTypeReq dateType;
}
