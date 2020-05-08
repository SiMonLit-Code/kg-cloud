package com.plantdata.kgcloud.domain.dw.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.constant.DWFileConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.data.utils.StringToDateUtil;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWFileTable;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.repository.DWDatabaseRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWTableRepository;
import com.plantdata.kgcloud.domain.dw.req.DWDatabaseUpdateReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableBatchReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableUpdateReq;
import com.plantdata.kgcloud.domain.dw.rsp.DWFileTableRsp;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.domain.dw.service.TableDataService;
import com.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import com.plantdata.kgcloud.domain.edit.service.EntityService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.req.DwTableDataSearchReq;
import com.plantdata.kgcloud.sdk.req.DwTableDataStatisticReq;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.template.FastdfsTemplate;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.plantdata.kgcloud.domain.dw.service.impl.PreBuilderServiceImpl.bytesToFile;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-30 16:43
 **/
@Service
public class TableDataServiceImpl implements TableDataService {

    private static final String MONGO_ID = CommonConstants.MongoConst.ID;
    private static final int CREATE_WAY = 2;
    private static final int IS_WRITE_DW = 1;
    private static final String DB_FIX_NAME_PREFIX = "dw_rerun_";
    private static final String DB_VIEW_STATUS = "Edit";
    private static final String DB_VIEW_DATA = "showData";
    @Autowired
    DWTableRepository dwTableRepository;

