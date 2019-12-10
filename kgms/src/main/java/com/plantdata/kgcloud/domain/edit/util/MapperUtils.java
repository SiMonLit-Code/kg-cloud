package com.plantdata.kgcloud.domain.edit.util;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.JacksonUtils;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 11:51
 * @Description:
 */
public class MapperUtils {

    private static final MapperFactory MAPPER_FACTORY = new DefaultMapperFactory.Builder().build();

    public static MapperFactory getMapperFactory() {
        return MAPPER_FACTORY;
    }

    public static <T> T map(Object source, Class<T> clazz) {
        try {
            return MAPPER_FACTORY.getMapperFacade().map(source, clazz);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.DATA_CONVERSION_ERROR);
        }
    }

    public static <T> T mapAttr(Object source, Class<T> clazz) {
        try {
            MAPPER_FACTORY.getConverterFactory().registerConverter(new CustomConverter<String, Map>() {
                @Override
                public Map convert(String source, Type<? extends Map> destinationType, MappingContext mappingContext) {
                    return JacksonUtils.readValue(source, destinationType.getRawType());
                }
            });
            return MAPPER_FACTORY.getMapperFacade().map(source, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.DATA_CONVERSION_ERROR);
        }
    }
}
