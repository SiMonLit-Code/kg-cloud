package com.plantdata.kgcloud.service.impl;

import com.plantdata.kgcloud.service.ReasoningService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Service
public class ReasoningServiceImpl implements ReasoningService {

//    @Autowired
//    private MongoClient mongoClient;
//    @Resource
//    private ConfigManage configManage;
//
//    @Override
//    public ReasoningResultBean list(String kgName, Integer execId, PageModel pageModel) {
//        ReasoningResultBean reasoningResultBean = new ReasoningResultBean();
//        long count = mongoClient.getDatabase("reasoning_store").getCollection(kgName).count(new Document("exec_id", execId));
//        if (count > 0) {
//            FindIterable<Document> documents = mongoClient.getDatabase("reasoning_store").getCollection(kgName).find(new Document("exec_id", execId));
//            if (pageModel.getPageSize() != null && pageModel.getPageNo() != null) {
//                int skip = pageModel.getPageNo();
//                documents = documents.skip(skip).limit(pageModel.getPageSize());
//            }
//
//            MongoCursor<Document> cursor = documents.iterator();
//            List<TripleBean> tripleList = new ArrayList<>();
//            while (cursor.hasNext()) {
//                Document doc = cursor.next();
//                Object data = doc.get("data");
//                String temp = JSON.toJSONString(data);
//                TripleBean tripleBean = JsonUtils.fromJson(temp, TripleBean.class);
//                tripleBean.setId(doc.getObjectId("_id").toString());
//                tripleBean.setStatus(doc.getInteger("status", 0));
//                tripleList.add(tripleBean);
//            }
//            reasoningResultBean.setTripleList(tripleList);
//        }
//        reasoningResultBean.setCount(count);
//        return reasoningResultBean;
//    }
//
//    @Override
//    public boolean importTriple(int type, String kgName, int mode, Integer taskId, List<String> dataIdList) {
//        List<TripleBean> tripleList = new ArrayList<>();
//        Document matchDoc = null;
//        if (type == 1) {
//            matchDoc = new Document("exec_id", taskId);
//        } else {
//            if (dataIdList != null && dataIdList.size() > 0) {
//                List<ObjectId> idList = new ArrayList<>();
//                for (String id : dataIdList) {
//                    idList.add(new ObjectId(id));
//                }
//                matchDoc = new Document("_id", new Document("$in", idList));
//            }
//        }
//
//        if (matchDoc != null) {
//            MongoCursor<Document> cursor = mongoClient.getDatabase("reasoning_store").getCollection(kgName).find(matchDoc).iterator();
//            String batch = "";
//            String source = "reasoning";
//            while (cursor.hasNext()) {
//                Document doc = cursor.next();
//                Object data = doc.get("data");
//                batch = doc.getString("batch");
//                String temp = JSON.toJSONString(data);
//                TripleBean tripleBean = JsonUtils.fromJson(temp, TripleBean.class);
//                tripleList.add(tripleBean);
//            }
//
//            Document metaData = new Document();
//            metaData.put("meta_data_2", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
//            metaData.put("meta_data_11", source);
//            metaData.put("meta_data_13", batch);
//
//            if (tripleList.size() > 0) {
//                String collection = null;
//                List<Document> attributeList = new ArrayList<>();
//                List<Document> summaryList = new ArrayList<>();
//                for (TripleBean tripleBean : tripleList) {
//                    Document doc = new Document();
//                    doc.put("entity_id", tripleBean.getStart().getId());
//                    doc.put("entity_type", tripleBean.getStart().getConceptId());
//                    if (mode == 1) {
//                        doc.put("attr_id", tripleBean.getEdge().getId());
//                    } else {
//                        doc.put("attr_name", tripleBean.getEdge().getName());
//                    }
//                    NodeBean end = tripleBean.getEnd();
//                    if(end.getType() == 0) {
//                        collection = "attribute_private_object";
//                        if (mode == 1) {
//                            collection = "attribute_object";
//                        }
//                        doc.put("attr_value", end.getId());
//                        doc.put("attr_value_type", end.getConceptId());
//                    } else if(end.getType() == 1){
//                        collection = "attribute_private_data";
//                        if (mode == 1) {
//                            collection = getCollection(end.getValueType());
//                            Document summary = new Document();
//                            summary.put("entity_id", tripleBean.getStart().getId());
//                            summary.put("attr_id", tripleBean.getEdge().getId());
//                            summaryList.add(summary);
//                        }
//                        doc.put("attr_value", getValue(end.getValue(), end.getValueType()));
//                    }
//                    if (tripleBean.getReason() != null) {
//                        metaData.put("meta_data_111", tripleBean.getReason());
//                    }
//                    doc.put("meta_data", metaData);
//                    attributeList.add(doc);
//                }
//
//                if (attributeList.size() > 0) {
//                    if(summaryList.size() > 0){
//                        mongoClient.getDatabase(getDBName(kgName)).getCollection("attribute_summary").insertMany(summaryList);
//                    }
//                    mongoClient.getDatabase(getDBName(kgName)).getCollection(collection).insertMany(attributeList);
//
//                    if ("true".equals(configManage.getKglogOpen().toLowerCase())) {
//                        List<LogBean> logs = new ArrayList<>();
//                        for (Document doc : attributeList) {
//                            Long id = doc.getLong("entity_id");
//                            LogBean log = new LogBean(id, 1, ModuleEnum.SCRIPT_REASON, ActionEnum.ATTR_OBJ_ADD);
//                            log.setAttrs(Lists.newArrayList(doc));
//                            logs.add(log);
//                        }
//                        LogProducer.send(configManage.getCloudKafkaHosts(), configManage.getKglogKafkaTopic(), kgName, logs);
//                    }
//                    mongoClient.getDatabase("reasoning_store").getCollection(kgName).updateMany(matchDoc, new Document("$set", new Document("status", 1)));
//                }
//
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public Object validate(String kgName, String bean, Long id){
//        return null;
//    }
//
//    private String getDBName(String kgName) {
//        MongoCursor<Document> iterator = mongoClient.getDatabase("kg_attribute_definition").getCollection("kg_db_name").find(new Document("kg_name", kgName)).iterator();
//        if (iterator.hasNext()) {
//            Document document = iterator.next();
//            return document.getString("db_name");
//        } else {
//            return kgName;
//        }
//    }
//
//    private String getCollection(int dataType){
//        switch (dataType){
//            case 1:
//            case 3:
//                return "attribute_integer";
//            case 2:
//                return "attribute_float";
//            case 4:
//            case 41:
//            case 42:
//                return "attribute_date_time";
//            case 10:
//                return "attribute_text";
//            case 5:
//            default:
//                return "attribute_string";
//        }
//    }
//
//    private Object getValue(Object input, int dataType){
//        try {
//            String t = input.toString();
//            switch (dataType){
//                case 1:
//                case 3:
//                    return Integer.parseInt(t);
//                case 2:
//                    return Double.parseDouble(t);
//                case 4:
//                case 41:
//                case 42:
//                case 10:
//                case 5:
//                default:
//                    return t;
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        } finally {
//        }
//        return input;
//    }
}