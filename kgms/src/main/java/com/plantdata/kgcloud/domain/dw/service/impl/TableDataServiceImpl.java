package com.plantdata.kgcloud.domain.dw.service.impl;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWFileTable;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.repository.DWDatabaseRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWFileTableRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWTableRepository;
import com.plantdata.kgcloud.domain.dw.req.DWDatabaseUpdateReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableBatchReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableReq;
import com.plantdata.kgcloud.domain.dw.req.DWFileTableUpdateReq;

import com.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import com.plantdata.kgcloud.domain.edit.service.EntityService;
import com.plantdata.kgcloud.sdk.req.DwTableDataSearchReq;
import com.plantdata.kgcloud.sdk.req.DwTableDataStatisticReq;

import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.domain.dw.rsp.DWFileTableRsp;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.domain.dw.service.TableDataService;
import com.plantdata.kgcloud.domain.edit.entity.MultiModal;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataOptQueryReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.template.FastdfsTemplate;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.DateUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import static com.plantdata.kgcloud.domain.dw.service.impl.PreBuilderServiceImpl.bytesToFile;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-03-30 16:43
 **/
@Service
public class TableDataServiceImpl implements TableDataService {

    @Autowired
    private DWService dwService;

    @Autowired
    private MongoProperties mongoProperties;

    @Autowired
    private FastdfsTemplate fastdfsTemplate;

    @Autowired
    private DWFileTableRepository fileTableRepository;

    @Autowired
    private EntityFileRelationService entityFileRelationService;

    @Autowired
    private EntityService entityService;

    @Autowired
    DWTableRepository dwTableRepository;

    @Autowired
    DWDatabaseRepository dwDatabaseRepository;

    @Autowired
    private MongoClient mongoClient;


