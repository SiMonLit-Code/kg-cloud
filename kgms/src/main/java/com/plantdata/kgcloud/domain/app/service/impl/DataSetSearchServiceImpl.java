package com.plantdata.kgcloud.domain.app.service.impl;

import ai.plantdata.kg.api.pub.MongoApi;
import ai.plantdata.kg.api.pub.req.MongoQueryFrom;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.plantdata.kgcloud.config.EsProperties;
import com.plantdata.kgcloud.domain.app.service.DataSetSearchService;
import com.plantdata.kgcloud.domain.app.util.EsUtils;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.sdk.rsp.app.main.DataLinkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.LinksRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Administrator
 */
@Service
public class DataSetSearchServiceImpl implements DataSetSearchService {

    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private EsProperties esProperties;
    @Autowired
    private MongoApi mongoApi;
    @Autowired
    private DataSetService dataSetService;
    @Autowired
    private DataOptService dataOptService;

    @Override
    public RestData<Map<String, Object>> readMongoData(String database, String table, int start, int offset, String query, List<String> fieldList, String sort) {
        Document bson = new Document();
        if (StringUtils.isNoneBlank(query)) {
            bson.putAll(JSON.parseObject(query));
        }
        Document field = new Document();
        if (fieldList != null) {
            fieldList.forEach(s -> field.put(s, 1));
        }
        Document sortDoc = new Document();
        if (StringUtils.isNoneBlank(sort)) {
            sortDoc.putAll(JSON.parseObject(sort));
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

    @Override
    public RestData<Map<String, Object>> readEsDataSet(List<String> databases, List<String> tables, List<String> fields, String query, String sort, int start, int offset) {
        Map<String, Object> requestData = Maps.newHashMap();
        if (Objects.nonNull(fields) && fields.size() > 0) {
            requestData.put("_source", fields);
        }
        if (StringUtils.isNoneBlank(query)) {
            requestData.put("query", JSON.parse(query));
            requestData.put("from", start);
            requestData.put("size", offset);
        }
        if (StringUtils.isNoneBlank(sort)) {
            requestData.put("sort", JSON.parse(sort));
        }
        String rs = EsUtils.sendPost(EsUtils.buildEsQuery(esProperties.getAddrs()), databases, tables, JSON.toJSONString(requestData));
        List<Map<String, Object>> rsList = new ArrayList<>(offset);
        long rsCount = 0;
        if (Objects.nonNull(rs)) {
            JSONObject jsonObj = JSON.parseObject(rs);
            JSONObject hits = jsonObj.getJSONObject("hits");
            JSONArray arr = hits.getJSONArray("hits");
            if (Objects.nonNull(arr)) {
                rsCount = hits.getLong("total");
                if (rsCount > 0) {
                    for (int i = 0; i < arr.size(); i++) {
                        Map<String, Object> map = arr.getJSONObject(i).getJSONObject("_source");
                        map.put("_id", arr.getJSONObject(i).getString("_id"));
                        map.put("_type", arr.getJSONObject(i).getString("_type"));
                        map.put("_index", arr.getJSONObject(i).getString("_index"));
                        rsList.add(map);
                    }
                }
            }
        }
        return new RestData<>(rsList, rsCount);
    }
    @Override
    public List<DataLinkRsp> getDataLinks(String kgName, String userId, Long entityId) {

        MongoQueryFrom mongoQueryFrom = new MongoQueryFrom();
        mongoQueryFrom.setKgName(kgName);
        mongoQueryFrom.setCollection("entity_annotation");
        List<Map<String, Object>> query = JacksonUtils.readValue("[{ $match : { entity_id : " + entityId.toString() + " } },{'$group':{'_id':'$data_set_id',count:{'$sum':1}}},{$limit : 5}]", new TypeReference<List<Map<String, Object>>>() {
        });
        mongoQueryFrom.setQuery(query);
        Optional<List<Map<String, Object>>> opt = RestRespConverter.convert(mongoApi.postJson(mongoQueryFrom));
        if (!opt.isPresent() || CollectionUtils.isEmpty(opt.get())) {
            return Collections.emptyList();
        }
        List<DataLinkRsp> dataLinks = new ArrayList<>();

        for (Map<String, Object> map : opt.get()) {
            Long dataSetId = Long.valueOf(map.get("_id").toString());

            query = JacksonUtils.readValue("[{ $match : { data_set_id:" + dataSetId + ",entity_id : " + entityId.toString() + " } },{ $sort: {score: -1 }},{$limit : 5}]", new TypeReference<List<Map<String, Object>>>() {
            });
            mongoQueryFrom.setQuery(query);
            Optional<List<Map<String, Object>>> oneOpt = RestRespConverter.convert(mongoApi.postJson(mongoQueryFrom));
            if (!oneOpt.isPresent()) {
                continue;
            }
            DataLinkRsp dataLink = new DataLinkRsp();
            dataLink.setDataSetId(dataSetId);
            List<LinksRsp> linkList = new ArrayList<>();
            for (Map<String, Object> map1 : oneOpt.get()) {
                LinksRsp links = new LinksRsp();
                links.setDataId(map1.get("data_id").toString());
                links.setScore(Double.valueOf(map1.get("score").toString()));
                links.setScore(Double.valueOf(map1.get("source").toString()));
                Map<String, String> mydata = getDataTitle(userId, map1.get("data_id").toString(), dataSetId);
                if (CollectionUtils.isEmpty(mydata)) {
                    continue;
                }
                if (dataLink.getDataSetTitle() == null) {
                    dataLink.setDataSetTitle(mydata.get("dataSetTitle"));
                }
                links.setDataTitle(mydata.get("dataTitle"));
                linkList.add(links);
            }
            dataLink.setLinks(linkList);
            dataLinks.add(dataLink);
        }
        return dataLinks;
    }

    /**
     * 多重循环查询 改不动
     *
     * @param userId
     * @param data_id
     * @param dataSetId
     * @return
     */
    private Map<String, String> getDataTitle(String userId, String data_id, Long dataSetId) {
        DataSet dataSet = dataSetService.findOne(userId, dataSetId);
        if (dataSet == null) {
            return Collections.emptyMap();
        }
        String dataTitle = StringUtils.EMPTY;
        Optional<DataSetSchema> schemaOpt = dataSet.getSchema().stream().filter(a -> (boolean) a.getSettings().get("setTitle")).findAny();
        if (schemaOpt.isPresent()) {
            DataOptConnect connect = DataOptConnect.of(dataSet);
            DataOptProvider provider = DataOptProviderFactory.createProvider(connect, dataSet.getDataType());
            Map<String, Object> dataMap = provider.findOne(data_id);
            dataTitle = dataMap.get("key").toString();
        }
        Map<String, String> map = Maps.newHashMapWithExpectedSize(NumberUtils.INTEGER_TWO);
        map.put("dataSetTitle", dataSet.getTitle());
        map.put("dataTitle", dataTitle);
        return map;
    }
}
