package ai.plantdata.kgcloud.domain.app.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/28 11:28
 */
public class DefaultUtils {

    public static <T> List<T> getOrDefault(List<T> list) {
        return CollectionUtils.isEmpty(list) ? Collections.emptyList() : list;
    }

    public static <T> List<T> listAdd(List<T> list, T obj) {
        if (list == null) {
            return Lists.newArrayList(obj);
        }
        list.add(obj);
        return list;
    }


    /**
     * list1 不为空 作为方法二的参数执行后计算 否则返回null
     *
     * @param list1
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> executeIfNoNull(List<T> list1, Function<List<T>, List<R>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptyList() : function.apply(list1);
    }

    public static <T, R> Set<R> executeIfNoNull(Set<T> list1, Function<Set<T>, Set<R>> function) {
        return CollectionUtils.isEmpty(list1) ? Collections.emptySet() : function.apply(list1);
    }

    public static void ifPresent(Consumer<? super Object> consumer, Object value) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public static <K, V> Map<K, V> oneElMap(K k, V v) {
        HashMap<K, V> hashMap = Maps.newHashMapWithExpectedSize(1);
        hashMap.put(k, v);
        return hashMap;
    }

}
