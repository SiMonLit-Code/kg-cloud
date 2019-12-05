package com.plantdata.kgcloud.domain.data.statistics.req;

import com.plantdata.kgcloud.domain.common.validator.ChooseCheck;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 17:20
 */
@Getter
@Setter
public class StatEntityGroupByAttributeByConceptIdReq {

    private Long conceptId;
    private String conceptKey;
    private List<String> tripleIds;
    @ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer sort = -1;
    private List<Integer> allowAttrs;
    List<String> allowAttrsKey;
    private String fromTime;
    private String toTime;
    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    @Min(-1)
    @Max(1000)
    private Integer size = 10;
}
