package com.plantdata.kgcloud.domain.app.service.impl;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.config.EsProperties;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.bo.DataSetStatisticBO;
import com.plantdata.kgcloud.domain.app.service.DataSetSearchService;
import com.plantdata.kgcloud.domain.app.service.DataSetStatisticService;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetRepository;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.DataType;
import com.plantdata.kgcloud.sdk.constant.DimensionEnum;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    @Autowired
    private DataSetSearchService dataSetSearchService;
    @Autowired
    private EsProperties esProperties;


    @Override
    public DataSetStatisticRsp statisticByDimensionAndTable(String userId, TableStatisticByDimensionalReq dimensionalReq, DimensionEnum dimension) {
        DataSetStatisticBO statistic = new DataSetStatisticBO().init(dimensionalReq.getAggs(), dimensionalReq.getQuery(), dimension, dimensionalReq.getReturnType());
        Map<String, Object> mapRestData = dataSetSearchService.readEsDataSet(esProperties.getAddrs(), dimensionalReq.getDataBaseList(), dimensionalReq.getTableList(), Collections.emptyList(), dimensionalReq.getAggs(), dimensionalReq.getQuery(), null, 0, dimensionalReq.getSize());
        try {
            return statistic.postDealData(mapRestData);
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(AppErrorCodeEnum.ES_RULE_ERROR);
        }
    }

    @Override
    public DataSetStatisticRsp statisticByDimension(String userId, StatisticByDimensionalReq dimensionalReq, String dataName, DimensionEnum dimension) {
        //查询数据集存储信息
        Optional<DataSet> dataOpt = dataSetRepository.findByDataName(dataName);
        if (!dataOpt.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS);
        }
        DataSet dataSet = dataOpt.get();
        if (!DataType.ELASTIC.equals(dataSet.getDataType())) {
            throw BizException.of(AppErrorCodeEnum.MY_DATA_NULL_ES);
        }
        TableStatisticByDimensionalReq tableStatisticReq = new TableStatisticByDimensionalReq();
        BeanUtils.copyProperties(dimensionalReq, tableStatisticReq);
        tableStatisticReq.setDataBaseList(Lists.newArrayList(dataSet.getDbName()));
        tableStatisticReq.setTableList(Lists.newArrayList(dataSet.getTbName()));
        return statisticByDimensionAndTable(userId, tableStatisticReq, dimension);
    }


}
