package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByEntityIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;

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
     * 统计实体 根据概念分组
     *
     * @param kgName       图谱名称
     * @param statisticReq 统计req
     * @return 。
     */
    Object statEntityGroupByConcept(String kgName, EntityStatisticGroupByConceptReq statisticReq);

    /**
     * 统计属性 根据概念分组
     *
     * @param kgName    图谱名称
     * @param attrIdReq req
     * @return
     */
    Object statisticAttrGroupByConcept(String kgName, EntityStatisticGroupByAttrIdReq attrIdReq);

    /**
     * 关系统计
     *
     * @param kgName       图谱名称
     * @param conceptIdReq req
     * @return 。
     */
    Object statisticRelation(String kgName, EdgeStatisticByConceptIdReq conceptIdReq);

    /**
     * 边属性统计
     *
     * @param kgName       图谱名称
     * @param statisticReq req
     * @return 。
     */
    Object statEdgeGroupByEdgeValue(String kgName, EdgeAttrStatisticByAttrValueReq statisticReq);

    /**
     * 读取数据集
     *
     * @param userId      用户id
     * @param nameReadReq 。。
     * @return a
     */
    RestData<Map<String, Object>> searchDataSet(String userId, NameReadReq nameReadReq);


}
