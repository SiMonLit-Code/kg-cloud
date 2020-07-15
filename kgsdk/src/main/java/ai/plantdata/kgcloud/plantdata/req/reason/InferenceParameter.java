package ai.plantdata.kgcloud.plantdata.req.reason;


import ai.plantdata.kgcloud.plantdata.req.common.PageModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InferenceParameter extends PageModel {

    private String kgName;
    private Integer attrId;
    private String name;
    private String ids;
    private Integer type;
    private Long domain;
    private List<Long> rangeList;
    private String pathRuleList;


}
