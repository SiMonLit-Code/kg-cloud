package com.plantdata.kgcloud.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
@Slf4j
public class JsonPathUtil {

    public static String read(String jsonStr, String path){

        String obj = null;
        try {
            obj = JSON.toJSON(JsonPath.read(jsonStr, path)).toString();
        }catch (Exception e) {
            log.info("not find result for jsonPath: " + path);
        }
        return obj;
    }

    public static List<String> getValeByPath(String jsonStr, String path) {

        List<String> value = Lists.newArrayList();
        if(StringUtils.isNotBlank(path)){
            String valueStr = JsonPathUtil.read(jsonStr, path);
            if (valueStr != null){
                if (!valueStr.startsWith("[") || !valueStr.endsWith("]")){
                    value.add(valueStr);
                }else {
                    try {
                        value.addAll(JSONArray.parseArray(valueStr, String.class));
                    }catch (Exception e){
                        value.add(valueStr);
                    }
                }
            }
        }
        return value;
    }
}
