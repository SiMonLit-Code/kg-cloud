package com.plantdata.kgcloud.domain.common.util;

import com.plantdata.kgcloud.constant.StringConstants;
import com.plantdata.kgcloud.util.JacksonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/8 11:20
 */
public class SortUtils {

    public static Optional<String> toJson(String[] sorts) {
        if (sorts == null || sorts.length == 0) {
            return Optional.empty();
        }
        Map<String, String> objectMap = new HashMap<>(sorts.length);
        for (String sort : sorts) {
            sort = sort.replace(StringConstants.COMMA_CH, StringConstants.COMMA_EN);
            if (!sort.contains(StringConstants.COMMA_EN)) {
                continue;
            }
            String[] split = sort.split(StringConstants.COMMA_EN);
            if (split.length < 2) {
                continue;
            }
            objectMap.put(split[0], split[1]);
        }
        return Optional.of(JacksonUtils.writeValueAsString(objectMap));
    }
}
