package com.plantdata.kgcloud.sdk.rsp.app.statistic;

import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
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


    @ApiModelProperty("实例列表")
    private List<CommonEntityRsp> entityList;
    @ApiModelProperty("路径分析结果")
    private List<GraphStatisticRsp> statisticResult;

    public StatisticRsp(List<BasicRelationRsp> relationList, Integer hasNextPage, List<CommonEntityRsp> entityList, List<GraphStatisticRsp> statisticResult) {
        super(relationList, hasNextPage);
        this.entityList = entityList;
        this.statisticResult = statisticResult;
    }

}
