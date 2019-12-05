package com.plantdata.kgcloud.domain.data.statistics.service.impl;

import com.plantdata.kgcloud.domain.data.obtain.req.CountRelationByEntityReq;
import com.plantdata.kgcloud.domain.common.rsp.DegreeRsp;
import com.plantdata.kgcloud.domain.data.statistics.service.GraphDataStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 18:20
 */
@Service
public class GraphDataStatisticsServiceImpl implements GraphDataStatisticsService {
    @Override
    public List<DegreeRsp> statCountRelationByEntity(CountRelationByEntityReq param) {
        return null;
    }
}
