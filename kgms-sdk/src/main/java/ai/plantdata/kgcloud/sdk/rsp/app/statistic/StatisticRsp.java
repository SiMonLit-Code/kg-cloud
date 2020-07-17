package ai.plantdata.kgcloud.sdk.rsp.app.statistic;

import ai.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/13 17:53
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticRsp extends BasicGraphExploreRsp {



    @ApiModelProperty("路径分析结果")
    private List<GraphStatisticRsp> statisticResult;

    public StatisticRsp(List<GraphRelationRsp> relationList, Integer hasNextPage, List<CommonEntityRsp> entityList, List<GraphStatisticRsp> statisticResult) {
        super(relationList, entityList,hasNextPage);
        this.statisticResult = statisticResult;
    }

}
