package com.plantdata.kgcloud.domain.task.service.impl;


import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.UpdateOptions;
import com.plantdata.graph.logging.core.GraphLog;
import com.plantdata.graph.logging.core.ServiceEnum;
import com.plantdata.graph.logging.core.segment.AttributeSegment;
import com.plantdata.graph.logging.core.segment.PrivateAttributeSegment;
import com.plantdata.graph.logging.core.segment.PrivateRelationSegment;
import com.plantdata.graph.logging.core.segment.RelationSegment;
import com.plantdata.graph.logging.core.segment.Segment;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.service.LogSender;
import com.plantdata.kgcloud.domain.edit.util.ThreadLocalUtils;
import com.plantdata.kgcloud.domain.task.dto.NodeBean;
import com.plantdata.kgcloud.domain.task.dto.ReasonBean;
import com.plantdata.kgcloud.domain.task.dto.TripleBean;
import com.plantdata.kgcloud.domain.task.service.ReasonService;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReasonServiceImpl implements ReasonService {

    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private LogSender logSender;

    @Override
    public ReasonBean listByPage(String kgName, Integer execId, BaseReq req) {
        ReasonBean reasonBean = new ReasonBean();
        long count = mongoClient.getDatabase("reasoning_store").getCollection(kgName).countDocuments(new Document("exec_id", execId));
        if (count > 0) {
            FindIterable<Document> documents = mongoClient.getDatabase("reasoning_store").getCollection(kgName).find(new Document("exec_id", execId));
            documents = documents.skip(req.getOffset()).limit(req.getLimit());
            List<TripleBean> tripleList = new ArrayList<>();
            for (Document doc : documents) {
                String temp = JacksonUtils.writeValueAsString(doc.get("data"));
                TripleBean tripleBean = JacksonUtils.readValue(temp, TripleBean.class);
                tripleBean.setId(doc.getObjectId("_id").toString());
                tripleBean.setStatus(doc.getInteger("status", 0));
                tripleList.add(tripleBean);
            }
            reasonBean.setTripleList(tripleList);
        }
        reasonBean.setCount(count);
        return reasonBean;
    }

    @Override
    public Map<String, Object> importTriple(int type, String kgName, int mode, Integer taskId, List<String> dataIdList) {

        Map<String, Object> map = new HashMap<>(2);
        logSender.setActionId();
        String logId = ThreadLocalUtils.getBatchNo();
        String dbName = KGUtil.dbName(kgName);
        try {
            List<TripleBean> tripleList = new ArrayList<>();

            Document matchDoc = null;
            if (type == 1) {
                matchDoc = new Document("exec_id", taskId).append("status", new Document("$ne", 1));
            } else {
                if (dataIdList != null && dataIdList.size() > 0) {
                    List<ObjectId> idList = new ArrayList<>();
                    for (String id : dataIdList) {
                        idList.add(new ObjectId(id));
                    }
                    matchDoc = new Document("_id", new Document("$in", idList));
                }
            }

            if (matchDoc != null) {
                FindIterable<Document> findIterable = mongoClient.getDatabase("reasoning_store").getCollection(kgName).find(matchDoc);
                String batch = "";
                String source = "reasoning";
                for (Document doc : findIterable) {
                    batch = doc.getString("batch");
                    String temp = JacksonUtils.writeValueAsString(doc.get("data"));
                    TripleBean tripleBean = JacksonUtils.readValue(temp, TripleBean.class);
                    tripleList.add(tripleBean);
                }

                Document metaData = new Document();
                metaData.put("meta_data_2", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
                metaData.put("meta_data_11", source);
                metaData.put("meta_data_13", batch);

                if (tripleList.size() > 0) {
                    String collection = null;
                    List<Document> attributeList = new ArrayList<>();
                    List<Document> summaryList = new ArrayList<>();
                    for (TripleBean tripleBean : tripleList) {
                        Document doc = new Document();
                        doc.put("entity_id", tripleBean.getStart().getId());
                        doc.put("entity_type", tripleBean.getStart().getConceptId());
                        if (mode == 1) {
                            doc.put("attr_id", tripleBean.getEdge().getId());
                        } else {
                            doc.put("attr_name", tripleBean.getEdge().getName());
                        }
                        NodeBean end = tripleBean.getEnd();
                        if (end.getType() == 0) {
                            collection = "attribute_private_object";
                            if (mode == 1) {
                                collection = "attribute_object";
                            }
                            doc.put("attr_value", end.getId());
                            doc.put("attr_value_type", end.getConceptId());
                        } else if (end.getType() == 1) {
                            collection = "attribute_private_data";
                            if (mode == 1) {
                                collection = getCollection(end.getValueType());
                                Document summary = new Document();
                                summary.put("entity_id", tripleBean.getStart().getId());
                                summary.put("attr_id", tripleBean.getEdge().getId());
                                summaryList.add(summary);
                            }
                            doc.put("attr_value", getValue(end.getValue(), end.getValueType()));
                        }
                        if (tripleBean.getReason() != null) {
                            metaData.put("meta_data_111", tripleBean.getReason());
                        }
                        doc.put("meta_data", metaData);
                        attributeList.add(doc);
                    }

                    if (attributeList.size() > 0 && collection != null) {
                        if (summaryList.size() > 0) {
                            upsertMany(mongoClient, dbName, "attribute_summary", summaryList, "entity_id", "attr_id");
                        }
                        String[] fields;
                        if (collection.contains("private")) {
                            fields = new String[]{"entity_id", "attr_name"};
                        } else {
                            fields = new String[]{"entity_id", "attr_id"};
                        }
                        upsertMany(mongoClient, dbName, collection, attributeList, fields);
                        Segment segment = null;
                        for (Document attr : attributeList) {
                            if ("attribute_private_object".equals(collection)) {
                                segment = PrivateRelationSegment.ofBson(attr);
                            } else if ("attribute_object".equals(collection)) {
                                segment = RelationSegment.ofBson(attr);
                            } else if ("attribute_private_data".equals(collection)) {
                                segment = PrivateAttributeSegment.ofBson(attr);
                            } else {
                                segment = AttributeSegment.ofBson(attr);
                            }
                            GraphLog kgLog = GraphLog.create(segment, null, logId);
                            logSender.sendDataLog(dbName, kgLog);
                        }
                        logSender.sendLog(kgName, ServiceEnum.SCRIPT_REASON);
                        mongoClient.getDatabase("reasoning_store").getCollection(kgName).updateMany(matchDoc, new Document("$set", new Document("status", 1)));
                    }
                }
            }
            map.put("status", 1);
            map.put("msg", "成功");
        } catch (Exception e) {
            map.put("status", 2);

            map.put("msg", "失败");
        }
        return map;
    }

    private void upsertMany(MongoClient client, String database, String collection, Collection<Document> ls, String... fieldArr) {

        if (ls == null || ls.isEmpty()) {
            return;
        }
        if (collection.contains("object")) {
            client.getDatabase(database).getCollection(collection).insertMany(Lists.newArrayList(ls));
        } else {
            List<UpdateManyModel<Document>> requests = ls.stream().map(s -> {
                Document filter = new Document();
                for (String field : fieldArr) {
                    filter.append(field, s.get(field));
                }
                Document update = new Document("$set", s);
                UpdateOptions upsert = new UpdateOptions().upsert(true);
                return new UpdateManyModel<Document>(filter, update, upsert);
            }).collect(Collectors.toList());
            client.getDatabase(database).getCollection(collection).bulkWrite(requests);
        }
    }

    private String getCollection(int dataType) {
        switch (dataType) {
            case 1:
            case 3:
                return "attribute_integer";
            case 2:
                return "attribute_float";
            case 4:
            case 41:
            case 42:
                return "attribute_date_time";
            case 10:
                return "attribute_text";
            case 5:
            default:
                return "attribute_string";
        }
    }

    private Object getValue(Object input, int dataType) {
        try {
            String t = input.toString();
            switch (dataType) {
                case 1:
                case 3:
                    return Integer.parseInt(t);
                case 2:
                    return Double.parseDouble(t);
                case 4:
                case 41:
                case 42:
                case 10:
                case 5:
                default:
                    return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return input;
    }
}