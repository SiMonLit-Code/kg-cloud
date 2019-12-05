package com.plantdata.kgcloud.domain.dataset.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 21:42
 **/
@Slf4j
public class NopProvider implements DataOptProvider {
    public NopProvider() {
        throw BizException.of(KgmsErrorCodeEnum.DATASET_TYPE_NONSUPPORT);
    }


    @Override
    public List<String> getFields() {
        return null;
    }

    @Override
    public List<Map<String, Object>> find(Integer offset, Integer limit, Map<String, Object> query) {
        return null;
    }

    @Override
    public void createTable(List<DataSetSchema> colList) {

    }

    @Override
    public void dropTable() {

    }

    @Override
    public Map<String, Object> insert(JsonNode node) {
        return null;
    }

    @Override
    public Map<String, Object> update(String id, JsonNode node) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void batchInsert(List<JsonNode> nodes) {

    }


    @Override
    public void batchDelete(Collection<String> ids) {

    }

    @Override
    public void close() throws IOException {

    }
}
