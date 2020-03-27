package com.plantdata.kgcloud.domain.edit.converter;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.common.collect.Lists;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/11/11 15:13
 * @Description:
 */
@Component
public class DocumentConverter {

    private static final Map<Class<?>, List<FieldMethod>> METHOD_CACHE = new ConcurrentHashMap<>();

    public <T> List<T> toBeans(FindIterable<Document> documents, Class<T> clazz) {
        List<T> list = Lists.newArrayList();
        for (Document document : documents) {
            list.add(toBean(document, clazz));
        }
        return list;
    }

    public <T> T toBean(Document document, Class<T> clazz) {
        if (Objects.isNull(document)) {
            return null;
        }
        try {
            objId2String(document);
            return JacksonUtils.readValue(JacksonUtils.writeValueAsString(document),clazz);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.DATA_CONVERSION_ERROR);
        }
    }

    private void objId2String(Document document) {
        if (document.get(CommonConstants.MongoConst.ID) != null) {
            document.put("id", document.get(CommonConstants.MongoConst.ID).toString());
            document.remove(CommonConstants.MongoConst.ID);
        }
    }

    public Bson buildObjectId(String id) {
        return Filters.eq(CommonConstants.MongoConst.ID, new ObjectId(id));
    }

    public <T> List<Document> toDocuments(List<T> beans) {
        return beans.stream().map(this::toDocument).collect(Collectors.toList());
    }

    public <T> Document toDocument(T query) {
        Document document = new Document();
        if (query == null) {
            return document;
        }
        try {
            Class<?> clz = query.getClass();
            Class<?> currentClass = clz;
            List<FieldMethod> list = METHOD_CACHE.get(clz);
            if (list == null) {
                list = Lists.newArrayList();
                List<Field> fields = Lists.newArrayList();
                while (clz != Object.class) {
                    fields.addAll(Lists.newArrayList(clz.getDeclaredFields()));
                    clz = clz.getSuperclass();
                }
                for (Field field : fields) {
                    String fieldName = field.getName();
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, currentClass);
                    if (field.isAnnotationPresent(JsonAlias.class)) {
                        fieldName = field.getAnnotation(JsonAlias.class).value()[0];
                    }
                    FieldMethod fieldMethod = new FieldMethod();
                    fieldMethod.setName(fieldName);
                    fieldMethod.setMethod(propertyDescriptor.getReadMethod());
                    list.add(fieldMethod);
                }
                METHOD_CACHE.put(currentClass, list);
            }
            for (FieldMethod fm : list) {
                Object fieldValue = fm.getMethod().invoke(query);
                if (fieldValue != null) {
                    document.append(fm.getName(), fieldValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (document.size() == 0 && query instanceof Map) {
            throw BizException.of(KgmsErrorCodeEnum.ILLEGAL_PARAM);
        }
        return document;
    }
}
