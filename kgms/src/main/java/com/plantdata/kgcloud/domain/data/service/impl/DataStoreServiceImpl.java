package com.plantdata.kgcloud.domain.data.service.impl;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.constant.MongoOperation;
import com.plantdata.kgcloud.domain.data.bo.DataStoreBO;
import com.plantdata.kgcloud.domain.data.req.DataStoreModifyReq;
import com.plantdata.kgcloud.domain.data.req.DataStoreScreenReq;
import com.plantdata.kgcloud.domain.data.rsp.DataStoreRsp;
import com.plantdata.kgcloud.domain.data.service.DataStoreSender;
import com.plantdata.kgcloud.domain.data.service.DataStoreService;
import com.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2020/3/24 15:21
 * @Description:
 */
@Service
public class DataStoreServiceImpl implements DataStoreService {

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private DocumentConverter documentConverter;

    @Autowired
    private DataStoreSender dataStoreSender;

    private static final String DB_NAME = "check_data_db";

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(DB_NAME).getCollection(SessionHolder.getUserId());
    }

    @Override
    public BasePage<DataStoreRsp> listDataStore(DataStoreScreenReq req) {
        MongoCollection<Document> collection = getCollection();
        Integer size = req.getSize();
        Integer page = (req.getPage() - 1) * size;
        List<Bson> bsons = new ArrayList<>(2);
        if (StringUtils.hasText(req.getDbName())) {
            bsons.add(Filters.eq("dbName", req.getDbName()));
        }
        if (StringUtils.hasText(req.getDbTable())) {
            bsons.add(Filters.eq("dbTable", req.getDbTable()));
        }
        FindIterable<Document> findIterable;
        long count = 0;
        if (bsons.isEmpty()) {
            count = collection.countDocuments();
            findIterable = collection.find().skip(page).limit(size);
        } else {
            count = collection.countDocuments(Filters.and(bsons));
            findIterable = collection.find(Filters.and(bsons)).skip(page).limit(size);
        }
        List<DataStoreRsp> dataStoreRsps = documentConverter.toBeans(findIterable, DataStoreRsp.class);
        return new BasePage<>(count, dataStoreRsps);
    }

    @Override
    public void updateData(DataStoreModifyReq modifyReq) {
        MongoCollection<Document> collection = getCollection();
        collection.updateOne(documentConverter.buildObjectId(modifyReq.getId()),
                new Document(MongoOperation.SET.getType(), new Document("data",
                        JacksonUtils.writeValueAsString(modifyReq.getData()))));
    }

    @Override
    public void deleteData(String id){
        MongoCollection<Document> collection = getCollection();
        collection.deleteOne(documentConverter.buildObjectId(id));
    }

    @Override
    public void sendData(List<String> ids){
        MongoCollection<Document> collection = getCollection();
        FindIterable<Document> findIterable = collection.find(Filters.in("_id", ids));
        List<DataStoreBO> bos = documentConverter.toBeans(findIterable, DataStoreBO.class);
        dataStoreSender.sendMsg(bos);
    }
}
