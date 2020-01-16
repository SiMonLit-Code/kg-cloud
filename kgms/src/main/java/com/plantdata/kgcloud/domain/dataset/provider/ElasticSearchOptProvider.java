package com.plantdata.kgcloud.domain.dataset.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Slf4j
public class ElasticSearchOptProvider implements DataOptProvider {


    private static final String PUT = "PUT";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String HEAD = "HEAD";
    private static final String DELETE = "DELETE";

    private final RestClient client;

    private final ObjectMapper objectMapper = JacksonUtils.getInstance();

    private final String database;

    private final String type;

    private BiFunction<JsonNode, Long, Map<String, Long>> smokeFun = (buckets, total) -> {
        Map<String, Long> smoke = new HashMap<>();
        long checked = 0L;
        if (buckets.isArray()) {
            for (JsonNode bucket : buckets) {
                String status = bucket.get("key_as_string").asText();
                long count = bucket.get("doc_count").asLong();
                checked += count;
                if (Objects.equals(status, "true")) {
                    smoke.put("2", count);
                } else if (Objects.equals(status, "false")) {
                    smoke.put("3", count);
                }
            }
            smoke.put("4", total - checked);
        }
        return smoke;
    };

    private BiFunction<JsonNode, Long, Map<String, Long>> smokeMsgFun = (buckets, total) -> {
        Map<String, Long> smoke = new HashMap<>();
        if (buckets.isArray()) {
            for (JsonNode bucket : buckets) {
                String msg = bucket.get("key").asText();
                long count = bucket.get("doc_count").asLong();
                smoke.put(msg, count);
            }
        }
        return smoke;
    };

    public ElasticSearchOptProvider(DataOptConnect info) {
        HttpHost[] hosts = info.getAddresses().stream()
                .map(this::buildHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        this.client = RestClient.builder(hosts).setMaxRetryTimeoutMillis(60000).build();
        this.database = info.getDatabase();
        this.type = info.getTable();
    }

    @Override
    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        Request request = new Request(GET, "/" + database + "/_mapping/" + type);
        Optional<String> send = send(request);
        String result = send.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        JsonNode node = readTree(result);
        JsonNode prop = node.get(database).get("mappings").get(type).get("properties");
        Iterator<String> iterator = prop.fieldNames();
        while (iterator.hasNext()) {
            fields.add(iterator.next());
        }
        return fields;
    }

    private ObjectNode buildQuery(Integer offset, Integer limit, Map<String, Object> query) {
        ObjectNode queryNode = objectMapper.createObjectNode();
        if (offset != null && offset >= 0) {
            queryNode.put("from", offset);
        }
        int size = limit != null && limit > 0 ? limit : 10;
        queryNode.put("size", size);
        if (CollectionUtils.isEmpty(query)) {
            return queryNode;
        }
        return handleQuery(queryNode, query);
    }

