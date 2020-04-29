package com.plantdata.kgcloud.domain.edit.service.impl;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.constant.DWFileConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import com.plantdata.kgcloud.domain.edit.entity.KnowledgeIndex;
import com.plantdata.kgcloud.domain.edit.entity.MultiModal;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import com.plantdata.kgcloud.domain.edit.req.file.IndexRelationReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.DWFileRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRelationRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author EYE
 */
@Service
public class EntityFileRelationServiceImpl implements EntityFileRelationService {

    @Autowired
    private BasicInfoService basicInfoService;
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private DocumentConverter documentConverter;

    @Autowired
    private UserClient userClient;



    public static List<LinkedHashMap<String, String>> readExcel(MultipartFile file, Integer indexType) throws IOException {
        // 返回的map
        LinkedHashMap<String, String> excelMap = new LinkedHashMap<>();

        // Excel列的样式，主要是为了解决Excel数字科学计数的问题
        CellStyle cellStyle;
        // 根据Excel构成的对象
        Workbook wb;
        String fileName = file.getOriginalFilename();
        // 如果是2007及以上版本，则使用想要的Workbook以及CellStyle
        if (fileName.substring(fileName.lastIndexOf(".") + 1).equals("xlsx")) {
            wb = new XSSFWorkbook(file.getInputStream());
            XSSFDataFormat dataFormat = (XSSFDataFormat) wb.createDataFormat();
            cellStyle = wb.createCellStyle();
            // 设置Excel列的样式为文本
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        } else {
            POIFSFileSystem fs = new POIFSFileSystem(file.getInputStream());
            wb = new HSSFWorkbook(fs);
            HSSFDataFormat dataFormat = (HSSFDataFormat) wb.createDataFormat();
            cellStyle = wb.createCellStyle();
            // 设置Excel列的样式为文本
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        }

        // 只查询第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        // 一个sheet表对于一个List
        List<LinkedHashMap<String, String>> list = new LinkedList<>();

        // 将第一行的列值作为json的key
        String[] cellNames;
        // 获取第一行
        Row fisrtRow = sheet.getRow(0);
        // 如果第一行就为空，则是空sheet表，该表跳过
        if (null == fisrtRow) {
            return null;
        }
        // 得到第一行有多少列
        int curCellNum = fisrtRow.getLastCellNum();
        // 得到总共有多少行
        int rowNum = sheet.getLastRowNum();
        // 根据第一行的列数来生成列头数组
        cellNames = new String[curCellNum];
        // 单独处理第一行，取出第一行的每个列值放在数组中，就得到了整张表的JSON的key
        for (int m = 0; m < curCellNum; m++) {
            Cell cell = fisrtRow.getCell(m);
            if (cell != null) {
                // 设置该列的样式是字符串
                cell.setCellStyle(cellStyle);
                cell.setCellType(CellType.STRING);
                // 取得该列的字符串值
                cellNames[m] = cell.getStringCellValue();
            }
        }
        List<String> name = Lists.newArrayList(cellNames);
        if (!name.contains("title") || (indexType == 1 && !name.contains("content")) || (indexType == 2 && !name.contains("url"))) {
            throw BizException.of(KgmsErrorCodeEnum.EXCEL_DATA_ERROR);
        }
        // 从第二行起遍历每一行
        for (int j = 1; j <= rowNum; j++) {
            // 一行数据对于一个Map
            LinkedHashMap<String, String> rowMap = new LinkedHashMap<>();
            // JSONObject rowMap = new JSONObject();
            // 取得某一行
            Row row = sheet.getRow(j);
            // 遍历每一列
            for (int k = 0; k < curCellNum; k++) {
                Cell cell = row.getCell(k);
                if (cell != null) {
                    cell.setCellStyle(cellStyle);
                    cell.setCellType(CellType.STRING);
                    // 保存该单元格的数据到该行中
                    rowMap.put(cellNames[k], cell.getStringCellValue());
                } else {
                    // 保存该单元格的数据到该行中
                    rowMap.put(cellNames[k], "");
                }
            }
            // 保存该行的数据到该表的List中
            list.add(rowMap);
        }
        return list;
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId()).getCollection(DWFileConstants.RELATION);
    }

    DWFileRsp convertToDWFileRsp(Document document, String kgName) {
        DWFileRsp dwFileRsp = DWFileRsp.builder().id(document.getObjectId("_id").toString())
                .title(document.getString("title")).indexType(document.getInteger("indexType"))
                .keyword(document.getString("keyword"))
                .description(document.getString("description")).url(document.getString("url")).build();
        List<EntityFileRelationRsp> list = convertToEntityFileRelationRspList((List<Document>) document.get("relationList"), kgName);
        dwFileRsp.setRelationList(list);
        return dwFileRsp;
    }

    List<EntityFileRelationRsp> convertToEntityFileRelationRspList(List<Document> documents, String kgName) {
        List<EntityFileRelationRsp> list = Lists.newArrayList();
        for (Document document : documents) {
            if (document.getString("kgName").equals(kgName)) {
                list.add(convertToEntityFileRelationRsp(document));
            }
        }
        return list;
    }

    EntityFileRelationRsp convertToEntityFileRelationRsp(Document document) {
        return EntityFileRelationRsp.builder().id(document.getObjectId("_id").toString())
                .kgName(document.getString("kgName")).entityId(document.getLong("entityId"))
                .dwFileId(document.getObjectId("dwFileId").toString())
                .indexType(document.getInteger("indexType"))
                .createTime(document.getDate("createTime")).build();
    }

    @Override
    public Page<DWFileRsp> listRelation(String kgName, EntityFileRelationQueryReq req) {

        int size = req.getSize();
        int pageNo = (req.getPage() - 1) * size;

        List<Bson> aggLs = new ArrayList<>();
        aggLs.add(Aggregates.skip(pageNo));
        aggLs.add(Aggregates.limit(size + 1));
        if (StringUtils.isNotBlank(req.getName())) {
            aggLs.add(Aggregates.match(Filters.regex("title", Pattern.compile("^.*" + req.getName() + ".*$"))));
        }

        MongoCursor<Document> iterator = null;
        if (req.getIndexType() == 1 || req.getIndexType() == 2) {
            aggLs.add(Aggregates.lookup(DWFileConstants.RELATION, "_id", "dwFileId", "relationList"));
            aggLs.add(Aggregates.match(Filters.eq("indexType", req.getIndexType())));
            MongoCollection<Document> collection = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId()).getCollection(DWFileConstants.INDEX);
            iterator = collection.aggregate(aggLs).iterator();
        } else if (req.getIndexType() == 0) {
            aggLs.add(Aggregates.lookup(DWFileConstants.RELATION, "_id", "dwFileId", "relationList"));
            aggLs.add(Aggregates.match(Filters.exists("relationList.0")));
            MongoCollection<Document> collection = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId()).getCollection(DWFileConstants.FILE);
            iterator = collection.aggregate(aggLs).iterator();
        }

        List<DWFileRsp> list = Lists.newArrayList();
        List<Long> entityIds = Lists.newArrayList();
        if (iterator != null) {
            iterator.forEachRemaining(s -> {
                DWFileRsp dwFileRsp = convertToDWFileRsp(s, kgName);
                if (dwFileRsp.getRelationList() != null) {
                    entityIds.addAll(dwFileRsp.getRelationList().stream().map(EntityFileRelationRsp::getEntityId).collect(Collectors.toList()));
                }
                list.add(dwFileRsp);
            });
        }
        HashSet<Long> h = new HashSet<>(entityIds);
        entityIds.clear();
        entityIds.addAll(h);

        Map<Long, String> nameMap = basicInfoService.listByIds(kgName, entityIds)
                .stream().collect(Collectors.toMap(BasicInfoRsp::getId, BasicInfoRsp::getName, (k1, k2) -> k1));

        for (DWFileRsp dwFileRsp : list) {
            List<EntityFileRelationRsp> relationList = dwFileRsp.getRelationList();
            for (EntityFileRelationRsp relationRsp : relationList) {
                relationRsp.setEntityName(nameMap.get(relationRsp.getEntityId()));
            }
        }

        int count = list.size();
        if (count > size) {
            list.remove(size);
            count += pageNo;
        }
        return new PageImpl<>(list, PageRequest.of(req.getPage() - 1, size), count);
    }

    @Override
    public void createRelation(String kgName, EntityFileRelationReq req) {
        EntityFileRelation relation = ConvertUtils.convert(EntityFileRelation.class).apply(req);
        relation.setKgName(kgName);
        relation.setCreateTime(new Date());
        Document document = documentConverter.toDocument(relation);
        document.put("dwFileId", new ObjectId(relation.getDwFileId()));
        getCollection().insertOne(document);
    }

    @Override
    public void deleteRelation(List<String> idList) {
        List<ObjectId> collect = idList.stream().map(ObjectId::new).collect(Collectors.toList());
        getCollection().deleteMany(Filters.in("_id", collect));
    }

    @Override
    public void deleteRelationByDwFileId(String dwFileId) {
        getCollection().deleteMany(Filters.eq("dwFileId", new ObjectId(dwFileId)));
    }

    @Override
    public void deleteById(String id) {
        getCollection().deleteOne(documentConverter.buildObjectId(id));
    }

    @Override
    public void deleteIndex(String kgName, List<String> idList) {
        List<ObjectId> collect = idList.stream().map(ObjectId::new).collect(Collectors.toList());
        MongoDatabase database = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId());
        database.getCollection(DWFileConstants.INDEX).deleteMany(Filters.in("_id", collect));
        deleteRelationByDwFileIds(collect);
    }

    public void deleteRelationByDwFileIds(List<ObjectId> dwFileIds) {
        getCollection().deleteMany(Filters.in("dwFileId", dwFileIds));
    }

    @Override
    public MultiModal getMultiModalById(String id) {
        String userId = SessionHolder.getUserId();
//        String userId = userClient.getCurrentUserDetail().getData().getId();
        MongoDatabase database = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + userId);
        Document document = database.getCollection(DWFileConstants.RELATION).find(Filters.eq("_id", new ObjectId(id))).first();
        if (document != null) {
            ObjectId dwFileId = document.getObjectId("dwFileId");
            Document file = database.getCollection(DWFileConstants.FILE).find(Filters.eq("_id", dwFileId)).first();
            MultiModal multiModal = documentConverter.toBean(file, MultiModal.class);
            multiModal.setEntityId(document.getLong("entityId"));
            return multiModal;
        }
        return null;
    }

    @Override
    public List<EntityFileRelationRsp> getRelationByDwFileId(String dwFileId) {
        MongoCursor<Document> cursor = getCollection().find(Filters.eq("dwFileId", new ObjectId(dwFileId))).iterator();
        List<EntityFileRelationRsp> list = Lists.newArrayList();
        if (cursor.hasNext()) {
            list.add(convertToEntityFileRelationRsp(cursor.next()));
        }
        return list;
    }

    @Override
    public List<EntityFileRsp> getRelationByKgNameAndEntityId(String kgName, Long entityId) {
        List<Bson> bsons = new ArrayList<>(3);
        bsons.add(Filters.eq("kgName", kgName));
        bsons.add(Filters.eq("entityId", entityId));
        bsons.add(Filters.eq("indexType", 0));
        MongoDatabase database = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId());
        MongoCursor<Document> cursor = database.getCollection(DWFileConstants.RELATION).find(Filters.and(bsons)).iterator();
        List<EntityFileRsp> list = Lists.newArrayList();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            ObjectId objectId = doc.getObjectId("dwFileId");
            Document document = database.getCollection(DWFileConstants.FILE).find(Filters.eq("_id", objectId)).first();
            if (document != null) {
                EntityFileRsp entityFileRsp = new EntityFileRsp();
                entityFileRsp.setId(doc.getObjectId("_id").toString());
                entityFileRsp.setEntityId(entityId);
                entityFileRsp.setName(document.getString("name"));
                entityFileRsp.setPath(document.getString("path"));
                entityFileRsp.setThumbPath(document.getString("thumbPath"));
                entityFileRsp.setType(document.getString("type"));
                list.add(entityFileRsp);
            }
        }
        return list;
    }

    @Override
    public List<EntityFileRsp> getRelationByKgNameAndEntityIdIn(String kgName, List<Long> entityIds, Integer type) {
        List<Bson> bsons = new ArrayList<>(2);
        bsons.add(Filters.in("entityId", entityIds));
        bsons.add(Filters.eq("kgName", kgName));
        if (type == 0) {
            bsons.add(Filters.eq("indexType", 0));
        } else if (type == 1) {
            bsons.add(Filters.in("indexType", Lists.newArrayList(1, 2)));
        }
        MongoDatabase database = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId());
        MongoCursor<Document> cursor = database.getCollection(DWFileConstants.RELATION).find(Filters.and(bsons)).iterator();
        List<EntityFileRsp> list = Lists.newArrayList();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            ObjectId objectId = doc.getObjectId("dwFileId");
            List<Bson> query = new ArrayList<>(2);
            query.add(Filters.eq("_id", objectId));
            Document document = null;
            if (type == 1) {
                document = database.getCollection(DWFileConstants.INDEX).find(Filters.and(query)).first();
            } else if (type == 0) {
                document = database.getCollection(DWFileConstants.FILE).find(Filters.and(query)).first();
            }
            if (document != null) {
                EntityFileRsp entityFileRsp = new EntityFileRsp();
                entityFileRsp.setId(doc.getObjectId("_id").toString());
                entityFileRsp.setEntityId(doc.getLong("entityId"));
                if (type == 0) {
                    entityFileRsp.setName(document.getString("name"));
                    entityFileRsp.setPath(document.getString("path"));
                    entityFileRsp.setThumbPath(document.getString("thumbPath"));
                    entityFileRsp.setType(document.getString("type"));
                } else if (type == 1) {
                    entityFileRsp.setTitle(document.getString("title"));
                    entityFileRsp.setKeyword(document.getString("keyword"));
                    entityFileRsp.setDescription(document.getString("description"));
                    entityFileRsp.setUrl(document.getString("url"));
                    entityFileRsp.setIndexType(document.getInteger("indexType"));
                }
                list.add(entityFileRsp);
            }
        }
        return list;
    }

    @Override
    public void addIndex(String kgName, Integer indexType, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw BizException.of(KgmsErrorCodeEnum.EXCEL_TYPE_ERROR);
        }
        String suffix = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        if (suffix.equals("xlsx") || suffix.equals("xls")) {
            List<LinkedHashMap<String, String>> dataList;
            try {
                dataList = readExcel(file, indexType);
            } catch (IOException e) {
                throw BizException.of(KgmsErrorCodeEnum.EXCEL_DATA_ERROR);
            }
            if (dataList != null) {
                List<Document> list = new ArrayList<>(dataList.size());
                for (LinkedHashMap<String, String> map : dataList) {
                    KnowledgeIndex knowledgeIndex = new KnowledgeIndex();
                    knowledgeIndex.setTitle(map.get("title"));
                    knowledgeIndex.setDescription(map.get("content"));
                    knowledgeIndex.setUrl(map.get("url"));
                    knowledgeIndex.setCreateTime(new Date());
                    knowledgeIndex.setKgName(kgName);
                    knowledgeIndex.setIndexType(indexType);
                    knowledgeIndex.setUserId(SessionHolder.getUserId());

                    Document document = documentConverter.toDocument(knowledgeIndex);
                    list.add(document);
                }
                MongoCollection<Document> collection = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId()).getCollection(DWFileConstants.INDEX);
                collection.insertMany(list);
            } else {
                throw BizException.of(KgmsErrorCodeEnum.EXCEL_DATA_NULL);
            }
        } else {
            throw BizException.of(KgmsErrorCodeEnum.EXCEL_TYPE_ERROR);
        }
    }

    @Override
    public void updateIndex(String kgName, IndexRelationReq req) {
        MongoCollection<Document> collection = getCollection();
        for (Long entityId : req.getEntityIds()) {
            if (!checkSize(kgName, entityId)) {
                throw BizException.of(KgmsErrorCodeEnum.FILE_SIZE_OVER);
            }
            if (!checkExist(kgName, entityId, req.getDwFileId())) {
                throw BizException.of(KgmsErrorCodeEnum.RELATION_IS_EXIST);
            }
            EntityFileRelation relation = new EntityFileRelation();
            relation.setKgName(kgName);
            relation.setCreateTime(new Date());
            relation.setEntityId(entityId);
            relation.setIndexType(req.getIndexType());
            Document document = documentConverter.toDocument(relation);
            document.put("dwFileId", new ObjectId(req.getDwFileId()));
            collection.insertOne(document);
        }
    }

    @Override
    public boolean checkExist(String kgName, Long entityId, String dwFileId) {
        List<Bson> query = new ArrayList<>(3);
        query.add(Filters.eq("kgName", kgName));
        query.add(Filters.eq("entityId", entityId));
        query.add(Filters.eq("dwFileId", new ObjectId(dwFileId)));
        Document first = getCollection().find(Filters.and(query)).first();
        return first == null;
    }

    @Override
    public boolean checkSize(String kgName, Long entityId) {
        MongoDatabase database = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId());
        List<Bson> query = new ArrayList<>(2);
        query.add(Filters.eq("kgName", kgName));
        query.add(Filters.eq("entityId", entityId));
        query.add(Filters.eq("indexType", 0));
        MongoCursor<Document> iterator = database.getCollection(DWFileConstants.RELATION).find(Filters.and(query)).iterator();
        Set<String> dwFileIdList = new HashSet<>();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            dwFileIdList.add(document.getObjectId("dwFileId").toString());
        }

        long size = 0L;
        MongoCursor<Document> fileIterator = database.getCollection(DWFileConstants.FILE).find(Filters.in("_id", dwFileIdList)).iterator();
        while (fileIterator.hasNext()) {
            Document document = fileIterator.next();
            Long fileSize = document.getLong("fileSize");
            if (fileSize != null) {
                size += fileSize;
            }
        }
        return size <= 20971520L;
    }

}
