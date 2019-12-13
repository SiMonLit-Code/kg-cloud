package com.plantdata.kgcloud.domain.app.service.impl;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.bo.DataSetStatisticBO;
import com.plantdata.kgcloud.domain.app.service.DataSetSearchService;
import com.plantdata.kgcloud.domain.app.service.DataSetStatisticService;
import com.plantdata.kgcloud.domain.app.util.EsUtils;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.dataset.constant.DataType;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetRepository;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.DimensionEnum;
import com.plantdata.kgcloud.sdk.req.DataSetSdkReq;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import com.plantdata.kgcloud.sdk.req.app.dataset.DataSetTwoDimStatisticReq;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private DataSetService dataSetService;
    @Autowired
    private DataOptService dataOptService;
    @Autowired
    private DataSetSearchService dataSetSearchService;

    @Override
    public DataSetStatisticRsp dataSetStatistic(String userId, DataSetTwoDimStatisticReq statisticReq, DimensionEnum dimension) {
        Optional<DataSet> dataOpt = dataSetRepository.findByDataName(statisticReq.getDataName());
        if (!dataOpt.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS);
        }
        DataSetStatisticBO statistic = new DataSetStatisticBO().init(statisticReq.getAggregation(), statisticReq.getQuery(), dimension, statisticReq.getReturnType());
        DataOptProvider provider = dataOptService.getProvider(userId, dataOpt.get().getId());

        List<Map<String, Object>> maps = provider.find(0, Integer.MAX_VALUE - 1, statistic.getEsDTO().parseMap());
        return statistic.postDealData(maps);
    }

    @Override
    public DataSetStatisticRsp statisticByDimensionAndTable(String userId, TableStatisticByDimensionalReq dimensionalReq, DimensionEnum dimension) {
        DataSetStatisticBO statistic = new DataSetStatisticBO().init(dimensionalReq.getAggs(), dimensionalReq.getQuery(), dimension, dimensionalReq.getReturnType());
        RestData<Map<String, Object>> mapRestData = dataSetSearchService.readEsDataSet(dimensionalReq.getDataBaseList(), dimensionalReq.getTableList(), Collections.emptyList(), dimensionalReq.getQuery(), null, 0, 0);
        return statistic.postDealData(mapRestData.getRsData());
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
            throw BizException.of(KgmsErrorCodeEnum.MY_DATA_NULL_ES);
        }
        TableStatisticByDimensionalReq tableStatisticReq = new TableStatisticByDimensionalReq();
        BeanUtils.copyProperties(dimensionalReq, tableStatisticReq);
        tableStatisticReq.setDataBaseList(Lists.newArrayList(dataSet.getDbName()));
        tableStatisticReq.setTableList(Lists.newArrayList(dataSet.getTbName()));
        return statisticByDimensionAndTable(userId, tableStatisticReq, dimension);
    }


}
