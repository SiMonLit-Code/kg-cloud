package ai.plantdata.kgcloud.domain.graph.quality.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class ConceptUtils {

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

    private MongoCollection<Document> getAttributeObjectCollection(String kgDbName) {
        return mongoClient.getDatabase(kgDbName).getCollection("attribute_object");
    }

    /**
     * 当前概念下的实体数量,不包括子概念
     *
     * @param conceptId
     * @return
     */
    public Long countEntityByConceptId(String kgDbname, Long conceptId) {
        Document queryEntityCount = new Document("type", 1).append("concept_id", conceptId);
        return getBasicInfoCollection(kgDbname).countDocuments(queryEntityCount);
    }

    /**
     * 批量查询概念下的实体数量,不包括子概念
     *
     * @param conceptIds
     * @return
     */
    public Map<Long, Long> countEntityByConceptIds(String kgDbname, Set<Long> conceptIds) {
        List<Document> aggregateList = new ArrayList<>();
        Document query = new Document("type", 1).append("concept_id", new Document("$in", conceptIds));
        Document groupDoc = new Document("_id", "$concept_id").append("total", new Document("$sum", 1));
        Document projectDoc = new Document("id", "$_id").append("total", 1).append("_id", 0);

        aggregateList.add(new Document("$match", query));
        aggregateList.add(new Document("$group", groupDoc));
        aggregateList.add(new Document("$project", projectDoc));
        ArrayList<Document> documents = Lists.newArrayList(getBasicInfoCollection(kgDbname).aggregate(aggregateList));
        Map<Long, Long> dataMap = Maps.newHashMap();

        for (Document document : documents) {
            dataMap.put(document.getLong("id"), document.getInteger("total").longValue());
        }
        return dataMap;
    }

    /**
     * 批量查询概念所定义的属性数（不继承父概念）
     *
     * @param conceptIds
     * @return
     */
    public Map<Long, Long> countAttrByConceptIds(String kgDbname, Set<Long> conceptIds) {
        List<Document> aggregateList = new ArrayList<>();
        Document query = new Document("domain_value", new Document("$in", conceptIds));
        Document groupDoc = new Document("_id", "$domain_value").append("total", new Document("$sum", 1));
        Document projectDoc = new Document("id", "$_id").append("total", 1).append("_id", 0);

        aggregateList.add(new Document("$match", query));
        aggregateList.add(new Document("$group", groupDoc));
        aggregateList.add(new Document("$project", projectDoc));
        ArrayList<Document> documents = Lists.newArrayList(getAttributeDefinitionCollection(kgDbname).aggregate(aggregateList));
        Map<Long, Long> dataMap = Maps.newHashMap();

        for (Document document : documents) {
            dataMap.put(document.getLong("id"), document.getInteger("total").longValue());
        }
        return dataMap;
    }

    /**
     * 批量查询对象属性的属性值总数（不继承父概念）
     *
     * @param attrIds
     * @return
     */
    public Map<Integer, Long> countObjectAttrValueByAttrIds(String kgDbname, Set<Integer> attrIds) {
        List<Document> aggregateList = new ArrayList<>();
        Document query = new Document("attr_id", new Document("$in", attrIds));
        Document groupDoc = new Document("_id", "$attr_id").append("total", new Document("$sum", 1));
        Document projectDoc = new Document("id", "$_id").append("total", 1).append("_id", 0);

        aggregateList.add(new Document("$match", query));
        aggregateList.add(new Document("$group", groupDoc));
        aggregateList.add(new Document("$project", projectDoc));
        ArrayList<Document> documents = Lists.newArrayList(getAttributeObjectCollection(kgDbname).aggregate(aggregateList));
        Map<Integer, Long> dataMap = Maps.newHashMap();

        for (Document document : documents) {
            dataMap.put(document.getInteger("id"), document.getInteger("total").longValue());
        }
        return dataMap;
    }

    /**
     * 批量查询基本属性的属性值总数（不继承父概念）
     *
     * @param attrIds
     * @return
     */
    public Map<Integer, Long> countBaseAttrValueByAttrIds(String kgDbname, Set<Integer> attrIds) {
        List<Document> aggregateList = new ArrayList<>();
        Document query = new Document("attr_id", new Document("$in", attrIds));
        Document groupDoc = new Document("_id", "$attr_id").append("total", new Document("$sum", 1));
        Document projectDoc = new Document("id", "$_id").append("total", 1).append("_id", 0);

        aggregateList.add(new Document("$match", query));
        aggregateList.add(new Document("$group", groupDoc));
        aggregateList.add(new Document("$project", projectDoc));
        ArrayList<Document> documents = Lists.newArrayList(getAttributeSummaryCollection(kgDbname).aggregate(aggregateList));
        Map<Integer, Long> dataMap = Maps.newHashMap();

        for (Document document : documents) {
            dataMap.put(document.getInteger("id"), document.getInteger("total").longValue());
        }
        return dataMap;
    }

}
