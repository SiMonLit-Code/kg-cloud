package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/9 20:39
 */
public interface KgDataService {

    /**
     * 根据实体统计边
     *
     * @param kgName       图谱名称
     * @param statisticReq 统计req
     * @return 。
     */
    List<Map<String, Object>> statisticCountEdgeByEntity(String kgName, EdgeStatisticByEntityIdReq statisticReq);

    /**
     * 根据概念统计实体
     *
     * @param kgName       图谱名称
     * @param statisticReq 统计req
     * @return 。
     */
    Object statEntityGroupByConcept(String kgName, EntityStatisticGroupByConceptReq statisticReq);
}
