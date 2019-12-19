package com.plantdata.kgcloud.domain.app.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 17:22
 */
@Slf4j
public class JsonUtils {

    public static Optional<JsonNode> parseJsonNode(String json) {
        try {
            if (StringUtils.isNoneEmpty(json)) {
                return Optional.of(JacksonUtils.getInstance().readTree(json));
            }
        } catch (IOException e) {
            log.error("parseJsonNode:{}", json);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static <T> List<T> objToList(Object o, Class<T> clazz) {
        ObjectMapper instance = JacksonUtils.getInstance();
        JavaType javaType = getCollectionType(instance, ArrayList.class, clazz);
        return JacksonUtils.readValue(JacksonUtils.writeValueAsString(o), javaType);
    }

    public static <T> T objToNewObj(Object o, Class<T> clazz) {
        return JacksonUtils.readValue(JacksonUtils.writeValueAsString(o), clazz);
    }

    private static JavaType getCollectionType(ObjectMapper instance, Class<?> collectionClass, Class<?>... elementClasses) {
        return instance.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
