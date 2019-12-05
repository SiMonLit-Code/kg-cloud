package com.plantdata.kgcloud.domain.data.statistics.service;

import com.plantdata.kgcloud.domain.data.obtain.req.CountRelationByEntityReq;
import com.plantdata.kgcloud.domain.common.rsp.DegreeRsp;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 17:39
 */
public interface GraphDataStatisticsService {

    /**
     * 查询实体的关系度数
     *
     * @param param 参数
     * @return 关系度数统计
     */
    List<DegreeRsp> statCountRelationByEntity(CountRelationByEntityReq param);
}
