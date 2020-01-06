package com.plantdata.kgcloud.plantdata.req.data;


import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import com.plantdata.kgcloud.plantdata.req.explore.common.AttrScreeningBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class QueryRelationParameter extends PageModel {

    @NotBlank
    private String kgName;
    private List<Long> entityIds;
    private List<Integer> attrIds;
    private List<String> attrKeys;
    private List<Long> attrValueIds;
    private String attrTimeFrom;
    private String attrTimeTo;
    private Integer direction = 0;
    private List<AttrScreeningBean> query;


}
