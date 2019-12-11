package com.plantdata.kgcloud.domain.app.util;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/28 11:28
 */
public class DefaultUtils {

    public static <T> List<T> getOrDefault(List<T> list) {
        return CollectionUtils.isEmpty(list) ? Collections.emptyList() : list;
    }

    public static <T> void listAdd(List<T> list, T obj) {
        if (CollectionUtils.isEmpty(list)) {
            list = Lists.newArrayList();
        }
        list.add(obj);
    }


    /**
     * list1 不为空 取list2 否则返回空
     *
     * @param list1 1
     * @param list2 2
     * @param <T>   t
     * @param <E>   e
     * @return ...
     */
    public static <T, E> List<E> getIfNoNull(List<T> list1, List<E> list2) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptyList() : list2;
    }

    public static void ifPresent(Consumer<? super Object> consumer, Object value) {
        if (value != null) {
            consumer.accept(value);
        }
    }

}
