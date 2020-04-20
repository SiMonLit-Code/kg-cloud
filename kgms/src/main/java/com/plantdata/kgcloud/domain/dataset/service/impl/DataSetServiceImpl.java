package com.plantdata.kgcloud.domain.dataset.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.config.EsProperties;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.constant.KgmsConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.common.converter.ApiReturnConverter;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.entity.DataSetFolder;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.repository.DataSetRepository;
import com.plantdata.kgcloud.domain.dataset.service.DataSetFolderService;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.KgtextClient;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.sdk.constant.DataType;
import com.plantdata.kgcloud.sdk.req.*;
import com.plantdata.kgcloud.sdk.rsp.CorpusSetInfoRsp;
import com.plantdata.kgcloud.sdk.rsp.DataSetRsp;
import com.plantdata.kgcloud.sdk.rsp.DataSetUpdateRsp;
import com.plantdata.kgcloud.sdk.rsp.UserLimitRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 10:40
 **/
@Slf4j
@Service
public class DataSetServiceImpl implements DataSetService {

    private final static String DATA_PREFIX = "dataset";
    private final static String JOIN = "_";
    private final static String JSON_START = "{";
    private final static String ARRAY_START = "[";
    private final static String ARRAY_STRING_START = "[\"";
    private final Function<DataSet, DataSetRsp> dataSet2rsp = (s) -> {
        DataSetRsp dataSetRsp = new DataSetRsp();
        BeanUtils.copyProperties(s, dataSetRsp);
        DataType dataType = s.getDataType();
        dataSetRsp.setDataType(dataType.getDataType());
        return dataSetRsp;
    };
    @Autowired
    private MongoProperties mongoProperties;
    @Autowired
    private EsProperties esProperties;
    @Autowired
    private DataSetRepository dataSetRepository;
    @Autowired
    private DataSetFolderService dataSetFolderService;
    @Autowired
    private KgKeyGenerator kgKeyGenerator;
    @Autowired
    private UserClient userClient;
    @Autowired
    private KgtextClient kgtextClient;

    private String genDataName(String userId, String key) {
        return userId + JOIN + DATA_PREFIX + JOIN + key;
    }

    @Override
    public List<DataSetRsp> findAll(String userId) {
        DataSet probe = DataSet.builder()
                .userId(SessionHolder.getUserId())
                .build();
        List<DataSet> all = dataSetRepository.findAll(Example.of(probe));
        return all.stream().map(dataSet2rsp).collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<DataSetRsp> findAll(String userId, DataSetPageReq req) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize(), sort);

        Specification<DataSet> specification = (Specification<DataSet>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            expressions.add(cb.equal(root.<String>get("userId"), userId));
            Long folderId = req.getFolderId();
            if (folderId != null) {
                expressions.add(cb.equal(root.<Long>get("folderId"), folderId));
            }
            Integer dataType = req.getDataType();
            if (dataType != null) {
                expressions.add(cb.equal(root.<DataType>get("dataType"), DataType.findType(dataType)));
            }
            if (StringUtils.hasText(req.getKw())) {
                expressions.add(cb.like(root.get("title"), "%" + req.getKw() + "%"));
            }
            return predicate;
        };

        Page<DataSet> all = dataSetRepository.findAll(specification, pageable);


