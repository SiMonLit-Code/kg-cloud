package com.plantdata.kgcloud.domain.edit.service.impl;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.constant.FileConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import com.plantdata.kgcloud.domain.edit.converter.EntityFileConverter;
import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
import com.plantdata.kgcloud.domain.edit.entity.MultiModal;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import com.plantdata.kgcloud.domain.edit.req.file.IndexRelationReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRelationRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityInfoRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import com.plantdata.kgcloud.domain.file.entity.FileData;
import com.plantdata.kgcloud.exception.BizException;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lp
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
    private EntityFileConverter entityFileConverter;
    // @Autowired
    // private TableDataService tableDataService;

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

    private MongoCollection<Document> getRelationCollection(String kgName) {
        return mongoClient.getDatabase(FileConstants.DW_PREFIX +
                SessionHolder.getUserId()).getCollection(FileConstants.RELATION + "_" + kgName);
    }

    private MongoCollection<Document> getFileCollection() {
        return mongoClient.getDatabase(FileConstants.DW_PREFIX + SessionHolder.getUserId()).getCollection(FileConstants.FILE);
    }

    private MongoDatabase getDatabase() {
        return mongoClient.getDatabase(FileConstants.DW_PREFIX + SessionHolder.getUserId());
    }

    @Override
    public Page<EntityFileRelationRsp> listRelation(String kgName, EntityFileRelationQueryReq req) {

        int size = req.getSize();
        int pageNo = (req.getPage() - 1) * size;

        List<Bson> query = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getName())) {
            query.add(Filters.regex("title", Pattern.compile("^.*" + req.getName() + ".*$")));
        }
        query.add(Filters.eq("indexType", req.getIndexType()));

        FindIterable<Document> findIterable = getRelationCollection(kgName).find(Filters.and(query)).skip(pageNo).limit(size + 1);
        List<EntityFileRelation> relations = entityFileConverter.toBeans(findIterable);

        List<EntityFileRelationRsp> list = Lists.newArrayList();
        HashSet<Long> entityIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(relations)) {
            for (EntityFileRelation relation : relations) {
                EntityFileRelationRsp entityFileRelationRsp = ConvertUtils.convert(EntityFileRelationRsp.class).apply(relation);
                List<Long> ids = relation.getEntityIds();
                // 保存实体Ids
                if (!CollectionUtils.isEmpty(ids)) {
                    entityIds.addAll(ids);
                    List<EntityInfoRsp> entityInfoList = ids.stream()
                            .map(s -> EntityInfoRsp.builder().entityId(s).build()).collect(Collectors.toList());
                    entityFileRelationRsp.setEntityInfoList(entityInfoList);
                }
                list.add(entityFileRelationRsp);
            }
        }

        Map<Long, String> nameMap = basicInfoService.listByIds(kgName, Lists.newArrayList(entityIds))
                .stream().collect(Collectors.toMap(BasicInfoRsp::getId, BasicInfoRsp::getName, (k1, k2) -> k1));

        List<Long> deleteIds = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            EntityFileRelationRsp entityFileRelationRsp = list.get(i);
            List<EntityInfoRsp> entityInfoList = entityFileRelationRsp.getEntityInfoList();
            for (int j = 0; j < entityInfoList.size(); j++) {
                EntityInfoRsp relationRsp = entityInfoList.get(j);
                if (StringUtils.isBlank(nameMap.get(relationRsp.getEntityId()))) {
                    entityInfoList.remove(j);
                    j--;
                    deleteIds.add(relationRsp.getEntityId());
                } else {
                    relationRsp.setEntityName(nameMap.get(relationRsp.getEntityId()));
                }
            }
            if (entityInfoList.size() == 0 && req.getIndexType().equals(0)) {
                list.remove(i);
                i--;
            }
        }
        deleteByEntityIds(kgName, deleteIds);

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
        relation.setCreateTime(new Date());
        Document document = entityFileConverter.toDocument(relation);
        getRelationCollection(kgName).insertOne(document);
    }

    @Override
    public void deleteRelationByFileId(String fileId) {
        Document document = getFileCollection().find(entityFileConverter.buildObjectId(fileId)).first();
        FileData fileData = documentConverter.toBean(document, FileData.class);
        List<String> kgNames = fileData.getKgNames();
        if (CollectionUtils.isEmpty(kgNames)) {
            return;
        }
        for (String kgName : kgNames) {
            getRelationCollection(kgName).deleteMany(Filters.eq("fileId", new ObjectId(fileId)));
        }
    }

    @Override
    public void deleteRelationByFileIds(List<String> fileIds) {
        for (String fileId : fileIds) {
            deleteRelationByFileId(fileId);
        }
    }

    @Override
    public void deleteIndexById(String kgName, String id) {
        getRelationCollection(kgName).deleteOne(entityFileConverter.buildObjectId(id));
    }

    @Override
    public void deleteIndexByIds(String kgName, List<String> idList) {
        List<ObjectId> collect = idList.stream().map(ObjectId::new).collect(Collectors.toList());
        getRelationCollection(kgName).deleteMany(Filters.in("_id", collect));
    }

    @Override
    public void deleteByEntityIds(String kgName, List<Long> entityIds) {
        getRelationCollection(kgName).deleteMany(Filters.in("entityId", entityIds));
    }


    @Override
    public MultiModal getMultiModalById(String kgName, String id) {
        Document document = getRelationCollection(kgName).find(entityFileConverter.buildObjectId(id)).first();
        if (document != null) {
            ObjectId fileId = document.getObjectId("fileId");
            Document file = getFileCollection().find(Filters.eq("_id", fileId)).first();
            MultiModal multiModal = documentConverter.toBean(file, MultiModal.class);
            multiModal.setEntityId(document.getLong("entityId"));
            return multiModal;
        }
        return null;
    }

    @Override
    public List<EntityFileRelation> getRelationByDwFileId(String kgName, String fileId) {
        FindIterable<Document> findIterable = getRelationCollection(kgName).find(Filters.eq("fileId", new ObjectId(fileId)));
        return entityFileConverter.toBeans(findIterable);
    }

    @Override
    public List<EntityFileRsp> getRelationByKgNameAndEntityId(String kgName, Long entityId) {
        List<Bson> bsons = new ArrayList<>(2);
        bsons.add(Filters.in("entityId", entityId));
        bsons.add(Filters.eq("indexType", 0));
        MongoCursor<Document> cursor = getRelationCollection(kgName).find(Filters.and(bsons)).iterator();
        List<EntityFileRsp> list = Lists.newArrayList();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            ObjectId objectId = doc.getObjectId("fileId");
            Document document = getFileCollection().find(Filters.eq("_id", objectId)).first();
            // DWFileTable dwFileTable = documentConverter.toBean(document, DWFileTable.clss);
            // if (dwFileTable != null) {
            //     EntityFileRsp entityFileRsp = ConvertUtils.convert(EntityFileRsp.class).apply(dwFileTable);
            //     entityFileRsp.setEntityId(entityId);
            //     list.add(entityFileRsp);
            // }
        }
        return list;
    }

    @Override
    public List<EntityFileRsp> getRelationByKgNameAndEntityIdIn(String kgName, List<Long> entityIds, Integer type) {
        List<Bson> bsons = new ArrayList<>(2);
        bsons.add(Filters.in("entityId", entityIds));
        if (type == 0) {
            bsons.add(Filters.eq("indexType", 0));
        } else if (type == 1) {
            bsons.add(Filters.in("indexType", Lists.newArrayList(1, 2)));
        }
        FindIterable<Document> findIterable = getRelationCollection(kgName).find(Filters.and(bsons));
        List<EntityFileRelation> relations = entityFileConverter.toBeans(findIterable);
        List<EntityFileRsp> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(relations)) {
            return list;
        }
        for (EntityFileRelation relation : relations) {
            List<Long> ids = relation.getEntityIds();
            for (Long entityId : entityIds) {
                if (ids != null && ids.contains(entityId)) {
                    EntityFileRsp entityFileRsp = ConvertUtils.convert(EntityFileRsp.class).apply(relation);
                    entityFileRsp.setEntityId(entityId);
                    if (type == 0) {
                        List<Bson> query = new ArrayList<>(1);
                        query.add(Filters.eq("_id", new ObjectId(relation.getFileId())));
                        Document document = getFileCollection().find(Filters.and(query)).first();
                        if (document != null) {
                            entityFileRsp.setName(document.getString("name"));
                            entityFileRsp.setPath(document.getString("path"));
                            entityFileRsp.setThumbPath(document.getString("thumbPath"));
                            entityFileRsp.setType(document.getString("type"));
                        }
                    }
                    list.add(entityFileRsp);
                }
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
                    EntityFileRelation relation = new EntityFileRelation();
                    relation.setIndexType(indexType);
                    relation.setTitle(map.get("title"));
                    relation.setDescription(map.get("content"));
                    relation.setUrl(map.get("url"));
                    relation.setCreateTime(new Date());
                    relation.setEntityIds(Lists.newArrayList());

                    Document document = documentConverter.toDocument(relation);
                    list.add(document);
                }
                getRelationCollection(kgName).insertMany(list);
            } else {
                throw BizException.of(KgmsErrorCodeEnum.EXCEL_DATA_NULL);
            }
        } else {
            throw BizException.of(KgmsErrorCodeEnum.EXCEL_TYPE_ERROR);
        }
    }

    @Override
    public void updateIndex(String kgName, IndexRelationReq req) {

        Document document = getRelationCollection(kgName).find(Filters.eq(entityFileConverter.buildObjectId(req.getRelationId()))).first();
        if (document == null) {
            return;
        }
        EntityFileRelation relation = entityFileConverter.toBean(document);

        // if (relation.getIndexType().equals(0)) {
        //     DWFileTable file = tableDataService.getOneFile(relation.getDwFileId());
        //     List<String> kgNames = file.getKgNames();
        //     if (kgNames == null) {
        //         kgNames = Lists.newArrayList(kgName);
        //     } else if (!kgNames.contains(kgName)) {
        //         kgNames.add(kgName);
        //     }
        //     file.setKgNames(kgNames);
        //     // 添加关系的图谱名称
        //     tableDataService.update(file);
        // }

        // 实体文件关系中关联的图谱实体ID
        List<Long> entityIds = relation.getEntityIds();
        if (entityIds == null) {
            entityIds = Lists.newArrayList();
        }

        for (Long entityId : req.getEntityIds()) {
            if (entityIds.contains(entityId)) {
                throw BizException.of(KgmsErrorCodeEnum.RELATION_IS_EXIST);
            }
            entityIds.add(entityId);
        }

        // 更新关联实体ID
        Document newDocument = new Document("entityIds", entityIds);

        getRelationCollection(kgName).updateOne(entityFileConverter.buildObjectId(relation.getId()), new Document("$set", newDocument));
    }

    @Override
    public void cancelIndex(String kgName, IndexRelationReq req) {
        Document document = getRelationCollection(kgName).find(entityFileConverter.buildObjectId(req.getRelationId())).first();
        if (document == null) {
            return;
        }
        EntityFileRelation relation = entityFileConverter.toBean(document);
        List<Long> entityIds = relation.getEntityIds();
        if (CollectionUtils.isEmpty(entityIds)) {
            return;
        }
        for (Long entityId : req.getEntityIds()) {
            entityIds.remove(entityId);
        }

        // 更新关联实体ID
        Document newDocument = new Document("entityIds", entityIds);

        getRelationCollection(kgName).updateOne(entityFileConverter.buildObjectId(relation.getId()), new Document("$set", newDocument));
    }

    @Override
    public void addFile(String kgName, Long fileSystemId, Long folderId) {
        List<Bson> query = new ArrayList<>(2);
        query.add(Filters.eq("fileSystemId", fileSystemId));
        query.add(Filters.eq("folderId", folderId));

        MongoCursor<Document> iterator = getFileCollection().find(Filters.and(query)).iterator();

        List<Document> collect = Lists.newArrayList();
        while (iterator.hasNext()) {
            Document doc = iterator.next();
            String id = doc.getObjectId("_id").toString();
            EntityFileRelation relation = new EntityFileRelation();
            relation.setFileId(id);
            relation.setIndexType(0);
            relation.setCreateTime(new Date());
            collect.add(entityFileConverter.toDocument(relation));
        }

        getRelationCollection(kgName).insertMany(collect);
    }

    @Override
    public boolean checkExist(String kgName, Long entityId, String fileId) {
        Document first = getRelationCollection(kgName).find(Filters.eq("fileId", new ObjectId(fileId))).first();
        if (first != null) {
            EntityFileRelation relation = entityFileConverter.toBean(first);
            List<Long> entityIds = relation.getEntityIds();
            return !CollectionUtils.isEmpty(entityIds) && entityIds.contains(entityId);
        }
        return false;
    }

    @Override
    public boolean checkSize(String kgName, Long entityId) {
        List<Bson> query = new ArrayList<>(2);
        query.add(Filters.in("entityId", entityId));
        query.add(Filters.eq("indexType", 0));
        MongoCursor<Document> relationIterator = getRelationCollection(kgName).find(Filters.and(query)).iterator();
        Set<ObjectId> fileIdList = new HashSet<>();
        while (relationIterator.hasNext()) {
            Document document = relationIterator.next();
            fileIdList.add(document.getObjectId("fileId"));
        }

        long size = 0L;
        MongoCursor<Document> fileIterator = getFileCollection().find(Filters.in("_id", fileIdList)).iterator();
        while (fileIterator.hasNext()) {
            Document document = fileIterator.next();
            Long fileSize = document.getLong("fileSize");
            if (fileSize != null) {
                size += fileSize;
            }
        }
        return size > 20971520L;
    }

}
