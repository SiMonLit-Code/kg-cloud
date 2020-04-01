package com.plantdata.kgcloud.domain.dataset.provider;

import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.dataset.converter.ApiReturnConverter;
import com.plantdata.kgcloud.sdk.KgtextClient;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.util.SpringContextUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 12:20
 **/
public class PdDocumentOptProvider implements DataOptProvider {

    private final String table;

    private KgtextClient kgtextClient;

    public PdDocumentOptProvider(DataOptConnect info) {
        table = info.getTable();
        kgtextClient = SpringContextUtils.getBean(KgtextClient.class);
    }


    @Override
    public List<String> getFields() {
        return null;
    }

    @Override
    public List<Map<String, Object>> find(Integer offset, Integer limit, Map<String, Object> query) {
        Long cpId = resolvingCorpus();
        int page = offset / limit + 1;
        Optional<BasePage<Map<String, Object>>> optional =
                ApiReturnConverter.convert(kgtextClient.listDataCorpuses(cpId,
                        page, limit, null, null, null, null, null, null, null, null));
        if (!optional.isPresent()) {
            return null;
        } else {
            BasePage<Map<String, Object>> corpusRsps = optional.get();
            return corpusRsps.getContent();
        }
    }

    private Long resolvingCorpus() {
        int indexOf = table.indexOf("_");
        String corpusId = table.substring(0, indexOf);
        return Long.parseLong(corpusId);
    }

    @Override
    public List<Map<String, Object>> findWithSort(Integer offset, Integer limit, Map<String, Object> query,
                                                  Map<String, Object> sort) {
        return null;
    }

    @Override
    public long count(Map<String, Object> query) {
        Long cpId = resolvingCorpus();
        Optional<BasePage<Map<String, Object>>> optional =
                ApiReturnConverter.convert(kgtextClient.listDataCorpuses(cpId, 1, 10, null, null, null, null, null,
                        null, null, null));
        if (!optional.isPresent()) {
            return 0;
        } else {
            BasePage<Map<String, Object>> corpusRsps = optional.get();
            return corpusRsps.getTotalElements();
        }
    }

    @Override
    public Map<String, Object> findOne(String id) {
        Optional<Map<String, Object>> optional =
                ApiReturnConverter.convert(kgtextClient.getDataDetails(resolvingCorpus(), id));
        return optional.orElse(null);
    }

    @Override
    public void createTable(List<DataSetSchema> colList) {

    }

    @Override
    public void dropTable() {

    }

    @Override
    public Map<String, Object> insert(Map<String, Object> node) {
        return null;
    }

    @Override
    public Map<String, Object> update(String id, Map<String, Object> node) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void batchInsert(List<Map<String, Object>> nodes) {

    }


    @Override
    public void batchDelete(Collection<String> ids) {

    }

    @Override
    public List<Map<String, Long>> statistics() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
