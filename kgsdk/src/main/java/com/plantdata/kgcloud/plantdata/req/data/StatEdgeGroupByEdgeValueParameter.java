package com.plantdata.kgcloud.plantdata.req.data;


import com.plantdata.kgcloud.plantdata.bean.DateTypeBean;
import com.plantdata.kgcloud.plantdata.validator.ChooseCheck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class StatEdgeGroupByEdgeValueParameter {
    @NotBlank
    private String kgName;
    private Integer attrId;
    private String attrKey;
    private Integer seqNo;
    private Set<Long> entityIds;
    private List<String> tripleIds;
    @ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer sort = 1;
    private String allowValues;
    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    @Min(-1)
    @Max(1000)
    private Integer size = 10;
    private Boolean merge = false;
    private DateTypeBean dateType;

}
