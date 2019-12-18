package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.sdk.rsp.app.main.DataLinkRsp;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 15:13
 */
public interface DataSetSearchService {
    /**
     * 查询关联数据集
     *
     * @param kgName
     * @param userId
     * @param entityId
     * @return
     */
    List<DataLinkRsp> getDataLinks(String kgName, String userId, Long entityId) throws IOException;

    RestData<Map<String, Object>> readDataSetData(DataSet dataSet, int offset, int limit, String query);

    RestData<Map<String, Object>> readEsDataSet(List<String> addressList, List<String> databases, List<String> tables, List<String> fields, String query, String sort, int start, int offset);
}
