package com.plantdata.kgcloud.plantdata.req.explore.gis;

import com.plantdata.kgcloud.plantdata.req.common.PageModel;
import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
import com.plantdata.kgcloud.plantdata.validator.DateCheck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/9/6 15:39
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GraphLocusGisParameter extends PageModel {
    @NotBlank
    private String kgName;
    private List<GisRuleParam> rules;
    @ChooseCheck(value = "['$box','$centerSphere']", name = "filterType", type = String.class, isBlank = true)
    private String filterType;
    private String gisFilters;
    @DateCheck(name = "fromTime")
    private String fromTime;
    @DateCheck(name = "toTime")
    private String toTime;
    @ChooseCheck(value = "[0,1,2,3]", name = "timeFilterType", isBlank = true)
    private Integer timeFilterType = 0;

    @Data
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GisRuleParam {
        private List<Long> ids;
        private Long ruleId;
        private String kql;
    }
}
