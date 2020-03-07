package com.plantdata.kgcloud.domain.graph.log.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.plantdata.graph.logging.core.GraphLogOperation;
import com.plantdata.graph.logging.core.GraphLogScope;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.graph.log.entity.DataLogRsp;
import com.plantdata.kgcloud.domain.graph.log.entity.ServiceLogReq;
import com.plantdata.kgcloud.domain.graph.log.entity.ServiceLogRsp;
import com.plantdata.kgcloud.domain.graph.log.service.GraphLogService;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.plantdata.kgcloud.constant.KgmsConstants.*;

/**
 * @author xiezhenxiang 2020/1/15
 */
@Slf4j
@Service
public class GraphLogServiceImpl implements GraphLogService {

    @Autowired
    private MongoClient mongoClient;

    @Override
    public BasePage<ServiceLogRsp> serviceLogList(String kgName, ServiceLogReq req) {

        List<ServiceLogRsp> ls = new ArrayList<>();
        int page = (req.getPage() - 1) * req.getSize();
        Bson query = new Document();
        if (req.getServiceEnum() != null) {
            query = Filters.eq("serviceEnum", req.getServiceEnum().name());
        }
        if (req.getStartTime() != null) {
            query = Filters.and(query, Filters.gte("createTime", req.getServiceEnum()));
        }
        if (req.getEndTime() != null) {
            query = Filters.and(query, Filters.lte("createTime", req.getEndTime()));
        }

        String dbName = LOG_DB_PREFIX + kgName;
        long count = mongoClient.getDatabase(dbName).getCollection(LOG_SERVICE_TB).countDocuments(query);
        MongoCursor<Document> cursor = mongoClient.getDatabase(dbName).getCollection(LOG_SERVICE_TB).find(query)
                .sort(Sorts.descending("createTime"))
                .skip(page).limit(req.getSize()).iterator();

        cursor.forEachRemaining(s -> {
            boolean isBatch = false;
            s.append("id", s.get("_id").toString());
            String batch = s.getString("batch");
            long actionCount = mongoClient.getDatabase(dbName).getCollection(LOG_DATA_TB).countDocuments(Filters.eq("batch", batch));

            if (actionCount == 1) {
                // 单条日志
                Document doc = mongoClient.getDatabase(dbName).getCollection(LOG_DATA_TB).find(Filters.eq("batch", batch)).first();
                s.putAll(doc);
            } else if (actionCount > 1) {
                isBatch = true;
                s.put("message", getBatchMsg(dbName, batch));
            }
            s.put("isBatch", isBatch);
            if (StringUtils.isNotBlank(s.getString("message"))) {
                ServiceLogRsp rsp = JSONObject.parseObject(JacksonUtils.writeValueAsString(s), ServiceLogRsp.class);
                ls.add(rsp);
            }
        });

        return new BasePage<>(count, ls);
    }

    private String getBatchMsg(String dbName, String batch) {

        StringBuilder builder = new StringBuilder();
        Bson addQuery = Filters.and(Filters.eq("batch", batch), Filters.eq("operation", GraphLogOperation.ADD.name()));
        Bson deleteQuery = Filters.and(Filters.eq("batch", batch), Filters.eq("operation", GraphLogOperation.DELETE.name()));
        MongoCollection col = mongoClient.getDatabase(dbName).getCollection(LOG_DATA_TB);
        long addAttrDefineCount = col.countDocuments(Filters.and(addQuery, Filters.eq("scope", GraphLogScope.ATTRIBUTE_DEFINE.name())));
        long addConceptCount = col.countDocuments(Filters.and(addQuery, Filters.eq("scope", GraphLogScope.CONCEPT.name())));
        long addEntityCount = col.countDocuments(Filters.and(addQuery, Filters.eq("scope", GraphLogScope.ENTITY.name())));
        long editAttrCount = col.countDocuments(Filters.and(addQuery, Filters.eq("scope", GraphLogScope.ATTRIBUTE.name())));
        long addRelationCount = col.countDocuments(Filters.and(addQuery, Filters.eq("scope", GraphLogScope.RELATION.name())));
        long delEntityCount = col.countDocuments(Filters.and(deleteQuery, Filters.eq("scope", GraphLogScope.ENTITY.name())));
        long delRelationCount = col.countDocuments(Filters.and(deleteQuery, Filters.eq("scope", GraphLogScope.RELATION.name())));

        if (addConceptCount > 0) {
            builder.append("新增概念").append(addConceptCount).append("个;");
        }
        if (addAttrDefineCount > 0) {
            builder.append("新增属性定义").append(addAttrDefineCount).append("个;");
        }
        if (addEntityCount > 0) {
            builder.append("新增实体").append(addEntityCount).append("个;");
        }
        if (editAttrCount > 0) {
            builder.append("编辑属性").append(editAttrCount).append("个;");
        }
        if (addRelationCount > 0) {
            builder.append("新增关系").append(addRelationCount).append("个;");
        }
        if (delEntityCount > 0) {
            builder.append("删除实体").append(delEntityCount).append("个;");
        }
        if (delRelationCount > 0) {
            builder.append("删除关系").append(delRelationCount).append("个;");
        }
        if (builder.length() == 0) {
            builder.append("批处理");
        }
        return builder.toString();
    }

