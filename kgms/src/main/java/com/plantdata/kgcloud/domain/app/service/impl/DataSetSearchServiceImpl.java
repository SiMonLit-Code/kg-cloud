package com.plantdata.kgcloud.domain.app.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import ai.plantdata.kg.api.pub.MongoApi;
import ai.plantdata.kg.api.pub.req.MongoQueryFrom;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.plantdata.kgcloud.config.EsProperties;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.service.DataSetSearchService;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import com.plantdata.kgcloud.domain.app.util.EsUtils;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.service.DataOptService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.sdk.rsp.app.main.DataLinkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.LinksRsp;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service
public class DataSetSearchServiceImpl implements DataSetSearchService {

    @Autowired
    private MongoApi mongoApi;
    @Autowired
    private DataSetService dataSetService;

    @Override
    public RestData<Map<String, Object>> readDataSetData(DataSet dataSet, int offset, int limit, String query, String sort) {
        DataOptConnect dataOptConnect = new DataOptConnect();
        dataOptConnect.setDatabase(dataSet.getDbName());
        dataOptConnect.setTable(dataSet.getTbName());
        dataOptConnect.setAddresses(dataSet.getAddr());
        DataOptProvider provider = DataOptProviderFactory.createProvider(dataOptConnect, dataSet.getDataType());
        Map<String, Object> queryMap;
        Map<String, Object> sortMap;
        try {
            queryMap = JacksonUtils.readValue(query, new TypeReference<Map<String, Object>>() {
            });
            sortMap = JacksonUtils.readValue(sort, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(AppErrorCodeEnum.ERROR_DATA_SET_QUERY);
        }
        List<Map<String, Object>> maps = provider.findWithSort(offset, limit, queryMap, sortMap);
        long count = provider.count(queryMap);
        return new RestData<>(maps, count);
    }

    @Override
    public RestData<Map<String, Object>> readEsDataSet(List<String> addressList, List<String> databases, List<String> tables, List<String> fields, String query, String sort, int start, int offset) {
        Map<String, Object> requestData = Maps.newHashMap();
        if (Objects.nonNull(fields) && fields.size() > 0) {
            requestData.put("_source", fields);
        }
        if (StringUtils.isNoneBlank(query)) {
            requestData.put("query", JacksonUtils.readValue(query, Object.class));
            requestData.put("from", start);
            requestData.put("size", offset);
        }
        if (StringUtils.isNoneBlank(sort)) {
            requestData.put("sort", JacksonUtils.readValue(sort, Object.class));
        }
        String rs = EsUtils.sendPost(EsUtils.buildEsQuery(addressList), databases, tables, JacksonUtils.writeValueAsString(requestData));
        List<Map<String, Object>> rsList = new ArrayList<>(offset);
        long rsCount = 0;
        Optional<JsonNode> jsonObjOpt = JsonUtils.parseJsonNode(rs);
        if (jsonObjOpt.isPresent()) {
            JsonNode jsonNode = jsonObjOpt.get();
            JsonNode hits = jsonNode.get("hits");
            ArrayNode arr = (ArrayNode) hits.get("hits");
            if (Objects.nonNull(arr)) {
                rsCount = hits.get("total").longValue();
                if (rsCount > 0) {
                    for (int i = 0; i < arr.size(); i++) {
                        Map<String, Object> map = (Map<String, Object>) arr.get(i).get("_source");
                        map.put("_id", arr.get(i).get("_id"));
                        map.put("_type", arr.get(i).get("_type"));
                        map.put("_index", arr.get(i).get("_index"));
                        rsList.add(map);
                    }
                }
            }
        }
        return new RestData<>(rsList, rsCount);
    }


    @Override
    public List<DataLinkRsp> getDataLinks(String kgName, String userId, Long entityId) throws IOException {
        MongoQueryFrom mongoQueryFrom = buildMongoQuery(kgName, entityId);

        Optional<List<Map<String, Object>>> opt = RestRespConverter.convert(mongoApi.postJson(mongoQueryFrom));
        if (!opt.isPresent() || CollectionUtils.isEmpty(opt.get())) {
            return Collections.emptyList();
        }
        List<DataLinkRsp> dataLinks = new ArrayList<>();

        for (Map<String, Object> map : opt.get()) {
            Long dataSetId = Long.valueOf(map.get("_id").toString());

            List<Map<String, Object>> query = JacksonUtils.getInstance().readValue("[{ $match : { data_set_id:" + dataSetId + ",entity_id : " + entityId.toString() + " } },{ $sort: {score: -1 }},{$limit : 5}]", new TypeReference<List<Map<String, Object>>>() {
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


    private static MongoQueryFrom buildMongoQuery(String kgName, long entityId) {
        MongoQueryFrom mongoQueryFrom = new MongoQueryFrom();
        mongoQueryFrom.setKgName(kgName);
        mongoQueryFrom.setCollection("entity_annotation");
        Map<String, Object> map = Maps.newHashMap();
        map.put("_id", "$data_set_id");
        map.put("count", DefaultUtils.oneElMap("$sum", 1));
        List<Map<String, Object>> mathMapList = Lists.newArrayList();
        mathMapList.add(DefaultUtils.oneElMap("$match", DefaultUtils.oneElMap("entity_id", String.valueOf(entityId))));
        mathMapList.add(DefaultUtils.oneElMap("$group", map));
        mongoQueryFrom.setQuery(mathMapList);
        return mongoQueryFrom;
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
