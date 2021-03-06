package ai.plantdata.kgcloud.sdk.req.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/9/6 15:39
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel("gis轨迹分析接口")
public class GisLocusReq extends GisExploreReq {
    @NotNull
    @ApiModelProperty("gis规则参数")
    private List<GisRuleParam> rules;

    @ApiModelProperty("时间筛选类型")
    private Integer timeFilterType = 0;

    @Getter
    @Setter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GisRuleParam {
        @ApiModelProperty("实例id")
        @NotNull
        private List<Long> ids;
        @ApiModelProperty("规则id")
        @NotNull
        private Long ruleId;
        @NotNull
        @ApiModelProperty("kgQl语句")
        private String kql;
    }

}
