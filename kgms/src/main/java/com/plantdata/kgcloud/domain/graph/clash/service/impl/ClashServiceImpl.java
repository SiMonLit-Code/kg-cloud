package com.plantdata.kgcloud.domain.graph.clash.service.impl;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.domain.common.util.KgQueryUtil;
import com.plantdata.kgcloud.domain.edit.req.entity.NumericalAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.ObjectAttrValueReq;
import com.plantdata.kgcloud.domain.edit.service.EntityService;
import com.plantdata.kgcloud.domain.graph.clash.entity.ClashListReq;
import com.plantdata.kgcloud.domain.graph.clash.entity.ClashToGraphReq;
import com.plantdata.kgcloud.domain.graph.clash.service.ClashService;
import com.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import com.plantdata.kgcloud.security.SessionHolder;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiezhenxiang 2019/12/12
 */
@Service
public class ClashServiceImpl implements ClashService {

    @Autowired
    private MongoClient mongoClient;
    private static final String CLASH_DB_NAME = "clash_check";
    @Autowired
    private EntityService entityService;
    @Autowired
    private GraphRepository graphRepository;

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> list(String kgName, ClashListReq req) {

        String kgDbName = graphRepository.findByKgNameAndUserId(kgName, SessionHolder.getUserId()).getDbName();
        int pageNo = (req.getPage() - 1) * req.getSize();
        List<Bson> aggLs = new ArrayList<>();
        aggLs.add(Aggregates.lookup("basic_info", "entity_id", "id", "basic"));
        if (StringUtils.isNotBlank(req.getName())) {
            aggLs.add(Aggregates.match(Filters.elemMatch("basic", Filters.regex("name",  req.getName()))));
        }
        aggLs.add(Aggregates.skip(pageNo));
        aggLs.add(Aggregates.limit(req.getSize() + 1));
        MongoCursor<Document> cursor = mongoClient.getDatabase(kgDbName).getCollection(CLASH_DB_NAME).aggregate(aggLs).iterator();

        List<Document> ls = new ArrayList<>();
        cursor.forEachRemaining(s -> {

            String entityName = "";
            List<Document> basic = (List<Document>) s.get("basic");
            if (basic.size() > 0) {
                entityName = basic.get(0).getString("name");
            }
            s.put("entity_name", entityName);

            s.put("_id", s.get("_id").toString());
            Integer attrId = s.getInteger("attr_id");
            // 补充数值属性约束
            String clashInfo = "{}";
            MongoCursor<Document> cursor1 = mongoClient.getDatabase(kgDbName).getCollection("attribute_definition")
                    .find(new Document("id", attrId)).iterator();

            int attrType = 0;
            String attrName = "";
            int dataType = 0;
            if (cursor1.hasNext()) {
                Document define = cursor1.next();
                attrType = define.getInteger("type");
                dataType = define.getInteger("data_type");
                attrName = define.getString("name");
                if (attrType == 0 && define.containsKey("constraints")) {
                    clashInfo = define.getString("constraints");
                } else if (attrType == 1 && define.containsKey("is_functional")) {
                    clashInfo = define.getInteger("is_functional").toString();
                }
            }
            s.put("clash_info", clashInfo);
            s.put("attr_type", attrType);
            s.put("attr_name", attrName);
            s.put("data_type", dataType);
            s.remove("basic");

            if (attrType == 1) {
                // 关系唯一性冲突, 补充已有的关系值
                String attrHaveValue = "";
                Long entityId = s.getLong("entity_id");
                Document query2 = new Document("entity_id", entityId).append("attr_id", attrId);
                MongoCursor<Document> cursor2 = mongoClient.getDatabase(kgDbName).getCollection("attribute_object")
                        .find(query2).iterator();
                if (cursor2.hasNext()) {
                    Long attrHaveValueId = cursor2.next().getLong("attr_value");
                    attrHaveValue = KgQueryUtil.getEntityNameById(mongoClient, kgDbName, attrHaveValueId);
                }
                s.put("attr_have_value", attrHaveValue);

                Long attrValueId = s.getLong("attr_value");
                String objValueName = KgQueryUtil.getEntityNameById(mongoClient, kgDbName, attrValueId);
                s.put("obj_value_name", objValueName);
            }
            ls.add(s);
        });

        int hasNext = 0;
        if (ls.size() > req.getSize()) {
            hasNext = 1;
            ls.remove(ls.get(ls.size() - 1));
        }
        Map<String, Object> rsData = new HashMap<>(10);
        rsData.put("hasNext", hasNext);
        rsData.put("rsCount", ls.size());
        rsData.put("rsData", ls);
        return rsData;
    }

    @Override
    public void toGraph(String kgName, ClashToGraphReq request) {

        if (request.getAttrType() == 0) {
            NumericalAttrValueReq req = new NumericalAttrValueReq();
            req.setEntityId(request.getEntityId());
            req.setAttrId(request.getAttrId());
            req.setAttrValue(request.getAttrValue());
            entityService.upsertNumericalAttrValue(kgName, req);
        } else if (request.getAttrType() == 1) {
            ObjectAttrValueReq req = new ObjectAttrValueReq();
            req.setEntityId(request.getEntityId());
            req.setAttrId(request.getAttrId());
            req.setAttrValue(request.getAttrValue());
            entityService.addObjectAttrValue(kgName, req);
        }

        delete(kgName, Lists.newArrayList(request.getId()));
    }

    @Override
    public void delete(String kgName, List<String> ls) {

        String kgDbName = graphRepository.findByKgNameAndUserId(kgName, SessionHolder.getUserId()).getDbName();
        Bson query = new Document();
        if (ls != null && !ls.isEmpty()) {
            List<ObjectId> objectIds = ls.stream().map(ObjectId::new).collect(Collectors.toList());
            query = Filters.in("_id", objectIds);
        }
        mongoClient.getDatabase(kgDbName).getCollection(CLASH_DB_NAME).deleteMany(query);
    }
}
