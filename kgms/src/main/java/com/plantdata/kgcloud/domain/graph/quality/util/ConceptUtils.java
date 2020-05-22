package com.plantdata.kgcloud.domain.graph.quality.util;

import ai.plantdata.kg.common.bean.BasicInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

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

    /**
     * 获取概念的所有子概念（不包含自己）
     *
     * @param conceptId
     */
    public Set<Long> getSonConceptId(String kgDbname, Long conceptId) {

        Queue<Long> queue = new LinkedBlockingDeque<>();
        queue.add(conceptId);

        Set<Long> sonConceptIds = new HashSet<>();
        Long tempConceptId;
        while (!queue.isEmpty()) {
            tempConceptId = queue.remove();
            MongoCursor<Document> iterator = getBasicInfoCollection(kgDbname)
                    .find(new Document("concept_id", tempConceptId).append("type", 0))
                    .projection(new Document("id", 1).append("name", 1).append("concept_id", 1))
                    .iterator();
            while (iterator.hasNext()) {
                Document next = iterator.next();
                Long sonId = next.getLong("id");
                Long concept_id = next.getLong("concept_id");
                if (sonId == concept_id) {
                    System.out.println(kgDbname + "获取所有子概念数据错误，父概念ID = 本身概念ID " + conceptId);
                    log.error(kgDbname + "获取所有子概念数据错误，父概念ID = 本身概念ID " + conceptId);
                    break;
                }
//                System.out.println(kgDbname + "获取子概念 conceptId: " + sonId + " name: " + next.getString("name"));
                sonConceptIds.add(sonId);
                queue.add(sonId);
            }

        }

        return sonConceptIds;
    }

    /**
     * 获取该该概念的所有父概念（手动去除0（根节点）不包括自己）
     *
     * @param kgDbname
     * @param conceptId
     * @return
     */
    public Set<Long> getParentConceptId(String kgDbname, Long conceptId) {

        Queue<Long> queue = new LinkedBlockingDeque<>();
        queue.add(conceptId);

        Set<Long> parentConceptIds = new HashSet<>();
        Long temp;
        while (!queue.isEmpty()) {
            temp = queue.remove();
            MongoCursor<Document> iterator = getBasicInfoCollection(kgDbname)
                    .find(new Document("id", temp).append("type", 0))
                    .projection(new Document("concept_id", 1).append("id", 1))
                    .iterator();
            while (iterator.hasNext()) {
                Document next = iterator.next();
                Long parentId = next.getLong("concept_id");
                Long id = next.getLong("id");
                if (id == parentId) {
                    System.out.println(kgDbname + "获取所有父概念数据错误，父概念ID = 本身概念ID " + conceptId);
                    break;
                }
                if (parentId == null) {
                    continue;
                }
                if (parentId == 0L) {

                }
                parentConceptIds.add(parentId);
                queue.add(parentId);
            }

        }
        // 手动去除根节点
        parentConceptIds.remove(0L);
        return parentConceptIds;
    }

    /**
     * 当前概念下的实体数量,不包括子概念
     *
     * @param conceptId
     * @return
     */
    public Long countEntityByOneConceptId(String kgDbname, Long conceptId) {
        Document queryEntityCount = new Document("type", 1).append("concept_id", conceptId);
        return getBasicInfoCollection(kgDbname).countDocuments(queryEntityCount);
    }

    /**
     * 获取当前概念下及子概念实体总数
     *
     * @param conceptId
     */
    public Long countEntityByConceptParentAndSon(String kgDbname, Long conceptId) {

        Set<Long> sonConceptIds = InitFunc.sonConceptIds.get(conceptId);
        sonConceptIds.add(conceptId);//添加自己本身
        Map<Long, Long> longLongMap = InitFunc.longLongMap;

        Long count = 0L;
        for (Long sonId : sonConceptIds) {
            if (longLongMap != null) {
                Long aLong = longLongMap.get(sonId);
                if (aLong != null) {
                    count += longLongMap.get(sonId);
                }
            }
        }

        return count;
    }

    /**
     * 所有概念的实体数
     * key :概念ID
     * value：实体数
     */
    public Map<Long, Long> countEntityallConcepts(String kgDbname) {

        Map<Long, Long> conceptEntityCountMap = new HashMap<>();

        // 获取所有 概念ID -> 名称
        Map<Long, String> conceptIdNameMap = InitFunc.conceptNameMap;
        for (Map.Entry<Long, String> entry : conceptIdNameMap.entrySet()) {
            Long conceptId = entry.getKey();
            // 获取当前概念的实体数
            Long countEntityByOneConceptId = countEntityByOneConceptId(kgDbname, conceptId);
            if (countEntityByOneConceptId == null) {
                countEntityByOneConceptId = 0L;
            }
            conceptEntityCountMap.put(conceptId, countEntityByOneConceptId);
        }

        return conceptEntityCountMap;
    }


    /**
     * 获取该概念所定义的属性数（继承父概念）
     *
     * @param sonId
     * @return
     */
    public Long countAttrDefinSonParent(String kgDbname, Long sonId) {

        Set<Long> parentConceptIds = InitFunc.parentConceptIds.get(sonId);
        parentConceptIds.add(sonId);

        Document query = new Document(new Document("domain_value", new Document("$in", parentConceptIds)));

        long count = getAttributeDefinitionCollection(kgDbname).count(query);

        return count;
    }

    /**
     * 获取对象属性的值域列表
     *
     * @param kgdbName
     * @param attrId
     * @return
     */
    public List<Long> getObjAttrRangeValues(String kgdbName, Integer attrId) {
        List<Long> rangConceptIds = new ArrayList<>();
        // 获取该对象属性的值域
        Document first = getAttributeDefinitionCollection(kgdbName)
                .find(new Document("id", attrId).append("type", 1))
                .first();

        JSONObject parse = JSON.parseObject(first.toJson());
        JSONArray range_value = parse.getJSONArray("range_value");
        int size = range_value.size();
        for (int i = 0; i < size; i++) {
            JSONObject jsonObject = range_value.getJSONObject(i);
            Long conceptId = jsonObject.getLong("$numberLong");
            rangConceptIds.add(conceptId);
        }

        return rangConceptIds;
    }

    /**
     * 获取对象属性的值域列表
     *
     * @param kgDbName
     * @param conceptIds
     * @return
     */
    public List<BasicInfo> getBasicInfoByConceptId(String kgDbName, Set<Long> conceptIds) {

        MongoCursor<Document> iterator = getBasicInfoCollection(kgDbName).find(Filters.in("id", conceptIds)).iterator();

        List<BasicInfo> list = Lists.newArrayList();
        if (iterator.hasNext()) {
            Document document = iterator.next();

            BasicInfo basicInfo = new BasicInfo();
            basicInfo.setId(document.getLong("id"));
            basicInfo.setName(document.getString("name"));
            basicInfo.setConceptId(document.getLong("concept_id"));
            list.add(basicInfo);
        }

        return list;
    }

}
