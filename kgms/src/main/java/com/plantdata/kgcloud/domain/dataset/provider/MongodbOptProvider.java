package com.plantdata.kgcloud.domain.dataset.provider;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Slf4j
public class MongodbOptProvider implements DataOptProvider {

    private static final String MONGO_ID = CommonConstants.MongoConst.ID;
    private final MongoClient client;
    private final String database;
    private final String table;

    public MongodbOptProvider(DataOptConnect info) {
        List<ServerAddress> addressList = info.getAddresses().stream()
                .map(this::buildServerAddress)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        MongoClientOptions clientOptions = new MongoClientOptions
                .Builder()
                .connectionsPerHost(10)
                .maxWaitTime(60000)
                .build();
        if (info.getUsername() != null && info.getPassword() != null) {
            MongoCredential credential = MongoCredential.createCredential(info.getUsername(), "admin", info.getPassword().toCharArray());
            this.client = new MongoClient(addressList, credential, clientOptions);
        } else {
            this.client = new MongoClient(addressList, clientOptions);
        }
        this.database = info.getDatabase();
        this.table = info.getTable();
    }

    private ServerAddress buildServerAddress(String addr) {
        String[] host = addr.trim().split(":");
        if (host.length == 2) {
            String ip = host[0].trim();
            int port = Integer.parseInt(host[1].trim());
            return new ServerAddress(ip, port);
        }
        return null;
    }

    private MongoCollection<Document> getCollection() {
        return client.getDatabase(database).getCollection(table);
    }

