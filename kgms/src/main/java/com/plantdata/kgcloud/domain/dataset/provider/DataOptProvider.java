package com.plantdata.kgcloud.domain.dataset.provider;

import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;

import java.io.Closeable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

public interface DataOptProvider extends Closeable {

    List<String> getFields();

    List<Map<String, Object>> find(Integer offset, Integer limit, Map<String, Object> query);

    List<Map<String, Object>> findWithSort(Integer offset, Integer limit, Map<String, Object> query, Map<String, Object> sort);

    long count(Map<String, Object> query);

    Map<String, Object> findOne(String id);

    void createTable(List<DataSetSchema> colList);

    void dropTable();

    Map<String, Object> insert(Map<String, Object> node);

    Map<String, Object> update(String id, Map<String, Object> node);

    void delete(String id);

    void deleteAll();

    void batchInsert(List<Map<String, Object>> nodes);

    void batchDelete(Collection<String> ids);

    List<Map<String, Long>> statistics();


    /**
     * 数据集下拉提示
     *
     * @param searchMap
     * @return
     */
    default List<Map<String, Object>> search(Map<String, String> searchMap,int offset,int limit) {
        return Collections.emptyList();
    }
}
