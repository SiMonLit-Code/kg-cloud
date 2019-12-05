package com.plantdata.kgcloud.domain.app.util;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/28 11:28
 */
public class DefaultUtils {

    public static <T> List<T> getOrDefault(List<T> list) {
        return CollectionUtils.isEmpty(list) ? Collections.emptyList() : list;
    }

    public static <T, E> Map<T, E> getOrDefault(Map<T, E> map) {
        return CollectionUtils.isEmpty(map) ? Collections.emptyMap() : map;
    }

    /**
     * list1 不为空 取list2 否则返回空
     * @param list1 1
     * @param list2 2
     * @param <T> t
     * @param <E> e
     * @return ...
     */
    public static <T,E> List<E> getIfNoNull(List<T> list1,List<E> list2) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptyList() : list2;
    }
}
