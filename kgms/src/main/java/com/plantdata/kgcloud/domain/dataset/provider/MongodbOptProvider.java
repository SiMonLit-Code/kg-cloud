package com.plantdata.kgcloud.domain.dataset.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Slf4j
public class MongodbOptProvider implements DataOptProvider {

    private final MongoClient client;

    private final String database;
    private final String table;

    private static final String MONGO_ID = CommonConstants.MongoConst.ID;

    private ServerAddress buildServerAddress(String addr) {
        String[] host = addr.trim().split(":");
        if (host.length == 2) {
            String ip = host[0].trim();
            int port = Integer.parseInt(host[1].trim());
            return new ServerAddress(ip, port);
        }
        return null;
    }


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

    @Override
    public List<Map<String, Object>> find(Integer offset, Integer limit, Map<String, Object> query) {
        FindIterable<Document> findIterable = getCollection().find();
        if (offset != null && offset > 0) {
            findIterable = findIterable.skip(offset);
        }
        int size = 10;
        if (limit != null && limit > 0) {
            findIterable = findIterable.limit(limit);
            size = limit;
        }
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
        return getCollection().countDocuments();
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
    public Map<String, Object> insert(JsonNode node) {
        MongoCollection<Document> collection = getCollection();
        Document map = Document.parse(node.toString());
        collection.insertOne(map);
        map.put(MONGO_ID, map.getObjectId(MONGO_ID).toHexString());
        return map;
    }

    @Override
    public Map<String, Object> update(String id, JsonNode node) {
        MongoCollection<Document> collection = getCollection();
        Document map = Document.parse(node.toString());
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
    public void batchInsert(List<JsonNode> nodes) {
        MongoCollection<Document> collection = getCollection();
        List<Document> docList = new ArrayList<>();
        for (JsonNode node : nodes) {
            Document map = Document.parse(node.toString());
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
    public void close() throws IOException {
        client.close();
    }
}
