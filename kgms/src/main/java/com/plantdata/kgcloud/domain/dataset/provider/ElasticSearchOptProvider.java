package com.plantdata.kgcloud.domain.dataset.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    private final String alias;
    private final String type;

    public ElasticSearchOptProvider(DataOptConnect info) {
        HttpHost[] hosts = info.getAddresses().stream()
                .map(this::buildHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        this.client = RestClient.builder(hosts).setMaxRetryTimeoutMillis(60000).build();
        this.alias = info.getDatabase();
        this.type = info.getTable();
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
        String endpoint = "/_alias/" + alias;
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

    @Override
    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        Request request = new Request(GET, "/" + alias + "/_mapping/" + type);
        Optional<String> send = send(request);
        String result = send.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        JsonNode node = readTree(result);
        JsonNode prop = node.get(alias).get("mappings").get(type).get("properties");
        Iterator<String> iterator = prop.fieldNames();
        while (iterator.hasNext()) {
            fields.add(iterator.next());
        }
        return fields;
    }

    @Override
    public List<Map<String, Object>> find(Integer offset, Integer limit, Map<String, Object> query) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        String endpoint = "/" + alias + "/" + type + "/_search";
        if (StringUtils.hasText(type)) {
            endpoint = "/" + alias + "/_search";
        }
        Request request = new Request(POST, endpoint);

//            NStringEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
//
//            request.setEntity(entity);


        Optional<String> send = send(request);
        String result = send.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        JsonNode node = readTree(result);

        System.out.println(node.toString());

        return mapList;
    }

    @Override
    public long count(Map<String, Object> query) {
        return 0;
    }

    @Override
    public Map<String, Object> findOne(String id) {
        return null;
    }

    public String sendDelete(String index, String type, String id) {
        String endpoint = "/" + index + "/" + type + "/" + id;
        if (StringUtils.isEmpty(type)) {
            endpoint = "/" + index;
        } else if (StringUtils.isEmpty(id)) {
            endpoint = "/" + index + "/" + type;
        }
        Request request = new Request(DELETE, endpoint);
        Optional<String> send = send(request);
        String rs = send.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        if (readTree(rs).get("acknowledged").asBoolean()) {
            log.info("index {}, type {}, id {} 删除成功", index, type, id);
        }
        return rs;
    }


    public String addAlias(String index) {
        String endpoint = "/" + index + "/_alias/" + alias;
        Request request = new Request(PUT, endpoint);
        return send(request).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
    }

    public void deleteAlias(String index) {
        String endpoint = "/" + index + "/_alias/" + alias;
        Request request = new Request(DELETE, endpoint);
        String rs = send(request).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        if (readTree(rs).get("acknowledged").asBoolean()) {
            log.info("{} 的 alias {} 删除成功", index, alias);
        }
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

    private void createMapping(ObjectNode mapping) {
        String type = alias + "_" + Long.toHexString(System.currentTimeMillis());
        String endpoint = "/" + type;
        Request request = new Request(PUT, endpoint);
        HttpEntity entity = new StringEntity(mapping.toString(), ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        send(request).orElseThrow(()->BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
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
        fields.putPOJO("_oprTime", FieldType.DATE.getEsProp());
        fields.putPOJO("_persistTime", FieldType.DATE.getEsProp());

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

    @Override
    public void dropTable() {
        String index = getIndexByAlias().orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        if (indicesExists(index)) {
            deleteAlias(index);
            sendDelete(index, null, null);
        }
    }

    @Override
    public Map<String, Object> insert(JsonNode node) {
        String endpoint = "/" + alias + "/" + type;
        Request request = new Request(PUT, endpoint);
        String string = node.toString();
        request.setEntity(new StringEntity(string, ContentType.APPLICATION_JSON));
        String send = send(request).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        JsonNode result = readTree(send);
        String id = result.get("_id").asText();
        try {
            Map<String, Object> map = objectMapper.readValue(string, new TypeReference<Map<String, Object>>() {
            });
            map.put("_id", id);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public Map<String, Object> update(String id, JsonNode node) {
        String endpoint = "/" + alias + "/" + type + "/" + id + "/_update/";
        Request request = new Request(POST, endpoint);
        String string = node.toString();
        request.setEntity(new StringEntity(string, ContentType.APPLICATION_JSON));
        send(request);
        try {
            return objectMapper.readValue(string, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
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
    public void batchInsert(List<JsonNode> nodes) {
        String endpoint = "/" + alias + "/" + type + "/_bulk";
        StringBuilder body = new StringBuilder();
        for (JsonNode node : nodes) {
            body.append("{\"create\" :").append(node.toString()).append("}\n");
        }
        Request request = new Request(POST, endpoint);
        request.setEntity(new StringEntity(body.toString(), ContentType.APPLICATION_JSON));
        send(request);
    }

    @Override
    public void batchDelete(Collection<String> ids) {
        String index = getIndexByAlias().orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_ES_REQUEST_ERROR));
        for (String id : ids) {
            sendDelete(index, type, id);
        }
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
