package com.plantdata.kgcloud.plantdata.converter.dataset;

import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.dataset.ReadParameter;
import com.plantdata.kgcloud.plantdata.req.dataset.SearchParameter;
import com.plantdata.kgcloud.plantdata.req.dataset.TwoDimensionalParameter;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import com.plantdata.kgcloud.util.JsonUtils;
import lombok.NonNull;

import java.util.Map;

/**
 * @author cjw
 */
public class DataSetConverter extends BasicConverter {

    public static NameReadReq readParameterToNameReadReq(@NonNull ReadParameter param) {
        NameReadReq nameReadReq = new NameReadReq();
        nameReadReq.setDataName(param.getDataName());
        nameReadReq.setFields(param.getFields());
        consumerIfNoNull(param.getQuery(), a -> nameReadReq.setQuery(JsonUtils.stringToMap(a)));
        consumerIfNoNull(param.getSort(), a -> {
            Map<String, Object> objectMap = JsonUtils.stringToMap(a);
            nameReadReq.setSort(objectMap);
        });

        nameReadReq.setPage(param.getPageNo());
        nameReadReq.setSize(param.getPageSize());
        return nameReadReq;
    }

    public static NameReadReq searchParameterToNameReadReq(@NonNull SearchParameter param) {
        NameReadReq nameReadReq = new NameReadReq();
        nameReadReq.setDataName(param.getDataName());
        nameReadReq.setFields(param.getFields());
        consumerIfNoNull(param.getQuery(), a -> nameReadReq.setQuery(JsonUtils.stringToMap(a)));
        consumerIfNoNull(param.getSort(), a -> {
            Map<String, Object> objectMap = JsonUtils.stringToMap(a);
            nameReadReq.setSort(objectMap);
        });
        nameReadReq.setPage(param.getPageNo());
        nameReadReq.setSize(param.getPageSize());
        return nameReadReq;
    }

    public static StatisticByDimensionalReq twoDimensionalParameterToStatisticByDimensionalReq(TwoDimensionalParameter param) {
        StatisticByDimensionalReq statistic = new StatisticByDimensionalReq();
        statistic.setAggs(param.getAggs());
        statistic.setQuery(param.getQuery());
        statistic.setReturnType(param.getReturnType());
        statistic.setSize(param.getPageSize());
        return statistic;
    }
}
