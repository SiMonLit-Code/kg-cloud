package com.plantdata.kgcloud.constant;

import ai.plantdata.cloud.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-11 09:49
 **/
public interface DataConst {

    String CREATE_AT = "_persistTime";
    String UPDATE_AT = "_oprTime";
    String HAS_SMOKE = "_smoke";


    String INDEX = "{\"type\": \"text\",\"analyzer\":\"ik_max_word\"}";
    JsonNode JSON_INDEX = JacksonUtils.readValue(INDEX, JsonNode.class);

    String SMOKE_MSG = "{\"type\":\"object\",\"properties\":{\"path\":{\"type\":\"keyword\"},\"category\":{\"type\":\"keyword\"},\"results\":{\"type\":\"boolean\"},\"msg\":{\"type\":\"keyword\"}}}";
    JsonNode JSON_SMOKE_MSG = JacksonUtils.readValue(SMOKE_MSG, JsonNode.class);

    String INTEGER = "{\"type\": \"integer\"}";
    JsonNode JSON_INTEGER = JacksonUtils.readValue(INTEGER, JsonNode.class);

    String STRING = "{\"type\": \"keyword\"}";
    JsonNode JSON_STRING = JacksonUtils.readValue(STRING, JsonNode.class);

    String LONG = "{\"type\": \"long\"}";
    JsonNode JSON_LONG = JacksonUtils.readValue(LONG, JsonNode.class);

    String DATE = "{\"type\":\"date\",\"format\": \"yyyyMMdd||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||HH:mm:ss\"}";
    JsonNode JSON_DATE = JacksonUtils.readValue(DATE, JsonNode.class);

    String OBJECT = "{\"type\": \"object\"}";
    JsonNode JSON_OBJECT = JacksonUtils.readValue(OBJECT, JsonNode.class);

    String NESTED = "{\"type\":\"nested\",\"properties\":{\"kgId\":{\"type\":\"long\"},\"classId\":{\"type\": \"long\"}, \"name\": {\"type\": \"keyword\"}, \"score\": {\"type\":\"double\"}, \"type\": {\"type\": \"long\"}}}";
    JsonNode JSON_NESTED = JacksonUtils.readValue(NESTED, JsonNode.class);

    String ARRAY = "{\"type\":\"object\"}";
    JsonNode JSON_ARRAY = JacksonUtils.readValue(ARRAY, JsonNode.class);

    String STRING_ARRAY = "{\"type\":\"object\"}";
    JsonNode JSON_STRING_ARRAY = JacksonUtils.readValue(STRING_ARRAY, JsonNode.class);

    String DOUBLE = "{\"type\":\"double\"}";
    JsonNode JSON_DOUBLE = JacksonUtils.readValue(DOUBLE, JsonNode.class);

    String FLOAT = "{\"type\":\"float\"}";
    JsonNode JSON_FLOAT = JacksonUtils.readValue(FLOAT, JsonNode.class);

    String DATE_TIME = "{\"type\":\"datetime\",\"format\": \"yyyy-MM-dd||HH:mm:ss\"}";
    JsonNode JSON_DATETIME = JacksonUtils.readValue(DATE_TIME, JsonNode.class);
}
