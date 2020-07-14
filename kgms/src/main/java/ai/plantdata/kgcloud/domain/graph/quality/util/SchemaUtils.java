package ai.plantdata.kgcloud.domain.graph.quality.util;

import ai.plantdata.kg.common.bean.BasicInfo;
import com.google.common.collect.Sets;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

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

    /**
     * 获取概念信息Map、父概念Map、子概念Map
     *
     * @param kgDbname 图谱mongo名称
     * @param initFunc 图谱信息
     */
    public void getConceptMap(String kgDbname, InitFunc initFunc) {

        Map<Long, BasicInfo> conceptMap = initFunc.getConceptMap();
        Map<Long, Set<Long>> sonConceptIdMap = initFunc.getSonConceptIdMap();
        Map<Long, Set<Long>> parentConceptIdMap = initFunc.getParentConceptIdMap();

        Document query = new Document();
        query.append("type", 0);

        Document returnDocument = new Document();
        returnDocument.append("id", 1);
        returnDocument.append("name", 1);
        returnDocument.append("concept_id", 1);

        MongoCursor<Document> iterator = getBasicInfoCollection(kgDbname)
                .find(query)
                .projection(returnDocument)
                .iterator();

        while (iterator.hasNext()) {
            Document document = iterator.next();
            Long id = document.getLong("id");
            String name = document.getString("name");
            Long conceptId = document.getLong("concept_id");

            if (conceptId != null) {
                // 保存子概念信息
                Set<Long> sonSet = sonConceptIdMap.get(conceptId);
                if (sonSet == null) {
                    sonSet = new HashSet<>();
                }
                sonSet.add(id);
                sonConceptIdMap.put(conceptId, sonSet);

                // 保存父概念信息
                Set<Long> parentSet = parentConceptIdMap.get(id);
                if (parentSet == null) {
                    parentSet = new HashSet<>();
                }
                // 手动去除根节点
                if (!conceptId.equals(0L)) {
                    parentSet.add(conceptId);
                }
                parentConceptIdMap.put(id, parentSet);
            }
            // 保存概念信息
            BasicInfo basicInfo = new BasicInfo();
            basicInfo.setId(id);
            basicInfo.setName(name);
            basicInfo.setConceptId(conceptId);
            conceptMap.put(id, basicInfo);
        }
        for (Long conceptId : conceptMap.keySet()) {
            if (conceptId != 0 && !parentConceptIdMap.containsKey(conceptId)) {
                parentConceptIdMap.put(conceptId, Sets.newHashSet());
            }
            if (!sonConceptIdMap.containsKey(conceptId)) {
                sonConceptIdMap.put(conceptId, Sets.newHashSet());
            }
        }
        Map<Long, Set<Long>> newSonConceptIdMap = new HashMap<>();
        Map<Long, Set<Long>> newParentConceptIdMap = new HashMap<>();
        for (Long conceptId : conceptMap.keySet()) {
            getSon(newSonConceptIdMap, sonConceptIdMap, conceptId);
            getParent(newParentConceptIdMap, parentConceptIdMap, conceptId);
        }

        initFunc.setSonConceptIdMap(newSonConceptIdMap);
        initFunc.setParentConceptIdMap(newParentConceptIdMap);
    }

    void getSon(Map<Long, Set<Long>> newSonConceptIdMap, Map<Long, Set<Long>> sonConceptIdMap, Long conceptId) {
        Queue<Long> queue = new LinkedBlockingDeque<>();
        queue.add(conceptId);

        Set<Long> sonConceptIds = new HashSet<>();
        Long tempConceptId;
        while (!queue.isEmpty()) {
            tempConceptId = queue.remove();
            // 是否已经保存数据
            Set<Long> ids = newSonConceptIdMap.get(tempConceptId);
            if (!CollectionUtils.isEmpty(ids)) {
                sonConceptIds.addAll(ids);
                continue;
            }
            Set<Long> conceptIds = sonConceptIdMap.get(tempConceptId);
            if (CollectionUtils.isEmpty(conceptIds)) {
                continue;
            }
            queue.addAll(conceptIds);
            sonConceptIds.addAll(conceptIds);
        }
        newSonConceptIdMap.put(conceptId, sonConceptIds);
    }

    void getParent(Map<Long, Set<Long>> newParentConceptIdMap, Map<Long, Set<Long>> parentConceptIdMap, Long conceptId) {
        Queue<Long> queue = new LinkedBlockingDeque<>();
        queue.add(conceptId);

        Set<Long> parentConceptIds = new HashSet<>();
        Long tempConceptId;
        while (!queue.isEmpty()) {
            tempConceptId = queue.remove();
            // 是否已经保存数据
            Set<Long> ids = newParentConceptIdMap.get(tempConceptId);
            if (!CollectionUtils.isEmpty(ids)) {
                parentConceptIds.addAll(ids);
                continue;
            }
            Set<Long> conceptIds = parentConceptIdMap.get(tempConceptId);
            if (CollectionUtils.isEmpty(conceptIds)) {
                continue;
            }
            queue.addAll(conceptIds);
            parentConceptIds.addAll(conceptIds);
        }
        newParentConceptIdMap.put(conceptId, parentConceptIds);
    }

    /**
     * 获取属性信息Map
     *
     * @param kgDbname    图谱mongo名称
     * @param attrIdMap   ID->Name
     * @param attrNameMap Name->ID
     */
    public void getAttrIdAndName(String kgDbname, Map<Integer, String> attrIdMap, Map<String, Integer> attrNameMap) {
        Document returnDoc = new Document();
        returnDoc.append("id", 1);
        returnDoc.append("name", 1);

        MongoCursor<Document> iterator = getAttributeDefinitionCollection(kgDbname)
                .find()
                .projection(returnDoc)
                .iterator();

        while (iterator.hasNext()) {
            Document next = iterator.next();
            Integer attrId = next.getInteger("id");
            String name = next.getString("name");

            attrIdMap.put(attrId, name);
            attrNameMap.put(name, attrId);
        }
    }

    /**
     * 当前概念自己的基本属性ID和对象属性ID
     *
     * @param kgDbname
     * @param conceptId
     * @param objectAttrId
     * @param baseAttrId
     * @return
     */
    public void getSelfAttrTypeByConceptId(String kgDbname, Long conceptId, Set<Integer> objectAttrId, Set<Integer> baseAttrId) {

        Document query = new Document();
        query.append("domain_value", conceptId);

        Document returnDoc = new Document();
        returnDoc.append("id", 1);
        returnDoc.append("type", 1);

        MongoCursor<Document> iterator = getAttributeDefinitionCollection(kgDbname)
                .find(query)
                .projection(returnDoc)
                .iterator();
        while (iterator.hasNext()) {
            Document next = iterator.next();
            Integer attrId = next.getInteger("id");
            Integer type = next.getInteger("type");

            if (type == 0) {
                baseAttrId.add(attrId);
            } else if (type == 1) {
                objectAttrId.add(attrId);
            }

        }
    }

}
