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

    public static <T> T mapAttr(Object source, Class<T> clazz) {
        try {
            MAPPER_FACTORY.getConverterFactory().registerConverter(new MyConverter());
            MapperFacade mapperFacade = MAPPER_FACTORY.getMapperFacade();
            return mapperFacade.map(source, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.DATA_CONVERSION_ERROR);
        }
    }

    public static <T> T mapAttrVO(Object source, Class<T> clazz) {
        try {
            MAPPER_FACTORY.classMap(AttributeDefinition.class, AttrDefinitionVO.class).customize(new MyCustomize()).register();
            MapperFacade mapperFacade = MAPPER_FACTORY.getMapperFacade();
            return mapperFacade.map(source, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.DATA_CONVERSION_ERROR);
        }
    }

    private static class MyConverter extends CustomConverter<String, Map> {
        @Override
        public Map convert(String source, Type<? extends Map> destinationType, MappingContext mappingContext) {
            return JacksonUtils.readValue(source, destinationType.getRawType());
        }
    }

    public static class MyCustomize extends CustomMapper<AttributeDefinition, AttrDefinitionVO> {
        @Override
        public void mapAtoB(AttributeDefinition attributeDefinition, AttrDefinitionVO attrDefinitionVO,
                            MappingContext context) {
            super.mapAtoB(attributeDefinition, attrDefinitionVO, context);
            String additionalInfo = attributeDefinition.getAdditionalInfo();
            attrDefinitionVO.setAdditionalInfo(JacksonUtils.readValue(additionalInfo, Map.class));
        }

        @Override
        public void mapBtoA(AttrDefinitionVO attrDefinitionVO, AttributeDefinition attributeDefinition,
                            MappingContext context) {
            super.mapBtoA(attrDefinitionVO, attributeDefinition, context);
            Map<String, Object> additionalInfo = attrDefinitionVO.getAdditionalInfo();
            attributeDefinition.setAdditionalInfo(JacksonUtils.writeValueAsString(additionalInfo));
        }
    }
}
