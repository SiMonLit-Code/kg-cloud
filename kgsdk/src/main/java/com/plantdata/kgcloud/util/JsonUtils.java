package com.plantdata.kgcloud.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 15:44
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper instance = JacksonUtils.getInstance();

    static {
        instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T jsonToObj(String jsonString, TypeReference<T> tr) {
        if (jsonString != null && !("".equals(jsonString))) {
            try {
                return instance.readValue(jsonString, tr);
            } catch (Exception e) {
                log.warn("json error:" + e.getMessage());
            }
        }
        return null;
    }

    public static <T> T parseObj(String json, Class<T> clazz) {
        try {
            return instance.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> stringToMap(String json) {
        return JsonUtils.jsonToObj(json, new TypeReference<Map<String, Object>>() {
        });
    }

    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        return jsonToObj(jsonString, new TypeReference<List<T>>() {
        });
    }

    public static Boolean isEmpty(String jsonStr) {
        return jsonStr == null || "".equals(jsonStr) || "[]".equals(jsonStr) || "{}".equals(jsonStr);
    }
}
