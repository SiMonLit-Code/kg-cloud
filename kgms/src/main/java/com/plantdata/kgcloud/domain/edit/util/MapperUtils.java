package com.plantdata.kgcloud.domain.edit.util;

import ai.plantdata.kg.common.bean.AttributeDefinition;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.edit.AttrDefinitionVO;
import com.plantdata.kgcloud.util.JacksonUtils;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
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

    public static MapperFactory getMapperFactory() {
        return MAPPER_FACTORY;
    }

    public static <S, T> T map(S source, Class<T> clazz) {
        try {
            return MAPPER_FACTORY.getMapperFacade().map(source, clazz);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.DATA_CONVERSION_ERROR);
        }
    }

    public static <S, T> List<T> map(List<S> source, Class<T> clazz) {
        try {
            return MAPPER_FACTORY.getMapperFacade().mapAsList(source, clazz);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.DATA_CONVERSION_ERROR);
        }
    }

}
