package com.plantdata.kgcloud.domain.app.service;

import ai.plantdata.kg.api.pub.resp.GraphVO;
import ai.plantdata.kg.common.bean.BasicInfo;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import com.plantdata.kgcloud.sdk.req.app.function.SecondaryScreeningInterface;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:33
 */
public interface GraphHelperService {

    List<Long> replaceByConceptKey(String kgName, List<String> keys);

    List<Integer> replaceByAttrKey(String kgName, List<String> keys);

    Map<Long, BasicInfo> getConceptIdMap(String kgName);

    /**
     * 图探索 属性，概念 key->转id
     *
     * @param kgName
     * @param exploreReq
     * @param <T>
     * @return
     */
    <T extends BasicGraphExploreReq> T keyToId(String kgName, T exploreReq);

    /**
     * @param kgName
     * @param req
     * @param rsp
     * @param <T>
     * @param <E>
     * @return
     */
    <T extends BasicGraphExploreRsp> Optional<T> graphSearchBefore(String kgName, SecondaryScreeningInterface req, T rsp);

    <T extends StatisticRsp> T buildExploreRspWithStatistic(String kgName, List<BasicStatisticReq> configList, GraphVO graphVO, T pathAnalysisRsp, boolean relationMerge);

}
