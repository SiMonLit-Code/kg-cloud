package com.plantdata.kgcloud.domain.dataset.provider;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.common.util.PatternUtils;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.sdk.constant.DataStoreSearchEnum;
import com.plantdata.kgcloud.sdk.req.DwTableDataStatisticReq;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.AggregateEnum;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
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
    private final static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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
        if (StringUtils.isNotEmpty(info.getUsername()) && StringUtils.isNotEmpty(info.getPassword())) {
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
                Map<String, Object> value = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> objectEntry : value.entrySet()) {
                    bsonList.add(Filters.eq(objectEntry.getKey(), objectEntry.getValue()));
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
            return new Document();
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
    public List<Map<String, Object>> search(Map<String, String> searchMap, int offset, int limit) {
        List<Document> findDoc = Lists.newArrayListWithExpectedSize(searchMap.size());
        Document projection = new Document();
        searchMap.forEach((k, v) -> {
            findDoc.add(new Document(k, PatternUtils.getLikeStr(v)));
            projection.put(k, 1);
        });
        FindIterable<Document> documents = getCollection().find(new Document("$or", findDoc))
                .skip(offset).limit(limit).projection(projection);
        List<Map<String, Object>> list = Lists.newArrayList();
        documents.iterator().forEachRemaining(a -> {
            Map<String, Object> dataMap = projection.keySet().stream().collect(Collectors.toMap(k -> k, v -> {
                String[] split = StringUtils.split(v, ".");
                Map<String, Object> data = a;
                for (int i = 0; i < split.length; i++) {
                    if (i == split.length - 1) {
                        return data.get(split[i]);
                    }
                    data = (Map<String, Object>) data.get(split[i]);
                }
                return a;
            }));
            BasicConverter.consumerIfNoNull(dataMap, list::add);
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> aggregateStatistics(Map<String, Object> filterMap, Map<String, DwTableDataStatisticReq.GroupReq> groupMap,
                                                         Map<SortTypeEnum, List<String>> sortMap) {

        List<Bson> operations = new ArrayList<>();
        //前置筛选
        BasicConverter.consumerIfNoNull(filterMap, a -> {
            a.forEach((k, v) -> {
                if (v instanceof Map) {
                    Map<String, Object> valMap = (Map<String, Object>) v;
                    Object like = valMap.get(DataStoreSearchEnum.LIKE.getName());
                    Object noLike = valMap.get(DataStoreSearchEnum.NOL_LIKE.getName());
                    if (noLike != null) {
                        a.put(k, PatternUtils.getNoLikeStr(noLike.toString()));
                    }
                    if (like != null) {
                        a.put(k, PatternUtils.getLikeStr(like.toString()));
                    }
                }
            });
            operations.add(Aggregates.match(new BasicDBObject(a)));
        });
        //group
        BasicConverter.consumerIfNoNull(groupMap, a -> {
            BasicDBObject basicDBObject = new BasicDBObject();
            List<BsonField> bsonFields = new ArrayList<>();
            groupMap.forEach((k, v) -> {

                if (AggregateEnum.COUNT == v.getAggregateType()) {
                    bsonFields.add(new BsonField(k, new BasicDBObject("$sum", 1)));
                } else if (AggregateEnum.SUM == v.getAggregateType()) {
                    bsonFields.add(new BsonField(k, new BasicDBObject("$sum", "$" + v.getJsonPath())));
                } else if (AggregateEnum.SHOW == v.getAggregateType()) {
                    basicDBObject.append(k, "$" + v.getJsonPath());
                } else {
                    throw BizException.of(KgmsErrorCodeEnum.DATA_STORE_STATISTIC_TYPE_ERROR);
                }
            });
            operations.add(Aggregates.group(basicDBObject, bsonFields));
        });
        //排序
        BasicConverter.consumerIfNoNull(sortMap, a -> {
            Bson[] bsonArray = sortMap.entrySet().stream().map(entry -> {
                if (entry.getKey() == SortTypeEnum.ASC) {
                    return Sorts.ascending(entry.getValue());
                }
                return Sorts.descending(entry.getValue());
            }).toArray(b -> new Bson[a.size()]);
            operations.add(Aggregates.sort(Sorts.orderBy(bsonArray)));
        });
        operations.forEach(a -> System.err.println(a.toString()));
        //执行
        AggregateIterable<Document> aggregate = getCollection().aggregate(operations);
        List<Map<String, Object>> resList = new ArrayList<>();
        aggregate.iterator().forEachRemaining(a -> {
            Map<String, Object> objectMap = Maps.newHashMap();
            a.forEach((k, v) -> {
                if (k.equals("_id")) {
                    objectMap.putAll((Map<String, String>) a.get("_id"));
                } else {
                    objectMap.put(k, v);
                }
            });
            resList.add(objectMap);
        });
        return resList;
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
        } else {
            findIterable.sort(Sorts.descending(DataConst.CREATE_AT));
        }
        if (offset != null && offset >= 0) {
            findIterable = findIterable.skip(offset);
        }
        int size = 10;
        if (limit != null && limit > 0) {
            size = limit;
        }
        findIterable = findIterable.limit(size);

        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Document document : findIterable) {
            Map<String, Object> map = new HashMap<>(size);
            map.putAll(document);
            try {

                if(map != null && !map.isEmpty()){

                    Map<String,Object> newMap = new HashMap<>(map);
                    for(Map.Entry<String,Object> entry : newMap.entrySet()){
                        if(entry.getValue() instanceof Decimal128){
                            Double d  = ((Decimal128)entry.getValue()).bigDecimalValue().doubleValue();
                            map.put(entry.getKey(),d);
                        }else if(entry.getValue() instanceof Date){

                            try {
                                format.setCalendar(new GregorianCalendar(
                                        new SimpleTimeZone(0, "GMT")));
                                String date = format.format((Date)entry.getValue());
                                map.put(entry.getKey(),date);
                            }catch (Exception e){}

                        }
                    }
                }

                map.put(MONGO_ID, document.getObjectId(MONGO_ID).toHexString());
            } catch (ClassCastException e) {
                map.put(MONGO_ID, document.getString(MONGO_ID));
            }
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
        try {
            Document first = getCollection().find(Filters.eq(MONGO_ID, new ObjectId(id))).first();
            if (first == null) {
                return new HashMap<>();
            }
            first.put(MONGO_ID, first.getObjectId(MONGO_ID).toHexString());
            return first;
        } catch (IllegalArgumentException e) {
            Document first = getCollection().find(Filters.eq(MONGO_ID, id)).first();
            if (first == null) {
                return new HashMap<>();
            }
            first.put(MONGO_ID, first.getString(MONGO_ID));
            return first;
        }

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
//        Document map = Document.parse(JacksonUtils.writeValueAsString(node));
        Document map = new Document(node);
        collection.insertOne(map);
        map.put(MONGO_ID, map.getObjectId(MONGO_ID).toHexString());
        return map;
    }

    @Override
    public Map<String, Object> update(String id, Map<String, Object> node) {
        MongoCollection<Document> collection = getCollection();
//        Document map = Document.parse(JacksonUtils.writeValueAsString(node));
        Document map = new Document(node);
        collection.updateOne(Filters.eq(MONGO_ID, new ObjectId(id)), new Document("$set", map));
        map.put(MONGO_ID, id);
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
            Document map = new Document(node);
//            Document map = Document.parse(JacksonUtils.writeValueAsString(node));
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
