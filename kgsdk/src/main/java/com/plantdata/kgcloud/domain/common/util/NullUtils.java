package com.plantdata.kgcloud.domain.common.util;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author liyan
 */
public class NullUtils {
    /**
     * data 为空时返回  defaultData
     */
    public static <T> T getDefault(T data, T defaultData) {
        //为空直接返回
        if (data == null) {
            return defaultData;
        }
        //继承 BaseNullBean 使用isNotNull 方法判断
//        if (data instanceof BaseNullBean) {
//            if (!((BaseNullBean) data).isNotNull()) {
//                return defaultData;
//            }
//        }
        // 空字符串判断
        if (data instanceof String) {
            if (StringUtils.isEmpty(data)) {
                return defaultData;
            }
        }
        return data;

    }

    public static Boolean isNotNull(Object data) {
        return !isNull(data);
    }

    public static Boolean isNull(Object data) {
        if (data == null) {
            return true;
        }
        //继承 BaseNullBean 使用isNotNull 方法判断
//        if (data instanceof BaseNullBean) {
//            if (((BaseNullBean) data).isNotNull()) {
//                return true;
//            }
//        }

        if (data instanceof Map) {
            if (((Map) data).isEmpty()) {
                return true;
            }
        }
        if (data instanceof Collection) {
            if (((Collection) data).isEmpty()) {
                return true;
            }
        }

        // 空字符串判断
        if (data instanceof String) {
            return StringUtils.isEmpty(data);
        }
        return false;
    }
}