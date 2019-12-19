package com.plantdata.kgcloud.domain.app.converter;

import lombok.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
public class BasicConverter {

    public static <T, R> R executeNoNull(T param, Function<T, R> function) {
        return param == null ? null : function.apply(param);
    }

    public static <T extends Collection, R> List<R> executeListNoNull(T list1, Function<T, List<R>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptyList() : function.apply(list1).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static <T, R> Set<R> executeSetNoNull(List<T> list1, Function<List<T>, Set<R>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptySet() : function.apply(list1);
    }

    protected static <T, R> List<R> listToRsp(Collection<T> list, Function<T, R> function) {
        return executeListNoNull(list, a -> listConvert(a, function));
    }

    public static <T, R> List<R> listConvert(@NonNull Collection<T> list, Function<T, R> function) {
        return list.stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
    }

    public static <T> void setIfNoNull(T param, Consumer<T> function) {
        if (param instanceof String && !StringUtils.isEmpty(param)) {
            function.accept(param);
        } else if (param instanceof Collection && !CollectionUtils.isEmpty((Collection) param)) {
            function.accept(param);
        } else if (param != null) {
            function.accept(param);
        }
    }
}
