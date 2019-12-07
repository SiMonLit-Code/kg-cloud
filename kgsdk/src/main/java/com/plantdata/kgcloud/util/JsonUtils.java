package com.plantdata.kgcloud.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 15:44
 */
@Slf4j
public class JsonUtils {


    public static <T> T jsonToObj(String jsonString, TypeReference<T> tr) {
        if (jsonString != null && !("".equals(jsonString))) {
            try {
                return JacksonUtils.getInstance().readValue(jsonString, tr);
            } catch (Exception e) {
                log.warn("json error:" + e.getMessage());
            }
        }
        return null;
    }

    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        return jsonToObj(jsonString, new TypeReference<List<T>>() {
        });
    }

    public static Boolean isEmpty(String jsonStr) {
        return jsonStr == null || "".equals(jsonStr) || "[]".equals(jsonStr) || "{}".equals(jsonStr);
    }
}
