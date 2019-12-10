package com.plantdata.kgcloud.sdk.rsp.app.analysis;

import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GraphRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.GraphStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/12 12:23
 */
@ToString
@Getter
@Setter
@ApiModel("路径分析视图")
@NoArgsConstructor
public class PathAnalysisRsp extends StatisticRsp {

    public PathAnalysisRsp(List<GraphRelationRsp> relationList, Integer hasNextPage, List<CommonEntityRsp> entityList, List<GraphStatisticRsp> statisticResult) {
        super(relationList, hasNextPage, entityList, statisticResult);
    }
}
