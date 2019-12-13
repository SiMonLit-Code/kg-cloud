package com.plantdata.kgcloud.plantdata.common.converter;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.exception.BizException;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/13 10:55
 */
public class ConverterUtils {

    private static final int SUCCESS = 200;

    private static <T> Optional<T> apiReturnData(ApiReturn<T> apiReturn) {
        if (SUCCESS != (apiReturn.getErrCode())) {
            //todo
            throw new BizException(apiReturn.getErrCode(), apiReturn.getMessage());
        }
        return Optional.ofNullable(apiReturn.getData());
    }

    public static <T, R> R convert(ApiReturn<T> apiReturn, Function<T, R> function) {
        Optional<T> optional = apiReturnData(apiReturn);
        T data = optional.orElse(null);
        return data == null ? null : function.apply(data);
    }

    public static <T, R> R executeIfNoNull(T param, Function<T, R> function) {
        return param == null ? null : function.apply(param);
    }

    protected static <T, R> List<R> listToRsp(List<T> list, Function<T, R> function) {
        return executeIfNoNull(list, a -> listConvert(a, function));
    }

    private static <T, R> List<R> executeIfNoNull(List<T> list1, Function<List<T>, List<R>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptyList() : function.apply(list1);
    }

    private static <T, R> List<R> listConvert(@NonNull List<T> list, Function<T, R> function) {
        return list.stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
    }
}
