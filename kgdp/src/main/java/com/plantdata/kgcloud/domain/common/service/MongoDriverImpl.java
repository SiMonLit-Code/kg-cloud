package com.plantdata.kgcloud.domain.common.service;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoDriverImpl implements MongoDriver {

    @Autowired
    private MongoClient client;
    
    /**
     * 添加
     *
     * @param database
     * @param collection
     * @param document
     */
    @Override
    public synchronized void insert(String database, String collection, Document document) {

        try {
            client.getDatabase(database).getCollection(collection).insertOne(document);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void insertMany(String database, String collection, List<Document> document) {

        try {
            client.getDatabase(database).getCollection(collection).insertMany(document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除
     *
     * @param database
     * @param collection
     * @param document
     */
    @Override
    public void delete(String database, String collection, Document document) {

        try {
            if (document == null) {
                return;
            }

            client.getDatabase(database).getCollection(collection).deleteOne(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取list
     *
     * @param database
     * @param collection
     * @param pageSize
     * @param pageNo
     * @return
     */
    @Override
    public List<Document> list(String database, String collection, Integer pageSize, Integer pageNo,
                                   Document document, Document sort) {

        try {
            FindIterable<Document> documents = null;
            if (document != null) {
                if(sort != null){
                    documents =
                            client.getDatabase(database).getCollection(collection).find(document).sort(sort).skip(pageNo).limit(pageSize);
                }else{
                    documents =
                            client.getDatabase(database).getCollection(collection).find(document).skip(pageNo).limit(pageSize);
                }
            } else {
                if(sort != null){
                    documents =
                            client.getDatabase(database).getCollection(collection).find().sort(sort).skip(pageNo).limit(pageSize);
                }else{
                    documents =
                            client.getDatabase(database).getCollection(collection).find().skip(pageNo).limit(pageSize);
                }

            }
            List<Document> documentList = Lists.newArrayList();

            MongoCursor<Document> iterator = documents.iterator();
            while (iterator.hasNext()) {
                documentList.add(iterator.next());
            }

            return documentList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Document> listAll(String database, String collection, Document document, Document sort) {

        try {
            FindIterable<Document> documents = null;
            if (document != null) {
                if(sort != null){
                    documents =
                            client.getDatabase(database).getCollection(collection).find(document).sort(sort);
                }else{
                    documents =
                            client.getDatabase(database).getCollection(collection).find(document);
                }
            } else {
                if(sort != null){
                    documents =
                            client.getDatabase(database).getCollection(collection).find().sort(sort);
                }else{
                    documents =
                            client.getDatabase(database).getCollection(collection).find();
                }

            }
            List<Document> documentList = Lists.newArrayList();

            MongoCursor<Document> iterator = documents.iterator();
            while (iterator.hasNext()) {
                documentList.add(iterator.next());
            }

            return documentList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取list
     *
     * @param database
     * @param collection
     * @return
     */
    @Override
    public long count(String database, String collection, Document document) {

        long count = 0L;
        try {
            FindIterable<Document> documents = null;
            if (document != null) {
                count = client.getDatabase(database).getCollection(collection).countDocuments(document);
            } else {
                count = client.getDatabase(database).getCollection(collection).countDocuments();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 根据指定条件查询
     *
     * @param database
     * @param collection
     * @param document
     * @return
     */
    @Override
    public List<Document> get(String database, String collection, Document document) {
        System.out.println("mongo查询条件:"+document.toJson());
        try {
            FindIterable<Document> documents = null;
            if (document != null) {
                documents = client.getDatabase(database).getCollection(collection).find(document);
            }

            List<Document> documentList = Lists.newArrayList();
            MongoCursor<Document> iterator = documents.iterator();
            if (iterator.hasNext()) {
                documentList.add(iterator.next());
            }

            return documentList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 更新文档
     *
     * @param database
     * @param collection
     * @param filter
     * @param document
     */
    @Override
    public void update(String database, String collection, Document filter, Document document) {

        try {
            Document var1 = new Document();
            if (document != null) {
                var1.put("$set", document);
            }


            if (document != null) {
                UpdateResult updateResult = client.getDatabase(database).getCollection(collection).updateOne(filter, var1,
                        new UpdateOptions().upsert(true));
                System.out.println(updateResult.getModifiedCount());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public MongoDatabase getMongoDatabase(String database) {
        return client.getDatabase(database);
    }


}
