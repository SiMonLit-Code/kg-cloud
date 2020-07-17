package ai.plantdata.kgcloud.plantdata.req.data;


import ai.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class StatEntityGroupByAttributeByConceptIdParameter {
    @NotBlank
    private String kgName;
    private Long conceptId;
    private String conceptKey;

    private List<String> tripleIds;

    @ChooseCheck(value = "[-1,1]", name = "sort")
    private Integer sort = -1;
    private List<Integer> allowAtts;
    private List<String> allowAttsKey;

    private String fromTime;
    private String toTime;

    @ChooseCheck(value = "[0,1]", name = "returnType")
    private Integer returnType = 0;
    @Min(-1)
    @Max(1000)
    private Integer size = 10;

}
