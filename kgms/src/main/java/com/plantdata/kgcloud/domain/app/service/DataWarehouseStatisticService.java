package com.plantdata.kgcloud.domain.app.service;


import com.plantdata.kgcloud.sdk.constant.DimensionEnum;
import com.plantdata.kgcloud.sdk.req.SqlQueryReq;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import com.plantdata.kgcloud.sdk.rsp.DataWarehouse2dTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DataWarehouse3dTableRsp;

/**
 * @author cx
 * @version 1.0
 * @date 2020/4/17 15:40
 */
public interface DataWarehouseStatisticService {

    /**
     *
     *
     * @param
     * @return ...
     */
    DataWarehouse2dTableRsp statisticBy2DTable(SqlQueryReq req);

    /**
     *
     *
     * @param
     * @return ...
     */
    DataWarehouse3dTableRsp statisticBy3DTable(SqlQueryReq req);
}