    private ObjectNode handleQuery(ObjectNode queryNode, Map<String, Object> query) {
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            if (Objects.equals(entry.getKey(), "sort")) {
                queryNode.put("sort", DataConst.CREATE_AT);
            }
            if (Objects.equals(entry.getKey(), "query")) {
                queryNode.putPOJO("query", entry.getValue());
            }
            if (Objects.equals(entry.getKey(), "search")) {
                Map<String, String> value = (Map<String, String>) entry.getValue();
                for (Map.Entry<String, String> objectEntry : value.entrySet()) {
                    String s = "{\"wildcard\":{\"" + objectEntry.getKey() + "\":{\"value\":\"*" + objectEntry.getValue() + "*\"}}}";
                    queryNode.putPOJO("query", JacksonUtils.readValue(s, JsonNode.class));
                }
            }
            if (Objects.equals(entry.getKey(), "resultType")) {
                Integer resultType = (Integer) entry.getValue();
                if (Objects.equals(resultType, 2)) {
                    String s = "{\"term\":{\"_smoke\":true}}";
                    queryNode.putPOJO("query", JacksonUtils.readValue(s, JsonNode.class));
                } else if (Objects.equals(resultType, 3)) {
                    String s = "{\"term\":{\"_smoke\":false}}";
                    queryNode.putPOJO("query", JacksonUtils.readValue(s, JsonNode.class));
                } else if (Objects.equals(resultType, 4)) {
                    String s = "{\"bool\":{\"must_not\":{\"exists\":{\"field\":\"_smoke\"}}}}";
                    queryNode.putPOJO("query", JacksonUtils.readValue(s, JsonNode.class));
                }
            }
        }
        return queryNode;
    }

    @Override
    public List<Map<String, Object>> find(Integer offset, Integer limit, Map<String, Object> query) {
        return findWithSort(offset, limit, query, null);
    }

    @Override
    public List<Map<String, Object>> findWithSort(Integer offset, Integer limit, Map<String, Object> query, Map<String, Object> sort) {
        if (!CollectionUtils.isEmpty(sort)) {
            query.put("sort", sort);
        }

        List<Map<String, Object>> mapList = new ArrayList<>();
        String endpoint = "/" + database + "/" + type + "/_search";
        if (!StringUtils.hasText(type)) {
            endpoint = "/" + database + "/_search";
        }

        Request request = new Request(RequestMethod.POST.toString(), endpoint);
        ObjectNode queryNode = buildQuery(offset, limit, query);
        NStringEntity entity = new NStringEntity(JacksonUtils.writeValueAsString(queryNode), ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        Optional<String> send = send(request);
        String result = send.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        JsonNode node = readTree(result);
        for (JsonNode jsonNode : node.get("hits").get("hits")) {
            String id = jsonNode.get("_id").asText();
            Map<String, Object> map = JacksonUtils.readValue(jsonNode.get("_source").traverse(), new TypeReference<Map<String, Object>>() {
            });
            map.put("_id", id);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public long count(Map<String, Object> query) {
        String endpoint = "/" + database + "/" + type + "/_search?_source=false";
        Request request = new Request(POST, endpoint);
        ObjectNode queryNode = buildQuery(null, null, query);
        NStringEntity entity = new NStringEntity(JacksonUtils.writeValueAsString(queryNode), ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        Optional<String> send = send(request);
        String result = send.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        JsonNode node = readTree(result);
        return node.get("hits").get("total").asLong();
    }

    @Override
    public Map<String, Object> findOne(String id) {
        String endpoint = "/" + database + "/" + type + "/" + id;
        Request request = new Request(GET, endpoint);
        String send = send(request).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        JsonNode node = readTree(send);
        String objId = node.get("_id").asText();
        Map<String, Object> map = JacksonUtils.readValue(node.get("_source").traverse(), new TypeReference<Map<String, Object>>() {
        });
        map.put("_id", objId);
        return map;
    }

    @Override
    public void createTable(List<DataSetSchema> colList) {
        ObjectNode properties = buildProperties(colList);
        ObjectNode mapping = buildMapping(properties);
        Optional<String> index = getIndexByAlias();
        if (index.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_ES_KEY_EXISTS);
        } else {
            createMapping(mapping);
        }
    }

    @Override
    public void dropTable() {
        String index = getIndexByAlias().orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        if (indicesExists(index)) {
            deleteAlias(index);
            sendDelete(index, null, null);
        }
    }

    @Override
    public Map<String, Object> insert(Map<String, Object> node) {
        String endpoint = "/" + database + "/" + type;
        Request request = new Request(POST, endpoint);
        String string = JacksonUtils.writeValueAsString(node);
        request.setEntity(new StringEntity(string, ContentType.APPLICATION_JSON));
        String send = send(request).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        JsonNode result = readTree(send);
        String id = result.get("_id").asText();
        Map<String, Object> map = JacksonUtils.readValue(string, new TypeReference<Map<String, Object>>() {
        });
        map.put("_id", id);
        return map;
    }

    @Override
    public Map<String, Object> update(String id, Map<String, Object> node) {
        String endpoint = "/" + database + "/" + type + "/" + id + "/_update/";
//        String endpoint = "/" + database + "/" + type + "/" + id + "/_update/?refresh=wait_for";
        Request request = new Request(POST, endpoint);
        Map<String, Object> map = new HashMap<>();
        map.put("doc", node);
        String string = JacksonUtils.writeValueAsString(map);
        request.setEntity(new StringEntity(string, ContentType.APPLICATION_JSON));
        send(request);
        return node;
    }

    @Override
    public void delete(String id) {
        String index = getIndexByAlias().orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        sendDelete(index, type, id);
    }

    @Override
    public void deleteAll() {
        String index = getIndexByAlias().orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        String endpoint = "/" + index + "/" + type + "/_delete_by_query?wait_for_completion=false";
        String body = "{\"query\":{\"match_all\":{}}}";
        Request request = new Request(POST, endpoint);
        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        send(request);
    }

    @Override
    public void batchInsert(List<Map<String, Object>> nodes) {
        String endpoint = "/" + database + "/" + type + "/_bulk?refresh=wait_for";
        StringBuilder body = new StringBuilder();
        for (Map<String, Object> node : nodes) {
            body.append("{ \"index\": {}}\n");
            body.append(JacksonUtils.writeValueAsString(node)).append("\n");
        }
        Request request = new Request(POST, endpoint);
        request.setEntity(new StringEntity(body.toString(), ContentType.APPLICATION_JSON));
        send(request);
    }

    @Override
    public void batchDelete(Collection<String> ids) {
        String endpoint = "/" + database + "/" + type + "/_bulk";
        StringBuilder body = new StringBuilder();
        for (String id : ids) {
            body.append("{ \"delete\": {\"_id\": \"" + id + "\" }}\n");
        }
        Request request = new Request(POST, endpoint);
        request.setEntity(new StringEntity(body.toString(), ContentType.APPLICATION_JSON));
        send(request);
    }

    @Override
    public List<Map<String, Long>> statistics() {
        String smokeQuery = "_smoke";
        String smokeMsgQuery = "_smokeMsg.msg";
        List<Map<String, Long>> maps = new ArrayList<>();
        Map<String, Long> smoke = statisticsSmoke(smokeQuery, smokeFun);
        Map<String, Long> smokeMsg = statisticsSmoke(smokeMsgQuery, smokeMsgFun);
        if (smoke != null) {
            maps.add(smoke);
        }
        if (smokeMsg != null) {
            maps.add(smokeMsg);
        }
        return maps;
    }

    private Map<String, Long> statisticsSmoke(String t, BiFunction<JsonNode, Long, Map<String, Long>> function) {
        String endpoint = "/" + database + "/" + type + "/_search";
        Request request = new Request(POST, endpoint);
        //language=JSON
        String statusQuery = "{\"aggs\":{\"smoke_aggs\":{\"terms\":{\"field\":\"" + t + "\"}}}}";
        request.setEntity(new StringEntity(statusQuery, ContentType.APPLICATION_JSON));
        Optional<String> send = send(request);
        if (send.isPresent()) {
            JsonNode node = readTree(send.get());
            JsonNode hits = node.get("hits");
            long total = hits.get("total").asLong();
            JsonNode buckets = node.get("aggregations").get("smoke_aggs").get("buckets");
            return function.apply(buckets, total);
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }


    private void createMapping(ObjectNode mapping) {
        String type = database + "_" + Long.toHexString(System.currentTimeMillis());
        String endpoint = "/" + type;
        Request request = new Request(PUT, endpoint);
        String string = mapping.toString();
        HttpEntity entity = new StringEntity(string, ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        send(request).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        addAlias(type);
    }

    private ObjectNode buildProperties(List<DataSetSchema> colList) {
        // 构建fields
        ObjectNode fields = objectMapper.createObjectNode();
        for (DataSetSchema col : colList) {
            if ("_id".equals(col.getField())) {
                continue;
            }
            if (col.getIsIndex() > 0) {
                fields.putPOJO(col.getField(), FieldType.INDEX.getEsProp());
            } else {
                fields.putPOJO(col.getField(), FieldType.findCode(col.getType()).getEsProp());
            }
        }
        fields.putPOJO("_smokeMsg", FieldType.SMOKE_MSG.getEsProp());
        fields.putPOJO(DataConst.UPDATE_AT, FieldType.DATE.getEsProp());
        fields.putPOJO(DataConst.CREATE_AT, FieldType.DATE.getEsProp());

        // 构建properties
        ObjectNode properties = objectMapper.createObjectNode();
        properties.putPOJO("properties", fields);
        return properties;
    }

    private ObjectNode buildMapping(ObjectNode properties) {
        // 构建type
        ObjectNode typeNode = objectMapper.createObjectNode();
        typeNode.putPOJO(type, properties);

        // 构建settings
        ObjectNode settings = objectMapper.createObjectNode();
        settings.put("number_of_shards", 5);
        settings.put("number_of_replicas", 2);

        // 构建mappings
        ObjectNode mapping = objectMapper.createObjectNode();
        mapping.putPOJO("settings", settings);
        mapping.putPOJO("mappings", typeNode);
        return mapping;
    }

    private String sendDelete(String index, String type, String id) {
        String endpoint = "/" + index + "/" + type + "/" + id;
        if (StringUtils.isEmpty(type)) {
            endpoint = "/" + index;
        } else if (StringUtils.isEmpty(id)) {
            endpoint = "/" + index + "/" + type;
        }
        Request request = new Request(DELETE, endpoint);
        Optional<String> send = send(request);
        return send.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
    }

    private String addAlias(String index) {
        String endpoint = "/" + index + "/_alias/" + database;
        Request request = new Request(PUT, endpoint);
        return send(request).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
    }

    private void deleteAlias(String index) {
        String endpoint = "/" + index + "/_alias/" + database;
        Request request = new Request(DELETE, endpoint);
        send(request).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
    }

    private boolean indicesExists(String index) {
        String endpoint = "/" + index;
        Request request = new Request(HEAD, endpoint);
        Response response = null;
        try {
            response = client.performRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int code = 0;
        if (response != null) {
            code = response.getStatusLine().getStatusCode();
        }
        return code == 200;
    }

    private HttpHost buildHttpHost(String addr) {
        String[] address = addr.split(":");
        if (address.length == 2) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port);
        }
        return null;
    }

    private Optional<String> send(Request request) {
        try {
            Response response = client.performRequest(request);
            HttpEntity entity = response.getEntity();
            return Optional.of(EntityUtils.toString(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private JsonNode readTree(String s) {
        try {
            return objectMapper.readTree(s);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private Optional<String> getIndexByAlias() {
        String endpoint = "/_alias/" + database;
        Request request = new Request(GET, endpoint);
        Optional<String> send = send(request);
        if (send.isPresent()) {
            JsonNode node = readTree(send.get());
            Iterator<String> iterator = node.fieldNames();
            if (iterator.hasNext()) {
                String next = iterator.next();
                return Optional.of(next);
            }
        }
        return Optional.empty();
    }
}
