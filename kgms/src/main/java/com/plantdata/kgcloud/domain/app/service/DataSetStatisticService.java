package com.plantdata.kgcloud.domain.app.service;


import com.plantdata.kgcloud.sdk.constant.DimensionEnum;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 15:12
 */
public interface DataSetStatisticService {

    /**
     * es数据集按照维度统计
     *
     * @param dimensionalReq 相关参数
     * @param dimension      维度
     * @return ...
     */
    DataSetStatisticRsp statisticByDimensionAndTable(TableStatisticByDimensionalReq dimensionalReq, DimensionEnum dimension);

    /**
     * es数据集按照mysql存储的 database & table & 维度统计
     *
     * @param dimensionalReq 相关参数
     * @param dimension      维度
     * @param dataSetKey     数据集唯一标识
     * @return ...
     */
    DataSetStatisticRsp statisticByDimension(StatisticByDimensionalReq dimensionalReq, String dataSetKey, DimensionEnum dimension);
}
