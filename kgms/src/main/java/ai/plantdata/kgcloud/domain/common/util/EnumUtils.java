package ai.plantdata.kgcloud.domain.common.util;

import ai.plantdata.kgcloud.sdk.constant.BaseEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/9 12:21
 */
public class EnumUtils {
    private static Map<Class, Object> map = new ConcurrentHashMap<>();

    public static <T extends BaseEnum> Optional<T> getEnumObject(Class<T> className, String dataValue) {
        if (!className.isEnum()) {
            return Optional.empty();
        }
        Object obj = map.get(className);
        T[] ts;
        if (obj == null) {
            ts = className.getEnumConstants();
            map.put(className, ts);
        } else {
            ts = (T[]) obj;
        }
        return Arrays.stream(ts).filter(s -> s.getValue().equals(dataValue)).findFirst();
    }


    public static <T extends BaseEnum> Optional<T> parseById(Class<T> className, Integer id) {
        if (!className.isEnum() || id == null) {
            return Optional.empty();
        }
        Object obj = map.get(className);
        T[] ts;
        if (obj == null) {
            ts = className.getEnumConstants();
            map.put(className, ts);
        } else {
            ts = (T[]) obj;
        }
        return Arrays.stream(ts).filter(s -> s.fetchId().equals(id)).findFirst();
    }
}
