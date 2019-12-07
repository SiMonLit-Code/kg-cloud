package com.plantdata.kgcloud.domain.app.service;

import ai.plantdata.kg.api.pub.resp.GraphVO;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:33
 */
public interface GraphHelperService {

    List<Long> replaceByConceptKey(String kgName, List<String> keys);

    List<Integer> replaceByAttrKey(String kgName, List<String> keys);

    <T extends BasicGraphExploreReq> T dealGraphReq(String kgName, T exploreReq);

    <T extends StatisticRsp> T buildExploreRspWithStatistic(String kgName, List<BasicStatisticReq> configList, GraphVO graphVO, T pathAnalysisRsp);
}
