package com.plantdata.kgcloud.domain.edit.service.impl;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.constant.DWFileConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.entity.DWFileTable;
import com.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import com.plantdata.kgcloud.domain.edit.entity.EntityFileRelation;
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

    public static List<LinkedHashMap<String, String>> readExcel(MultipartFile file) throws IOException {
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
            // 设置该列的样式是字符串
            cell.setCellStyle(cellStyle);
            cell.setCellType(CellType.STRING);
            // 取得该列的字符串值
            cellNames[m] = cell.getStringCellValue();
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

    DWFileRsp convertToDWFileRsp(Document document) {
        DWFileRsp dwFileRsp = DWFileRsp.builder().id(document.getObjectId("_id").toString())
                .title(document.getString("title")).indexType(document.getInteger("indexType"))
                .keyword(document.getString("keyword"))
                .description(document.getString("description")).url("url").build();
        List<EntityFileRelationRsp> list = convertToEntityFileRelationRspList((List<Document>) document.get("relationList"));
        dwFileRsp.setRelationList(list);
        return dwFileRsp;
    }

    List<EntityFileRelationRsp> convertToEntityFileRelationRspList(List<Document> documents) {
        List<EntityFileRelationRsp> list = Lists.newArrayList();
        for (Document document : documents) {
            EntityFileRelationRsp relationRsp = EntityFileRelationRsp.builder().id(document.getObjectId("_id").toString())
                    .kgName(document.getString("kgName")).entityId(document.getLong("entityId"))
                    .dwFileId(document.getObjectId("dwFileId").toString()).createTime(document.getDate("createTime")).build();
            list.add(relationRsp);
        }
        return list;
    }

    EntityFileRelationRsp convertToEntityFileRelationRsp(Document document) {
        return EntityFileRelationRsp.builder().id(document.getObjectId("_id").toString())
                .kgName(document.getString("kgName")).entityId(document.getLong("entityId"))
                .dwFileId(document.getObjectId("dwFileId").toString()).createTime(document.getDate("createTime")).build();
    }

    @Override
    public BasePage<DWFileRsp> listRelation(String kgName, EntityFileRelationQueryReq req) {

        int size = req.getSize();
        int pageNo = (req.getPage() - 1) * size;
        List<Bson> aggLs = new ArrayList<>();
        aggLs.add(Aggregates.lookup(DWFileConstants.RELATION, "_id", "dwFileId", "relationList"));
        if (StringUtils.isNotBlank(req.getName())) {
            aggLs.add(Aggregates.match(Filters.elemMatch("relationList", Filters.eq("kgName", kgName))));
        }
        aggLs.add(Aggregates.match(Filters.regex("title", Pattern.compile("^.*" + req.getName() + ".*$"))));
        aggLs.add(Aggregates.match(Filters.eq("indexType", req.getIndexType())));
        aggLs.add(Aggregates.skip(pageNo));
        aggLs.add(Aggregates.limit(size + 1));
        MongoCollection<Document> collection = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId()).getCollection(DWFileConstants.FILE);
        MongoCursor<Document> iterator = collection.aggregate(aggLs).iterator();

        List<DWFileRsp> list = Lists.newArrayList();
        List<Long> entityIds = Lists.newArrayList();
        iterator.forEachRemaining(s -> {
            DWFileRsp dwFileRsp = convertToDWFileRsp(s);
            if (dwFileRsp.getRelationList() != null) {
                entityIds.addAll(dwFileRsp.getRelationList().stream().map(EntityFileRelationRsp::getEntityId).collect(Collectors.toList()));
            }
            list.add(dwFileRsp);
        });
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
        }
        return new BasePage<>(count, list);
    }

    @Override
    public void createRelation(String kgName, EntityFileRelationReq req) {
        EntityFileRelation relation = ConvertUtils.convert(EntityFileRelation.class).apply(req);
        relation.setKgName(kgName);
        relation.setCreateTime(new Date());
        Document document = documentConverter.toDocument(relation);
        document.put("dwFileId", new ObjectId(document.getString("dwFileId")));
        getCollection().insertOne(document);
    }

    @Override
    public void deleteRelation(List<String> idList) {
        List<Bson> query = idList.stream().map(id -> documentConverter.buildObjectId(id)).collect(Collectors.toList());
        getCollection().deleteMany(Filters.and(query));
    }

    @Override
    public void deleteRelationByDwFileId(String dwFileId) {
        getCollection().deleteMany(Filters.eq("dwFileId", new ObjectId(dwFileId)));
    }

    @Override
    public void deleteById(String id) {
        getCollection().deleteMany(documentConverter.buildObjectId(id));
    }

    @Override
    public List<EntityFileRelationRsp> getRelationByDwFileId(String dwFileId) {
        MongoCollection<Document> mongoCollection = getCollection();
        MongoCursor<Document> cursor = mongoCollection.find(Filters.eq("dwFileId", new ObjectId(dwFileId))).iterator();
        List<EntityFileRelationRsp> list = Lists.newArrayList();
        if (cursor.hasNext()) {
            list.add(convertToEntityFileRelationRsp(cursor.next()));
        }
        return list;
    }

    @Override
    public List<EntityFileRsp> getRelationByKgNameAndEntityId(String kgName, Long entityId) {
        List<Bson> bsons = new ArrayList<>(2);
        bsons.add(Filters.eq("kgName", kgName));
        bsons.add(Filters.eq("entityId", entityId));
        MongoDatabase database = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId());
        MongoCursor<Document> cursor = database.getCollection(DWFileConstants.RELATION).find(Filters.and(bsons)).iterator();
        List<EntityFileRsp> list = Lists.newArrayList();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            ObjectId objectId = doc.getObjectId("dwFileId");
            Document document = database.getCollection(DWFileConstants.FILE).find(Filters.eq(CommonConstants.MongoConst.ID, objectId)).first();
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
    public List<EntityFileRsp> getRelationByKgNameAndEntityIdIn(String kgName, List<Long> entityIds) {
        List<Bson> bsons = new ArrayList<>(2);
        bsons.add(Filters.in("entityId", entityIds));
        bsons.add(Filters.eq("kgName", kgName));
        MongoDatabase database = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId());
        MongoCursor<Document> cursor = database.getCollection(DWFileConstants.RELATION).find(Filters.and(bsons)).iterator();
        List<EntityFileRsp> list = Lists.newArrayList();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            ObjectId objectId = doc.getObjectId("dwFileId");
            Document document = database.getCollection(DWFileConstants.FILE).find(Filters.eq(CommonConstants.MongoConst.ID, objectId)).first();
            if (document != null) {
                EntityFileRsp entityFileRsp = new EntityFileRsp();
                entityFileRsp.setId(doc.getObjectId("_id").toString());
                entityFileRsp.setEntityId(doc.getLong("entityId"));
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
    public void addIndex(String kgName, Integer indexType, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw BizException.of(CommonErrorCode.BAD_REQUEST);
        }
        String suffix = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        if (suffix.equals("xlsx") || suffix.equals("xls")) {
            List<LinkedHashMap<String, String>> dataList;
            try {
                dataList = readExcel(file);
            } catch (IOException e) {
                throw BizException.of(KgmsErrorCodeEnum.EXCEL_READ_ERROR);
            }
            if (dataList != null) {
                List<Document> list = new ArrayList<>(dataList.size());
                for (LinkedHashMap<String, String> map : dataList) {
                    DWFileTable dwFileTable = new DWFileTable();
                    if (indexType == 1) {
                        dwFileTable.setDescription(map.get("content"));
                    } else if (indexType == 2) {
                        dwFileTable.setUrl(map.get("url"));
                    }
                    dwFileTable.setTitle(map.get("title"));
                    dwFileTable.setCreateTime(new Date());
                    dwFileTable.setIndexType(indexType);
                    dwFileTable.setType(suffix);
                    dwFileTable.setUserId(SessionHolder.getUserId());

                    Document document = documentConverter.toDocument(dwFileTable);
                    list.add(document);
                }
                MongoCollection<Document> collection = mongoClient.getDatabase(DWFileConstants.DW_PREFIX + SessionHolder.getUserId()).getCollection(DWFileConstants.FILE);
                collection.insertMany(list);
            }
        }
    }

    @Override
    public void updateIndex(String kgName, IndexRelationReq req) {
        List<Document> list = new ArrayList<>(req.getEntityIds().size());

        for (Long entityId : req.getEntityIds()) {
            EntityFileRelation relation = new EntityFileRelation();
            relation.setKgName(kgName);
            relation.setCreateTime(new Date());
            relation.setEntityId(entityId);
            Document document = documentConverter.toDocument(relation);
            document.put("dwFileId", new ObjectId(req.getDwFileId()));
            list.add(document);
        }
        getCollection().insertMany(list);
    }

}
