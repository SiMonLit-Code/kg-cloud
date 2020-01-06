package com.plantdata.kgcloud.plantdata.req.explore.gis;

import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import com.plantdata.kgcloud.plantdata.validator.ChooseCheck;
import com.plantdata.kgcloud.plantdata.validator.DateCheck;
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
public class GraphRectangleParameter extends PageModel {
    @NotBlank
    private String kgName;
    private List<Long> allowTypes;
    private List<String> allowTypesKey;
    private Integer attrId;
    private String attrKey;
    private Boolean isInherit = false;
    @ChooseCheck(value = "[0,1,2]", name = "direction")
    private Integer direction = 0;
    @DateCheck(name = "fromTime")
    private String fromTime;
    @DateCheck(name = "toTime")
    private String toTime;
    @ChooseCheck(value = "['$box','$centerSphere']", name = "filterType", type = String.class, isBlank = true)
    private String filterType;
    private String gisFilters;
}
