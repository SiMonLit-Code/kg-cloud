package com.plantdata.kgcloud.domain.dataset.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;

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
        String address = info.getAddresses();
        String[] addrs = address.split(",");
        int length = addrs.length;
        HttpHost[] hosts = new HttpHost[length];
        for (int i = 0; i < length; i++) {
            String addr = addrs[i];
            String[] host = addr.split(":");

            hosts[i] = new HttpHost(host[0], Integer.parseInt(host[1]));
        }
        this.client = RestClient.builder(hosts).setMaxRetryTimeoutMillis(60000).build();
        this.alias = info.getDatabase();
        this.type = info.getTable();
    }

    private String send(Request request) {
        try {
            Response response = client.performRequest(request);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
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

    private String getIndexByAlias() {
        String endpoint = "/_alias/" + alias;
        Request request = new Request(GET, endpoint);
        String result = send(request);
        JsonNode node = readTree(result);
        Iterator<String> iterator = node.fieldNames();
        if (iterator.hasNext()) {
            String next = iterator.next();
            log.info("alias {} 的 index 为 {}", alias, next);
            return next;
        }
        return null;
    }

    @Override
    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        Request request = new Request(GET, "/" + alias + "/_mapping/" + type);
        JsonNode node = readTree(send(request));
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


        JsonNode node = readTree(send(request));
//            JsonNode prop = node.get(index).get("mappings").get(type).get("properties");
//            Iterator<String> iterator = prop.fieldNames();
//
//            while (iterator.hasNext()) {
//                mapList.add(iterator.next());
//            }

        return mapList;
    }

    public String sendDelete(String index, String type, String id) {

        String endpoint = "/" + index + "/" + type + "/" + id;
        if (StringUtils.isEmpty(type)) {
            endpoint = "/" + index;
        } else if (StringUtils.isEmpty(id)) {
            endpoint = "/" + index + "/" + type;
        }
        Request request = new Request(DELETE, endpoint);
        String rs = send(request);
        if (readTree(rs).get("acknowledged").asBoolean()) {
            log.info("index {}, type {}, id {} 删除成功", index, type, id);
        }
        return rs;
    }


    public String addAlias(String index) {
        String endpoint = "/_aliases";
        Request request = new Request(POST, endpoint);
        JsonNode node = buildAction(buildAliases(index));
        StringEntity entity = new StringEntity(node.toString(), ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        return send(request);
    }

    public void deleteAlias(String index) {
        String endpoint = "/" + index + "/_alias/" + alias;
        Request request = new Request(DELETE, endpoint);
        String rs = send(request);
        if (readTree(rs).get("acknowledged").asBoolean()) {
            log.info("{} 的 alias {} 删除成功", index, alias);
        }
    }

    private JsonNode buildAction(ObjectNode... nodes) {
        ObjectNode node = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (ObjectNode jsonNodes : nodes) {
            arrayNode.add(jsonNodes);
        }
        node.putPOJO("actions", arrayNode);
        return node;
    }

    private ObjectNode buildAliases(String index) {
        ObjectNode node = objectMapper.createObjectNode();
        ObjectNode actionNode = objectMapper.createObjectNode();
        actionNode.put("index", index);
        actionNode.put("alias", alias);
        node.putPOJO("add", actionNode);
        return node;
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
        String index = getIndexByAlias();
        if (index == null || !indicesExists(index)) {
            index = alias + "_" + Long.toHexString(System.currentTimeMillis());
            String endpoint = "/" + index;
            Request request = new Request(PUT, endpoint);
            HttpEntity entity = new StringEntity(mapping.toString(), ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            send(request);
            addAlias(index);
        }
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
        settings.put("number_of_shards", 1);
        settings.put("number_of_replicas", 0);

        // 构建mappings
        ObjectNode mapping = objectMapper.createObjectNode();
        mapping.putPOJO("settings", settings);
        mapping.putPOJO("mappings", typeNode);
        return mapping;
    }

    @Override
    public void dropTable() {
        String index = getIndexByAlias();
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
        String send = send(request);
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
        String index = getIndexByAlias();
        sendDelete(index, type, id);
    }

    @Override
    public void deleteAll() {
        String index = getIndexByAlias();
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
        String index = getIndexByAlias();
        for (String id : ids) {
            sendDelete(index, type, id);
        }
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
