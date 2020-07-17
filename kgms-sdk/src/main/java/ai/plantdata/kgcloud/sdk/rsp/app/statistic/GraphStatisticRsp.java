package ai.plantdata.kgcloud.sdk.rsp.app.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw 2019-11-12 12:08:32
 */
@Getter
@Setter
@ApiModel("路径统计视图")
@NoArgsConstructor
@AllArgsConstructor
public class GraphStatisticRsp {

    @ApiModelProperty("key 为唯一标识，用于区分多组结果")
    private String key;
    @ApiModelProperty("概念id")
    private Long conceptId;
    @ApiModelProperty("属性id")
    private List<Integer> attrIdList;
    @ApiModelProperty("统计详情")
    private List<GraphStatisticDetailRsp> statisticDetails;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("统计详情")
    public static class GraphStatisticDetailRsp {
        @ApiModelProperty("实例id")
        private Long id;
        @ApiModelProperty("实例数量")
        private Integer count;
    }
}
