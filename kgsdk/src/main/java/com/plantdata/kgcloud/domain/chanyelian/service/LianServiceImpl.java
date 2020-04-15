package com.plantdata.kgcloud.domain.chanyelian.service;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.domain.chanyelian.model.Node;
import com.plantdata.kgcloud.domain.chanyelian.model.Pair;
import com.plantdata.kgcloud.domain.chanyelian.model.ViewRsp;
import com.plantdata.kgcloud.util.DriverUtil;
import com.plantdata.kgcloud.util.MongoUtil;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiezhenxiang 2020/4/14
 */
@Service
public class LianServiceImpl implements LianService {

    @Resource
    private MongoClient mongoClient;

    @Value("${db.kgms}")
    private String mysqlUrl;
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public ViewRsp view(String kgName) {

        MongoUtil mongoUtil = new MongoUtil(mongoClient);
        DriverUtil driverUtil = DriverUtil.getInstance(mysqlUrl, userName, password);
        String sql = "select db_name from graph where kg_name = ?";
        Map<String, Object> data = driverUtil.findOne(sql, kgName);
        String kgDbName = data.get("db_name").toString();
        ViewRsp viewRsp = new ViewRsp();
        viewRsp.setDeep(0);
        List<Pair> relation = new ArrayList<>();
        MongoCursor<Document> cursor = mongoUtil.find(kgDbName, "basic_info", Filters.ne("type", 0));
        cursor.forEachRemaining(s -> {
            Long id = s.getLong("id");
            String name = getNodeName(mongoUtil, kgDbName, id);
            Node sNode = new Node(id, name);
            Integer deep = viewRsp.getDeep();
            dsNext(kgDbName, sNode, 0, mongoUtil, viewRsp, relation);
            if (viewRsp.getDeep() > deep) {
                viewRsp.setNode(sNode);
            }
        });
        viewRsp.setRelation(relation);
        return viewRsp;
    }

    @SuppressWarnings("unchecked")
    private static void dsNext(String kgDbName, Node sNode, int deep, MongoUtil mongoUtil, ViewRsp viewRsp, List<Pair> relation) {

        MongoCursor<Document> nextCursor = mongoUtil.find(kgDbName, "attribute_object", new Document("entity_id", sNode.getId()));
        if (!nextCursor.hasNext()) {
            if (deep > viewRsp.getDeep()) {
                viewRsp.setDeep(deep);
            }
            return;
        }

        List<Node> next = new ArrayList<>();
        nextCursor.forEachRemaining(s -> {
            Integer attrId = s.getInteger("attr_id");
            Long valueId = s.getLong("attr_value");
            String valueNodeName = getNodeName(mongoUtil, kgDbName, valueId);

            Document define = mongoUtil.find(kgDbName, "attribute_definition", new Document("id", attrId)).next();
            List<Document> extraInfo = (List<Document>) define.get("extra_info");
            String attName = define.getString("name");
            Map<String, Object> attr = new HashMap<>();
            for (String key : s.keySet()) {
                if (key.contains("attr_ext_")) {
                    Integer sideAttrId = Integer.parseInt(key.substring(key.lastIndexOf("_") + 1));
                    String sideAttrName = extraInfo.stream().filter(s1 -> s1.getInteger("seqNo").equals(sideAttrId)).findFirst().get().getString("name");
                    attr.put(sideAttrName, s.get(key));
                }
            }
            Node nextNode = new Node(valueId, valueNodeName, attName);
            if (!attr.isEmpty()) {
                nextNode.setAttr(attr);
            }

            dsLast(kgDbName, nextNode, mongoUtil, sNode.getId(), relation);

            List<Long> lastIds = Lists.newArrayList(sNode.getLastIds());
            lastIds.add(sNode.getId());
            nextNode.setLastIds(lastIds);
            if (!lastIds.contains(valueId)) {
                dsNext(kgDbName, nextNode, deep + 1, mongoUtil, viewRsp, relation);
            }
            Pair pair = new Pair(sNode.getId(), valueId, attName, attr);
            relation.add(pair);
            next.add(nextNode);
        });
        sNode.setNext(next);
    }

    @SuppressWarnings("unchecked")
    private static void dsLast(String kgDbName, Node sNode, MongoUtil mongoUtil, Long lastId, List<Pair> relation) {

        Bson query = Filters.and(Filters.eq("attr_value", sNode.getId()), Filters.ne("entity_id", lastId));
        MongoCursor<Document> lastCursor = mongoUtil.find(kgDbName, "attribute_object", query);
        if (!lastCursor.hasNext()) {
            return;
        }
        List<Node> last = new ArrayList<>();
        lastCursor.forEachRemaining(s -> {
            Integer attrId = s.getInteger("attr_id");
            Long entityId = s.getLong("entity_id");
            String entityNodeName = getNodeName(mongoUtil, kgDbName, entityId);

            Document define = mongoUtil.find(kgDbName, "attribute_definition", new Document("id", attrId)).next();
            List<Document> extraInfo = (List<Document>) define.get("extra_info");
            String attName = define.getString("name");
            Map<String, Object> attr = new HashMap<>();
            for (String key : s.keySet()) {
                if (key.contains("attr_ext_")) {
                    Integer sideAttrId = Integer.parseInt(key.substring(key.lastIndexOf("_") + 1));
                    String sideAttrName = extraInfo.stream().filter(s1 -> s1.getInteger("seqNo").equals(sideAttrId)).findFirst().get().getString("name");
                    attr.put(sideAttrName, s.get(key));
                }
            }
            Node lastNode = new Node(entityId, entityNodeName, attName);
            if (!attr.isEmpty()) {
                lastNode.setAttr(attr);
            }
            List<Long> nextIds = Lists.newArrayList(sNode.getNextIds());
            nextIds.add(sNode.getId());
            lastNode.setNextIds(nextIds);
            if (!nextIds.contains(entityId)) {
                dsLast(kgDbName, lastNode, mongoUtil, sNode.getId(), relation);
            }
            Pair pair = new Pair(entityId, sNode.getId(), attName, attr);
            relation.add(pair);
            last.add(lastNode);
        });
        sNode.setLast(last);
    }

    private static String getNodeName(MongoUtil mongoUtil, String kgDbName, Long id) {
        MongoCursor<Document> cursor = mongoUtil.find(kgDbName, "basic_info", new Document("id", id));
        return cursor.hasNext() ? ((Document)cursor.next()).getString("name") : null;
    }
}
