package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.constant.ExportTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryWithConditionReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.req.app.statistic.*;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.EdgeStatisticByEntityIdRsp;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
    List<EdgeStatisticByEntityIdRsp> statisticCountEdgeByEntity(String kgName, EdgeStatisticByEntityIdReq statisticReq);


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
     * &
     * 导出sparQl 查询结果
     *
     * @param kgName
     * @param type
     * @param query
     * @param size
     * @param response
     * @throws IOException
     */
    void sparkSqlExport(String kgName, ExportTypeEnum type, String query, int size, HttpServletResponse response) throws IOException;

    /**
     * 根据名称和消歧标识 查询实体
     *
     * @param kgName
     * @param conditionReqs
     * @return
     */
    List<OpenEntityRsp> queryEntityByNameAndMeaningTag(String kgName, List<EntityQueryWithConditionReq> conditionReqs);
}
