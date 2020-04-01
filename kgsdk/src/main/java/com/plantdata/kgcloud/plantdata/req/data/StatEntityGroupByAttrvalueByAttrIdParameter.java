package com.plantdata.kgcloud.plantdata.req.data;


import com.plantdata.kgcloud.plantdata.bean.DateTypeBean;
import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class StatEntityGroupByAttrvalueByAttrIdParameter {
    @NotBlank
    private String kgName;
    private Integer attrId;
    private String attrKey;
    private List<Long> entityIds;
    @ChooseCheck(value = "[1,-1]",name = "sort")
    private Integer sort = -1;
    private String allowValues;
    @ChooseCheck(value = "[0,1]",name = "returnType")
    private Integer returnType = 0;
    @Min(1)
    @Max(10000)
    private Integer size = 10;
    private boolean isMerge = false;
    private DateTypeBean dateType;

}
