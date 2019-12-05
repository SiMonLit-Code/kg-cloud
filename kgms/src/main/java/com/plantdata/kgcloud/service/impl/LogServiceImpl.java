package com.plantdata.kgcloud.service.impl;

import com.plantdata.kgcloud.service.LogService;
import org.springframework.stereotype.Service;

/**
 * @author xiezhenxiang 2019/9/10
 */
@Service
public class LogServiceImpl implements LogService {

//    @Resource
//    private MongoClient mongoClient;
//
//    @Override
//    public RestData list(String kgName, Integer pageNo, Integer pageSize) {
//
//        List<Document> ls = new ArrayList<>();
//        String tbName = kgName + "_log";
//        pageNo = (pageNo - 1) * pageSize;
//        MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find().sort(new Document("actionTime", -1))
//                .skip(pageNo).limit(pageSize).iterator();
//        long count = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).count();
//
//        cursor.forEachRemaining(s -> {
//
//            String docId = s.get("_id").toString();
//            s.append("_id", docId);
//            Integer type = s.getInteger("type");
//            String name = s.getString("name");
//            String oriName = getOriName(tbName, s.get("id"), s.get("_id").toString(), s.get("actionTime"));
//
//            if (StringUtils.isBlank(name)) {
//                name = oriName;
//                s.append("name", name);
//            }
//            if (StringUtils.isNotBlank(oriName) && !oriName.equals(name)) {
//                s.append("nameOri", oriName);
//            }
//
//            if (type == null || type == 1) {
//                getParents(tbName, s);
//            }
//
//            getConceptName(tbName, s);
//            getOriAttrDefine(tbName, s);
//            getOriMeaning(tbName, s);
//            getOriAbs(tbName, s);
//            getOriImg(tbName, s);
//            getOriAttrs(tbName, s);
//            ls.add(s);
//        });
//
//        return new RestData<>(ls, count);
//    }
//
//    @SuppressWarnings("unchecked")
//    private void getOriAttrDefine(String tbName, Document doc) {
//
//        Object actionTime = doc.get("actionTime");
//        List<Map<String, Object>> attrDefines = (List<Map<String, Object>>) doc.get("attrDefines");
//        if (attrDefines == null) {
//            return;
//        }
//
//        for (Map<String, Object> attrDefine : attrDefines) {
//
//            Integer attrId = Integer.parseInt(attrDefine.get("id").toString());
//
//            getAttrOriName(tbName, attrId, attrDefine, actionTime);
//
//            String rangValue = (String) attrDefine.get("range_value");
//            String rangeName = getRangeName(tbName, rangValue, actionTime);
//            if (StringUtils.isNotBlank(rangeName)) {
//                attrDefine.put("rangeName", rangeName);
//            }
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private void getParents(String tbName, Document doc) {
//
//        List<Object> parents = (List<Object>) doc.get("parents");
//        Object actionTime = doc.get("actionTime");
//        Object id = doc.get("id");
//        if (parents != null && !parents.isEmpty()) {
//            return;
//        }
//
//        Bson query = Filters.and(new Document("id", id), Filters.lt("actionTime", actionTime),
//                Filters.exists("parents"));
//
//        MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find(query).sort(new Document("actionTime", -1))
//                .iterator();
//
//        if (cursor.hasNext()) {
//            doc.put("parents", cursor.next().get("parents"));
//        }
//    }
//
//    /**
//     * 原来的属性
//     **/
//    @SuppressWarnings("unchecked")
//    private void getOriAttrs(String tbName, Document doc) {
//
//        Object actionTime = doc.get("actionTime");
//        List<Document> attrs = (List<Document>) doc.get("attrs");
//        if (attrs == null) {
//            return;
//        }
//
//        for (Document s : attrs) {
//
//            int type = s.containsKey("attr_value_type") ? 1 : 0;
//            s.put("type", type);
//            if (s.containsKey("attr_name")) {
//                type = 2;
//            }
//            Integer attrId = s.getInteger("attr_id");
//
//            if (type != 2) {
//                if (attrId == null) {
//                    String tripleId = s.getString("_id");
//                    attrId = getAttrId(tbName, tripleId, actionTime);
//                    s.put("attr_id", attrId);
//                }
//                getAttrOriName(tbName, attrId, s, actionTime);
//            }
//
//            if (type == 0) {
//                getAttrValueOriName(tbName, attrId, s, actionTime);
//            } else if (type == 1){
//                getRelationOriName(tbName, attrId, s, actionTime);
//            } else {
//                s.put("name", s.getString("attr_name"));
//
//            }
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private Integer getAttrId(String tbName, String tripleId, Object actionTime) {
//
//        Integer attrId = null;
//        Bson query = Filters.and(Filters.lt("actionTime", actionTime),
//                Filters.elemMatch("attrs", Filters.and(Filters.eq("_id", tripleId), Filters.exists("attr_id"))));
//        MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find(query).sort(new Document("actionTime", -1)).iterator();
//
//        if (cursor.hasNext()) {
//            List<Document> attrs = (List<Document>) cursor.next().get("attrs");
//            attrId = attrs.get(0).getInteger("attr_id");
//        }
//        return attrId;
//    }
//
//    @SuppressWarnings("unchecked")
//    private void getRelationOriName(String tbName, Integer attrId, Document attr, Object actionTime) {
//
//        Bson query = Filters.and(Filters.lt("actionTime", actionTime),
//                Filters.elemMatch("attrs", Filters.and(Filters.eq("attr_id", attrId))));
//
//
//        if (attr.containsKey("attr_value")) {
//
//            String attrValue = attr.get("attr_value").toString();
//            String rangeName = getRangeName(tbName, attrValue, actionTime);
//            attr.put("attrValue", rangeName);
//            MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find(query)
//                    .sort(new Document("actionTime", -1)).iterator();
//
//            Object attrValueOri = "";
//            if (cursor.hasNext()) {
//                List<Document> attrs = (List<Document>) cursor.next().get("attrs");
//
//                for (Document e : attrs) {
//                    if (e.getInteger("attr_id").equals(attrId)) {
//                        Object value = e.get("attr_value");
//                        attrValueOri = getRangeName(tbName, value, actionTime);
//                        break;
//                    }
//                }
//            }
//
//            pkgSideAttr(attr, tbName, actionTime);
//
//            attr.put("attrValueOri", attrValueOri);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private void pkgSideAttr(Document attr, String tbName, Object actionTime) {
//
//        int attrId = attr.getInteger("attr_id");
//        List<Map<String, Object>> sideAtts = new ArrayList<>();
//
//        for (String key : attr.keySet()) {
//
//            if (key.startsWith("attr_ext_")) {
//
//                Integer seqNo = Integer.parseInt(key.substring(key.lastIndexOf("_") + 1));
//                Map<String, Object> sideAttr = new HashMap<>();
//                sideAttr.put("value", attr.get(key));
//
//                Bson query = Filters.and(Filters.lt("actionTime", actionTime),
//                        Filters.elemMatch("attrDefines", Filters.and(Filters.eq("id", attrId + ""))));
//
//                MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find(query)
//                        .sort(new Document("actionTime", -1)).iterator();
//
//                while (cursor.hasNext()) {
//
//                    Document s = cursor.next();
//
//                    List<Document> attrDefines = (List<Document>) s.get("attrDefines");
//
//                    Optional<Document> define =  attrDefines.stream().filter(d -> d.containsKey("extra_info")).findFirst();
//
//                    if (define.isPresent()) {
//                        Document info = (Document) define.get().get("extra_info");
//
//                        List<JSONObject> extras = JSONArray.parseArray(info.getString("extra_info"), JSONObject.class);
//                        String sideAttrName = extras.stream().filter(t -> t.getInteger("seqNo").equals(seqNo)).findFirst().get()
//                                .getString("name");
//                        sideAttr.put("name", sideAttrName);
//                        break;
//                    }
//                }
//                sideAtts.add(sideAttr);
//            }
//        }
//
//        if (!sideAtts.isEmpty()) {
//            attr.put("sideAttrs", sideAtts);
//        }
//    }
//
//
//    private String getRangeName(String tbName, Object rangeValue, Object actionTime) {
//
//        if (rangeValue == null) {
//            return "";
//        }
//        String[] ranges = rangeValue.toString().split(" ");
//
//        String rangeName = "";
//        for (String range : ranges) {
//            if (StringUtils.isNotBlank(range)) {
//                rangeName += getOriName(tbName, Long.parseLong(range), null, actionTime) + ",";
//            }
//        }
//
//        if (rangeName.length() > 0) {
//            rangeName = rangeName.substring(0, rangeName.length() - 1);
//        }
//
//        return rangeName;
//    }
//
//    @SuppressWarnings("unchecked")
//    private void getAttrValueOriName(String tbName, Integer attrId, Document attr, Object actionTime) {
//
//        Object attrValue = attr.get("attr_value");
//        Object entityId = attr.get("entity_id");
//        attr.put("attrValue", attrValue);
//        Object attrValueOri = "";
//        Bson query = Filters.and(Filters.lt("actionTime", actionTime),
//                Filters.elemMatch("attrs", Filters.and(Filters.eq("attr_id", attrId),
//                        Filters.eq("entity_id", entityId))));
//
//        MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find(query)
//                .sort(new Document("actionTime", -1)).iterator();
//
//        if (cursor.hasNext()) {
//
//            Document e = cursor.next();
//            List<Document> attrs = (List<Document>) e.get("attrs");
//            attrValueOri = attrs.get(0).get("attr_value");
//        }
//        if (attrValueOri != null && !attrValueOri.toString().equals(attrValue.toString())) {
//            attr.put("attrValueOri", attrValueOri);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private String getAttrOriName(String tbName, Integer attrId, Map<String, Object> doc, Object actionTime) {
//
//        String nameOri = "";
//        Object name = doc.get("name");
//        Bson query = Filters.and(Filters.lt("actionTime", actionTime),
//                Filters.elemMatch("attrDefines", Filters.and(Filters.eq("id", attrId), Filters.exists("name"))));
//
//        MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find(query)
//                .sort(new Document("actionTime", -1)).iterator();
//
//        if (cursor.hasNext()) {
//
//            Document attr = cursor.next();
//            List<Document> attrDefines = (List<Document>) attr.get("attrDefines");
//
//            for (Document s : attrDefines) {
//                if (s.getInteger("id").equals(attrId)) {
//                    nameOri = s.getString("name");
//                    break;
//                }
//            }
//        }
//        if (name == null) {
//            doc.put("name", nameOri);
//        } else if (!name.toString().equals(nameOri)) {
//            doc.put("nameOri", nameOri);
//        }
//        return nameOri;
//    }
//
//    /**
//     * 概念名称
//     **/
//    @SuppressWarnings("unchecked")
//    private void getConceptName(String tbName, Document doc) {
//
//        List<Object> parents = (List<Object>) doc.get("parents");
//        List<Object> sons = (List<Object>) doc.get("sons");
//        Object actionTime = doc.get("actionTime");
//
//        if (parents != null) {
//
//            List<Map> ls = pkgConceptLs(tbName, parents, actionTime);
//
//            doc.append("parents", ls);
//        }
//
//        if (sons != null) {
//
//            List<Map> ls = pkgConceptLs(tbName, sons, actionTime);
//
//            doc.append("sons", ls);
//        }
//    }
//
//    private List<Map> pkgConceptLs(String tbName, List<Object> conceptLs, Object actionTime) {
//
//        List<Map> ls = new ArrayList<>();
//        conceptLs.forEach(id -> {
//
//            Map<String, Object> m = new HashMap<>(8);
//            m.put("id", id);
//            m.put("name", getOriName(tbName, id, null, actionTime));
//            ls.add(m);
//        });
//
//        return ls;
//    }
//
//    /**
//     * 原来的实体图片
//     **/
//    private void getOriImg(String tbName, Document doc) {
//
//        String img = doc.getString("img");
//        Object actionTime = doc.get("actionTime");
//        Object id = doc.get("id");
//
//        if (StringUtils.isNotBlank(img)) {
//
//            String oriImg = "";
//            Bson query = Filters.and(new Document("id", id), Filters.lt("actionTime", actionTime),
//                   Filters.exists("img"));
//
//            MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find(query)
//                    .sort(new Document("actionTime", -1)).iterator();
//
//            if (cursor.hasNext()) {
//                oriImg = cursor.next().getString("img");
//            }
//
//            if (!oriImg.equals(img)) {
//                doc.append("imgOri", oriImg);
//            }
//        }
//    }
//
//    /**
//     * 原来的实体摘要
//     **/
//    private void getOriAbs(String tbName, Document doc) {
//
//        String abs = doc.getString("abs");
//        Object actionTime = doc.get("actionTime");
//        Object id = doc.get("id");
//
//        if (StringUtils.isNotBlank(abs)) {
//
//            String oriAbs = "";
//            Bson query = Filters.and(new Document("id", id), Filters.lt("actionTime", actionTime),
//                   Filters.exists("abs"));
//
//            MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find(query)
//                    .sort(new Document("actionTime", -1)).iterator();
//
//            if (cursor.hasNext()) {
//                oriAbs = cursor.next().getString("abs");
//            }
//
//            if (! abs.equals(oriAbs)) {
//                doc.append("absOri", oriAbs);
//            }
//        }
//    }
//
//    /**
//     * 原来的消歧标识
//     **/
//    private void getOriMeaning(String tbName, Document doc) {
//
//        String meaning = doc.getString("meaning");
//        Object actionTime = doc.get("actionTime");
//        Object id = doc.get("id");
//
//        if (StringUtils.isNotBlank(meaning)) {
//
//            String oriMeaning = "";
//            Bson query = Filters.and(new Document("id", id), Filters.lt("actionTime", actionTime),
//                    Filters.exists("meaning"));
//
//            MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find(query)
//                    .sort(new Document("actionTime", -1)).iterator();
//
//            if (cursor.hasNext()) {
//                oriMeaning = cursor.next().getString("meaning");
//            }
//            if (!meaning.equals(oriMeaning)) {
//                doc.append("meaningOri", oriMeaning);
//            }
//        }
//    }
//
//    /**
//     * 原本的实体名称
//     * @author xiezhenxiang 2019/9/17
//     **/
//    private String getOriName(String tbName, Object id, String objId, Object actionTime) {
//
//        String oriName = "";
//        if (id == null) {
//            return oriName;
//        }
//        Bson query;
//        if (objId != null) {
//            query = Filters.and(new Document("id", id), Filters.lte("actionTime", actionTime),
//                    Filters.exists("name"), Filters.ne("_id", new ObjectId(objId)));
//        } else {
//            query = Filters.and(new Document("id", id), Filters.lte("actionTime", actionTime),
//                    Filters.exists("name"));
//        }
//
//        MongoCursor<Document> cursor = mongoClient.getDatabase(LOG_DATABASE).getCollection(tbName).find(query)
//                .sort(new Document("actionTime", -1)).iterator();
//
//        if (cursor.hasNext()) {
//            oriName = cursor.next().getString("name");
//        }
//
//        return oriName;
//    }
}
