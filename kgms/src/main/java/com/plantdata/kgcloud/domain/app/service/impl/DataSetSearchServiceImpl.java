package com.plantdata.kgcloud.domain.app.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.plantdata.kgcloud.domain.app.service.DataSetSearchService;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Administrator
 */
@Service
public class DataSetSearchServiceImpl implements DataSetSearchService {

    @Autowired
    private MongoClient mongoClient;


    @Override
    public RestData<Map<String, Object>> readMongoData(String database, String table, int start, int offset, String query, List<String> fieldList, String sort) {
        Document bson = new Document();
        if (StringUtils.isNoneBlank(query)) {
            bson.putAll(JacksonUtils.readValue(query, new TypeReference<Map<String, Object>>() {
            }));
        }
        Document field = new Document();
        if (fieldList != null) {
            fieldList.forEach(s -> field.put(s, 1));
        }
        Document sortDoc = new Document();
        if (StringUtils.isNoneBlank(sort)) {
            sortDoc.putAll(JacksonUtils.readValue(sort, new TypeReference<Map<String, Object>>() {
            }));
        }
        final List<Map<String, Object>> rsList = Lists.newArrayList();
        long rsCount = 0;
        try {
            MongoCollection<Document> collection = mongoClient.getDatabase(database).getCollection(table);

            rsCount = collection.count(bson);
            FindIterable<Document> find = collection.find(bson);
            if (field.size() != 0) {
                find = find.projection(field);
            }
            if (sortDoc.size() != 0) {
                find = find.sort(sortDoc);
            }
            for (Document document : find.skip(start).limit(offset)) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("_id", document.get("_id").toString());
                for (Entry<String, Object> m : document.entrySet()) {
                    if (!"_id".equals(m.getKey())) {
                        map.put(m.getKey(), m.getValue());
                    }
                }
                rsList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new RestData<>(rsList, rsCount);
    }

//    public RestData<Map<String, Object>> searchData(String ip, Integer port, String username, String password,String database, List<String> tables, List<String> fields, String query, String sort, int start, int offset) {
//
//        Map<String, Object> requestData = Maps.newHashMap();
//
//        if (Objects.nonNull(fields) && fields.size() > 0) {
//            requestData.put("_source", fields);
//        }
//
//        if (StringUtils.isNoneBlank(query)) {
//            requestData.put("query", JSON.parse(query));
//            requestData.put("from", start);
//            requestData.put("size", offset);
//        }
//
//        if (StringUtils.isNoneBlank(sort)) {
//            requestData.put("sort", JSON.parse(sort));
//        }
//        DataOptConnect dataOptConnect = new DataOptConnect();
//        dataOptConnect.setDatabase(databases);
//        ElasticSearchOptProvider optProvider = new ElasticSearchOptProvider();
//        String rs = EsRestClient.sendPost(ip, port, databases, tables, JSON.toJSONString(requestData));
//        List<Map<String, Object>> rsList = new ArrayList<>(offset);
//        long rsCount = 0;
//        if (Objects.nonNull(rs)) {
//            JSONObject jsonObj = JSON.parseObject(rs);
//            JSONObject hits = jsonObj.getJSONObject("hits");
//            JSONArray arr = hits.getJSONArray("hits");
//            if (Objects.nonNull(arr)) {
//                rsCount = hits.getLong("total");
//                if (rsCount > 0) {
//                    for (int i = 0; i < arr.size(); i++) {
//                        Map<String, Object> map = arr.getJSONObject(i).getJSONObject("_source");
//                        map.put("_id", arr.getJSONObject(i).getString("_id"));
//                        map.put("_type", arr.getJSONObject(i).getString("_type"));
//                        map.put("_index", arr.getJSONObject(i).getString("_index"));
//                        rsList.add(map);
//                    }
//                }
//            }
//        }
//        return new RestData<>(rsList, rsCount);
//    }
}
