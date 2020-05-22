package com.plantdata.kgcloud.domain.app.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 16:07
 */
@Slf4j
public class BasicConverter {

    public static <T> void applyIfTrue(Boolean bool, T param, Consumer<T> consumer) {
        if (bool != null && bool) {
            consumer.accept(param);
        }
    }

    static <T> void consumerWithDefault(@NonNull T def, T a, Consumer<T> consumer) {
        consumer.accept(a == null ? def : a);
    }

    public static <T> List<T> flatToList(Collection<Collection<T>> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static <T> Set<T> flatToSet(Collection<Collection<T>> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.stream().filter(a->!CollectionUtils.isEmpty(a)).flatMap(Collection::stream).collect(Collectors.toSet());
    }


    /**
     * 非空 消费 批量
     */
    public static <T> void listConsumerIfNoNull(List<T> paramList, Consumer<T> function) {
        if (!CollectionUtils.isEmpty(paramList)) {
            paramList.forEach(a -> consumerIfNoNull(a, function));
        }
    }

    /**
     * 非空 消费
     */
    public static <T> void consumerIfNoNull(T param, Consumer<T> function) {
        if (!isNull(param)) {
            function.accept(param);
        }
    }

    public static <T> List<T> mergeList(List<T> source, List<T> target) {
        if (CollectionUtils.isEmpty(source)) {
            return target;
        }
        if (!CollectionUtils.isEmpty(target)) {
            source.addAll(target);
        }

        return source;
    }

    public static <T, R> List<R> listConvert(@NonNull Collection<T> list, Function<T, R> function) {
        return CollectionUtils.isEmpty(list) ? Collections.emptyList() : list.stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
    }

    private static <T> boolean isNull(T param) {
        if (param == null) {
            return true;
        }
        if (param instanceof String && StringUtils.isEmpty(param)) {
            return true;
        }
        if (param instanceof Collection && CollectionUtils.isEmpty((Collection) param)) {
            return true;
        }
        return param instanceof Map && CollectionUtils.isEmpty((Map) param);
    }

    public static <T, R> R copy(T t, Class<R> clazz) {
        R r = null;
        try {
            r = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("clazz:{} 创建实例失败", JacksonUtils.writeValueAsString(clazz));
            e.printStackTrace();
        }
        assert r != null;
        BeanUtils.copyProperties(t, r);
        return r;
    }

    static <T> Map<Integer, T> keyStrToInt(Map<String, T> oldMap) {
        Map<Integer, T> newMap = Maps.newHashMapWithExpectedSize(oldMap.size());
        oldMap.forEach((k, v) -> newMap.put(Integer.valueOf(k), v));
        return newMap;
    }

    static <T, R> R executeNoNull(T param, Function<T, R> function) {
        return param == null ? null : function.apply(param);
    }

    public static <T, R> List<R> listToRsp(Collection<T> list, Function<T, R> function) {
        return toListNoNull(list, a -> listConvert(a, function));
    }

    static <T extends Collection, R> List<R> toListNoNull(T list1, Function<T, Collection<R>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptyList() : function.apply(list1).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static <T, R> Set<R> listToSetNoNull(List<T> list1, Function<List<T>, Set<R>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptySet() : function.apply(list1);
    }

    public static <T, K, V> Map<K, V> listToMapNoNull(List<T> list1, Function<List<T>, Map<K, V>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptyMap() : function.apply(list1);
    }

    protected static void infoLog(String name, Object obj) {
        ObjectMapper instance = JacksonUtils.getInstance();
        instance.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            log.info("{}:{}", name, instance.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
