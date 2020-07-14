package ai.plantdata.kgcloud.sdk.req.app.statistic;

import ai.plantdata.kgcloud.sdk.req.app.function.AttrDefListKeyReqInterface;
import ai.plantdata.kgcloud.sdk.req.app.function.ConceptKeyReqInterface;
import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 12:03
 */
@Getter
@Setter
public class EdgeStatisticByConceptIdReq implements ConceptKeyReqInterface, AttrDefListKeyReqInterface {


    private Long conceptId;

    private String conceptKey;

    private List<String> tripleIds;

    @ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer sort = -1;
    private List<Integer> allowAttrs;
    private List<String> allowAttrsKey;

    private String fromTime;
    private String toTime;

    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    @Min(-1)
    @Max(1000)
    private Integer size = 10;
}
