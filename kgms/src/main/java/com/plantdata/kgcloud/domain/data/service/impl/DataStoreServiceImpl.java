package com.plantdata.kgcloud.domain.data.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.constant.MongoOperation;
import com.plantdata.kgcloud.domain.access.entity.DWTask;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.domain.data.bo.DataStoreBO;
import com.plantdata.kgcloud.domain.data.entity.DataStore;
import com.plantdata.kgcloud.domain.data.req.*;
import com.plantdata.kgcloud.domain.data.rsp.DataStoreRsp;
import com.plantdata.kgcloud.domain.data.rsp.DbAndTableRsp;
import com.plantdata.kgcloud.domain.data.service.DataStoreSender;
import com.plantdata.kgcloud.domain.data.service.DataStoreService;
import com.plantdata.kgcloud.domain.dw.repository.DWDatabaseRepository;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import com.plantdata.kgcloud.domain.edit.util.MapperUtils;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private DWService dwService;

    @Autowired
    private AccessTaskService accessTaskService;

    @Autowired
    private UserClient userClient;

    @Autowired
    DWDatabaseRepository dwDatabaseRepository;
    private static final String MONGO_ID = CommonConstants.MongoConst.ID;
    private static final String DB_NAME = "check_data_db";
    private static final String DB_FIX_NAME_PREFIX = "dw_rerun_";
    private static final String DB_VIEW_STATUS = "Edit";
    private static final String DB_VIEW_DATA = "_showData";

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(DB_NAME).getCollection(SessionHolder.getUserId() == null ? userClient.getCurrentUserDetail().getData().getId() : SessionHolder.getUserId());
    }

    @Override
    public List<DbAndTableRsp> listAll(DtReq dtReq) {
        MongoCollection<Document> collection = getCollection();
       /* List<Bson> bsons = new ArrayList<>(2);
        if (StringUtils.hasText(dtReq.getDbName())) {
            bsons.add(Filters.regex("dbName", Pattern.compile(dtReq.getDbName())));
        }
        if (StringUtils.hasText(dtReq.getDbTable())) {
            bsons.add(Filters.regex("dbTable", Pattern.compile(dtReq.getDbTable())));
        }
        FindIterable<Document> findIterable;
        if (bsons.isEmpty()) {
            findIterable = collection.find();
        } else {
            findIterable = collection.find(Filters.and(bsons));
        }


        List<DataStore> dataStores = documentConverter.toBeans(findIterable, DataStore.class);
        return MapperUtils.map(dataStores, DbAndTableRsp.class);
*/
        Document groupField = new Document();
        groupField.put("dbName", "$dbName");
        groupField.put("dbTable", "$dbTable");
        Document group = new Document();
        group.put("$group", new Document("_id", groupField));
        AggregateIterable<Document> aggr = collection.aggregate(Lists.newArrayList(group));

        MongoCursor<Document> cursor = aggr.iterator();

        Map<String, List<String>> rs = Maps.newHashMap();

        while (cursor.hasNext()) {
            Document item_doc = cursor.next();
            String dbName = item_doc.get("_id", Document.class).getString("dbName");
            String dbTable = item_doc.get("_id", Document.class).getString("dbTable");
            if (rs.containsKey(dbName)) {
                rs.get(dbName).add(dbTable);
            } else {
                rs.put(dbName, Lists.newArrayList(dbTable));
            }
        }

        List<DbAndTableRsp> tableRspList = Lists.newArrayList();
        Map<String, String> dataMap = Maps.newHashMap();
        for (Map.Entry<String, List<String>> entry : rs.entrySet()) {
            DbAndTableRsp dataStore = new DbAndTableRsp();
            dataStore.setDbName(entry.getKey());
            dataStore.setDbTable(entry.getValue());
            if (dataMap.containsKey(entry.getKey())) {
                dataStore.setDbTitle(dataMap.get(entry.getKey()));
            } else {
                DWDatabaseRsp databaseRsp = dwService.getDbByDataName(entry.getKey());
                if (databaseRsp != null) {
                    dataMap.put(entry.getKey(), databaseRsp.getTitle());
                    dataStore.setDbTitle(databaseRsp.getTitle());
                } else {
                    dataMap.put(entry.getKey(), null);
                }
            }
            tableRspList.add(dataStore);
        }

        return tableRspList;
    }

    @Override
    public List<DbAndTableRsp> listErrDataNameSearch() {
        String userId = SessionHolder.getUserId();
        //查询出该用户下的所有表
        Map<String, String> allDb = new HashMap();
        dwDatabaseRepository.findByUserId(userId).stream().forEach(a -> allDb.put(a.getDataName(), a.getTitle()));
        List<String> strList = new LinkedList<>();
        allDb.keySet().stream().forEach(s -> strList.add(DB_FIX_NAME_PREFIX + s));
        //在mongo中查询出 所有的表名
        List<String> filterList = mongoClient.getDatabaseNames().stream().filter(s -> s.startsWith(DB_FIX_NAME_PREFIX)).collect(Collectors.toList());
        //取交集得出结果
        Collection<String> result = CollectionUtils.intersection(strList, filterList);
        List<DbAndTableRsp> rsps = new LinkedList<>();
        for (String a : result) {
            DbAndTableRsp tableRsp = new DbAndTableRsp();
            String s = a.replaceFirst(DB_FIX_NAME_PREFIX, "");
            String title = allDb.get(s);
            tableRsp.setDbName(s);
            tableRsp.setDbTitle(title);
            Set<String> db = mongoClient.getDB(a).getCollectionNames();
            ArrayList<String> list = new ArrayList<>(db);
            tableRsp.setDbTable(list);
            rsps.add(tableRsp);
        }
        return rsps;
    }

    @Override
    public BasePage<DataStoreRsp> listDataStore(DataStoreScreenReq req) {
        MongoCollection<Document> collection = getCollection();
        Integer size = req.getSize();
        Integer page = (req.getPage() - 1) * size;
        List<Bson> bsons = new ArrayList<>(3);
        if (StringUtils.hasText(req.getDbName())) {
            bsons.add(Filters.eq("dbName", req.getDbName()));
        }
        if (StringUtils.hasText(req.getDbTable())) {

            bsons.add(Filters.eq("dbTable", req.getDbTable()));
        }

        if (!StringUtils.isEmpty(req.getKeyword())) {
            //对特殊字符进行转义
            String keyWord = egrularEscape(req.getKeyword());
            bsons.add(Filters.regex("errorReason", keyWord));
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
        List<DataStore> dataStores = documentConverter.toBeans(findIterable, DataStore.class);
        List<DataStoreRsp> dataStoreRsps = MapperUtils.map(dataStores, DataStoreRsp.class);

        addDataStoreTitle(dataStoreRsps);

        return new BasePage<>(count, dataStoreRsps);
    }

    private void addDataStoreTitle(List<DataStoreRsp> dataStoreRsps) {
        if (dataStoreRsps == null || dataStoreRsps.isEmpty()) {
            return;
        }

        Map<String, String> dataMap = Maps.newHashMap();
        for (DataStoreRsp dataStore : dataStoreRsps) {

            String dataName = dataStore.getDbName();

            if (dataMap.containsKey(dataName)) {
                dataStore.setTitle(dataMap.get(dataName));
            } else {
                DWDatabaseRsp databaseRsp = dwService.getDbByDataName(dataName);
                if (databaseRsp != null) {
                    dataMap.put(dataName, databaseRsp.getTitle());
                    dataStore.setTitle(databaseRsp.getTitle());
                } else {
                    dataMap.put(dataName, null);
                }
            }
        }

        return;
    }

    @Override
    public void updateData(DataStoreModifyReq modifyReq) {
        MongoCollection<Document> collection = getCollection();
        collection.updateOne(documentConverter.buildObjectId(modifyReq.getId()),
                new Document(MongoOperation.SET.getType(), new Document("data",
                        JacksonUtils.writeValueAsString(modifyReq.getData())).append("status", "right")));
    }

    @Override
    public void deleteData(String id) {
        MongoCollection<Document> collection = getCollection();
        collection.deleteOne(documentConverter.buildObjectId(id));
    }

    @Override
    public void sendData(List<String> ids) {
        MongoCollection<Document> collection = getCollection();
        List<ObjectId> objectIds = ids.stream().map(ObjectId::new).collect(Collectors.toList());
        FindIterable<Document> findIterable = collection.find(Filters.in("_id", objectIds));
        List<DataStoreBO> bos = documentConverter.toBeans(findIterable, DataStoreBO.class);
        try {
            dataStoreSender.sendMsg(bos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        collection.deleteMany(Filters.in("_id", objectIds));
    }

    @Override
    public void updateErrData(DataStoreReq req) {
        if (StringUtils.isEmpty(req.getDbName()) || StringUtils.isEmpty(req.getDbTable())) {
            throw BizException.of(KgmsErrorCodeEnum.ILLEGAL_PARAM);
        }
        //创建新的集合
        MongoCollection<Document> collection = mongoClient.getDatabase(DB_FIX_NAME_PREFIX + req.getDbName()).getCollection(req.getDbTable());
        //查询旧集合
        MongoCollection<Document> collectionOld = getCollection();

        MongoCursor<Document> iterator = collectionOld.find(documentConverter.buildObjectId(req.getId())).iterator();
        if (!iterator.hasNext()) {
            return;
        }
        Document document = iterator.next();
        if (Objects.equals(req.getData(), document.get("data"))) {
            throw BizException.of(KgmsErrorCodeEnum.NO_DATA_CHANGE);
        }
        Document allData = getAllData(document, req);
        try {
            collection.insertOne(allData);
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.TAG_JSON_PASER_ERROR);
        }
        collectionOld.deleteOne(documentConverter.buildObjectId(req.getId()));
    }

    @Override
    public BasePage listErrDataStore(DataStoreScreenReq req) {
        if (StringUtils.isEmpty(req.getDbName()) || StringUtils.isEmpty(req.getDbTable())) {
            throw BizException.of(KgmsErrorCodeEnum.ILLEGAL_PARAM);
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(DB_FIX_NAME_PREFIX + req.getDbName()).getCollection(req.getDbTable());
        Integer size = req.getSize();
        Integer page = (req.getPage() - 1) * size;
        FindIterable<Document> findIterable;
        long count = collection.countDocuments();
        findIterable = collection.find().sort(Sorts.descending("createdate")).skip(page).limit(size);
        return new BasePage(count, filterData(findIterable));
    }

    @Override
    public void rerun(DtReq req) {

        if (req.getDbTable() == null || req.getDbName() == null) {
            throw BizException.of(KgmsErrorCodeEnum.ILLEGAL_PARAM);
        }

        DWDatabaseRsp databaseRsp = dwService.findDatabaseByDataName(req.getDbName());

        List<DWTask> all = accessTaskService.getTableTask(databaseRsp.getId(), req.getDbTable());

        if (all == null || all.isEmpty()) {
            return;
        }

        List<String> resourceNames = all.stream().map(DWTask::getName).collect(Collectors.toList());

        accessTaskService.addRerunTask(databaseRsp.getId(), req.getDbTable(), resourceNames);
    }

    private static final String OLD_DATA = "_id\": { \"$oid\" :\"";
    private static final String NEW_DATA = "oldId \":{\"";

    //对于 带有_id的数据 会导致 插入失败， 因此需要过滤处理
    private Map<String, Object> filterDataId(Map<String, Object> data) {
        String dataString = JSONObject.toJSONString(data).replace(OLD_DATA, NEW_DATA);
        return JSONObject.parseObject(dataString);

    }


    private List<DataStoreRsp> filterData(FindIterable<Document> findIterable) {
        List<DataStore> list = new ArrayList<>();
        for (Document document : findIterable) {
            JSONObject jsonObject = JSON.parseObject(document.toJson());
            Map rawData = JSON.parseObject(jsonObject.get(DB_VIEW_DATA) + "", Map.class);
            document.remove(DB_VIEW_DATA);
            String dataFrom = rawData.get("dataFrom") + "";
            DataStore dataStore;
            //数据来源是 数仓数据修改
            if (Objects.equals(dataFrom, "dw")) {
                dataStore = new DataStore();
                dataStore.setDbName(rawData.get("dbName") + "");
                dataStore.setStatus(DB_VIEW_STATUS);

            } else {
                dataStore = documentConverter.toBean(new Document(rawData), DataStore.class);

            }
            // dataStore.setId(document.getObjectId("_id").toHexString());
            document.remove(MONGO_ID);
            dataStore.setData(JSONObject.toJSONString(document));
            list.add(dataStore);
        }
        List<DataStoreRsp> dataStoreRsps = MapperUtils.map(list, DataStoreRsp.class);
        addDataStoreTitle(dataStoreRsps);
        return dataStoreRsps;
    }

    private Document getAllData(Document document, DataStoreReq req) {
        document.remove("data");
        document.remove(MONGO_ID);
        document.append("status", DB_VIEW_STATUS).append("createdate", DateUtils.formatDatetime());
        Document allDocument = new Document();
        allDocument.append(DB_VIEW_DATA, document);
        allDocument.putAll(filterDataId(req.getData()));
        return allDocument;
    }


    public String egrularEscape(String keyword) {
        if (!StringUtils.isEmpty(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\\\" + key);
                }
            }
        }
        return keyword;

    }
}