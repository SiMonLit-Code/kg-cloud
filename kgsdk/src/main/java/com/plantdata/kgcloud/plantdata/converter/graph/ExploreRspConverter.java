package com.plantdata.kgcloud.plantdata.converter.graph;

import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.explore.common.GraphBean;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/14 16:31
 */
@Slf4j
public class ExploreRspConverter extends BasicConverter {

    public static GraphBean commonBasicGraphExploreRspToGraphBean(CommonBasicGraphExploreRsp exploreRsp) {
        return ExploreCommonConverter.basicGraphExploreRspToGraphBean(exploreRsp);
    }

    public static <T extends StatisticRsp> GraphBean statisticRspToGraphBean(T analysisRsp) {
        GraphBean graphBean = ExploreCommonConverter.basicGraphExploreRspToGraphBean(analysisRsp);
        ExploreCommonConverter.fillStatisticConfig(graphBean, analysisRsp.getStatisticResult());
        return graphBean;
    }
}
