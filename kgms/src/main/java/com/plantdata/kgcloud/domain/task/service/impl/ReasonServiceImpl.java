package com.plantdata.kgcloud.domain.task.service.impl;


import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.task.dto.NodeBean;
import com.plantdata.kgcloud.domain.task.dto.ReasonBean;
import com.plantdata.kgcloud.domain.task.dto.TripleBean;
import com.plantdata.kgcloud.domain.task.service.ReasonService;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReasonServiceImpl implements ReasonService {

    @Autowired
    private MongoClient mongoClient;

    @Override
    public ReasonBean listByPage(String kgName, Integer execId, BaseReq req) {
        ReasonBean reasonBean = new ReasonBean();
        long count = mongoClient.getDatabase("reasoning_store").getCollection(kgName).countDocuments(new Document("exec_id", execId));
        if (count > 0) {
            FindIterable<Document> documents = mongoClient.getDatabase("reasoning_store").getCollection(kgName).find(new Document("exec_id", execId));

            documents = documents.skip(req.getOffset()).limit(req.getLimit());


            MongoCursor<Document> cursor = documents.iterator();
            List<TripleBean> tripleList = new ArrayList<>();
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Object data = doc.get("data");
                String temp = JacksonUtils.writeValueAsString(data);
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

        try {
            List<TripleBean> tripleList = new ArrayList<>();

            Document matchDoc = null;
            if (type == 1) {
                matchDoc = new Document("exec_id", taskId);
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
                MongoCursor<Document> cursor = mongoClient.getDatabase("reasoning_store").getCollection(kgName).find(matchDoc).iterator();
                String batch = "";
                String source = "reasoning";
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    Object data = doc.get("data");
                    batch = doc.getString("batch");
                    String temp = JacksonUtils.writeValueAsString(data);
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
                            mongoClient.getDatabase(getDbName(kgName)).getCollection("attribute_summary").insertMany(summaryList);
                        }
                        mongoClient.getDatabase(getDbName(kgName)).getCollection(collection).insertMany(attributeList);
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

    private String getDbName(String kgName) {
        MongoCursor<Document> iterator = mongoClient.getDatabase("kg_attribute_definition").getCollection("kg_db_name").find(new Document("kg_name", kgName)).iterator();
        if (iterator.hasNext()) {
            Document document = iterator.next();
            return document.getString("db_name");
        } else {
            return kgName;
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