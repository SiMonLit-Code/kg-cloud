package com.plantdata.kgcloud.domain.app.service.impl;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.bo.DataSetStatisticBO;
import com.plantdata.kgcloud.domain.app.dto.DataCountRsp;
import com.plantdata.kgcloud.domain.app.service.DataSetStatisticService;
import com.plantdata.kgcloud.domain.dataset.constant.DataType;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetRepository;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.DimensionEnum;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/7 15:13
 */
@Service
public class DataSetStatisticStatisticServiceImpl implements DataSetStatisticService {

    @Autowired
    private DataSetRepository dataSetRepository;

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
        Optional<DataSet> dataOpt = dataSetRepository.findByDataName(dataSetKey);
        if (!dataOpt.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS);
        }
        DataSet dataSet = dataOpt.get();
        if (!DataType.ELASTIC.equals(dataSet.getDataType())) {
            throw BizException.of(KgmsErrorCodeEnum.MY_DATA_NULL_ES);
        }
        TableStatisticByDimensionalReq tableStatisticReq = new TableStatisticByDimensionalReq();
        BeanUtils.copyProperties(dimensionalReq, tableStatisticReq);
        tableStatisticReq.setDatabases(Lists.newArrayList(dataSet.getDbName()));
        tableStatisticReq.setTables(Lists.newArrayList(dataSet.getTbName()));
        return statisticByDimensionAndTable(tableStatisticReq, dimension);
    }


}
