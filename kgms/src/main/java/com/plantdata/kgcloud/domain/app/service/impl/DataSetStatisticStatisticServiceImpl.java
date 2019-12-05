package com.plantdata.kgcloud.domain.app.service.impl;

import com.plantdata.kgcloud.domain.app.bo.DataSetStatisticBO;
import com.plantdata.kgcloud.domain.app.dto.DataCountRsp;
import com.plantdata.kgcloud.domain.app.service.DataSetStatisticService;
import com.plantdata.kgcloud.sdk.constant.DimensionEnum;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 15:13
 */
@Service
public class DataSetStatisticStatisticServiceImpl implements DataSetStatisticService {


    @Override
    public DataSetStatisticRsp statisticByDimensionAndTable(TableStatisticByDimensionalReq param, DimensionEnum dimension) {

        DataSetStatisticBO statistic = new DataSetStatisticBO().init(param.getAggs(), param.getQuery(), dimension, param.getReturnType());

        DataCountRsp<Map<String, Object>> mapDataCountRsp = null;
        ///todo kgms提供sdk
        // elasticSearchRepository.searchData(param.getDatabases(), param.getTables(), statistic.getEsDTO());

        return statistic.postDealData(mapDataCountRsp.getRsData());
    }

    @Override
    public DataSetStatisticRsp statisticByDimension(StatisticByDimensionalReq dimensionalReq, String dataSetKey, DimensionEnum dimension) {
        //查询数据集存储信息
        //todo kgms提供sdk
        TableStatisticByDimensionalReq tableStatisticReq = new TableStatisticByDimensionalReq();
        BeanUtils.copyProperties(dimensionalReq, tableStatisticReq);
        tableStatisticReq.setDatabases(Collections.emptyList());
        tableStatisticReq.setTables(Collections.emptyList());
        return statisticByDimensionAndTable(tableStatisticReq, dimension);
    }


}
