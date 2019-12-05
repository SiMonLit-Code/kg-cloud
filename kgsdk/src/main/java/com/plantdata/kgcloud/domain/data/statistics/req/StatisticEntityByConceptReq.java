package com.plantdata.kgcloud.domain.data.statistics.req;

import com.plantdata.kgcloud.domain.common.validator.ChooseCheck;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 17:17
 */
@Getter
@Setter
public class StatisticEntityByConceptReq {

    @NotBlank
    private String kgName;
    private List<Long> entityIds;
    @ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer sort = -1;
    private List<Long> allowTypes;
    private List<String> allowTypesKey;
    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    private Integer size = 10;
}
