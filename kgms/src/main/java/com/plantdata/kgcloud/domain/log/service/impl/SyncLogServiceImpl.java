package com.plantdata.kgcloud.domain.log.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.domain.log.req.SyncLogReq;
import com.plantdata.kgcloud.domain.log.rsp.SyncLogRsp;
import com.plantdata.kgcloud.domain.log.service.SyncLogService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-06-13 22:01
 **/
@Service
public class SyncLogServiceImpl implements SyncLogService {

    @Autowired
    private MongoClient mongoClient;

    @Value("${mongo.prefix:}")
    private String mongoPrefix;

    private static String SYNC_LOG_DB = "sync_log";

    @Override
    public BasePage<SyncLogRsp> list(SyncLogReq req) {

        String mongoLogDb = mongoPrefix + SYNC_LOG_DB;

        Document document = new Document();
        document.put("dataName",req.getDbName());
        document.put("tableName",req.getTbName());
        MongoCursor<Document> iterable = mongoClient.getDatabase(mongoLogDb).getCollection(req.getKgName()).find(document).skip(req.getOffset()).limit(req.getSize()).iterator();

        List<SyncLogRsp> rsList = new ArrayList<>();
        while (iterable.hasNext()){

            Document doc = iterable.next();
            SyncLogRsp rsp = new SyncLogRsp();
            rsp.setDbName(req.getDbName());
            rsp.setTbName(req.getTbName());
            rsp.setId(doc.getString("dataId"));
            rsp.setValue(doc.get("data", JSONObject.class));

            rsList.add(rsp);
        }

        long count = mongoClient.getDatabase(mongoLogDb).getCollection(req.getKgName()).countDocuments(document);
        return new BasePage<>(count,rsList);
    }
}
