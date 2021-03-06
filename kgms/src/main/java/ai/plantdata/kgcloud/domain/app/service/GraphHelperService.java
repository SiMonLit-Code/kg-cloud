package ai.plantdata.kgcloud.domain.app.service;

import ai.plantdata.kg.common.bean.BasicInfo;
import ai.plantdata.kgcloud.domain.app.dto.GraphRspDTO;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReqList;
import ai.plantdata.kgcloud.sdk.req.app.explore.common.BasicStatisticReq;
import ai.plantdata.kgcloud.sdk.req.app.function.*;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.StatisticRsp;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 16:33
 */
public interface GraphHelperService {
    /**
     * 查询概念id
     *
     * @param kgName  图谱名称
     * @param keyList 概念key
     * @return
     */
    List<Long> queryConceptByKey(String kgName, List<String> keyList);

    /**
     * 替换conceptKey
     *
     * @param kgName        图谱名称
     * @param conceptKeyReq req
     */
    void replaceByConceptKey(String kgName, ConceptKeyListReqInterface conceptKeyReq);


    /**
     * 替换conceptKey
     *
     * @param kgName        图谱名称
     * @param conceptKeyReq req
     */
    void replaceByConceptKey(String kgName, ConceptKeyReqInterface conceptKeyReq);

    /**
     * 替换属性key 为attrs  Id
     *
     * @param kgName        图谱名称
     * @param attrDefKeyReq req
     */
    void replaceByAttrKey(String kgName, AttrDefListKeyReqInterface attrDefKeyReq);

    void replaceByAttrKey(String kgName, AttrDefKeyReqInterface attrDefKeyReq, boolean requireAny);


    Map<Long, BasicInfo> getConceptIdMap(String kgName);

    /**
     * 图探索 属性，概念 key->转id
     *
     * @param kgName
     * @param exploreReq
     * @param <T>
     * @return
     */
    <T extends BasicGraphExploreReqList> T keyToId(String kgName, T exploreReq);

    /**
     * 前置搜索
     *
     * @param kgName
     * @param req
     * @param rsp
     * @param <T>
     * @return
     */
    <T extends BasicGraphExploreRsp> Optional<T> graphSearchBefore(String kgName, SecondaryScreeningInterface req, T rsp);

    /**
     * 构建统计结果
     *
     * @param kgName
     * @param configList
     * @param pathAnalysisRsp
     * @param graphAfter
     * @param <T>
     * @return
     */
    <T extends StatisticRsp> T buildExploreRspWithStatistic(String kgName, List<BasicStatisticReq> configList, T pathAnalysisRsp, GraphRspDTO graphAfter);

    /**
     * 实体关键字替换为id
     *
     * @param kgName
     * @param req
     */
    void replaceEntityNameToId(String kgName, EntityName2Id req);

    /**
     * 替换知识卡片中实体名称 为 实体id 精确
     *
     * @param kgName
     * @param req
     */
    void replaceEntityNameToId(String kgName, BatchEntityName2Id req);
}
