package com.plantdata.kgcloud.domain.common.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author cjw
 * @date 2020/4/15  14:07
 */
public class PatternUtils {

    public static Map<String, Object> getLikeStr(String findStr) {
        Map<String, Object> regexMap = Maps.newHashMap();
        Pattern compile = Pattern.compile("^.*" + findStr + ".*$", Pattern.CASE_INSENSITIVE);
        regexMap.put("$regex", compile);
        return regexMap;
    }

    public static Map<String, Object> getNoLikeStr(String findStr) {
        Map<String, Object> regexMap = Maps.newHashMap();
        Pattern compile = Pattern.compile("^((?!" + findStr + ").)*$");
        regexMap.put("$regex", compile);
        return regexMap;
    }

}
