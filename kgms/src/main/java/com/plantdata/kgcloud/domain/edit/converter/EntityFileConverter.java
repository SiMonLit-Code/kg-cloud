package com.plantdata.kgcloud.domain.edit.converter;

import ai.plantdata.cloud.constant.CommonConstants;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.util.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.common.collect.Lists;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelationScore;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lp
 * @date 2020/5/19 20:30
 */
@Component
public class EntityFileConverter {

    @Autowired
    private DocumentConverter documentConverter;

    private static final Map<Class<?>, List<FieldMethod>> METHOD_CACHE = new ConcurrentHashMap<>();

    public List<EntityFileRelation> toBeans(FindIterable<Document> documents) {
        List<EntityFileRelation> list = Lists.newArrayList();
        for (Document document : documents) {
            list.add(toBean(document));
        }
        return list;
    }

    public List<EntityFileRelation> toBeans(List<Document> documents) {
        List<EntityFileRelation> list = Lists.newArrayList();
        for (Document document : documents) {
            list.add(toBean(document));
        }
        return list;
    }

    public EntityFileRelation toBean(Document document) {
        if (Objects.isNull(document)) {
            return null;
        }
        try {
            objId2String(document);
            String s = JacksonUtils.writeValueAsString(document);
            return JacksonUtils.readValue(s, EntityFileRelation.class);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.DATA_CONVERSION_ERROR);
        }
    }

    private void objId2String(Document document) {
        if (document.get(CommonConstants.MongoConst.ID) != null) {
            document.put("id", document.get(CommonConstants.MongoConst.ID).toString());
            document.remove(CommonConstants.MongoConst.ID);
        }
        if (document.get("fileId") != null) {
            document.replace("fileId", document.get("fileId").toString());
        }
    }

    public Bson buildObjectId(String id) {
        return Filters.eq(CommonConstants.MongoConst.ID, new ObjectId(id));
    }

    public List<Document> toDocuments(List<EntityFileRelation> beans) {
        return beans.stream().map(this::toDocument).collect(Collectors.toList());
    }

    public Document toDocument(EntityFileRelation relation) {
        Document document = new Document();
        if (relation == null) {
            return document;
        }
        try {
            Class<?> clz = relation.getClass();
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
                Object fieldValue = fm.getMethod().invoke(relation);
                if (fieldValue != null) {
                    document.append(fm.getName(), fieldValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (document.size() == 0 && relation instanceof Map) {
            throw BizException.of(KgmsErrorCodeEnum.ILLEGAL_PARAM);
        }
        String fileId = relation.getFileId();
        document.remove("fileId");
        if (StringUtils.isNotBlank(fileId)) {
            document.put("fileId", new ObjectId(fileId));
        }
        List<EntityFileRelationScore> entityAnnotation = relation.getEntityAnnotation();
        if (entityAnnotation != null) {
            List<Document> documents = documentConverter.toDocuments(entityAnnotation);
            document.put("entityAnnotation", documents);
        }

        return document;
    }
}
