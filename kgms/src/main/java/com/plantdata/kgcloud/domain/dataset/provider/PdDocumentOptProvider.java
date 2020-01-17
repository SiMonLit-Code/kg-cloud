package com.plantdata.kgcloud.domain.dataset.provider;

import com.plantdata.kgcloud.domain.dataset.converter.ApiReturnConverter;
import com.plantdata.kgcloud.sdk.KgtextClient;
import com.plantdata.kgcloud.sdk.req.CorpusSearchReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.rsp.CorpusRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 12:20
 **/
public class PdDocumentOptProvider implements DataOptProvider {

    private final String database;

    public PdDocumentOptProvider(DataOptConnect info) {
        database = info.getDatabase();
    }

    @Autowired
    private KgtextClient kgtextClient;

    @Override
    public List<String> getFields() {
        return null;
    }

    @Override
    public List<Map<String, Object>> find(Integer offset, Integer limit, Map<String, Object> query) {
        CorpusSearchReq corpusSearchReq = new CorpusSearchReq();
        corpusSearchReq.setCpId(Long.parseLong(database));
        corpusSearchReq.setPage(offset + 1);
        corpusSearchReq.setSize(limit);
        Optional<Page<CorpusRsp>> optional = ApiReturnConverter.convert(kgtextClient.listCorpuses(corpusSearchReq));
        if (!optional.isPresent()) {
            return null;
        } else {
            Page<CorpusRsp> corpusRsps = optional.get();
            List<CorpusRsp> contents = corpusRsps.getContent();
            return contents.stream().map(this::beanToMap).collect(Collectors.toList());
        }
    }

    private Map<String, Object> beanToMap(CorpusRsp corpusRsp) {
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo b = Introspector.getBeanInfo(corpusRsp.getClass(), CorpusRsp.class);
            PropertyDescriptor[] pds = b.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                String propertyName = pd.getName();
                Method m = pd.getReadMethod();
                Object properValue = m.invoke(corpusRsp);
                map.put(propertyName, properValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> findWithSort(Integer offset, Integer limit, Map<String, Object> query,
                                                  Map<String, Object> sort) {
        return null;
    }

    @Override
    public long count(Map<String, Object> query) {
        return 0;
    }

    @Override
    public Map<String, Object> findOne(String id) {
        Optional<CorpusRsp> optional = ApiReturnConverter.convert(kgtextClient.getDetails(Long.parseLong(database),
                id));
        if (!optional.isPresent()) {
            return null;
        } else {
            CorpusRsp corpusRsp = optional.get();
            return this.beanToMap(corpusRsp);
        }

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
