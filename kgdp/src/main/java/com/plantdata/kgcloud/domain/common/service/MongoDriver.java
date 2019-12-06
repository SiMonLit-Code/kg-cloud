package com.plantdata.kgcloud.domain.common.service;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public interface MongoDriver {

    void insert(String database, String collection, Document document);

    void insertMany(String database, String collection, List<Document> document);

    void delete(String database, String collection, Document document);

    List<Document> list(String database, String collection, Integer pageSize, Integer pageNo, Document document, Document sort);

    List<Document> listAll(String database, String collection, Document document, Document sort);

    long count(String database, String collection, Document document);

    List<Document> get(String database, String collection, Document document);

    void update(String database, String collection, Document filter, Document document);

    MongoDatabase getMongoDatabase(String database);
}