    @Autowired
    DWDatabaseRepository dwDatabaseRepository;
    @Autowired
    private DWService dwService;
    @Autowired
    private MongoProperties mongoProperties;
    @Autowired
    private FastdfsTemplate fastdfsTemplate;
    @Autowired
    private EntityFileRelationService entityFileRelationService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private DocumentConverter documentConverter;

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId()).getCollection(DWFileConstants.FILE);
    }

    @Override
    public Page<Map<String, Object>> getData(String userId, Long datasetId, Long tableId, DataOptQueryReq baseReq) {
        Map<String, Object> query = new HashMap<>();
        if (StringUtils.hasText(baseReq.getField()) && StringUtils.hasText(baseReq.getKw())) {
            Map<String, String> value = new HashMap<>();
            value.put(baseReq.getField(), baseReq.getKw());
            query.put("search", value);
        }

        try (DataOptProvider provider = getProvider(userId, datasetId, tableId, mongoProperties)) {

            PageRequest pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize());
            DWTable table = dwService.getTableDetail(tableId);
            List<Map<String, Object>> maps = provider.find(baseReq.getOffset(), baseReq.getLimit(), query);
            List<Map<String, Object>> mapResult = new ArrayList<>();
            for (int i = 0; i < maps.size(); i++) {
                Map<String, Object> map = maps.get(i);

                Map<String, Object> result = filterSchema(table, map);

                mapResult.add(result);
            }
            long count = provider.count(query);
            return new PageImpl<>(mapResult, pageable, count);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_CONNECT_ERROR);
        }
    }

    // TODO
    @Override
    public Map<String, Object> getDataById(String userId, Long datasetId, Long tableId, String dataId) {
        try (DataOptProvider provider = getProvider(userId, datasetId, tableId, mongoProperties)) {
            //从Mongodb中取得这条数据
            Map<String, Object> one = provider.findOne(dataId);
            DWTable table = dwService.getTableDetail(tableId);
            if (table == null) {
                throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
            }

            //遍历这条数据里所有的key value
            return filterSchema(table, one);

        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    private Map<String, Object> filterSchema(DWTable table, Map<String, Object> one) {

        //得到这条数据的Schema
        List<DataSetSchema> schema = table.getSchema();
        //把Schema信息放入到这个Map集合中.  Key=id,value=object
        Map<String, DataSetSchema> schemaMap = new HashMap<>();
        for (DataSetSchema o : schema) {
            schemaMap.put(o.getField(), o);
        }
        Map<String, Object> result = new HashMap<>();

        //遍历这条数据里所有的key value
        for (Map.Entry<String, Object> entry : one.entrySet()) {
            //如果schema中有这条数据的key
            DataSetSchema scm = schemaMap.get(entry.getKey());
            if (scm != null) {
                if (Objects.equals(scm.getType(), FieldType.DOUBLE.getCode()) ||
                        Objects.equals(scm.getType(), FieldType.FLOAT.getCode())) {
                    BigDecimal value = new BigDecimal(entry.getValue().toString());
                    if (value.compareTo(new BigDecimal(value.intValue())) == 0) {
                        DecimalFormat f = new DecimalFormat("##.0");
                        result.put(entry.getKey(), f.format(value));
                    } else {
                        result.put(entry.getKey(), value);
                    }
                }
                //如果类型是时间类型。为了前端方便校验 需要转换为标准格式。
                else if (Objects.equals(scm.getType(), FieldType.DATETIME.getCode()) && entry.getValue() != null) {
                    SimpleDateFormat dataString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = StringToDateUtil.stringToDate(entry.getValue().toString());
                    String value = null;
                    if (date != null) {
                        value = dataString.format(date);
                    }
                    result.put(entry.getKey(), value);
                } else if (Objects.equals(scm.getType(), FieldType.DATE.getCode()) && entry.getValue() != null) {
                    SimpleDateFormat dataString = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = StringToDateUtil.stringToDate(entry.getValue().toString());
                    String value = dataString.format(date);
                    result.put(entry.getKey(), value);
                } else if (Objects.equals(scm.getType(), FieldType.TEXT.getCode())) {
                    //对于长文本 为防止前端解析jsonObject失败 需转为String类型
                    if (entry.getValue() instanceof Map) {
                        Object json = JSON.toJSON(entry.getValue());
                        result.put(entry.getKey(), json.toString());
                    } else {
                        result.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    result.put(entry.getKey(), entry.getValue());
                }
            } else {
                result.put(entry.getKey(), entry.getValue());
            }

        }
        return result;
    }

    private DataOptProvider getProvider(String userId, Long datasetId, Long tableId, MongoProperties
            mongoProperties) {

        return getProvider(false, userId, datasetId, tableId, mongoProperties);
    }

    private DataOptProvider getProvider(boolean isLocal, String userId, Long datasetId, Long
            tableId, MongoProperties mongoProperties) {

        DWDatabaseRsp database = dwService.getDetail(datasetId);

        if (database == null) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }

        DWTable table = dwService.getTableDetail(tableId);
        if (table == null) {
            throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
        }

        DataOptConnect connect = DataOptConnect.of(isLocal, database, table, mongoProperties);
        return DataOptProviderFactory.createProvider(connect);
    }

    @Override
    public List<Map<String, Object>> statistic(String userId, Long datasetId, Long tableId, DwTableDataStatisticReq
            statisticReq) {
        try (DataOptProvider provider = getProvider(userId, datasetId, tableId, mongoProperties)) {
            return provider.aggregateStatistics(statisticReq.getFilterMap(), statisticReq.getGroupMap(), statisticReq.getSortMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> search(String userId, Long datasetId, Long tableId, DwTableDataSearchReq
            searchReq) {
        DWDatabaseRsp database = dwService.getDetail(datasetId);
        if (database == null) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }
        DWTable table = dwService.getTableDetail(tableId);
        if (table == null) {
            throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
        }
        DataOptConnect connect = DataOptConnect.of(database, table, mongoProperties);
        try (DataOptProvider provider = DataOptProviderFactory.createProvider(connect)) {
            List<String> fields = CollectionUtils.isEmpty(searchReq.getFields()) ? provider.getFields() : searchReq.getFields();
            Map<String, String> searchMap = Maps.newHashMapWithExpectedSize(fields.size());
            fields.forEach(a -> searchMap.put(a, searchReq.getKw()));
            return provider.search(searchMap, searchReq.getOffset(), searchReq.getLimit());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public DWFileTable fileAdd(DWFileTableReq req) {

        byte[] bytes = fastdfsTemplate.downloadFile(req.getPath());

        DWFileTable fileTable = ConvertUtils.convert(DWFileTable.class).apply(req);
        fileTable.setFileSize((long) bytes.length);
        fileTable.setUserId(SessionHolder.getUserId());
        if (req.getFileName() != null && req.getFileName().contains(".")) {
            fileTable.setType(req.getFileName().substring(req.getFileName().lastIndexOf(".") + 1));
        }
        fileTable.setTitle(req.getFileName());
        fileTable.setIndexType(0);
        fileTable.setUserId(SessionHolder.getUserId());
        fileTable.setCreateTime(new Date());
        Document document = documentConverter.toDocument(fileTable);
        getCollection().insertOne(document);
        DWFileTable dwFileTable = documentConverter.toBean(document, DWFileTable.class);


        // 对压缩包进行解压
        String zFile = "" + req.getPath().substring(req.getPath().lastIndexOf("/"));
        bytesToFile(bytes, zFile);
        try {
            if ("rar".equals(fileTable.getType())) {
                unRar(new File(zFile), req.getPath().substring(0, req.getPath().lastIndexOf("/") + 1));
            } else if ("zip".equals(fileTable.getType())) {
                unZip(new File(zFile), req.getPath().substring(0, req.getPath().lastIndexOf("/") + 1));
            }
        } catch (Exception e) {
        }

        return dwFileTable;
    }

    public void unZip(File zipFile, String outDir) throws Exception {

        File outFileDir = new File(outDir);
        if (!outFileDir.exists()) {
            boolean isMakDir = outFileDir.mkdirs();
            if (!isMakDir) {
                throw new Exception();
            }
        }

        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration enumeration = zip.getEntries(); enumeration.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) enumeration.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);

            if (entry.isDirectory()) {      //处理压缩文件包含文件夹的情况
                File fileDir = new File(outDir + zipEntryName);
                fileDir.mkdir();
                continue;
            }

            File file = new File(outDir, zipEntryName);
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) > 0) {
                out.write(buff, 0, len);
            }
            in.close();
            out.close();
        }
    }

    public void unRar(File rarFile, String outDir) throws Exception {
        File outFileDir = new File(outDir);
        if (!outFileDir.exists()) {
            boolean isMakDir = outFileDir.mkdirs();
            if (!isMakDir) {
                throw new Exception();
            }
        }
        Archive archive = new Archive(new FileInputStream(rarFile));
        FileHeader fileHeader = archive.nextFileHeader();
        while (fileHeader != null) {
            if (fileHeader.isDirectory()) {
                fileHeader = archive.nextFileHeader();
                continue;
            }
            File out = new File(outDir + fileHeader.getFileNameString());
            if (!out.exists()) {
                if (!out.getParentFile().exists()) {
                    out.getParentFile().mkdirs();
                }
                out.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(out);
            archive.extractFile(fileHeader, os);

            os.close();

            fileHeader = archive.nextFileHeader();
        }
        archive.close();
    }

    @Override
    public Page<DWFileTableRsp> getFileData(String userId, Long databaseId, Long tableId, DataOptQueryReq baseReq) {

        Integer size = baseReq.getSize();
        Integer page = (baseReq.getPage() - 1) * size;
        List<Bson> bsons = new ArrayList<>(3);
        if (StringUtils.hasText(baseReq.getKw())) {
            bsons.add(Filters.regex("name", Pattern.compile("^.*" + baseReq.getKw() + ".*$")));
        }
        bsons.add(Filters.eq("dataBaseId", databaseId));
        bsons.add(Filters.eq("tableId", tableId));

        FindIterable<Document> findIterable = getCollection().find(Filters.and(bsons)).skip(page).limit(size + 1).sort(new Document("createTime", -1));
        List<DWFileTable> dwFileTables = documentConverter.toBeans(findIterable, DWFileTable.class);
        List<DWFileTableRsp> dataStoreRsps = dwFileTables.stream().map(ConvertUtils.convert(DWFileTableRsp.class)).collect(Collectors.toList());
        int count = dataStoreRsps.size();
        if (count > size) {
            dataStoreRsps.remove(size.intValue());
            count += page;
        }

        return new PageImpl<>(dataStoreRsps, PageRequest.of(baseReq.getPage() - 1, size), count);
    }

    @Override
    public void fileUpdate(DWFileTableUpdateReq fileTableReq) {
        MongoCollection<Document> mongoCollection = getCollection();
        MongoCursor<Document> cursor = mongoCollection.find(documentConverter.buildObjectId(fileTableReq.getId())).iterator();
        if (!cursor.hasNext()) {
            return;
        }
        Document document = cursor.next();
        DWFileTable dwFileTable = documentConverter.toBean(document, DWFileTable.class);
        String id = dwFileTable.getId();
        dwFileTable.setId(null);
        dwFileTable.setName(fileTableReq.getName());
        dwFileTable.setTitle(fileTableReq.getName() + "." + dwFileTable.getType());
        dwFileTable.setKeyword(fileTableReq.getKeyword());
        dwFileTable.setDescription(fileTableReq.getDescription());
        dwFileTable.setOwner(fileTableReq.getOwner());
        Document newDocument = documentConverter.toDocument(dwFileTable);
        mongoCollection.updateOne(documentConverter.buildObjectId(id), new Document("$set", newDocument));
    }

    @Override
    public void fileDelete(String id) {
        getCollection().deleteOne(documentConverter.buildObjectId(id));
        // 删除实体文件关联
        entityFileRelationService.deleteRelationByDwFileId(id);
    }

    @Override
    public void fileDeleteBatch(List<String> ids) {
        List<ObjectId> collect = ids.stream().map(ObjectId::new).collect(Collectors.toList());
        getCollection().deleteMany(Filters.in("_id", collect));
    }

    @Override
    public void fileAddBatch(DWFileTableBatchReq fileTableReq, MultipartFile[] files) {

        if (files == null || files.length == 0) {
            return;
        }

        for (MultipartFile file : files) {
            DWFileTable fileTable = ConvertUtils.convert(DWFileTable.class).apply(fileTableReq);
            fileTable.setFileSize(file.getSize());
            fileTable.setUserId(SessionHolder.getUserId());
            if (file.getOriginalFilename().contains(".")) {
                fileTable.setType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
                fileTable.setName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
            } else {
                fileTable.setName(file.getOriginalFilename());
            }
            fileTable.setTitle(file.getOriginalFilename());
            fileTable.setIndexType(0);
            fileTable.setPath(fastdfsTemplate.uploadFile(file).getFullPath());
            fileTable.setCreateTime(new Date());
            Document document = documentConverter.toDocument(fileTable);
            getCollection().insertOne(document);

            // 对压缩包进行解压
            byte[] bytes = fastdfsTemplate.downloadFile(fileTable.getPath());
            String zFile = "" + fileTable.getPath().substring(fileTable.getPath().lastIndexOf("/"));
            bytesToFile(bytes, zFile);
            try {
                if ("rar".equals(fileTable.getType())) {
                    unRar(new File(zFile), fileTable.getPath().substring(0, fileTable.getPath().lastIndexOf("/") + 1));
                } else if ("zip".equals(fileTable.getType())) {
                    unZip(new File(zFile), fileTable.getPath().substring(0, fileTable.getPath().lastIndexOf("/") + 1));
                }
            } catch (Exception e) {
            }

        }
    }

    @Override
    public void dataUpdate(DWDatabaseUpdateReq baseReq) {
        DWTable table = dwTableRepository.findOne(Example.of(DWTable.builder().id(baseReq.getTableId()).dwDataBaseId(baseReq.getDataBaseId()).build()))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST));
        if (table.getCreateWay() != CREATE_WAY && (table.getIsWriteDW() == null || table.getIsWriteDW() != IS_WRITE_DW)) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_CREATE_WAY_ERROR);
        }
        DWDatabase database = dwDatabaseRepository.findById(baseReq.getDataBaseId())
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST));
        MongoCollection<Document> collection = mongoClient.getDatabase(DB_FIX_NAME_PREFIX + database.getDataName()).getCollection(table.getTableName());
        MongoCollection<Document> collectionLog = mongoClient.getDatabase(database.getDataName()).getCollection(table.getTableName());

        Map<String, Object> data = baseReq.getData();
        String mongoId = baseReq.getId();
        Map<Object, Object> map = new HashMap<>();
        map.put("dbName", database.getDataName());
        map.put("dataFrom", "dw");
        map.put("status", DB_VIEW_STATUS);
        data.put(DB_VIEW_DATA, map);
        Object id = getMongoId(baseReq.getId());
        update(data, id, collection);
        data.remove(mongoId);
        data.remove(DB_VIEW_DATA);
        data.put(DataConst.UPDATE_AT, DateUtils.formatDatetime());
        update(data, id, collectionLog);


    }


    public Object getMongoId(String mongoId) {
        Object id;
        if (ObjectId.isValid(mongoId)) {
            id = new ObjectId(mongoId);
        } else {
            id = mongoId;
        }
        return id;
    }

    public void update(Map<String, Object> data, Object mongoId, MongoCollection<Document> collectionLog) {
        Document parse = Document.parse(JacksonUtils.writeValueAsString(data));
        List<Bson> bsonList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : parse.entrySet()) {
            Bson set = Updates.set(entry.getKey(), entry.getValue());
            bsonList.add(set);
        }
        collectionLog.updateMany(Filters.eq(MONGO_ID, mongoId), Updates.combine(bsonList), new UpdateOptions().upsert(true));
    }
}
