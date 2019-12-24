package com.plantdata.kgcloud.plantdata.config;

import com.google.common.collect.Sets;
import com.plantdata.kgcloud.config.MarkObject;
import com.plantdata.kgcloud.sdk.constant.BaseEnum;
import com.plantdata.kgcloud.util.EnumUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * describe about this class
 *
 * @author: DingHao
 * @date: 2019/7/1 10:46
 */
@Component
public class StringToObjectGenericConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Sets.newHashSet(new ConvertiblePair(String.class, MarkObject.class),
                new ConvertiblePair(String.class, BaseEnum.class),
                new ConvertiblePair(String.class, Collection.class),
                new ConvertiblePair(String.class, Map.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        if (targetType.getType().getSuperclass() == Enum.class) {
            return EnumUtils.getEnumObject((Class) targetType.getType(), source.toString()).get();
        }
        ResolvableType resolvableType = targetType.getResolvableType();
        if (resolvableType.hasGenerics()) {
            return JacksonUtils.readValue(StringEscapeUtils.unescapeHtml(source.toString()), JacksonUtils.getInstance().constructType(resolvableType.getType()));
        }
        return JacksonUtils.readValue(source.toString(), resolvableType.resolve());
    }
}
