package com.plantdata.kgcloud.domain.edit.util;

import com.google.gson.Gson;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.exception.BizException;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

import java.util.List;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 11:51
 * @Description:
 */
public class MapperUtils {

    private static final MapperFactory MAPPER_FACTORY = new DefaultMapperFactory.Builder().build();

    static {
        MAPPER_FACTORY.getConverterFactory().registerConverter(new MyConverter());
    }

    public static MapperFactory getMapperFactory() {
        return MAPPER_FACTORY;
    }

    public static <S, T> T map(S source, Class<T> clazz) {
        try {
            return getMapperFactory().getMapperFacade().map(source, clazz);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.DATA_CONVERSION_ERROR);
        }
    }

    public static <S, T> List<T> map(List<S> source, Class<T> clazz) {
        try {
            return getMapperFactory().getMapperFacade().mapAsList(source, clazz);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.DATA_CONVERSION_ERROR);
        }
    }

    public static class MyConverter extends CustomConverter<String, Map<String, Object>> {

        @Override
        public Map<String, Object> convert(String stringObjectMap, Type<? extends Map<String, Object>> type,
                                           MappingContext mappingContext) {
            return new Gson().fromJson(stringObjectMap, type.getRawType());
        }

    }
}
