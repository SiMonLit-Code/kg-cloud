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
import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelationScore;
import com.plantdata.kgcloud.domain.edit.entity.MultiModal;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationQueryReq;
import com.plantdata.kgcloud.domain.edit.req.file.EntityFileRelationReq;
import com.plantdata.kgcloud.domain.edit.req.file.IndexRelationReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.EntityFileRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import com.plantdata.kgcloud.domain.file.entity.FileData;
import com.plantdata.kgcloud.domain.file.service.FileDataService;
import com.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.EntityFileRelationAddReq;
import com.plantdata.kgcloud.sdk.rsp.edit.EntityFileRelationRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.EntityInfoRsp;
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
    @Autowired
    private FileDataService fileDataService;
    @Autowired
    private GraphRepository graphRepository;


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
        String kgDbName = graphRepository.findByKgNameAndUserId(kgName, SessionHolder.getUserId()).getDbName();
        return mongoClient.getDatabase(kgDbName).getCollection(FileConstants.FILE + FileConstants.JOIN + FileConstants.RELATION);
    }

    private MongoCollection<Document> getFileCollection() {
        return mongoClient.getDatabase(FileConstants.ENTITY_FILE_PREFIX + SessionHolder.getUserId()).getCollection(FileConstants.FILE);
    }

    private MongoDatabase getDatabase() {
        return mongoClient.getDatabase(FileConstants.ENTITY_FILE_PREFIX + SessionHolder.getUserId());
    }

    @Override
    public Page<EntityFileRelationRsp> listRelation(String kgName, EntityFileRelationQueryReq req) {

        int size = req.getSize();
        int pageNo = (req.getPage() - 1) * size;

        List<Bson> query = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getName())) {
            query.add(Filters.regex("title", Pattern.compile("^.*" + req.getName() + ".*$")));
        }
        if (req.getIndexType() != null) {
            query.add(Filters.eq("indexType", req.getIndexType()));
        }
        if (req.getIsRelatedEntity() != null && req.getIsRelatedEntity() != 0) {
            query.add(Filters.exists("entityAnnotation.0", req.getIsRelatedEntity().equals(1)));
        }

        FindIterable<Document> findIterable = null;
        if (CollectionUtils.isEmpty(query)) {
            findIterable = getRelationCollection(kgName).find().skip(pageNo).limit(size + 1);
        } else {
            findIterable = getRelationCollection(kgName).find(Filters.and(query)).skip(pageNo).limit(size + 1);
        }

        List<EntityFileRelation> relations = entityFileConverter.toBeans(findIterable);

        List<EntityFileRelationRsp> list = Lists.newArrayList();
        HashSet<Long> entityIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(relations)) {
            for (EntityFileRelation relation : relations) {
                EntityFileRelationRsp entityFileRelationRsp = ConvertUtils.convert(EntityFileRelationRsp.class).apply(relation);

                List<EntityFileRelationScore> entityAnnotation = relation.getEntityAnnotation();
                // 保存实体Ids
                if (!CollectionUtils.isEmpty(entityAnnotation)) {
                    List<Long> ids = entityAnnotation.stream().map(EntityFileRelationScore::getEntityId)
                            .collect(Collectors.toList());
                    entityIds.addAll(ids);
                    List<EntityInfoRsp> entityInfoList = ids.stream()
                            .map(s -> EntityInfoRsp.builder().entityId(s).build()).collect(Collectors.toList());
                    entityFileRelationRsp.setEntityInfoList(entityInfoList);
                }
                list.add(entityFileRelationRsp);
            }
        }

        Map<Long, String> nameMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(entityIds)) {
            nameMap = basicInfoService.listByIds(kgName, Lists.newArrayList(entityIds))
                    .stream().collect(Collectors.toMap(BasicInfoRsp::getId, BasicInfoRsp::getName, (k1, k2) -> k1));
        }

        List<Long> deleteIds = Lists.newArrayList();
        for (EntityFileRelationRsp entityFileRelationRsp : list) {
            List<EntityInfoRsp> entityInfoList = entityFileRelationRsp.getEntityInfoList();
            if (CollectionUtils.isEmpty(entityInfoList)) {
                entityFileRelationRsp.setIsRelatedEntity(2);
                continue;
            }
            for (int j = 0; j < entityInfoList.size(); j++) {
                EntityInfoRsp relationRsp = entityInfoList.get(j);
                // 移除图谱中不存在的实体的标引关系
                if (StringUtils.isBlank(nameMap.get(relationRsp.getEntityId()))) {
                    entityInfoList.remove(j);
                    j--;
                    deleteIds.add(relationRsp.getEntityId());
                } else {
                    relationRsp.setEntityName(nameMap.get(relationRsp.getEntityId()));
                }
            }
            // 设置是否存在实体关联状态
            if (CollectionUtils.isEmpty(entityInfoList)) {
                entityFileRelationRsp.setIsRelatedEntity(2);
            } else {
                entityFileRelationRsp.setIsRelatedEntity(1);
            }
        }
        if (!CollectionUtils.isEmpty(deleteIds)) {
            deleteByEntityIds(kgName, deleteIds);
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
        EntityFileRelation exist = checkFileExist(kgName, req.getFileId());
        // 关系不存在，新建关系
        if (exist == null) {
            EntityFileRelation relation = ConvertUtils.convert(EntityFileRelation.class).apply(req);
            relation.setCreateTime(new Date());
            Long entityId = req.getEntityId();
            if (entityId != null) {
                relation.setEntityAnnotation(Lists.newArrayList(new EntityFileRelationScore(entityId, 1d, 0)));
            }
            Document document = entityFileConverter.toDocument(relation);
            getRelationCollection(kgName).insertOne(document);
        } else {// 关系已存在，添加实体ID
            List<EntityFileRelationScore> entityAnnotation = exist.getEntityAnnotation();
            if (req.getEntityId() != null) {
                if (entityAnnotation == null) {
                    entityAnnotation = Lists.newArrayList(new EntityFileRelationScore(req.getEntityId(), 1d, 0));
                } else {
                    List<Long> entityIds = entityAnnotation.stream().map(EntityFileRelationScore::getEntityId)
                            .collect(Collectors.toList());
                    if (!entityIds.contains(req.getEntityId())) {
                        entityAnnotation.add(new EntityFileRelationScore(req.getEntityId(), 1d, 0));
                    }
                }
                exist.setEntityAnnotation(entityAnnotation);
                String id = exist.getId();
                exist.setId(null);
                Document document = entityFileConverter.toDocument(exist);
                getRelationCollection(kgName).updateOne(documentConverter.buildObjectId(id), new Document("$set", document));
            }
        }
    }

    @Override
    public void deleteRelationByFileId(String fileId) {
        Document document = getFileCollection().find(entityFileConverter.buildObjectId(fileId)).first();
        FileData fileData = documentConverter.toBean(document, FileData.class);
        if (fileData == null) {
            return;
        }
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
        getRelationCollection(kgName).deleteMany(Filters.in("entityAnnotation.entityId", entityIds));
    }


    @Override
    public MultiModal deleteMultiModalById(String kgName, String id, Long entityId) {
        Document document = getRelationCollection(kgName).find(entityFileConverter.buildObjectId(id)).first();
        EntityFileRelation relation = entityFileConverter.toBean(document);
        if (relation != null) {
            // 删除当前关联的实体
            List<EntityFileRelationScore> entityAnnotation = relation.getEntityAnnotation();
            List<EntityFileRelationScore> collect = entityAnnotation.stream().filter(s -> !entityId.equals(s.getEntityId()))
                    .collect(Collectors.toList());
            relation.setEntityAnnotation(collect);
            relation.setId(null);
            getRelationCollection(kgName).updateOne(documentConverter.buildObjectId(id), new Document("$set", entityFileConverter.toDocument(relation)));

            Document file = getFileCollection().find(documentConverter.buildObjectId(relation.getFileId())).first();
            FileData fileData = documentConverter.toBean(file, FileData.class);
            MultiModal multiModal = ConvertUtils.convert(MultiModal.class).apply(fileData);
            multiModal.setDataHref(fileData.getPath());
            multiModal.setEntityId(entityId);
            return multiModal;
        }
        return null;
    }

    @Override
    public EntityFileRelation getRelationByFileId(String kgName, String fileId) {
        Document document = getRelationCollection(kgName).find(Filters.eq("fileId", new ObjectId(fileId))).first();
        return entityFileConverter.toBean(document);
    }

    @Override
    public List<EntityFileRsp> getRelationByKgNameAndEntityId(String kgName, Long entityId) {
        List<Bson> bsons = new ArrayList<>(2);
        bsons.add(Filters.in("entityAnnotation.entityId", entityId));
        bsons.add(Filters.eq("indexType", 0));
        MongoCursor<Document> cursor = getRelationCollection(kgName).find(Filters.and(bsons)).iterator();
        List<EntityFileRsp> list = Lists.newArrayList();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            ObjectId objectId = doc.getObjectId("fileId");
            Document document = getFileCollection().find(Filters.eq("_id", objectId)).first();
            FileData fileData = documentConverter.toBean(document, FileData.class);
            if (fileData != null) {
                EntityFileRsp entityFileRsp = ConvertUtils.convert(EntityFileRsp.class).apply(fileData);
                entityFileRsp.setId(doc.getObjectId("_id").toString());
                entityFileRsp.setEntityId(entityId);
                entityFileRsp.setIndexType(0);
                list.add(entityFileRsp);
            }
        }
        return list;
    }

    @Override
    public List<EntityFileRsp> getRelationByKgNameAndEntityIdIn(String kgName, List<Long> entityIds, Integer type) {
        List<Bson> bsons = new ArrayList<>(2);
        bsons.add(Filters.in("entityAnnotation.entityId", entityIds));
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
            List<Long> ids = relation.getEntityAnnotation().stream().map(EntityFileRelationScore::getEntityId)
                    .collect(Collectors.toList());
            for (Long entityId : entityIds) {
                if (ids.contains(entityId)) {
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
                    relation.setEntityAnnotation(Lists.newArrayList());

                    Document document = entityFileConverter.toDocument(relation);
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

        Document document = getRelationCollection(kgName).find(entityFileConverter.buildObjectId(req.getRelationId())).first();
        if (document == null) {
            return;
        }
        EntityFileRelation relation = entityFileConverter.toBean(document);

        if (relation.getIndexType().equals(0)) {
            FileData fileData = fileDataService.get(relation.getFileId());
            List<String> kgNames = fileData.getKgNames();
            if (kgNames == null) {
                kgNames = Lists.newArrayList(kgName);
            } else if (!kgNames.contains(kgName)) {
                kgNames.add(kgName);
            }
            fileData.setKgNames(kgNames);
            // 添加关系的图谱名称
            fileDataService.update(fileData);
        }

        // 实体文件关系中关联的图谱实体ID
        List<EntityFileRelationScore> entityAnnotation = relation.getEntityAnnotation();
        if (entityAnnotation == null) {
            entityAnnotation = Lists.newArrayList();
        }
        List<Long> entityIds = entityAnnotation.stream().map(EntityFileRelationScore::getEntityId)
                .collect(Collectors.toList());

        for (Long entityId : req.getEntityIds()) {
            if (entityIds.contains(entityId)) {
                continue;
                // throw BizException.of(KgmsErrorCodeEnum.RELATION_IS_EXIST);
            }
            entityAnnotation.add(new EntityFileRelationScore(entityId, 1d, 0));
        }
        relation.setEntityAnnotation(entityAnnotation);
        String id = relation.getId();
        relation.setId(null);

        // 更新关联实体ID
        List<Document> documents = documentConverter.toDocuments(entityAnnotation);
        Document newDocument = new Document("entityAnnotation", documents);

        getRelationCollection(kgName).updateOne(entityFileConverter.buildObjectId(id), new Document("$set", newDocument));
    }

    @Override
    public void cancelIndex(String kgName, IndexRelationReq req) {
        Document document = getRelationCollection(kgName).find(entityFileConverter.buildObjectId(req.getRelationId())).first();
        if (document == null) {
            return;
        }
        EntityFileRelation relation = entityFileConverter.toBean(document);
        List<EntityFileRelationScore> entityAnnotation = relation.getEntityAnnotation();
        if (CollectionUtils.isEmpty(entityAnnotation)) {
            return;
        }
        List<EntityFileRelationScore> collect = entityAnnotation.stream().filter(s -> !req.getEntityIds().contains(s.getEntityId()))
                .collect(Collectors.toList());

        // 更新关联实体ID
        List<Document> documents = documentConverter.toDocuments(collect);
        Document newDocument = new Document("entityAnnotation", documents);

        getRelationCollection(kgName).updateOne(entityFileConverter.buildObjectId(relation.getId()), new Document("$set", newDocument));
    }

    @Override
    public void addFile(String kgName, Long fileSystemId, Long folderId) {
        List<Bson> query = new ArrayList<>(2);
        query.add(Filters.eq("fileSystemId", fileSystemId));
        query.add(Filters.eq("folderId", folderId));

        FindIterable<Document> findIterable = getFileCollection().find(Filters.and(query));
        List<FileData> fileDatas = documentConverter.toBeans(findIterable, FileData.class);
        if (CollectionUtils.isEmpty(fileDatas)) {
            return;
        }

        // 查询该文件是否已存在标引记录中
        List<ObjectId> ids = fileDatas.stream().map(s -> new ObjectId(s.getId())).collect(Collectors.toList());
        MongoCursor<Document> cursor = getRelationCollection(kgName).find(Filters.in("fileId", ids)).iterator();
        List<String> deleteIds = Lists.newArrayList();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String id = document.getObjectId("fileId").toString();
            deleteIds.add(id);
        }

        // 移除不需要添加的文件
        List<FileData> newFileDatas = fileDatas.stream()
                .filter(s -> !deleteIds.contains(s.getId())).collect(Collectors.toList());

        List<Document> collect = Lists.newArrayList();
        for (FileData fileData : newFileDatas) {
            EntityFileRelation relation = ConvertUtils.convert(EntityFileRelation.class).apply(fileData);
            relation.setId(null);
            relation.setFileId(fileData.getId());
            relation.setIndexType(0);
            relation.setCreateTime(new Date());
            collect.add(entityFileConverter.toDocument(relation));
        }

        if (!CollectionUtils.isEmpty(collect)) {
            getRelationCollection(kgName).insertMany(collect);
        }
    }

    @Override
    public EntityFileRelationRsp createRelation(String kgName, EntityFileRelationAddReq req) {
        // 关联的实体信息
        List<EntityFileRelationScore> collect = req.getEntityIds().stream()
                .map(s -> new EntityFileRelationScore(s, 1d, 0))
                .collect(Collectors.toList());
        if (req.getIndexType() == 0) {
            // 文件标引
            EntityFileRelation relation = getRelationByFileId(kgName, req.getFileId());
            if (relation == null) {
                if (StringUtils.isBlank(req.getFileId())) {
                    throw BizException.of(KgmsErrorCodeEnum.INDEX_FILE_ID_IS_NULL);
                }
                FileData fileData = fileDataService.get(req.getFileId());
                EntityFileRelation newRelation = ConvertUtils.convert(EntityFileRelation.class).apply(fileData);
                newRelation.setId(null);
                newRelation.setIndexType(0);
                newRelation.setFileId(req.getFileId());
                newRelation.setCreateTime(new Date());
                newRelation.setEntityAnnotation(collect);
                Document document = entityFileConverter.toDocument(newRelation);
                getRelationCollection(kgName).insertOne(document);
                return ConvertUtils.convert(EntityFileRelationRsp.class).apply(entityFileConverter.toBean(document));
            }
            List<EntityFileRelationScore> entityAnnotation = relation.getEntityAnnotation();

            if (req.getEntityIds() != null) {
                if (entityAnnotation == null) {
                    entityAnnotation = collect;
                } else {
                    // 已存在的实体id
                    List<Long> ids = entityAnnotation.stream().map(EntityFileRelationScore::getEntityId)
                            .collect(Collectors.toList());
                    // 添加的实体id
                    List<Long> entityIds = req.getEntityIds();
                    // 最终要添加的信息
                    List<EntityFileRelationScore> list = entityIds.stream().filter(s -> !ids.contains(s))
                            .map(s -> new EntityFileRelationScore(s, 1d, 0))
                            .collect(Collectors.toList());
                    entityAnnotation.addAll(list);
                }
                relation.setEntityAnnotation(entityAnnotation);
                String id = relation.getId();
                relation.setId(null);
                Document document = entityFileConverter.toDocument(relation);
                getRelationCollection(kgName).updateOne(documentConverter.buildObjectId(id), new Document("$set", document));
                return ConvertUtils.convert(EntityFileRelationRsp.class).apply(entityFileConverter.toBean(document));
            }
        } else if (req.getIndexType() == 1) {
            // 文本标引
            EntityFileRelation relation = ConvertUtils.convert(EntityFileRelation.class).apply(req);
            // 关联的实体信息
            relation.setEntityAnnotation(collect);
            relation.setIndexType(1);
            relation.setCreateTime(new Date());
            if (StringUtils.isBlank(relation.getTitle()) || StringUtils.isBlank(relation.getDescription())) {
                throw BizException.of(KgmsErrorCodeEnum.INDEX_TITLE_DESCRIPTION_IS_NULL);
            }
            Document document = entityFileConverter.toDocument(relation);
            getRelationCollection(kgName).insertOne(document);
            return ConvertUtils.convert(EntityFileRelationRsp.class).apply(entityFileConverter.toBean(document));
        } else if (req.getIndexType() == 2) {
            // 链接标引
            EntityFileRelation relation = ConvertUtils.convert(EntityFileRelation.class).apply(req);
            relation.setEntityAnnotation(collect);
            relation.setIndexType(2);
            relation.setCreateTime(new Date());
            if (StringUtils.isBlank(relation.getTitle()) || StringUtils.isBlank(relation.getUrl())) {
                throw BizException.of(KgmsErrorCodeEnum.INDEX_TITLE_URL_IS_NULL);
            }
            Document document = entityFileConverter.toDocument(relation);
            getRelationCollection(kgName).insertOne(document);
            return ConvertUtils.convert(EntityFileRelationRsp.class).apply(entityFileConverter.toBean(document));
        }
        return new EntityFileRelationRsp();
    }

    @Override
    public boolean checkExist(String kgName, Long entityId, String fileId) {
        Document first = getRelationCollection(kgName).find(Filters.eq("fileId", new ObjectId(fileId))).first();
        if (first != null) {
            EntityFileRelation relation = entityFileConverter.toBean(first);
            List<EntityFileRelationScore> entityAnnotation = relation.getEntityAnnotation();
            if (CollectionUtils.isEmpty(entityAnnotation)) {
                return false;
            }
            List<Long> entityIds = entityAnnotation.stream().map(EntityFileRelationScore::getEntityId)
                    .collect(Collectors.toList());
            return !CollectionUtils.isEmpty(entityIds) && entityIds.contains(entityId);
        }
        return false;
    }

    @Override
    public EntityFileRelation checkFileExist(String kgName, String fileId) {
        Document document = getRelationCollection(kgName).find(Filters.eq("fileId", new ObjectId(fileId))).first();
        return entityFileConverter.toBean(document);
    }

    @Override
    public boolean checkSize(String kgName, Long entityId) {
        List<Bson> query = new ArrayList<>(2);
        query.add(Filters.in("entityAnnotation.entityId", entityId));
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
