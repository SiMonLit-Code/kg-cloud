package com.plantdata.kgcloud.domain.data.statistics.req;

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
    private Integer sort = -1;
    private List<Integer> allowAttrs;
    List<String> allowAttrsKey;
    private String fromTime;
    private String toTime;
    private Integer returnType = 0;
    @Min(-1)
    @Max(1000)
    private Integer size = 10;
}
