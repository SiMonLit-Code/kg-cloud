package com.plantdata.kgcloud.domain.dataset.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;


import java.io.Closeable;
import java.util.Collection;
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

    void createTable(List<DataSetSchema> colList);

    void dropTable();

    Map<String, Object> insert(JsonNode node);

    Map<String, Object> update(String id, JsonNode node);

    void delete(String id);

    void deleteAll();

    void batchInsert(List<JsonNode> nodes);

    void batchDelete(Collection<String> ids);

}
