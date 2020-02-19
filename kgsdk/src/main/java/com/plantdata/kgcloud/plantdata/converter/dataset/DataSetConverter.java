package com.plantdata.kgcloud.plantdata.converter.dataset;

import com.plantdata.kgcloud.plantdata.req.dataset.ReadParameter;
import com.plantdata.kgcloud.plantdata.req.dataset.SearchParameter;
import com.plantdata.kgcloud.sdk.req.app.dataset.NameReadReq;
import lombok.NonNull;

/**
 * @author cjw
 */
public class DataSetConverter {

    public static NameReadReq readParameterToNameReadReq(@NonNull ReadParameter param) {
        NameReadReq nameReadReq = new NameReadReq();
        nameReadReq.setDataName(param.getDataName());
        nameReadReq.setFields(param.getFields());
        nameReadReq.setQuery(param.getQuery());
        nameReadReq.setSort(param.getSort());
        nameReadReq.setPage(param.getPageNo());
        nameReadReq.setSize(param.getPageSize());
        return nameReadReq;
    }

    public static  NameReadReq searchParameterToNameReadReq(@NonNull SearchParameter param) {
        NameReadReq nameReadReq = new NameReadReq();
        nameReadReq.setDataName(param.getDataName());
        nameReadReq.setFields(param.getFields());
        nameReadReq.setQuery(param.getQuery());
        nameReadReq.setSort(param.getSort());
        nameReadReq.setPage(param.getPageNo());
        nameReadReq.setSize(param.getPageSize());
        return nameReadReq;
    }
}
