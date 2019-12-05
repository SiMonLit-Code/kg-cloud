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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public MongodbOptProvider(DataOptConnect info) {
        String addresses = info.getAddresses().trim();
        List<ServerAddress> addressList = new ArrayList<>();
        for (String addr : addresses.split(",")) {
            String[] host = addr.trim().split(":");
            ServerAddress serverAddress;
            if (host.length > 1) {
                serverAddress = new ServerAddress(host[0].trim(), Integer.parseInt(host[1].trim()));
            } else {
                serverAddress = new ServerAddress(host[0].trim());
            }
            addressList.add(serverAddress);
        }
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
            map.put(CommonConstants.MongoConst.ID, document.getObjectId(CommonConstants.MongoConst.ID).toHexString());
            mapList.add(map);
        }
        return mapList;
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
        map.put(CommonConstants.MongoConst.ID, map.getObjectId(CommonConstants.MongoConst.ID).toHexString());
        return map;
    }

    @Override
    public Map<String, Object> update(String id, JsonNode node) {
        MongoCollection<Document> collection = getCollection();
        Document map = Document.parse(node.toString());
        collection.updateOne(Filters.eq(CommonConstants.MongoConst.ID, new ObjectId(id)), new Document("$set", map));
        map.put(CommonConstants.MongoConst.ID, map.getObjectId(CommonConstants.MongoConst.ID).toHexString());
        return map;
    }

    @Override
    public void delete(String id) {
        MongoCollection<Document> collection = getCollection();
        collection.deleteOne(Filters.eq(CommonConstants.MongoConst.ID, new ObjectId(id)));
    }

    @Override
    public void deleteAll() {
        MongoCollection<Document> collection = getCollection();
        collection.deleteMany(Filters.exists(CommonConstants.MongoConst.ID));
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
        collection.deleteMany(Filters.in(CommonConstants.MongoConst.ID, objs));
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
