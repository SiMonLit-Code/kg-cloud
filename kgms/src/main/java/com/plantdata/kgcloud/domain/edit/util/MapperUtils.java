package com.plantdata.kgcloud.domain.edit.util;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.exception.BizException;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

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
}