    @Override
    public BasePage<DataLogRsp> dataLogList(String kgName, String batch, BaseReq req) {

        int page = (req.getPage() - 1) * req.getSize();
        Bson query = Filters.eq("batch", batch);
        return logList(kgName, query, page, req.getSize());
    }

    private BasePage<DataLogRsp> logList(String kgName, Bson query, Integer page, Integer size) {

        String dbName = LOG_DB_PREFIX + kgName;
        MongoCursor<Document> cursor = mongoClient.getDatabase(dbName).getCollection(LOG_DATA_TB).find(query)
                .sort(Sorts.descending("createTime"))
                .skip(page)
                .limit(size)
                .iterator();

        long count = mongoClient.getDatabase(dbName).getCollection(LOG_DATA_TB).countDocuments(query);
        List<DataLogRsp> ls = new ArrayList<>();
        cursor.forEachRemaining(s -> {
            s.append("id", s.get("_id").toString());
            DataLogRsp rsp = JSONObject.parseObject(JacksonUtils.writeValueAsString(s), DataLogRsp.class);
            ls.add(rsp);
        });
        return new BasePage<>(count, ls);
    }

    @Override
    public BasePage<DataLogRsp> entityLogList(String kgName, Long id, Integer type, BaseReq req) {

        int page = (req.getPage() - 1) * req.getSize();
        Bson query;
        if (type == 0) {
            query = Filters.and(
                    Filters.eq("scope", GraphLogScope.CONCEPT.name()),
                    Filters.or(Filters.eq("newValue.id", id), Filters.eq("oldValue.id", id))
            );
        } else {
            query = Filters.and(
                    Filters.in("scope", Lists.newArrayList(GraphLogScope.ENTITY.name(), GraphLogScope.ENTITY_LINK.name(), GraphLogScope.ENTITY_TAG.name(), GraphLogScope.ATTRIBUTE.name(), GraphLogScope.PRIVATE_ATTRIBUTE.name())),
                    Filters.or(Filters.eq("newValue.id", id), Filters.eq("oldValue.id", id),
                            Filters.eq("newValue.entityId", id), Filters.eq("oldValue.entityId", id))
            );
        }
        return logList(kgName, query, page, req.getSize());
    }

    @Override
    public BasePage<DataLogRsp> attrDefineLogList(String kgName, Integer attrId, BaseReq req) {
        int page = (req.getPage() - 1) * req.getSize();
        Bson query = Filters.and(
                Filters.eq("scope", GraphLogScope.ATTRIBUTE_DEFINE.name()),
                Filters.or(Filters.eq("newValue.id", attrId), Filters.eq("oldValue.id", attrId))
        );
        return logList(kgName, query, page, req.getSize());
    }

    @Override
    public BasePage<DataLogRsp> edgeAttrLogList(String kgName, Integer relationAttrId, BaseReq req) {

        int page = (req.getPage() - 1) * req.getSize();
        Bson query = Filters.and(
                Filters.eq("scope", GraphLogScope.SIDE_ATTR_DEFINE.name()),
                Filters.or(Filters.eq("newValue.attrId", relationAttrId), Filters.eq("oldValue.attrId", relationAttrId))
        );
        return logList(kgName, query, page, req.getSize());
    }

    @Override
    public BasePage<DataLogRsp> relationLogList(String kgName, Long entityId, BaseReq req) {

        int page = (req.getPage() - 1) * req.getSize();
        Bson query = Filters.and(
                Filters.in("scope", GraphLogScope.RELATION.name(), GraphLogScope.RELATION_OBJECT.name()
                        , GraphLogScope.RELATION_VALUE.name(), GraphLogScope.PRIVATE_RELATION.name()),
                Filters.or(Filters.eq("newValue.entityId", entityId), Filters.eq("oldValue.entityId", entityId))
        );
        return logList(kgName, query, page, req.getSize());
    }
}
