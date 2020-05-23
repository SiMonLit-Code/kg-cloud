package com.plantdata.kgcloud.domain.graph.quality.util;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class SchemaUtils {

    @Autowired
    private MongoClient mongoClient;

    private MongoCollection<Document> getBasicInfoCollection(String kgDbName) {
        return mongoClient.getDatabase(kgDbName).getCollection("basic_info");
    }

    private MongoCollection<Document> getAttributeDefinitionCollection(String kgDbName) {
        return mongoClient.getDatabase(kgDbName).getCollection("attribute_definition");
    }

    private MongoCollection<Document> getAttributeSummaryCollection(String kgDbName) {
        return mongoClient.getDatabase(kgDbName).getCollection("attribute_summary");
    }

    public Integer batchSize = 1000;

    /**
     * 根据属性ID获取属性值总数
     *
     * @param kgdbname
     * @param attrId
     * @return
     */
    public Long getValueCountByAttrId(String kgdbname, Integer attrId) {
//        log.info("==============获取该属性值总数 开始==============");
        Boolean notfinished = true;
        Object _id = null;

        Document query = new Document();
        query.append("attr_id", attrId);
        Document returnDoc = new Document();
        returnDoc.append("attr_id", 1);

        Long attrIdValueTotal = 0L;
        while (notfinished) {
            if (_id != null) {
                query.append("_id", new Document("$gt", _id));
            }
            MongoCursor<Document> iterator = getAttributeSummaryCollection(kgdbname)
                    .find(query)
                    .projection(returnDoc)
                    .limit(batchSize)
                    .iterator();

            List<Document> list = new ArrayList<>();
            while (iterator.hasNext()) {
                Document next = iterator.next();
                _id = next.get("_id");
                list.add(next);
                attrIdValueTotal++;
            }
            if (list.isEmpty()) {
                notfinished = false;
            } else {
                list.clear();
            }
        }
//        log.info("==============获取该属性值总数 结束==============");
        return attrIdValueTotal;
    }

    /**
     * 获取所有概念ID及名称
     *
     * @param kgdbname
     * @return
     */
    public Map<Long, String> conceptNamemap1(String kgdbname) {
        Document query = new Document();
        query.append("type", 0);

        Document returnDocument = new Document();
        returnDocument.append("id", 1);
        returnDocument.append("name", 1);

        MongoCursor<Document> iterator = getBasicInfoCollection(kgdbname)
                .find(query)
                .projection(returnDocument)
                .iterator();

        Map<Long, String> conceptNameMap = new HashMap<>();

        while (iterator.hasNext()) {
            Document next = iterator.next();
            Long conceptId = next.getLong("id");
            String name = next.getString("name");
            conceptNameMap.put(conceptId, name);
        }

        return conceptNameMap;
    }

    /**
     * 所有当前概念的父概念
     *
     * @param kgdbname
     * @return
     */
    public Map<Long, Long> sonOneParentMap2(String kgdbname) {
        Document query = new Document("type", 0);
        Document returnDoc = new Document("id", 1).append("concept_id", 1);
        MongoCursor<Document> iterator = getBasicInfoCollection(kgdbname)
                .find(query)
                .projection(returnDoc)
                .iterator();
        Map<Long, Long> sonParentMap = new HashMap<>();

        while (iterator.hasNext()) {
            Document next = iterator.next();
            Long son = next.getLong("id");
            Long parent = next.getLong("concept_id");

            sonParentMap.put(son, parent);

        }
        return sonParentMap;
    }

    /**
     * 获取该概念的所有属性ID（继承父概念的属性）
     *
     * @param kgdbname
     * @param conceptId
     * @return
     */
    public List<Integer> getAllAttrIdByConcept(String kgdbname, Long conceptId) {
        // 获取该概念的所有父概念
        Set<Long> parentConceptIdList = InitFunc.parentConceptIds.get(conceptId);
        Document query = new Document(new Document("domain_value", new Document("$in", parentConceptIdList)));
        Document returnDoc = new Document("id", 1);
        MongoCursor<Document> iterator = getAttributeDefinitionCollection(kgdbname)
                .find(query)
                .projection(returnDoc)
                .iterator();
        List<Integer> attrIds = new ArrayList<>();
        while (iterator.hasNext()) {
            Document next = iterator.next();
            Integer attrId = next.getInteger("id");
            attrIds.add(attrId);
        }
        return attrIds;
    }

    /**
     * 获取该概念自己独有的所有属性
     *
     * @param kgdbname
     * @param conceptId
     * @return
     */
    public List<Integer> getSelfAttrIdsByConceptId(String kgdbname, Long conceptId) {
        List<Integer> selfAttrIds = new ArrayList<>();

        Document query = new Document();
        query.append("domain_value", conceptId);

        Document returnDoc = new Document();
        returnDoc.append("id", 1);

        MongoCursor<Document> iterator = getAttributeDefinitionCollection(kgdbname)
                .find(query)
                .projection(returnDoc)
                .iterator();
        while (iterator.hasNext()) {
            Document next = iterator.next();
            Integer attrId = next.getInteger("id");
            selfAttrIds.add(attrId);
        }

        return selfAttrIds;
    }

    /**
     * 该概念自己的属性ID和类型（对象 基本）
     *
     * @param kgdbname
     * @param conceptId
     * @return
     */
    public Map<Integer, Integer> getSelfAttrTypeByConceptId(String kgdbname, Long conceptId) {

        Map<Integer, Integer> attrAndTypeMap = new HashMap<>();
        Document query = new Document();
        query.append("domain_value", conceptId);

        Document returnDoc = new Document();
        returnDoc.append("id", 1);
        returnDoc.append("type", 1);

        MongoCursor<Document> iterator = getAttributeDefinitionCollection(kgdbname)
                .find(query)
                .projection(returnDoc)
                .iterator();
        while (iterator.hasNext()) {
            Document next = iterator.next();
            Integer attrId = next.getInteger("id");
            Integer type = next.getInteger("type");

            attrAndTypeMap.put(attrId, type);
        }

        return attrAndTypeMap;
    }

    /**
     * 所有概念及该概念对应的所有属性
     *
     * @param kgdbname
     * @return
     */
    public Map<Long, List<Integer>> getAllAttrIdAndConceptId(String kgdbname) {
        Map<Long, Long> longLongMap = InitFunc.longLongMap;

        Map<Long, List<Integer>> conceptAndAttrIds = new HashMap<>();
        for (Map.Entry<Long, Long> entry : longLongMap.entrySet()) {
            Long conceptId = entry.getKey();
            // 获取该概念的所有父概念
            Set<Long> parentConceptIdList = InitFunc.parentConceptIds.get(conceptId);
            Document query = new Document(new Document("domain_value", new Document("$in", parentConceptIdList)));
            Document returnDoc = new Document("id", 1);

            long count = getAttributeDefinitionCollection(kgdbname)
                    .count(query);

            MongoCursor<Document> iterator = getAttributeDefinitionCollection(kgdbname)
                    .find(query)
                    .projection(returnDoc)
                    .iterator();
            List<Integer> attrIds = new ArrayList<>();
            if (count > 0) {
                while (iterator.hasNext()) {
                    Document next = iterator.next();
                    Integer attrId = next.getInteger("id");
                    attrIds.add(attrId);

                    conceptAndAttrIds.put(conceptId, attrIds);
                }
            } else {
                conceptAndAttrIds.put(conceptId, null);
            }
        }

        return conceptAndAttrIds;
    }

    /**
     * k:属性ID
     * v:name
     *
     * @param kgdbname
     * @return
     */
    public Map<Integer, String> getAttrName(String kgdbname) {
        Document returnDoc = new Document("id", 1);
        returnDoc.append("name", 1);
        MongoCursor<Document> iterator = getAttributeDefinitionCollection(kgdbname)
                .find()
                .projection(returnDoc)
                .iterator();
        Map<Integer, String> map = new HashMap<>();
        while (iterator.hasNext()) {
            Document next = iterator.next();
            Integer attrId = next.getInteger("id");
            String name = next.getString("name");

            map.put(attrId, name);
        }
        return map;
    }

    public Long entityTotal(String kgdbname) {
        long count = getBasicInfoCollection(kgdbname).count();
        return count;
    }

    /**
     * 获取该概念下的所有对象属性个数
     *
     * @param kgdbname
     * @param conceptId
     * @return
     */
    public long getAllObjAttrsByConceptId(String kgdbname, Long conceptId) {
        // 获取所有子概念ID 包括自己
        Set<Long> sonConceptids = InitFunc.sonConceptIds.get(conceptId);
        sonConceptids.add(conceptId);
        sonConceptids.remove(0L);
        Document query = new Document(new Document("domain_value", new Document("$in", sonConceptids)).append("type", 1));
        long count = getAttributeDefinitionCollection(kgdbname).count(query);

        return count;
    }
}
