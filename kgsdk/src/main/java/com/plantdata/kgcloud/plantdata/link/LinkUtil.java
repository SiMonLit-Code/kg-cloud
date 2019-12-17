package com.plantdata.kgcloud.plantdata.link;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 仅用于基本数据类型
 *
 * @author cjw
 * @version 1.0
 * @date 2019/12/14 17:02
 */
@Slf4j
public class LinkUtil {

    private static final String DATE_REG = "yyyy-MM-dd";

    @Getter
    @AllArgsConstructor
    @ToString
    private static class FieldDTO {
        private Object val;
        private Class<?> type;
    }

    public static <T, R> R link(T to) {
        Class<?> tClazz = to.getClass();

        //解析旧对象
        LinkModel annotation = AnnotationUtils.findAnnotation(tClazz, LinkModel.class);
        Map<String, FieldDTO> fieldValMap = Maps.newHashMap();
        Field[] fields = tClazz.getDeclaredFields();
        for (Field field : fields) {
            LinkField linkField = AnnotationUtils.findAnnotation(field, LinkField.class);
            Object fieldVal = getObjectFieldName(to, field.getName());
            String name = linkField == null ? field.getName() : linkField.name();
            fieldValMap.put(name, new FieldDTO(fieldVal, field.getType()));
        }
        //创建新对象
        Object obj = null;
        try {
            assert annotation != null;
            obj = annotation.clazz().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Field field : annotation.clazz().getDeclaredFields()) {
            field.setAccessible(true);
            FieldDTO val = fieldValMap.get(field.getName());
            if (val.getVal() == null) {
                continue;
            }
            if (val.type != null && !val.type.equals(field.getType())) {
                val.val = convertByType(tClazz.getSimpleName(), val.val, field.getType());
            }
            try {
                field.set(obj, val.val);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return (R) obj;
    }

    private static Object getObjectFieldName(Object obj, String fieldName) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (fieldName.equals(field.getName())) {
                try {
                    Object rst = field.get(obj);
                    if (rst != null) {
                        return rst;
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private static Object convertByType(String toName, Object val, Class<?> type) {
        if (val == null) {
            return null;
        }
        if (type.equals(String.class)) {
            if (val instanceof Date) {
                return DateUtils.formatDate((Date) val, DATE_REG);
            }
            return val.toString();
        }
        if (type.equals(Long.class) || type == long.class) {
            return Long.valueOf(val.toString());
        }
        if (type.equals(Integer.class) || type == int.class) {
            return Integer.parseInt(val.toString());
        }
        if (type.equals(Double.class) || type == double.class) {
            return Double.parseDouble(val.toString());
        }
        if (type.equals(Float.class) || type == float.class) {
            return Float.parseFloat(val.toString());
        }
        if (type.equals(BigDecimal.class)) {
            return new BigDecimal(val.toString());
        }
        if (type.equals(Date.class)) {
            return DateUtils.parseDate(val.toString(), DATE_REG);
        }

        log.debug("noParse,class:{},sourceType:{};targetType:{}", toName, val.getClass().getSimpleName(), type.getSimpleName());
        return null;
    }

    @Setter
    @Getter
    @LinkModel(clazz = B.class)
    public static class A {
        @LinkField(name = "key")
        private Map<String, Object> name;
    }

    @Setter
    @Getter
    public static class B {
        private Map<String, Object> key;
    }

    public static void main(String[] args) {
        A a = new A();
        a.name = Maps.newHashMap();
        a.name.put("a", "b");
        B b = link(a);
        System.out.println(JacksonUtils.writeValueAsString(b));
    }
}