        DataSetFolder folder = dataSetFolderService.getDefaultFolder(userId);
        Set<Long> folderIds = dataSetFolderService.getFolderIds(userId);
        for (DataSet dataSet : all.getContent()) {
            if (!folderIds.contains(dataSet.getFolderId())) {
                dataSet.setFolderId(folder.getId());
                dataSetRepository.save(dataSet);
            }
        }
        return all.map(dataSet2rsp);
    }

    @Override
    public List<Long> findByDataNames(String userId, List<String> dataNames) {
        List<DataSet> list = dataSetRepository.findByUserIdAndDataNameIn(userId, dataNames);
        List<Long> longs = new ArrayList<>();
        for (DataSet dataSet : list) {
            longs.add(dataSet.getId());
        }
        return longs;
    }

    @Override
    public List<Long> findByDatabase(String userId, List<DataSetSdkReq> database) {
        List<Long> longs = new ArrayList<>();
        for (DataSetSdkReq req : database) {
            Optional<DataSet> dataSet = dataSetRepository.findByUserIdAndDbNameAndTbName(userId, req.getDatabase(), req.getDatabase());
            dataSet.ifPresent(k -> longs.add(k.getId()));
        }
        return longs;
    }

    @Override
    public List<DataSet> findByFolderId(Long folderId) {
        return dataSetRepository.findByFolderId(folderId);
    }

    @Override
    public DataSet findOne(String userId, Long id) {
        return dataSetRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS));
    }


    @Override
    public DataSetUpdateRsp findById(String userId, Long id) {
        Optional<DataSet> one = dataSetRepository.findByUserIdAndId(userId, id);
        DataSetUpdateRsp rsp = one.map((s) -> {
            DataSetUpdateRsp dataSetRsp = new DataSetUpdateRsp();
            BeanUtils.copyProperties(s, dataSetRsp);
            DataType dataType = s.getDataType();
            dataSetRsp.setDataType(dataType.getDataType());
            return dataSetRsp;
        }).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS));
        Set<String> set = new HashSet<>(rsp.getFields());
        set.add("_id");
        set.add(DataConst.CREATE_AT);
        set.add(DataConst.UPDATE_AT);
        set.add("_smoke");
        set.add("_smokeMsg");
        set.add("annotation_tag");
        set.add("parent_annotation_tag");
        set.add("isAnnotation");
        Set<String> newField = new LinkedHashSet<>();
        List<DataSetSchema> newSchema = new ArrayList<>();
        DataOptConnect connect = DataOptConnect.of(one.get());
        try (DataOptProvider provider = DataOptProviderFactory.createProvider(connect, one.get().getDataType())) {
            List<Map<String, Object>> mapList = provider.find(0, 100, null);
            for (Map<String, Object> map : mapList) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (!set.contains(entry.getKey())) {
                        newField.add(entry.getKey());
                    }
                }
            }
        } catch (IOException e) {
            log.error("connect fail...", e);
        }
        for (String s : newField) {
            DataSetSchema dataSetSchema = new DataSetSchema();
            dataSetSchema.setField(s);
            dataSetSchema.setType(1);
            dataSetSchema.setIsIndex(0);
            newSchema.add(dataSetSchema);
        }
        rsp.setNewFields(new ArrayList<>(newField));
        rsp.setNewSchema(newSchema);
        return rsp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String userId, Long id) {
        DataSet dataSet = findOne(userId, id);
        DataOptConnect connect = DataOptConnect.of(dataSet);
        try (DataOptProvider provider = DataOptProviderFactory.createProvider(connect, dataSet.getDataType())) {
            provider.dropTable();
        } catch (IOException e) {
            log.error("delete fail...", e);
        }
        dataSetRepository.deleteByUserIdAndId(userId, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(String userId, Collection<Long> ids) {
        for (Long id : ids) {
            delete(userId, id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataSetRsp insert(String userId, DataSetCreateReq req) {
        checkUserLimit(userId);
        DataSet target = new DataSet();
        BeanUtils.copyProperties(req, target);
        Set<Long> folderIds = dataSetFolderService.getFolderIds(userId);
        if (!folderIds.contains(target.getFolderId())) {
            DataSetFolder folder = dataSetFolderService.getDefaultFolder(userId);
            target.setFolderId(folder.getId());
        }
        String dataName = genDataName(userId, req.getKey());
        Optional<DataSet> dataSet = dataSetRepository.findByDataName(dataName);
        if (dataSet.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_KEY_EXISTS);
        }

        target.setId(kgKeyGenerator.getNextId());
        target.setUserId(userId);
        target.setDataName(dataName);
        DataType type = DataType.findType(req.getDataType());
        target.setDataType(type);
        target.setDbName(userId + JOIN + DATA_PREFIX);
        if (type == DataType.MONGO) {
            target.setAddr(Arrays.asList(mongoProperties.getAddrs()));
            target.setTbName(req.getKey());
        } else if (type == DataType.ELASTIC) {
            target.setAddr(esProperties.getAddrs());
            target.setDbName(dataName);
            target.setTbName("_doc");
        }
        target.setEditable(true);
        target.setPrivately(true);
        List<DataSetSchema> schema = req.getSchema();
        target.setFields(transformFields(schema));

        DataOptConnect dataOptConnect = DataOptConnect.of(target);
        try (DataOptProvider provider = DataOptProviderFactory.createProvider(dataOptConnect, type)) {
            provider.createTable(schema);
            target.setUserId(userId);
            target = dataSetRepository.save(target);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }
        return dataSet2rsp.apply(target);
    }

    @Override
    public DataSetRsp insert(String userId, DataSetPdReq req) {
        checkUserLimit(userId);
        String dataName = genDataName(userId, req.getKey());
        Optional<DataSet> dataSet = dataSetRepository.findByDataName(dataName);
        if (dataSet.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_KEY_EXISTS);
        }
        DataSet target = ConvertUtils.convert(DataSet.class).apply(req);
        target.setDataType(DataType.PD_DOCUMENT);
        List<DataSetSchema> dataSetSchemas = new ArrayList<>();
        DataSetSchema idField = new DataSetSchema();
        idField.setField("id");
        idField.setType(FieldType.STRING.getCode());
        idField.setIsIndex(0);
        DataSetSchema titleField = new DataSetSchema();
        titleField.setField("title");
        titleField.setType(FieldType.STRING.getCode());
        titleField.setIsIndex(0);
        DataSetSchema labelField = new DataSetSchema();
        labelField.setField("label");
        labelField.setType(FieldType.ARRAY.getCode());
        labelField.setIsIndex(0);
        dataSetSchemas.add(idField);
        dataSetSchemas.add(titleField);
        dataSetSchemas.add(labelField);
        target.setSchema(dataSetSchemas);
        target.setDataName(dataName);
        target.setEditable(false);
        target.setPrivately(false);
        target.setFields(transformFields(dataSetSchemas));
        target.setId(kgKeyGenerator.getNextId());
        target.setUserId(userId);
        target.setAddr(Arrays.asList(mongoProperties.getAddrs()));
        DataOptConnect dataOptConnect = DataOptConnect.of(target);
        try {
            CorpusSetInfoRsp rsp = ApiReturnConverter.convert(kgtextClient.csInfo(req.getPdId()));
            target.setDbName(rsp.getDbName());
            target.setTbName(rsp.getTbName());
            target = dataSetRepository.save(target);

        }catch (Exception e){
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_PDTEXT_ERROR);
        }
        return dataSet2rsp.apply(target);
    }

    private void checkUserLimit(String userId){
        UserLimitRsp data = userClient.getCurrentUserLimitDetail().getData();
        if (data != null) {
            DataSet probe = new DataSet();
            probe.setUserId(userId);
            long count = dataSetRepository.count(Example.of(probe));
            Integer datasetCount = data.getDatasetCount();
            if (datasetCount != null && count >= datasetCount) {
                throw BizException.of(KgmsErrorCodeEnum.GRAPH_OUT_LIMIT);
            }
        }
    }

    private List<String> transformFields(List<DataSetSchema> schema) {
        LinkedHashSet<String> fields = new LinkedHashSet<>();
        for (DataSetSchema dataSetSchema : schema) {
            fields.add(dataSetSchema.getField());
        }
        return new ArrayList<>(fields);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataSetRsp update(String userId, Long id, DataSetUpdateReq req) {
        Optional<DataSet> one = dataSetRepository.findByUserIdAndId(userId, id);
        DataSet target = one.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DATASET_NOT_EXISTS));
        BeanUtils.copyProperties(req, target);
        List<DataSetSchema> schema = req.getSchema();
        target.setFields(transformFields(schema));
        target = dataSetRepository.save(target);
        return dataSet2rsp.apply(target);

    }

    @Override
    public List<DataSetSchema> schemaResolve(Integer dataType, MultipartFile file) {
        List<DataSetSchema> schemaList = new ArrayList<>();
        String filename = file.getOriginalFilename();
        if (filename != null) {
            int i = filename.lastIndexOf(".");
            String extName = filename.substring(i);
            if (KgmsConstants.FileType.XLSX.equalsIgnoreCase(extName) || KgmsConstants.FileType.XLS.equalsIgnoreCase(extName)) {
                excelFileSchema(schemaList, file);
            } else if (KgmsConstants.FileType.JSON.equalsIgnoreCase(extName)) {
                jsonFileSchema(schemaList, file);
            }
        }
        return schemaList;
    }

    private void excelFileSchema(List<DataSetSchema> setSchemas, MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), new AnalysisEventListener<Map<Integer, Object>>() {
                Map<Integer, DataSetSchema> dataSetSchemaMap = new HashMap<>();

                @Override
                public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                    for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
                        DataSetSchema dataSetSchema = new DataSetSchema();
                        dataSetSchema.setField(entry.getValue());
                        Object val = entry.getValue();
                        if (val != null) {
                            FieldType type = readType(val);
                            dataSetSchema.setType(type.getCode());
                        }else{
                            dataSetSchema.setType(1);
                        }
                        dataSetSchemaMap.put(entry.getKey(), dataSetSchema);
                    }
                }

                @Override
                public void invoke(Map<Integer, Object> data, AnalysisContext context) {
                    Integer rowIndex = context.readRowHolder().getRowIndex();
                    if (rowIndex < 10) {
                        for (Map.Entry<Integer, Object> entry : data.entrySet()) {
                            Object val = entry.getValue();
                            if (val != null) {
                                FieldType type = readType(val);
                                dataSetSchemaMap.get(entry.getKey()).setType(type.getCode());
                            }
                        }
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    setSchemas.addAll(dataSetSchemaMap.values());
                }
            }).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonFileSchema(List<DataSetSchema> setSchemas, MultipartFile file) {
        Object json;
        try {
            json = JacksonUtils.readValue(file.getInputStream(), new TypeReference<Object>() {
            });
        } catch (IOException e) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        if (json instanceof List) {
            List l = (List) json;
            map = (Map<String, Object>) l.get(0);
        } else if (json instanceof Map) {
            map = (Map<String, Object>) json;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String o = entry.getKey();
            FieldType type = readType(entry.getValue());
            DataSetSchema dataSetSchema = new DataSetSchema();
            dataSetSchema.setField(o);
            dataSetSchema.setType(type.getCode());
            setSchemas.add(dataSetSchema);
        }
    }

    @Override
    public FieldType readType(Object val) {
        FieldType type = null;
        if(val == null){
            type = FieldType.STRING;
            return type;
        }
        String string = val.toString();
        if (string.startsWith(JSON_START)) {
            try {
                JacksonUtils.getInstance().readValue(string, ObjectNode.class);
                type = FieldType.OBJECT;
            } catch (Exception e) {
                type = FieldType.STRING;
            }
        } else if (string.startsWith(ARRAY_START)) {
            if (string.startsWith(ARRAY_STRING_START)) {
                try {
                    JacksonUtils.getInstance().readValue(string, new TypeReference<List<String>>() {
                    });
                    type = FieldType.STRING_ARRAY;
                } catch (Exception e) {
                    type = FieldType.STRING;
                }
            } else {
                try {
                    JacksonUtils.getInstance().readValue(string, ArrayNode.class);
                    type = FieldType.ARRAY;
                } catch (Exception e) {
                    type = FieldType.STRING;
                }
            }
        } else if (val instanceof Integer) {
            type = FieldType.INTEGER;
        } else if (val instanceof Long) {
            type = FieldType.LONG;
        } else if (val instanceof Date) {
            type = FieldType.DATE;
        } else if (val instanceof Double) {
            type = FieldType.DOUBLE;
        } else if (val instanceof Float) {
            type = FieldType.FLOAT;
        } else {
            try {
                Integer.parseInt(string);
                type = FieldType.INTEGER;
            }catch (Exception e){
                try {
                    Long.parseLong(string);
                    type = FieldType.LONG;
                }catch (Exception e1){
                    try {
                        Double.parseDouble(string);
                        type = FieldType.DOUBLE;
                    }catch (Exception e4){
                        try {
                            String date = "\\d{4}-\\d{2}-\\d{2}";
                            String time = "\\d{2}:\\d{2}:\\d{2}";
                            String dateTime = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
                            if (Pattern.matches(date, string)) {
                                LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                type = FieldType.DATE;
                            } else if (Pattern.matches(time, string)) {
                                LocalTime.parse(string, DateTimeFormatter.ofPattern("HH:mm:ss"));
                                type = FieldType.DATE;
                            } else if (Pattern.matches(dateTime, string)) {
                                LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                type = FieldType.DATE;
                            }
                        }catch (Exception e2){
                        }
                    }
                }
            }

            if(type == null){
                type = FieldType.STRING;
            }
        }
        return type;
    }

    @Override
    public void move(String userId, Collection<Long> ids, Long folderId) {
        if (folderId == null) {
            folderId = dataSetFolderService.getDefaultFolder(userId).getId();
        } else {
            dataSetFolderService.getFolder(userId, folderId).orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.FOLDER_NOT_EXISTS));
        }
        List<DataSet> dataSetList = dataSetRepository.findAllById(ids);
        for (DataSet dataSet : dataSetList) {
            dataSet.setFolderId(folderId);
        }
        dataSetRepository.saveAll(dataSetList);
    }

}
