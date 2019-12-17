package com.plantdata.kgcloud.domain.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 17:22
 */
@Slf4j
public class JsonUtils {

    public static <T> List<T> readToList(ArrayNode arrayNode, Class<T> clazz) {
        try {
            return JacksonUtils.getInstance().treeToValue(arrayNode, List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }



    public static <T> List<T> readToList(String str, Class<T> clazz) {
        try {
            return (List<T>) JacksonUtils.getInstance().readValue(str, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static String toJson(Object o) {
        try {
            return JacksonUtils.getInstance().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

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
}