    @Autowired
    private DocumentConverter documentConverter;
    private static final String MONGO_ID = CommonConstants.MongoConst.ID;
    private static final int CREATE_WAY = 2;
    private static final int IS_WRITE_DW = 1;
    private static final String DB_FIX_NAME_PREFIX = "dw_rerun_";
    private static final String DB_VIEW_STATUS = "Edit";
    private static final String DB_VIEW_DATA = "_showData";

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
            List<Map<String, Object>> maps = provider.find(baseReq.getOffset(), baseReq.getLimit(), query);
            long count = provider.count(query);
            return new PageImpl<>(maps, pageable, count);
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_CONNECT_ERROR);
        }
    }

    private DataOptProvider getProvider(String userId, Long datasetId, Long tableId, MongoProperties mongoProperties) {

        return getProvider(false,userId,datasetId,tableId,mongoProperties);
    }
    private DataOptProvider getProvider(boolean isLocal,String userId, Long datasetId, Long tableId, MongoProperties mongoProperties) {

        DWDatabaseRsp database = dwService.getDetail(datasetId);

        if (database == null) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }

        DWTable table = dwService.getTableDetail(tableId);
        if (table == null) {
            throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
        }

        DataOptConnect connect = DataOptConnect.of(isLocal,database, table, mongoProperties);
        return DataOptProviderFactory.createProvider(connect);
    }

    @Override
    public List<Map<String, Object>> statistic(String userId, Long datasetId, Long tableId, DwTableDataStatisticReq statisticReq) {
        try (DataOptProvider provider = getProvider(userId, datasetId, tableId, mongoProperties)) {
            return provider.aggregateStatistics(statisticReq.getFilterMap(), statisticReq.getGroupMap(), statisticReq.getSortMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> search(String userId, Long datasetId, Long tableId, DwTableDataSearchReq searchReq) {
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
    public Map<String, Object> getDataById(String userId, Long datasetId, Long tableId, String dataId) {
        try (DataOptProvider provider = getProvider(userId, datasetId, tableId, mongoProperties)) {
            Map<String, Object> one = provider.findOne(dataId);
            DWTable table = dwService.getTableDetail(tableId);
            if (table == null) {
                throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
            }
            List<DataSetSchema> schema = table.getSchema();
            Map<String, DataSetSchema> schemaMap = new HashMap<>();
            for (DataSetSchema o : schema) {
                schemaMap.put(o.getField(), o);
            }
            Map<String, Object> result = new HashMap<>();

            for (Map.Entry<String, Object> entry : one.entrySet()) {
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
                    } else {
                        result.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
            return result;
        } catch (IOException e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
    }

    @Override
    public DWFileTable fileAdd(DWFileTableReq req) {

        byte[] bytes = fastdfsTemplate.downloadFile(req.getPath());

        DWFileTable fileTable = ConvertUtils.convert(DWFileTable.class).apply(req);
        fileTable.setFileSize(new Long(bytes.length));
        fileTable.setUserId(SessionHolder.getUserId());
        if (req.getFileName() != null && req.getFileName().contains(".")) {
            fileTable.setType(req.getFileName().substring(req.getFileName().lastIndexOf(".") + 1));
        }
        fileTable.setDataBaseId(req.getDataBaseId());

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

        return fileTableRepository.save(fileTable);
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

        PageRequest pageable = PageRequest.of(baseReq.getPage() - 1, baseReq.getSize(), Sort.by(Sort.Order.desc("createAt")));

        Specification<DWFileTable> specification = new Specification<DWFileTable>() {
            @Override
            public Predicate toPredicate(Root<DWFileTable> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (!com.alibaba.excel.util.StringUtils.isEmpty(baseReq.getKw())) {

                    Predicate likename = criteriaBuilder.like(root.get("name").as(String.class), "%" + baseReq.getKw() + "%");
                    predicates.add(likename);
                }

                Predicate databaseIdPre = criteriaBuilder.equal(root.get("dataBaseId").as(Long.class), databaseId);
                predicates.add(databaseIdPre);

                Predicate tableIdPre = criteriaBuilder.equal(root.get("tableId").as(Long.class), tableId);
                predicates.add(tableIdPre);

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Page<DWFileTable> all = fileTableRepository.findAll(specification, pageable);

        Page<DWFileTableRsp> map = all.map(ConvertUtils.convert(DWFileTableRsp.class));

        return map;
    }

    @Override
    public void fileUpdate(DWFileTableUpdateReq fileTableReq) {

        Optional<DWFileTable> opt = fileTableRepository.findById(fileTableReq.getId());

        if (opt.isPresent()) {

            DWFileTable fileTable = opt.get();
            fileTable.setName(fileTableReq.getName());
            fileTable.setOwner(fileTableReq.getOwner());
            fileTable.setKeyword(fileTableReq.getKeyword());
            fileTable.setDescription(fileTableReq.getDescription());
            fileTableRepository.save(fileTable);

            // 更新关联表
            List<EntityFileRelation> relationList = entityFileRelationService.getRelationByDwFileId(fileTable.getId());
            if (!CollectionUtils.isEmpty(relationList)) {
                relationList.forEach(s->{
                    s.setName(fileTableReq.getName());
                    s.setKeyword(fileTableReq.getKeyword());
                    s.setDescription(fileTableReq.getDescription());
                });
            }
            entityFileRelationService.updateRelations(relationList);
        }

    }

    @Override
    public void fileDelete(Integer id) {
        fileTableRepository.deleteById(id);
        // 删除实体文件关联
        entityFileRelationService.deleteRelationByDwFileId(id);
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
            fileTable.setDataBaseId(fileTableReq.getDataBaseId());
            fileTable.setPath(fastdfsTemplate.uploadFile(file).getFullPath());

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

            fileTableRepository.save(fileTable);
        }

        return;
    }

    @Override
    public void dataUpdate(DWDatabaseUpdateReq baseReq) {
       // String userId = SessionHolder.getUserId();
        DWTable table = dwTableRepository.findOne(Example.of(DWTable.builder().id(baseReq.getTableId()).dwDataBaseId(baseReq.getDataBaseId()).build()))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST));
        if (table.getCreateWay() != CREATE_WAY && (table.getIsWriteDW() == null || table.getIsWriteDW() != IS_WRITE_DW)) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_CREATE_WAY_ERROR);
        }
<<<<<<< HEAD
        DWDatabase database = dwDatabaseRepository.findById(baseReq.getDataBaseId())
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST));
        MongoCollection<Document> collection = mongoClient.getDatabase(DB_FIX_NAME_PREFIX + database.getDataName()).getCollection(table.getTableName());
        MongoCollection<Document> collectionLog = mongoClient.getDatabase(database.getDataName()).getCollection(table.getTableName());
        long count;
        try {
            count = collection.countDocuments(documentConverter.buildObjectId(baseReq.getId()));
        } catch (Exception e) {
            count = collection.countDocuments(Filters.eq(baseReq.getId()));
        }
        if (baseReq.getId().length() <= 32) {

            Map<String, Object> data = baseReq.getData();
            String mongoId = baseReq.getId();
            Map<Object, Object> map = new HashMap<>();
            map.put("dbName", database.getDataName());
            map.put("tableId", baseReq.getDataBaseId());
            map.put("dataFrom", "dw");
            map.put("status", DB_VIEW_STATUS);
            data.put(DB_VIEW_DATA, map);
            if (count == 0) {
              //  data.put(MONGO_ID, new ObjectId(mongoId));
                collection.insertOne(new Document(data));
                data.remove(MONGO_ID);
                data.remove(DB_VIEW_DATA);
                collectionLog.updateOne(Filters.eq(MONGO_ID, new ObjectId(mongoId)), new Document("$set", new Document(data)));
            } else {
                data.remove(MONGO_ID);
                collection.updateOne(Filters.eq(MONGO_ID, new ObjectId(mongoId)), new Document("$set", new Document(data)));
                data.remove(DB_VIEW_DATA);
                collectionLog.updateOne(Filters.eq(MONGO_ID, new ObjectId(mongoId)), new Document("$set", new Document(data)));

            }
        } else {
=======


        try(DataOptProvider provider = getProvider(true,userId, baseReq.getDataBaseId(), baseReq.getTableId(), mongoProperties)) {

            DWDatabase database = dwDatabaseRepository.findById(baseReq.getDataBaseId())
                    .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST));
            MongoCollection<Document> collection = mongoClient.getDatabase(DB_FIX_NAME_PREFIX + database.getDataName()).getCollection(table.getTableName());
            long count = collection.countDocuments(documentConverter.buildObjectId(baseReq.getId()));
>>>>>>> 3a3e6b97d634600955aceb721bb4a7b29607ff62
            Map<String, Object> data = baseReq.getData();
            String mongoId = baseReq.getId();
            Map<Object, Object> map = new HashMap<>();
            map.put("dbName", database.getDataName());
            map.put("tableId", baseReq.getDataBaseId());
            map.put("dataFrom", "dw");
            map.put("status", DB_VIEW_STATUS);
<<<<<<< HEAD
            data.put(DB_VIEW_DATA, map);
            if (count == 0) {
                data.put(MONGO_ID, mongoId);
=======
            data.put(DataConst.UPDATE_AT, DateUtils.formatDatetime());
            data.put(DB_VIEW_DATA, map);
            if (count == 0) {
                data.put(MONGO_ID, new ObjectId(mongoId));
>>>>>>> 3a3e6b97d634600955aceb721bb4a7b29607ff62
                collection.insertOne(new Document(data));
                data.remove(MONGO_ID);
                data.remove(DB_VIEW_DATA);
                Document document = new Document(data);
<<<<<<< HEAD
                collectionLog.updateOne(Filters.eq(MONGO_ID, mongoId), new Document("$set", document));

=======
                provider.update(mongoId, document);
>>>>>>> 3a3e6b97d634600955aceb721bb4a7b29607ff62
            } else {
                data.remove(MONGO_ID);
                collection.updateOne(Filters.eq(MONGO_ID, new ObjectId(mongoId)), new Document("$set", new Document(data)));
                data.remove(DB_VIEW_DATA);
<<<<<<< HEAD
                collectionLog.updateOne(Filters.eq(MONGO_ID, mongoId), new Document("$set", new Document(data)));

            }

        }
=======
                provider.update(mongoId, new Document(data));
            }

        }catch (Exception e){
        }
        ;

    }
>>>>>>> 3a3e6b97d634600955aceb721bb4a7b29607ff62

    }
}
