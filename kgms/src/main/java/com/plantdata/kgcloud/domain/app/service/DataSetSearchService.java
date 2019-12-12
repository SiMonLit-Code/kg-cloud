package com.plantdata.kgcloud.domain.app.service;

import com.plantdata.kgcloud.sdk.rsp.app.RestData;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/12 15:13
 */
public interface DataSetSearchService {


    RestData<Map<String, Object>> readMongoData(String database, String table, int start, int offset, String query, List<String> fieldList, String sort);
}
