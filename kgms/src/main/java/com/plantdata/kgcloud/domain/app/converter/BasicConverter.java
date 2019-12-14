package com.plantdata.kgcloud.domain.app.converter;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.util.DefaultUtils;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 16:07
 */
public class BasicConverter {

    public static <T, R> R executeNoNull(T param, Function<T, R> function) {
        return param == null ? null : function.apply(param);
    }

    public static <T extends Collection, R> List<R> executeListNoNull(T list1, Function<T, List<R>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptyList() : function.apply(list1);
    }

    public static <T extends Map, R> List<R> executeMapNoNull(T list1, Function<T, List<R>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptyList() : function.apply(list1);
    }

    protected static <T, R> List<R> listToRsp(Collection<T> list, Function<T, R> function) {
        return executeListNoNull(list, a -> listConvert(a, function));
    }


    private static <T, R> List<R> listConvert(@NonNull Collection<T> list, Function<T, R> function) {
        return list.stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
    }
}
