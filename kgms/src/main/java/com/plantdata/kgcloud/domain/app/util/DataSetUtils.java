package com.plantdata.kgcloud.domain.app.util;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.DataSetConstants;

import java.util.Map;

/**
 * @author cjw
 * @date 2020/4/7  10:23
 */
public class DataSetUtils {

    public static Map<String, Object> buildSearchByDataType(Map<String, Object> queryMap) {
        if (queryMap == null) {
            queryMap = Maps.newHashMap();
        }
        Map<String, Object> searchMap = Maps.newHashMapWithExpectedSize(1);
        searchMap.put(DataSetConstants.QUERY_KEY, queryMap);
        return searchMap;
    }
}