    @Override
    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        MongoCollection<Document> collection = getCollection();
        for (Document document : collection.find().limit(1)) {
            fields.addAll(document.keySet());
        }
        return fields;
    }

    private Bson buildQuery(Map<String, Object> query) {
        List<Bson> bsonList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            if (Objects.equals(entry.getKey(), "search")) {
                Map<String, String> value = (Map<String, String>) entry.getValue();
                for (Map.Entry<String, String> objectEntry : value.entrySet()) {
                    bsonList.add(Filters.regex(objectEntry.getKey(), objectEntry.getValue()));
                }
            }
            if (Objects.equals(entry.getKey(), "resultType")) {
                Integer resultType = (Integer) entry.getValue();
                if (Objects.equals(resultType, 2)) {
                    bsonList.add(Filters.eq(DataConst.HAS_SMOKE, true));
                } else if (Objects.equals(resultType, 3)) {
                    bsonList.add(Filters.eq(DataConst.HAS_SMOKE, false));
                } else if (Objects.equals(resultType, 4)) {
                    bsonList.add(Filters.exists(DataConst.HAS_SMOKE, false));
                }
            }
        }
        if (bsonList.isEmpty()) {
            return Filters.and();
        } else {
            return Filters.and(bsonList);
        }
    }

    private Bson buildSort(Map<String, Object> query) {
        List<Bson> bsonList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            Integer sort = (Integer) entry.getValue();
            if (sort == -1) {
                bsonList.add(Sorts.descending(entry.getKey()));
            } else {
                bsonList.add(Sorts.ascending(entry.getKey()));
            }
        }
        return Sorts.orderBy(bsonList);
    }

    @Override
    public List<Map<String, Object>> find(Integer offset, Integer limit, Map<String, Object> query) {
        return findWithSort(offset, limit, query, null);
    }

    @Override
    public List<Map<String, Object>> findWithSort(Integer offset, Integer limit, Map<String, Object> query, Map<String, Object> sort) {
        FindIterable<Document> findIterable;
        if (query != null && !query.isEmpty()) {
            findIterable = getCollection().find(buildQuery(query));
        } else {
            findIterable = getCollection().find();
        }
        if (!CollectionUtils.isEmpty(sort)) {
            findIterable.sort(buildSort(sort));
        }
        if (offset != null && offset >= 0) {
            findIterable = findIterable.skip(offset);
        }
        int size = 10;
        if (limit != null && limit > 0) {
            limit = size;
            findIterable = findIterable.limit(limit);
        }
        findIterable.sort(Sorts.descending(DataConst.CREATE_AT));
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Document document : findIterable) {
            Map<String, Object> map = new HashMap<>(size);
            map.putAll(document);
            map.put(MONGO_ID, document.getObjectId(MONGO_ID).toHexString());
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public long count(Map<String, Object> query) {
        if (query != null && !query.isEmpty()) {
            return getCollection().countDocuments(buildQuery(query));
        } else {
            return getCollection().countDocuments();
        }
    }

    @Override
    public Map<String, Object> findOne(String id) {
        Document first = getCollection().find(Filters.eq(MONGO_ID, new ObjectId(id))).first();
        if (first == null) {
            return new HashMap<>();
        }
        first.put(MONGO_ID, first.getObjectId(MONGO_ID).toHexString());
        return first;
    }

    @Override
    public void createTable(List<DataSetSchema> colList) {
        MongoCollection<Document> collection = getCollection();
        for (DataSetSchema schema : colList) {
            if (schema.getIsIndex() == 1) {
                collection.createIndex(Indexes.ascending(schema.getField()));
            }
        }
    }

    @Override
    public void dropTable() {
        MongoCollection<Document> collection = getCollection();
        collection.drop();
    }

    @Override
    public Map<String, Object> insert(Map<String, Object> node) {
        MongoCollection<Document> collection = getCollection();
        Document map = Document.parse(JacksonUtils.writeValueAsString(node));
        collection.insertOne(map);
        map.put(MONGO_ID, map.getObjectId(MONGO_ID).toHexString());
        return map;
    }

    @Override
    public Map<String, Object> update(String id, Map<String, Object> node) {
        MongoCollection<Document> collection = getCollection();
        Document map = Document.parse(JacksonUtils.writeValueAsString(node));
        collection.updateOne(Filters.eq(MONGO_ID, new ObjectId(id)), new Document("$set", map));
        map.put(MONGO_ID, map.getObjectId(MONGO_ID).toHexString());
        return map;
    }

    @Override
    public void delete(String id) {
        MongoCollection<Document> collection = getCollection();
        collection.deleteOne(Filters.eq(MONGO_ID, new ObjectId(id)));
    }

    @Override
    public void deleteAll() {
        MongoCollection<Document> collection = getCollection();
        collection.deleteMany(Filters.exists(MONGO_ID));
    }

    @Override
    public void batchInsert(List<Map<String, Object>> nodes) {
        MongoCollection<Document> collection = getCollection();
        List<Document> docList = new ArrayList<>();
        for (Map<String, Object> node : nodes) {
            Document map = Document.parse(JacksonUtils.writeValueAsString(node));
            docList.add(map);
        }
        collection.insertMany(docList);
    }


    @Override
    public void batchDelete(Collection<String> ids) {
        MongoCollection<Document> collection = getCollection();
        List<ObjectId> objs = new ArrayList<>();
        for (String id : ids) {
            ObjectId objectId = new ObjectId(id);
            objs.add(objectId);
        }
        collection.deleteMany(Filters.in(MONGO_ID, objs));
    }

    @Override
    public List<Map<String, Long>> statistics() {
        MongoCollection<Document> collection = getCollection();
        long passed = collection.countDocuments(Filters.eq(DataConst.HAS_SMOKE, true));
        long unPassed = collection.countDocuments(Filters.eq(DataConst.HAS_SMOKE, false));
        long unDone = collection.countDocuments(Filters.exists(DataConst.HAS_SMOKE, false));
        Map<String, Long> countMap = new HashMap<>();
        countMap.put("2", passed);
        countMap.put("3", unPassed);
        countMap.put("4", unDone);

        Map<String, Long> reasonMap = new HashMap<>();
        AggregateIterable<Document> total = collection.aggregate(Arrays.asList(
                Aggregates.group("$_smokeMsg.msg", Accumulators.sum("total", 1L))
        ));

        for (Document document : total) {
            if (document.get("_id") != null) {
                List<String> msgList = (List<String>) document.get("_id");
                Long tempTotal = document.getLong("total");
                for (String s : msgList) {
                    reasonMap.compute(s, (k, v) -> v == null ? tempTotal : v + tempTotal);
                }
            }
        }
        List<Map<String, Long>> result = Lists.newArrayList();
        result.add(countMap);
        result.add(reasonMap);
        return result;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
