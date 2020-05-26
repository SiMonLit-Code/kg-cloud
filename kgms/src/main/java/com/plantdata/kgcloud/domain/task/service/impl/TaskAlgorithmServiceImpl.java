package com.plantdata.kgcloud.domain.task.service.impl;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.task.rsp.EntityIntimateRsp;
import com.plantdata.kgcloud.domain.task.rsp.EntityKeyRsp;
import com.plantdata.kgcloud.domain.task.service.TaskAlgorithmService;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiezhenxiang 2020/5/21
 */
@Service
public class TaskAlgorithmServiceImpl implements TaskAlgorithmService {

    @Autowired
    private MongoClient mongoClient;
    private static final String ENTITY_KEY_TB = "entity_key";
    private static final String ENTITY_INTIMATE_TB = "entity_intimate";

    @Override
    public BasePage<EntityKeyRsp> entityKeyList(String kgName, String kw, BaseReq baseReq) {
        String kgDbName = KGUtil.dbName(kgName);
        Bson query = new Document();
        if (!StringUtils.isEmpty(kw)) {
            List<Long> ids = new ArrayList<>();
            MongoCursor<Document> cursor = mongoClient.getDatabase(kgDbName).getCollection("basic_info")
                    .find(Filters.regex("name", "^" + kw))
                    .projection(new Document("id", 1)).iterator();
            cursor.forEachRemaining(s -> ids.add(s.getLong("id")));
            query = Filters.in("id", ids);
        }
        Document sorts = getMongoSort(baseReq);
        long count = mongoClient.getDatabase(kgDbName).getCollection(ENTITY_KEY_TB).countDocuments(query);
        List<EntityKeyRsp> ls = new ArrayList<>();
        MongoCursor<Document> cursor = mongoClient.getDatabase(kgDbName).getCollection(ENTITY_KEY_TB).find(query).sort(sorts)
                .skip(baseReq.getOffset()).limit(baseReq.getSize()).iterator();
        cursor.forEachRemaining(s -> {
            EntityKeyRsp keyNodeRsp = new EntityKeyRsp();
            keyNodeRsp.setId(s.getLong("id"));
            keyNodeRsp.setInCount(s.getLong("in_count"));
            keyNodeRsp.setOutCount(s.getLong("out_count"));
            keyNodeRsp.setTotalCount(s.getLong("total_count"));
            ls.add(keyNodeRsp);
        });
        return new BasePage<>(count, ls);
    }

    @Override
    public BasePage<EntityIntimateRsp> entityIntimateList(String kgName, BaseReq baseReq) {
        String kgDbName = KGUtil.dbName(kgName);
        Document sorts = getMongoSort(baseReq);
        List<EntityIntimateRsp> ls = new ArrayList<>();
        long count = mongoClient.getDatabase(kgDbName).getCollection(ENTITY_INTIMATE_TB).countDocuments();
        MongoCursor<Document> cursor = mongoClient.getDatabase(kgDbName).getCollection(ENTITY_INTIMATE_TB).find().sort(sorts)
                .skip(baseReq.getOffset()).limit(baseReq.getSize()).iterator();
        cursor.forEachRemaining(s -> {
            EntityIntimateRsp entityIntimateRsp = new EntityIntimateRsp();
            entityIntimateRsp.setEntityId(s.getLong("entity_id"));
            entityIntimateRsp.setAttrValue(s.getLong("attr_value"));
            entityIntimateRsp.setIntimate(s.getInteger("intimate"));
            Document attrCount = (Document) s.get("attr_count");
            Map<Integer, Integer> m = new HashMap<>(10);
            for (Map.Entry<String, Object> entry : attrCount.entrySet()) {
                m.put(Integer.parseInt(entry.getKey()), (Integer) entry.getValue());
            }
            entityIntimateRsp.setAttrCount(m);
            ls.add(entityIntimateRsp);
        });
        return new BasePage<>(count, ls);
    }

    private Document getMongoSort(BaseReq baseReq) {
        Document sorts = new Document();
        if (baseReq.getSorts() != null) {
            for (String sort : baseReq.getSorts()) {
                String[] arr = sort.split(":");
                String field = arr[0];
                int direct = "desc".equalsIgnoreCase(arr[1]) ? -1 : 1;
                sorts.append(field, direct);
            }
        }
        return sorts;
    }
}
