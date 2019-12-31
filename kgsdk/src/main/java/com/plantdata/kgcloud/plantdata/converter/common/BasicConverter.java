package com.plantdata.kgcloud.plantdata.converter.common;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 10:55
 */
@Slf4j
public class BasicConverter {

    private static final int SUCCESS = 200;
    private static final String DATE_REG = "yyyy-MM-ddd hh:mm:ss";

    public static <T, R> List<R> listToRsp(List<T> list, Function<T, R> function) {
        return executeIfNoNull(list, a -> listConvert(a, function));
    }

    public static <T, R> R convert(ApiReturn<T> apiReturn, Function<T, R> function) {
        Optional<T> optional = apiReturnData(apiReturn);
        T data = optional.orElse(null);
        return data == null ? null : function.apply(data);
    }

    public static <T> Map<String, T> keyIntToStr(Map<Integer, T> oldMap) {
        return keyConvert(oldMap, String.class);
    }

    public static <T> Map<Integer, T> keyStringToInt(Map<String, T> oldMap) {
        return keyConvert(oldMap, Integer.class);
    }

    public static <T> Map<Long, T> keyIntToLong(Map<Integer, T> oldMap) {
        return keyConvert(oldMap, Long.class);
    }

    public static <T, R> List<R> convertList(ApiReturn<List<T>> apiReturn, Function<T, R> function) {
        Optional<List<T>> ts = apiReturnData(apiReturn);
        List<T> data = ts.orElse(null);
        return CollectionUtils.isEmpty(data) ? Collections.emptyList() : listConvert(data, function);
    }

    public static <T> Optional<T> apiReturnData(ApiReturn<T> apiReturn) {
        if (SUCCESS != (apiReturn.getErrCode())) {
            throw new BizException(apiReturn.getErrCode(), apiReturn.getMessage());
        }
        return Optional.ofNullable(apiReturn.getData());
    }

    public static <T, R> RestData<R> basePageToRestData(BasePage<T> pageData, Function<T, R> function) {
        if (pageData == null) {
            return new RestData<>(Collections.emptyList(), NumberUtils.LONG_ZERO);
        }
        List<R> rs = listToRsp(pageData.getContent(), function);
        return new RestData<>(rs, pageData.getTotalElements());
    }

    static <T, R> R executeIfNoNull(T param, Function<T, R> function) {
        return param == null ? null : function.apply(param);
    }

    protected static <T> void consumerIfNoNull(T param, Consumer<T> function) {
        if (param == null) {
            return;
        }
        if (param instanceof String && StringUtils.isEmpty(param)) {
            return;
        }
        if (param instanceof Collection && CollectionUtils.isEmpty((Collection) param)) {
            return;
        }
        function.accept(param);
    }

    protected static <T> void applyIfTrue(Boolean bool, T param, Consumer<T> consumer) {
        if (bool != null && bool) {
            consumer.accept(param);
        }
    }

    protected static <T> List<T> flatList(List<List<T>> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    protected static Date stringToDate(String str) {
        return DateUtils.parseDate(str, DATE_REG);
    }

    protected static String dateToString(Date date) {
        return DateUtils.formatDate(date, DATE_REG);
    }

    protected static <T, R> R copy(T t, Class<R> clazz) {
        R r = null;
        try {
            r = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("clazz:{} 创建实例失败", JacksonUtils.writeValueAsString(clazz));
            e.printStackTrace();
        }
        if (r != null) {
            BeanUtils.copyProperties(t, r);
        }
        return r;
    }

    private static <T, R, E> Map<E, R> keyConvert(Map<T, R> oldMap, Class<E> clazz) {
        if (CollectionUtils.isEmpty(oldMap)) {
            return Collections.emptyMap();
        }
        Map<E, R> newMap = Maps.newHashMapWithExpectedSize(oldMap.size());
        oldMap.forEach((k, v) -> {
            E tempKey = null;
            if (clazz.equals(String.class)) {
                tempKey = (E) k.toString();
            }
            if (clazz.equals(Integer.class)) {
                tempKey = (E) Integer.valueOf(k.toString());
            }
            if (clazz.equals(Long.class)) {
                tempKey = (E) Long.valueOf(k.toString());
            }
            newMap.put(tempKey, v);
        });
        return newMap;
    }


    private static <T, R> List<R> executeIfNoNull(List<T> list1, Function<List<T>, List<R>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptyList() : function.apply(list1);
    }

    private static <T, R> List<R> listConvert(@NonNull List<T> list, Function<T, R> function) {
        return list.stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
    }

}
