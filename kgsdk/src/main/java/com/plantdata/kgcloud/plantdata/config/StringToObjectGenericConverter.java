package com.plantdata.kgcloud.plantdata.config;

import ai.plantdata.cloud.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.plantdata.rsp.MarkObject;
import com.plantdata.kgcloud.sdk.constant.BaseEnum;
import com.plantdata.kgcloud.util.EnumUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
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
                new ConvertiblePair(String.class, String.class),
                new ConvertiblePair(String.class, Collection.class),
                new ConvertiblePair(String.class, Map.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        String sourceStr = StringEscapeUtils.unescapeHtml4(source.toString());
        if (targetType.getType() == String.class) {
            return sourceStr;
        }
        if (targetType.getType().getSuperclass() == Enum.class) {
            return EnumUtils.getEnumObject((Class) targetType.getType(), sourceStr).get();
        }
        ResolvableType resolvableType = targetType.getResolvableType();
        ObjectMapper instance = JacksonUtils.getInstance();
        //兼容没有
        instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //兼容单引号
        instance.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        try {
            if (resolvableType.hasGenerics()) {
                return instance.readValue(sourceStr, JacksonUtils.getInstance().constructType(resolvableType.getType()));
            }
            return instance.readValue(sourceStr, resolvableType.resolve());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source;
    }
}
