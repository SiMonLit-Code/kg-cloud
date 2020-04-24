package com.plantdata.kgcloud.domain.dw.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hiekn.bean.Pdd2rBean;
import com.hiekn.pddocument.bean.PdDocument;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.constant.AccessTaskType;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.constant.KgmsConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.domain.access.util.YamlTransFunc;
import com.plantdata.kgcloud.domain.data.entity.DWDataStatusDatail;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.provider.MongodbOptProvider;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWFileTable;
import com.plantdata.kgcloud.domain.dw.entity.DWPrebuildModel;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.repository.DWDatabaseRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWFileTableRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWPrebuildModelRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWTableRepository;
import com.plantdata.kgcloud.domain.dw.req.*;
import com.plantdata.kgcloud.domain.dw.rsp.*;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.domain.dw.service.PreBuilderService;
import com.plantdata.kgcloud.domain.dw.service.StandardTemplateService;
import com.plantdata.kgcloud.domain.dw.util.ExampleTagJson;
import com.plantdata.kgcloud.domain.dw.util.ExampleYaml;
import com.plantdata.kgcloud.domain.dw.util.PaserYaml2SchemaUtil;
import com.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import com.plantdata.kgcloud.domain.edit.util.MapperUtils;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.sdk.constant.DWDataFormat;
import com.plantdata.kgcloud.sdk.constant.DataType;
import com.plantdata.kgcloud.sdk.req.DWConnceReq;
import com.plantdata.kgcloud.sdk.req.DWDatabaseReq;
import com.plantdata.kgcloud.sdk.req.DWTableReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.rsp.ModelRangeRsp;
import com.plantdata.kgcloud.sdk.rsp.UserLimitRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.UUIDUtils;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class DWServiceImpl implements DWService {

    private final static String DW_PREFIX = "dw_db";
    private final static String JOIN = "_";
    private final static String KETTLE_LOGS_DATABASE = "kettle_logs";
    private final static String KETTLE_LOGS_COLLECTION = "logs_data";
    private final static String KETTLE_LOGS_RECODE = "logs_data_recode";
    private static final String MONGO_ID = CommonConstants.MongoConst.ID;
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private DWDatabaseRepository dwRepository;
    @Autowired
    private DocumentConverter documentConverter;
    @Autowired
    private DWTableRepository tableRepository;

    @Autowired
    private PreBuilderService preBuilderService;

    @Autowired
    private StandardTemplateService standardTemplateService;

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private AccessTaskService accessTaskService;

    @Autowired
    private MongoProperties mongoProperties;

    @Autowired
    private DWFileTableRepository fileTableRepository;

    @Autowired
    private DWPrebuildModelRepository modelRepository;

    @Autowired
    private UserClient userClient;

    private final Function<DWDatabase, DWDatabaseRsp> dw2rsp = (s) -> {
        DWDatabaseRsp dwRsp = new DWDatabaseRsp();
        BeanUtils.copyProperties(s, dwRsp);
        return dwRsp;
    };

    private final Function<DWTable, DWTableRsp> table2rsp = (s) -> {
        DWTableRsp tableRsp = new DWTableRsp();
        BeanUtils.copyProperties(s, tableRsp);
        return tableRsp;
    };

    private void checkUserLimit(String userId) {
        UserLimitRsp data = userClient.getCurrentUserLimitDetail().getData();
        if (data != null) {
            DWDatabase probe = new DWDatabase();
            probe.setUserId(data.getUserId());
            long count = dwRepository.count(Example.of(probe));
            Integer datasetCount = data.getDwCount();
            if (datasetCount != null && count >= datasetCount) {
                throw BizException.of(KgmsErrorCodeEnum.DW_OUT_LIMIT);
            }
        }
    }

    @Override
    public DWDatabaseRsp createDatabase(String userId, DWDatabaseReq req) {

        checkUserLimit(userId);

        DWDatabase dw = saveDatabase(req);

        List<DWTableRsp> tables = new ArrayList<>();
        if (DWDataFormat.isStandard(req.getDataFormat())) {

            List<ModelSchemaConfigRsp> modelSchemas = new ArrayList<>();

            for (Integer stId : req.getStandardTemplateId()) {
                StandardTemplateRsp standardTemplateRsp = standardTemplateService.findOne(userId, stId);

                List<ModelSchemaConfigRsp> tagjsons = standardTemplateRsp.getTagJson();
                if (tagjsons == null || tagjsons.isEmpty()) {
                    continue;
                }

                tagjsons.forEach(config -> config.setModelId(stId));
                modelSchemas.addAll(standardTemplateRsp.getTagJson());
            }

            dw.setTagJson(modelSchemas);
            dwRepository.save(dw);
        }

        DWDatabaseRsp databaseRsp = dw2rsp.apply(dw);
        databaseRsp.setTables(tables);
        return databaseRsp;
    }

    private ModelSchemaConfigRsp mergeModel(List<ModelSchemaConfigRsp> tagJsons) {

        if (tagJsons == null || tagJsons.isEmpty()) {
            return new ModelSchemaConfigRsp();
        }

        ModelSchemaConfigRsp modelSchema = null;

        for (ModelSchemaConfigRsp tagJson : tagJsons) {

            if (tagJson == null) {
                continue;
            }

            if (modelSchema == null) {
                modelSchema = tagJson;
                if (modelSchema.getEntity() == null) {
                    modelSchema.setEntity(new HashSet<>());
                }

                if (modelSchema.getRelation() == null) {
                    modelSchema.setRelation(new HashSet<>());
                }

                if (modelSchema.getAttr() == null) {
                    modelSchema.setAttr(new HashSet<>());
                }
                continue;
            }


            if (tagJson.getEntity() != null) {
                modelSchema.getEntity().addAll(tagJson.getEntity());
            }

            if (tagJson.getRelation() != null) {

                Map<String, ModelRelationBeanRsp> map = new HashMap<>();

                for (ModelRelationBeanRsp relation : modelSchema.getRelation()) {
                    map.put(relation.getDomain() + "-" + relation.getName(), relation);
                }

                for (ModelRelationBeanRsp relation : tagJson.getRelation()) {
                    if (map.containsKey(relation.getDomain() + "-" + relation.getName())) {
                        ModelRelationBeanRsp rela = map.get(relation.getDomain() + "-" + relation.getName());

                        //值域相加
                        rela.getRange().addAll(relation.getRange());

                        if (rela.getAttrs() == null) {
                            rela.setAttrs(new HashSet<>());
                        }

                        if (relation.getAttrs() != null && !relation.getAttrs().isEmpty()) {
                            List<String> existName =  new ArrayList<>();
                            rela.getAttrs().forEach(s -> existName.add(s.getName()));

                            relation.getAttrs().forEach(a ->{
                                if(!existName.contains(a.getName())){
                                    existName.add(a.getName());
                                    rela.getAttrs().add(a);
                                }
                            });
                        }
                    } else {
                        map.put(relation.getDomain() + "-" + relation.getName(), relation);
                        modelSchema.getRelation().add(relation);
                    }
                }
            }

            if (tagJson.getAttr() != null) {
                modelSchema.getAttr().addAll(tagJson.getAttr());
            }

        }

        return modelSchema;
    }

    private List<DataSetSchema> getIndustryTableSchema(Long databaseId, String tableName) {

        DWDatabaseRsp database = getDetail(databaseId);

        List<Integer> templateIds = database.getStandardTemplateId();

        if (templateIds == null || templateIds.isEmpty()) {
            return null;
        }

        List<DWPrebuildModel> models = modelRepository.findAllById(templateIds);

        if (models == null || models.isEmpty()) {
            return null;
        }

        for (DWPrebuildModel model : models) {

            List<StandardTemplateSchema> schemas = model.getSchemas();

            if (schemas == null || schemas.isEmpty()) {
                continue;
            }

            for (StandardTemplateSchema schema : schemas) {
                if (schema.getTableName().equals(tableName)) {
                    return schema.getSchemas();
                }
            }

        }

        return null;

    }

    private String getIndustryTableKtr(Long databaseId, String tableName) {

        DWDatabaseRsp database = getDetail(databaseId);

        List<Integer> templateIds = database.getStandardTemplateId();

        if (templateIds == null || templateIds.isEmpty()) {
            return null;
        }

        List<DWPrebuildModel> models = modelRepository.findAllById(templateIds);

        if (models == null || models.isEmpty()) {
            return null;
        }

        for (DWPrebuildModel model : models) {

            List<TableKtrRsp> ktrs = model.getKtr();

            if (ktrs == null || ktrs.isEmpty()) {
                continue;
            }

            for (TableKtrRsp ktr : ktrs) {
                if (ktr.getTableName().equals(tableName)) {
                    return ktr.getKtr();
                }
            }

        }

        return null;

    }

    private DWDatabase saveDatabase(DWDatabaseReq req) {

        DWDatabase dw = DWDatabase.builder()
                .userId(SessionHolder.getUserId())
                .title(req.getTitle())
                .dataFormat(req.getDataFormat())
                .standardTemplateId(req.getStandardTemplateId())
                .build();

        dw.setDataName(DW_PREFIX + JOIN + SessionHolder.getUserId() + JOIN + UUIDUtils.getShortString().substring(0, 5));

        return dwRepository.save(dw);
    }

    @Override
    public void yamlUpload(Long databaseId, MultipartFile file) {

        if (file == null || !file.getOriginalFilename().endsWith(".yaml")) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_TYPE_ERROR);
        }
        Optional<DWDatabase> dwDatabase = dwRepository.findById(databaseId);

        if (!dwDatabase.isPresent()) {
            return;
        }
        DWDatabase database = dwDatabase.get();

        //不是自定义类型数据库不用上传yaml
        if (!DWDataFormat.isCustom(database.getDataFormat())) {
            throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
        }

        List<DWTableRsp> tables = findTableAll(SessionHolder.getUserId(), databaseId);

        if (tables == null || tables.isEmpty()) {
            throw BizException.of(KgmsErrorCodeEnum.EMTRY_TABLE_NOT_UPLOAD_MODEL_ERROR);
        }

        List<String> tableNames = tables.stream().map(DWTableRsp::getTableName).collect(Collectors.toList());

        JSONObject json;
        String result;
        try {

            result = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);

            if (result.startsWith("/*")) {
                result = result.substring(result.indexOf("*/") + 2);
            }

            Object value = new Yaml().load(result);

            //生成json
            json = JacksonUtils.readValue(JacksonUtils.writeValueAsString(value), JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.YAML_PARSE_ERROR);
        }

        List<ModelSchemaConfigRsp> modelSchemaConfig = PaserYaml2SchemaUtil.parserYaml2TagJson(json, tables);

        boolean isEmpty = true;
        for (ModelSchemaConfigRsp schema : modelSchemaConfig) {
            if (!tableNames.contains(schema.getTableName())) {
                throw BizException.of(KgmsErrorCodeEnum.TABLE_NOT_EXIST_IN_DATABASE);
            }

            if (schema.getEntity() != null && !schema.getEntity().isEmpty()) {
                isEmpty = false;
            }
        }

        if (isEmpty) {
            throw BizException.of(KgmsErrorCodeEnum.YAML_PARSE_ERROR);
        }

        YamlTransFunc.tranTagConfig(result);
        database.setYamlContent(result);
        database.setTagJson(modelSchemaConfig);

        dwRepository.save(database);
        return;
    }


    @Override
    public List<DWDatabaseRsp> findAll(String userId) {
        DWDatabase probe = DWDatabase.builder()
                .userId(SessionHolder.getUserId())
                .build();
        List<DWDatabase> all = dwRepository.findAll(Example.of(probe), Sort.by(Sort.Order.desc("createAt")));
        return all.stream().map(dw2rsp).collect(Collectors.toList());
    }

    @Override
    public DWDatabaseRsp setConn(String userId, DWConnceReq req) {


        Optional<DWDatabase> dwDatabase = dwRepository.findById(req.getDwDatabaseId());

        if (!dwDatabase.isPresent()) {
            return null;
        }

        DWDatabase database = dwDatabase.get();
        database.setAddr(req.getAddr());
        database.setPassword(req.getPassword());
        database.setUsername(req.getUsername());
        database.setDbName(req.getDbName());
        database.setDataType(req.getType().getDataType());
        dwRepository.save(database);
        return dw2rsp.apply(database);
    }

    @Override
    public DWDatabaseRsp getDetail(Long dwDatabaseId) {
        Optional<DWDatabase> dwDatabase = dwRepository.findById(dwDatabaseId);

        if (!dwDatabase.isPresent()) {
            return null;
        }
        return ConvertUtils.convert(DWDatabaseRsp.class).apply(dwDatabase.get());
    }

    @Override
    public DWTable getTableDetail(Long tableId) {
        Optional<DWTable> table = tableRepository.findById(tableId);

        if (!table.isPresent()) {
            return null;
        }
        return table.get();
    }

    @Override
    public void batchCreateTable(String userId, List<DWTableReq> reqs) {
        if (reqs == null || reqs.isEmpty()) {
            return;
        }

        for (DWTableReq req : reqs) {
            createTable(userId, req);
        }
    }

    @Override
    public void updateDatabaseName(String userId, DWDatabaseNameReq req) {

        Optional<DWDatabase> dwDatabase = dwRepository.findById(req.getDataBaseId());

        if (!dwDatabase.isPresent()) {
            return;
        }
        DWDatabase database = dwDatabase.get();

        if (Objects.equals(database.getUserId(), userId)) {
            database.setTitle(req.getName());
            dwRepository.save(database);
        }

    }

    @Override
    public void exampleDownload(String userId, Long databaseId, HttpServletResponse response) {
        try {

            DWDatabaseRsp database = getDetail(databaseId);

            if (database == null || (!DWDataFormat.isCustom(database.getDataFormat()) && !DWDataFormat.isPDd2r(database.getDataFormat()) && !DWDataFormat.isPDdoc(database.getDataFormat()))) {
                return;
            }

            List<DWTableRsp> tableRsps = findTableAll(userId, databaseId);
            if (tableRsps == null || tableRsps.isEmpty()) {
                return;
            }
            response.reset();
            byte[] bytes;
            if (DWDataFormat.isCustom(database.getDataFormat())) {

                bytes = ExampleYaml.create(tableRsps);
                response.setHeader("Content-Disposition", "attachment;filename=" + new String((database.getTitle() + ".yaml").getBytes(),
                        "iso-8859-1"));
                response.getOutputStream().write(bytes);
            } else if (DWDataFormat.isPDd2r(database.getDataFormat()) || DWDataFormat.isPDdoc(database.getDataFormat())) {
                bytes = ExampleTagJson.create(tableRsps);
                response.setHeader("Content-Disposition", "attachment;filename=" + new String((database.getTitle() + ".json").getBytes(),
                        "iso-8859-1"));
                response.getOutputStream().write(bytes);
            } else {
                throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteData(String userId, Long databaseId, Long tableId) {
        Optional<DWDatabase> dwOpt = dwRepository.findOne(Example.of(DWDatabase.builder().userId(userId).id(databaseId).build()));
        if (!dwOpt.isPresent()) {
            return;
        }

        if (DWDataFormat.isFile(dwOpt.get().getDataFormat())) {
            //文件系统

            List<DWFileTable> files = fileTableRepository.findAll(Example.of(DWFileTable.builder().tableId(tableId).build()));
            if (files != null && !files.isEmpty()) {

                for (DWFileTable file : files) {
                    fileTableRepository.deleteById(file.getId());
                }
            }
        } else {

            Optional<DWTable> opt = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(databaseId).id(tableId).build()));
            if (opt.isPresent()) {
                try (DataOptProvider provider = getProvider(true, userId, databaseId, tableId, mongoProperties)) {
                    provider.deleteAll();
                } catch (Exception e) {
                    throw BizException.of(KgmsErrorCodeEnum.TABLE_CONNECT_ERROR);
                }
            }

        }

    }

    @Override
    public DWDatabaseRsp getDbByDataName(String dataName) {

        Optional<DWDatabase> databaseOpt = dwRepository.findOne(Example.of(DWDatabase.builder().dataName(dataName).build()));

        if (databaseOpt.isPresent()) {
            return ConvertUtils.convert(DWDatabaseRsp.class).apply(databaseOpt.get());
        }

        return null;
    }

    @Override
    public void updateTagJson(Long databaseId, List<TagJsonReq> tagJsonReqs) {
        Optional<DWDatabase> dwDatabase = dwRepository.findById(databaseId);

        if (!dwDatabase.isPresent()) {
            return;
        }

        DWDatabase database = dwDatabase.get();

        //不是PDDOC类型数据库不用上传tagjson
        if (!DWDataFormat.isPDd2r(database.getDataFormat()) && !DWDataFormat.isPDdoc(database.getDataFormat())) {
            throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
        }


        List<DWTableRsp> tables = findTableAll(SessionHolder.getUserId(), databaseId);
        if (tables == null || tables.isEmpty()) {
            throw BizException.of(KgmsErrorCodeEnum.EMTRY_TABLE_NOT_UPLOAD_MODEL_ERROR);
        }


        List<String> tableNames = tables.stream().map(DWTableRsp::getTableName).collect(Collectors.toList());
        List<ModelSchemaConfigRsp> modelSchemaConfig = tagJsonReqs.stream().map(s -> {
            ModelSchemaConfigRsp rsp = new ModelSchemaConfigRsp();
            BeanUtils.copyProperties(s, rsp);
            return rsp;
        }).collect(Collectors.toList());

        for (ModelSchemaConfigRsp schema : modelSchemaConfig) {
            if (!tableNames.contains(schema.getTableName())) {
                throw BizException.of(KgmsErrorCodeEnum.EMTRY_TABLE_NOT_UPLOAD_MODEL_ERROR);
            }

            Set<String> entry = schema.getEntity();

            if (schema.getAttr() != null && !schema.getAttr().isEmpty()) {
                schema.getAttr().forEach(attrBean -> {
                    if (!entry.contains(attrBean.getDomain())) {
                        throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_DOMAIN_NOT_EXIST_ERROR);
                    }

                    if (!PaserYaml2SchemaUtil.attrTypeList.contains(attrBean.getDataType())) {
                        throw BizException.of(KgmsErrorCodeEnum.TAG_ATTR_TYPE_PARSER_ERROR);
                    }
                });


            }

            if (schema.getRelation() != null && !schema.getRelation().isEmpty()) {
                schema.getRelation().forEach(relationBean -> {
                    if (!entry.contains(relationBean.getDomain())) {
                        throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_DOMAIN_NOT_EXIST_ERROR);
                    }
                    if (!entry.containsAll(relationBean.getRange())) {
                        throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_RANGE_NOT_EXIST_ERROR);
                    }

                    Set<ModelRelationAttrBeanRsp> relationAttrs = relationBean.getAttrs();
                    if (relationAttrs != null && !relationAttrs.isEmpty()) {
                        relationAttrs.forEach(relationAttr -> {
                            if (!PaserYaml2SchemaUtil.attrTypeList.contains(relationAttr.getDataType())) {
                                throw BizException.of(KgmsErrorCodeEnum.TAG_ATTR_TYPE_PARSER_ERROR);
                            }
                        });
                    }

                });
            }
        }

        database.setTagJson(modelSchemaConfig);

        dwRepository.save(database);
        return;
    }

    @Override
    public List<ModelSchemaConfigRsp> getTagJson(Long databaseId) {
        Optional<DWDatabase> dwDatabase = dwRepository.findById(databaseId);

        if (!dwDatabase.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }

        DWDatabase database = dwDatabase.get();

        //不是PDDOC类型数据库不用上传tagjson
        if (!DWDataFormat.isPDd2r(database.getDataFormat()) && !DWDataFormat.isPDdoc(database.getDataFormat())) {
            throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
        }

        return database.getTagJson();
    }

    @Override
    public List<CustomTableRsp> getCustomLabel(Long databaseId, Boolean isDefault) {
        Optional<DWDatabase> dwDatabase = dwRepository.findById(databaseId);

        if (!dwDatabase.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }
        DWDatabase database = dwDatabase.get();

        if (!DWDataFormat.isCustom(database.getDataFormat())) {
            throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
        }

        List<DWTableRsp> tables = findTableAll(SessionHolder.getUserId(), databaseId);

        if (tables == null || tables.isEmpty()) {
            throw BizException.of(KgmsErrorCodeEnum.EMTRY_TABLE_NOT_UPLOAD_MODEL_ERROR);
        }

        List<CustomTableRsp> customTableRsps;

        if(isDefault != null && isDefault){

            customTableRsps = ExampleYaml.createCustom(tables);

        }else{

            customTableRsps = database.getTableLabels();
        }
        return customTableRsps;
    }

    @Override
    public List<String> getTableFieldEnum(Long databaseId, String tableName, String field) {
        Optional<DWDatabase> dwDatabase = dwRepository.findById(databaseId);

        if (!dwDatabase.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }
        DWDatabaseRsp database = ConvertUtils.convert(DWDatabaseRsp.class).apply(dwDatabase.get());

        if (!DWDataFormat.isCustom(database.getDataFormat())) {
            throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
        }

        List<String> fieldEnums;
        if (database.getDataType().equals(DataType.MONGO.getDataType())) {

            try {
                fieldEnums = getMongoAggr(database,tableName,field);

            } catch (Exception e) {
                throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_FIND_ERROR);
            }
        } else {

            DataSource dataSource = getDataSource(database);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            String sql = getTableFieldEnumSql(database.getDataType(),tableName,field);

            try {
                fieldEnums = jdbcTemplate.queryForList(sql, String.class);
            } catch (Exception e) {
                throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_FIND_ERROR);
            } finally {
                try {
                    if (dataSource != null && dataSource.getConnection() != null) {
                        dataSource.getConnection().close();
                    }
                } catch (Exception e) {
                }
            }

        }


        return fieldEnums;
    }

    private List<String> getMongoAggr(DWDatabaseRsp dwDatabase, String tableName,String field) {
        MongoClient mongoClient = null;
        try {
            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
            //ServerAddress()两个参数分别为 服务器地址 和 端口
            ServerAddress serverAddress = new ServerAddress(dwDatabase.getAddr().get(0).split(":")[0], Integer.parseInt(dwDatabase.getAddr().get(0).split(":")[1]));
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
            addrs.add(serverAddress);

            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码

            if (StringUtils.hasText(dwDatabase.getUsername()) && StringUtils.hasText(dwDatabase.getPassword())) {
                MongoCredential credential = MongoCredential.createScramSha1Credential(dwDatabase.getUsername(), dwDatabase.getDbName(), dwDatabase.getPassword().toCharArray());
                List<MongoCredential> credentials = new ArrayList<MongoCredential>();
                credentials.add(credential);
                mongoClient = new MongoClient(addrs, credentials);
            } else {
                mongoClient = new MongoClient(addrs);
            }

            //通过连接认证获取MongoDB连接
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase(dwDatabase.getDbName());


            MongoCollection<Document> collection  = mongoDatabase.getCollection(tableName);
            List<String> colls = new ArrayList<>();

            Document group = new Document();
            group.put("$group", new Document("_id", "$"+field));
            AggregateIterable<Document> aggr = collection.aggregate(Lists.newArrayList(group)).batchSize(50);

            MongoCursor<Document> cursor = aggr.iterator();

            while (cursor.hasNext()) {
                Document item_doc = cursor.next();
                Object value = item_doc.get("_id", Object.class);

                colls.add(value+"");
            }

            return colls;
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_FIND_ERROR);
        } finally {
            if (mongoClient != null) {
                try {
                    mongoClient.close();
                } catch (Exception e) {
                }
            }
        }

    }

    private String getTableFieldEnumSql(Integer dataType,String tableName,String field) {

        return "select "+field +" as field from " + tableName + " group by "+ field +" limit 50";
    }

    @Override
    public void updateCustomLabel(Long databaseId, List<CustomTableRsp> tableLabels) {
        Optional<DWDatabase> dwDatabase = dwRepository.findById(databaseId);

        if (!dwDatabase.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }
        DWDatabase database = dwDatabase.get();

        if (!DWDataFormat.isCustom(database.getDataFormat())) {
            throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
        }

        database.setTableLabels(tableLabels);
        dwRepository.save(database);
    }

    @Override
    public DWDatabaseRsp findDatabaseByDataName(String dataName) {

        Optional<DWDatabase> databaseOpt = dwRepository.findOne(Example.of(DWDatabase.builder().dataName(dataName).build()));

        return ConvertUtils.convert(DWDatabaseRsp.class).apply(databaseOpt.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST)));
    }

    @Override
    public void upload(String userId, Long databaseId, Long tableId, MultipartFile file) {

        List<DataSetSchema> schemas = null;
        String tableName = null;
        //文件上传 本地库
        DWDatabaseRsp database = getDetail(databaseId);

        if (tableId != null) {
            Optional<DWTable> tableOptional = tableRepository.findById(tableId);
            if (tableOptional.isPresent()) {
                DWTable table = tableOptional.get();

                //远程表不能上传文件
                if (table.getCreateWay() == null) {
                    table.setCreateWay(2);
                    tableRepository.save(table);
                } else if (table.getCreateWay().equals(1)) {
                    throw BizException.of(KgmsErrorCodeEnum.TABLE_CREATE_WAY_ERROR);
                }

                schemas = table.getSchema();
                List<DataSetSchema> tableSchemas = schemaResolve(file, null);
                if (schemas == null) {

                    if((DWDataFormat.isPDd2r(database.getDataFormat()) || DWDataFormat.isPDdoc(database.getDataFormat())) && StringUtils.hasText(table.getPdSingleField()) ){
                        if(tableSchemas != null && !tableSchemas.isEmpty()){
                            List<DataSetSchema> schemaList = new ArrayList<>(1);
                            for(DataSetSchema s: schemaList) {
                                if(table.getPdSingleField().equals(s.getField())){
                                    schemaList.add(s);
                                    break;
                                }
                            }

                            if(!schemaList.isEmpty()){
                                schemas = schemaList;
                            }
                        }

                    }else{
                        schemas = tableSchemas;
                    }
                    table.setSchema(schemas);
                    table.setFields(transformFields(schemas));
                    tableRepository.save(table);
                }

                if (DWDataFormat.isPDdoc(database.getDataFormat())) {
                    if(StringUtils.hasText(table.getPdSingleField())){
                        checkPDDocSchema(schemas,Lists.newArrayList(table.getPdSingleField()));
                    }else{
                        checkPDDocSchema(schemas,null);
                    }
                }else if(DWDataFormat.isPDd2r(database.getDataFormat())){
                    if(StringUtils.hasText(table.getPdSingleField())){
                        checkPDD2rSchema(schemas,Lists.newArrayList(table.getPdSingleField()));
                    }else{
                        checkPDD2rSchema(schemas,null);
                    }
                } else if (DWDataFormat.isStandard(database.getDataFormat()) && StringUtils.hasText(table.getMapper())) {
                    List<DataSetSchema> industrySchema = getIndustryTableSchema(databaseId, table.getMapper());
                    checkIndutrySchema(industrySchema, schemas);
                }
                tableName = table.getTableName();

            }
        }

        //写入数据

        Map<String, DataSetSchema> schemaMap = new HashMap<>();
        if (schemas != null) {
            for (DataSetSchema o : schemas) {
                schemaMap.put(o.getField(), o);
            }
        }

        try (DataOptProvider provider = getProvider(database.getDataName(), tableName)) {
            String filename = file.getOriginalFilename();
            if (filename != null) {
                int i = filename.lastIndexOf(".");
                String extName = filename.substring(i);
                Long sum = 0L;
                if (KgmsConstants.FileType.XLSX.equalsIgnoreCase(extName) || KgmsConstants.FileType.XLS.equalsIgnoreCase(extName)) {
                    sum = excelFileHandle(provider, schemaMap, file);
                } else if (KgmsConstants.FileType.JSON.equalsIgnoreCase(extName)) {
                    sum = jsonFileHandle(provider, schemaMap, file);
                }

                writeInsertCount(database, tableName, sum);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.FILE_IMPORT_ERROR);
        }

    }

    private void writeInsertCount(DWDatabaseRsp database, String tableName, Long sum) {

        if (sum == null || sum.equals(0L)) {
            return;
        }
        Long now = System.currentTimeMillis();
        Long logTimeStamp = now - (now % (1000 * 60 * 60));
        Map<String, Object> search = new HashMap<>();
        search.put("dataName", database.getDataName());
        search.put("tableName", tableName);
        search.put("logTimeStamp", logTimeStamp);
        search.put("userId", database.getUserId());
        Map<String, Object> query = new HashMap<>();
        query.put("search", search);
        try(DataOptProvider provider = getProvider(KETTLE_LOGS_DATABASE, KETTLE_LOGS_COLLECTION)) {
            List<Map<String, Object>> rs = provider.find(0, 1, query);
            if (rs == null || rs.isEmpty()) {

                Map<String, Object> value = new HashMap<>();

                value.put("logTimeStamp", logTimeStamp);
                value.put("time_flag", "hour");
                value.put("W", new Long(sum));
                value.put("dataName", database.getDataName());
                value.put("dbId", database.getId());
                value.put("dbTitle", database.getTitle());
                value.put("tableName", tableName);
                value.put("tbName", tableName);
                value.put("target", tableName);
                value.put("userId", database.getUserId());
                value.put("updateTime", now);
                provider.insert(value);
            } else {

                Map<String, Object> value = rs.get(0);
                Long count = value.get("W") == null ? 0L : Long.parseLong(value.get("W").toString());

                count += sum;
                value.put("W", new Long(count));
                value.put("updateTime", now);
                String id = value.get(MONGO_ID).toString();
                value.remove(MONGO_ID);
                provider.update(id, value);

            }
        }catch (Exception e){
        }

    }

    public DataOptProvider getProvider(String dbName, String tbName) {
        DataOptConnect connect = getDefaultOpt(dbName, tbName);
        return DataOptProviderFactory.createProvider(connect, DataType.MONGO);
    }

    @Override
    public DWTableRsp createTable(String userId, DWTableReq req) {

        DWTable target = new DWTable();
        BeanUtils.copyProperties(req, target);

        DWDatabaseRsp dwDatabase = getDetail(req.getDwDataBaseId());

        Optional<DWTable> opt = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(req.getDwDataBaseId()).tableName(req.getTitle()).build()));

        if (opt.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_NAME_EXIST);
        }

        if (StringUtils.hasText(req.getTableName())) {

            DWTable tableMap = DWTable.builder().dwDataBaseId(req.getDwDataBaseId()).mapper(req.getTableName()).build();
            if (req.getModelId() != null) {
                tableMap.setModelId(req.getModelId());
            }

            Optional<DWTable> optTb = tableRepository.findOne(Example.of(tableMap));

            if (optTb.isPresent()) {
                throw BizException.of(KgmsErrorCodeEnum.MAP_TABLE_EXIST);
            }

            target.setTableName(req.getTitle());
            target.setMapper(req.getTableName());
            target.setModelId(req.getModelId());
        } else {
            target.setTableName(req.getTitle());
        }
        target.setTitle(req.getTitle());
        //本地库创建结构
        List<DataSetSchema> schema = null;
        if (StringUtils.hasText(req.getTableName())) {
            schema = getIndustryTableSchema(req.getDwDataBaseId(), req.getTableName());
            target.setKtr(getIndustryTableKtr(req.getDwDataBaseId(), req.getTableName()));
        } else if (DWDataFormat.isPDdoc(dwDatabase.getDataFormat()) || DWDataFormat.isPDd2r(dwDatabase.getDataFormat())) {
            if (StringUtils.hasText(req.getField())) {
                target.setPdSingleField(req.getField());
            } else {
                schema = schemaResolve(null, dwDatabase.getDataFormat());
            }
        } else {
            schema = req.getSchemas();
        }
        target.setFields(transformFields(schema));
        target.setSchema(schema);
        target.setCreateWay(2);

        if (schema != null && !schema.isEmpty()) {
            try (DataOptProvider provider = new MongodbOptProvider(getDefaultOpt(dwDatabase.getDataName(), target.getTableName()))) {
                provider.createTable(schema);
            } catch (Exception e) {
                throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
            }
        }

        target = tableRepository.save(target);

        return table2rsp.apply(target);
    }


    @Override
    public List<DataSetSchema> schemaResolve(MultipartFile file, Integer dataFormat) {

        if (DWDataFormat.isPDdoc(dataFormat)) {
            return beanSchema(PdDocument.class);
        }

        if(DWDataFormat.isPDd2r(dataFormat)){
            return beanSchema(Pdd2rBean.class);
        }

        return dataSetService.schemaResolve(null, file);
    }

    private List<DataSetSchema> beanSchema(Class c) {
        Field[] fields = c.getDeclaredFields();

        List<DataSetSchema> schemas = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {

            Field field = fields[i];

            String name = field.getName();
            DataSetSchema schema = new DataSetSchema();
            schema.setField(name);
            try {
                if(field.getType() == Integer.class){
                    schema.setType(FieldType.INTEGER.getCode());
                }else if(field.getType() == Long.class){
                    schema.setType(FieldType.LONG.getCode());
                }else if(field.getType() == String.class){
                    schema.setType(FieldType.STRING.getCode());
                }else if(field.getType() == Date.class){
                    schema.setType(FieldType.DATE.getCode());
                }else if(field.getType() == List.class || field.getType() == Set.class){
                    schema.setType(FieldType.ARRAY.getCode());
                }else{
                    schema.setType(FieldType.STRING.getCode());
                }
            } catch (Exception e) {
                schema.setType(FieldType.STRING.getCode());
            }
            schemas.add(schema);

        }
        return schemas;
    }

    @Override
    public List<DWTableRsp> findTableAll(String userId, Long databaseId) {

        List<DWTable> dwTableList = tableRepository.findAll(Example.of(DWTable.builder().dwDataBaseId(databaseId).build()), Sort.by(Sort.Order.desc("createAt")));

        List<DWTableRsp> tableRsps = dwTableList.stream().map(table2rsp).collect(Collectors.toList());
        if (tableRsps == null || tableRsps.isEmpty()) {
            return tableRsps;
        }

        DWDatabaseRsp database = getDetail(databaseId);
        if (database == null) {
            return tableRsps;
        }

        if (DWDataFormat.isFile(database.getDataFormat())) {
            //文件系统，增加文件夹拥有文件数量
            for (DWTableRsp tableRsp : tableRsps) {
                tableRsp.setFileCount(setTableFileCount(tableRsp.getId(), database.getId()));
            }
        }

        tableRsps.forEach(t -> {
            t.setDataName(database.getDataName());
            t.setDbName(database.getDbName());
        });

        return tableRsps;
    }

    @Override
    public List<JSONObject> getRemoteTables(String userId, Long databaseId) {

        DWDatabaseRsp dwDatabase = getDetail(databaseId);

        if (dwDatabase == null || dwDatabase.getAddr() == null || dwDatabase.getAddr().isEmpty() || (dwDatabase.getAddr().size() == 1 && !StringUtils.hasText(dwDatabase.getAddr().get(0)))) {
            return new ArrayList<>();
        }

        List<String> tables;

        Map<String,List<DataSetSchema>> schemaMap = new HashMap<>();
        if (dwDatabase.getDataType().equals(DataType.MONGO.getDataType())) {

            try {
                tables = getMongoCollection(dwDatabase);

            } catch (Exception e) {
                throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_FIND_ERROR);
            }
        } else {

            DataSource dataSource = getDataSource(dwDatabase);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            String sql = getQueryTableSql(dwDatabase.getDataType());

            try {
                tables = jdbcTemplate.queryForList(sql, String.class);
            } catch (Exception e) {
                throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_FIND_ERROR);
            } finally {
                try {
                    if (dataSource != null && dataSource.getConnection() != null) {
                        dataSource.getConnection().close();
                    }
                } catch (Exception e) {
                }
            }

        }


        if(tables != null && !tables.isEmpty()){

            for(String table : tables){
                List<DataSetSchema> schemas = getTableSchema(dwDatabase, table);
                schemaMap.put(table,schemas);
            }

        }

        List<JSONObject> tabList = Lists.newArrayList();
        if (tables != null && !tables.isEmpty()) {

            List<DWTableRsp> tableRsps = findTableAll(userId, databaseId);
            List<String> existList = Lists.newArrayList();
            if (tableRsps != null && !tableRsps.isEmpty()) {
                tableRsps.forEach(rsp -> {
                    if (StringUtils.hasText(rsp.getTbName())) {
                        existList.add(rsp.getTbName());
                    }
                });
            }

            for (String t : tables) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tableName", t);
                jsonObject.put("schema",schemaMap.get(t));
                if (existList.contains(t)) {
                    jsonObject.put("status", 1);
                } else {
                    jsonObject.put("status", 0);
                }

                tabList.add(jsonObject);

            }

        }

        return tabList;
    }

    private List<String> getMongoCollection(DWDatabaseRsp dwDatabase) {

        MongoClient mongoClient = null;
        try {
            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
            //ServerAddress()两个参数分别为 服务器地址 和 端口
            ServerAddress serverAddress = new ServerAddress(dwDatabase.getAddr().get(0).split(":")[0], Integer.parseInt(dwDatabase.getAddr().get(0).split(":")[1]));
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
            addrs.add(serverAddress);

            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码

            if (StringUtils.hasText(dwDatabase.getUsername()) && StringUtils.hasText(dwDatabase.getPassword())) {
                MongoCredential credential = MongoCredential.createScramSha1Credential(dwDatabase.getUsername(), dwDatabase.getDbName(), dwDatabase.getPassword().toCharArray());
                List<MongoCredential> credentials = new ArrayList<MongoCredential>();
                credentials.add(credential);
                mongoClient = new MongoClient(addrs, credentials);
            } else {
                mongoClient = new MongoClient(addrs);
            }

            //通过连接认证获取MongoDB连接
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase(dwDatabase.getDbName());


            Iterable<String> it = mongoDatabase.listCollectionNames();
            List<String> colls = new ArrayList<>();
            it.forEach(coll -> colls.add(coll));

            return colls;
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_FIND_ERROR);
        } finally {
            if (mongoClient != null) {
                try {
                    mongoClient.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private String getQueryTableSql(Integer dataType) {

        if (DataType.DM.equals(DataType.findType(dataType))) {
            return "select t.table_name tableName from user_tables t;";
        } else {
            return "show tables;";
        }
    }

    @Override
    public void addRemoteTables(String userId, Long databaseId, List<RemoteTableAddReq> reqList) {

        if (reqList == null || reqList.isEmpty()) {
            return;
        }

        DWDatabaseRsp database = getDetail(databaseId);

        if (database == null) {
            return;
        }

        for (RemoteTableAddReq req : reqList) {

            if (!StringUtils.hasText(req.getTbName())) {
                throw BizException.of(KgmsErrorCodeEnum.NOT_SELECT_MAP_TABLE);
            }

            Optional<DWTable> optTb = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(databaseId).tbName(req.getTbName()).build()));

            if (optTb.isPresent()) {
                throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_EXIST);
            }

            Optional<DWTable> optTbName = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(databaseId).tableName(req.getTbName()).build()));

            if (optTbName.isPresent()) {
                throw BizException.of(KgmsErrorCodeEnum.TABLE_NAME_EXIST);
            }


            //行业表映射
            if (StringUtils.hasText(req.getTableName())) {

                DWTable tableopt = DWTable.builder().dwDataBaseId(databaseId).mapper(req.getTableName()).build();
                if (req.getModelId() != null) {
                    tableopt.setModelId(req.getModelId());
                }
                Optional<DWTable> opt = tableRepository.findOne(Example.of(tableopt));

                if (opt.isPresent()) {
                    throw BizException.of(KgmsErrorCodeEnum.MAP_TABLE_EXIST);
                }

                List<DataSetSchema> schemaList = getIndustryTableSchema(databaseId, req.getTableName());
                List<DataSetSchema> tableSchemaList = getTableSchema(database, req.getTbName());
                if (schemaList == null) {
                    schemaList = getTableSchema(database, req.getTbName());
                } else {
                    checkIndutrySchema(schemaList, tableSchemaList);
                }

                DWTable table = DWTable.builder()
                        .dwDataBaseId(databaseId)
                        .schema(schemaList)
                        .tableName(req.getTbName())
                        .tbName(req.getTbName())
                        .title(req.getTbName())
                        .ktr(getIndustryTableKtr(databaseId, req.getTableName()))
                        .createWay(1)
                        .fields(transformFields(schemaList))
                        .mapper(req.getTableName())
                        .modelId(req.getModelId())
                        .build();

                tableRepository.save(table);
            } else {

                List<DataSetSchema> schemaList = getTableSchema(database, req.getTbName());

                List<String> fields = null;

                if (DWDataFormat.isPDdoc(database.getDataFormat())) {

                    if(StringUtils.hasText(req.getField())){
                        DataSetSchema dataSetSchema = req.getDataSetSchema();
                        schemaList = Lists.newArrayList(dataSetSchema);
                        fields = transformFields(schemaList);
                        checkPDDocSchema(schemaList,fields);
                    }else{
                        checkPDDocSchema(schemaList,null);
                    }

                }else if(DWDataFormat.isPDd2r(database.getDataFormat())){


                    if(StringUtils.hasText(req.getField())){
                        DataSetSchema dataSetSchema = req.getDataSetSchema();
                        schemaList = Lists.newArrayList(dataSetSchema);
                        fields = transformFields(schemaList);
                        checkPDD2rSchema(schemaList,fields);
                    }else{
                        checkPDD2rSchema(schemaList,null);
                    }

                }

                if(fields == null){
                    fields = transformFields(schemaList);
                }


                DWTable table = DWTable.builder()
                        .dwDataBaseId(databaseId)
                        .tableName(req.getTbName())
                        .schema(schemaList)
                        .tbName(req.getTbName())
                        .title(req.getTbName())
                        .createWay(1)
                        .fields(fields)
                        .pdSingleField(req.getField())
                        .build();

                tableRepository.save(table);

            }

        }

    }

    private void checkPDD2rSchema(List<DataSetSchema> schemaList,List<String> fields) {
        if (schemaList == null || schemaList.isEmpty()) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_SCHEMA_MISMATCHING_STIPULATE);
        }
        boolean flag = false;

        if(fields != null && fields.size() == 1){

            for (DataSetSchema dataSetSchema : schemaList) {
                if (Objects.equals(dataSetSchema.getField(), fields.get(0))) {
                    flag = true;
                    break;
                }
            }

        }else {
            for (DataSetSchema dataSetSchema : schemaList) {
                if (Objects.equals(dataSetSchema.getField(), "entities")
                        || Objects.equals(dataSetSchema.getField(), "relations")) {

                    flag = true;

                    break;
                }
            }
        }

        if (!flag) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_SCHEMA_MISMATCHING_STIPULATE);
        }
    }

    private void checkPDDocSchema(List<DataSetSchema> schemaList,List<String> fields) {

        if (schemaList == null || schemaList.isEmpty()) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_SCHEMA_MISMATCHING_STIPULATE);
        }

        boolean flag = false;

        if(fields != null && fields.size() == 1){

            for (DataSetSchema dataSetSchema : schemaList) {
                if (Objects.equals(dataSetSchema.getField(), fields.get(0))) {
                    flag = true;
                    break;
                }
            }

        }else{
            for (DataSetSchema dataSetSchema : schemaList) {
                if (Objects.equals(dataSetSchema.getField(), "pdEntity")
                        || Objects.equals(dataSetSchema.getField(), "pdRelation")
                        || Objects.equals(dataSetSchema.getField(), "pdEvent")) {

                    flag = true;

                    break;
                }
            }
        }

        if (!flag) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_SCHEMA_MISMATCHING_STIPULATE);
        }

    }

    private void checkIndutrySchema(List<DataSetSchema> schemaList, List<DataSetSchema> tableSchemaList) {

        List<String> schemaFieldList = transformFields(schemaList);
        List<String> tableFieldList = transformFields(tableSchemaList);

        if (!tableFieldList.containsAll(schemaFieldList)) {
            throw BizException.of(KgmsErrorCodeEnum.TABLE_SCHEMA_MISMATCHING_STIPULATE);
        }
    }

    @Override
    public Object testConnect(DWConnceReq req) {

        if (DataType.MONGO.equals(req.getType())) {
            return getMongoClient(req);
        }
        DataSource dataSource = getDataSource(req);
        Map<String, String> map = new HashMap<>();
        String s = "";
        Connection connection = null;
        try {
            if (DataType.ORACLE.equals(req.getType())) {
                Class.forName("oracle.jdbc.OracleDriver");
                DriverManager.setLoginTimeout(1);
                String url = "jdbc:oracle:thin:@" + req.getAddr().get(0) + ":" + req.getDbName();
                Properties props = new Properties();
                props.put("user", req.getUsername());
                props.put("password", req.getPassword());
                props.put("oracle.net.CONNECT_TIMEOUT", "1000");
                props.put("oracle.jdbc.ReadTimeout", "1000");
                connection = DriverManager.getConnection(url, props);
            } else {
                connection = dataSource.getConnection();
            }
            s = "连接测试成功!";
            map.put("status", "success");
        } catch (Exception e) {
            String sysErr = e.getMessage();
            s = "连接失败";
            if ("The url cannot be null".equals(sysErr) || sysErr.indexOf("String index out of range") != -1) {
                s = "请输入正确的数据库类型!";
            }
            if (sysErr.indexOf("Communications link failure") != -1) {
                s = "请输入正确的数据库地址或者端口!";
            }
            if (sysErr.indexOf("Access denied for user") != -1) {
                s = "请输入正确的用户名和密码!";
            }
            map.put("status", "fail");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        map.put("msg", s);
        return map;
    }

    private Object getMongoClient(DWConnceReq req) {

        Map<String, String> map = new HashMap<>();
        String s = "";
        MongoClient mongoClient = null;
        try {
            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
            //ServerAddress()两个参数分别为 服务器地址 和 端口
            ServerAddress serverAddress = new ServerAddress(req.getAddr().get(0).split(":")[0], Integer.parseInt(req.getAddr().get(0).split(":")[1]));
            List<ServerAddress> addrs = new ArrayList<>();
            addrs.add(serverAddress);

            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码

            if (StringUtils.hasText(req.getUsername()) && StringUtils.hasText(req.getPassword())) {
                MongoCredential credential = MongoCredential.createScramSha1Credential(req.getUsername(), req.getDbName(), req.getPassword().toCharArray());
                List<MongoCredential> credentials = new ArrayList<MongoCredential>();
                credentials.add(credential);
                mongoClient = new MongoClient(addrs, credentials);
            } else {
                mongoClient = new MongoClient(addrs);
            }

            //通过连接认证获取MongoDB连接
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase(req.getDbName());

            Iterable<String> it = mongoDatabase.listCollectionNames();

            List<String> colls = new ArrayList<>();
            it.forEach(coll -> colls.add(coll));
            if (colls.isEmpty()) {
                s = "连接测试失败";
                map.put("status", "fail");
            } else {
                s = "连接测试成功!";
                map.put("status", "success");
            }
        } catch (Exception e) {
            s = "连接失败";
            map.put("status", "fail");
        } finally {
            if (mongoClient != null) {
                try {
                    mongoClient.close();
                } catch (Exception e) {
                }
            }
        }
        map.put("msg", s);
        return map;

    }

    @Override
    public List<DWTable> getTableByIds(List<Long> tableIds) {

        List<DWTable> tables = tableRepository.findByIdIn(tableIds);
        return tables;
    }

    @Override
    public void setTableCron(String userId, List<DWTableCronReq> reqs) {

        if (reqs == null || reqs.isEmpty()) {
            return;
        }

        DWDatabaseRsp database = getDetail(reqs.get(0).getDatabaseId());

        if (database == null) {
            return;
        }

        for (DWTableCronReq req : reqs) {

            Optional<DWTable> tableOpt = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(req.getDatabaseId()).id(req.getTableId()).build()));

            if (!tableOpt.isPresent()) {

                throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
            }

            DWTable table = tableOpt.get();


            if (req.getIsAll() != null && req.getIsAll().equals(2) && table.getFields().contains(req.getField())) {
                table.setQueryField(req.getField());
            }
            table.setCron(req.getCron());
            table.setIsAll(req.getIsAll());
            table.setSchedulingSwitch(req.getSchedulingSwitch());
            table.setIsWriteDW(req.getIsWriteDW());

            tableRepository.save(table);

            DWTableRsp tableRsp = ConvertUtils.convert(DWTableRsp.class).apply(table);

            if (tableRsp.getIsAll() != null && tableRsp.getIsAll().equals(2) && tableRsp.getQueryField() == null) {
                continue;
            }
            updateSchedulingConfig(database, tableRsp, tableRsp.getDwDataBaseId(), tableRsp.getTableName(), req.getCron(), req.getIsAll(), req.getField());

            if (tableRsp.getCreateWay().equals(1) && req.getIsWriteDW().equals(1)) {
                createTableSchedulingConfig(table);
            }

        }

    }

    @Override
    public Page<DWDatabaseRsp> list(String userId, DWDatabaseQueryReq req) {
        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());


        Specification<DWDatabase> specification = new Specification<DWDatabase>() {
            @Override
            public Predicate toPredicate(Root<DWDatabase> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (req.getCreateWay() != null) {

                    Predicate databaseId = criteriaBuilder.equal(root.get("createWay").as(Integer.class), req.getCreateWay());
                    predicates.add(criteriaBuilder.or(databaseId, criteriaBuilder.isNull(root.get("createWay").as(Integer.class))));
                }

                if (req.getDataFormat() != null) {
                    Predicate dataFormat = criteriaBuilder.equal(root.get("dataFormat").as(Integer.class), req.getDataFormat());
                    predicates.add(dataFormat);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Page<DWDatabase> all = dwRepository.findAll(specification, pageable);


        Page<DWDatabaseRsp> map = all.map(ConvertUtils.convert(DWDatabaseRsp.class));
        return map;
    }

    @Override
    public void tagUpload(Long databaseId, MultipartFile file) {
        if (file == null || !file.getOriginalFilename().endsWith(".json")) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_TYPE_ERROR);
        }
        Optional<DWDatabase> dwDatabase = dwRepository.findById(databaseId);

        if (!dwDatabase.isPresent()) {
            return;
        }

        DWDatabase database = dwDatabase.get();

        //不是PDDOC类型数据库不用上传tagjson
        if (!DWDataFormat.isPDdoc(database.getDataFormat()) && !DWDataFormat.isPDd2r(database.getDataFormat())) {
            throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
        }


        List<DWTableRsp> tables = findTableAll(SessionHolder.getUserId(), databaseId);
        if (tables == null || tables.isEmpty()) {
            throw BizException.of(KgmsErrorCodeEnum.EMTRY_TABLE_NOT_UPLOAD_MODEL_ERROR);
        }


        List<String> tableNames = tables.stream().map(DWTableRsp::getTableName).collect(Collectors.toList());
        String result;
        List<ModelSchemaConfigRsp> modelSchemaConfig;
        try {
            result = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);
            //生成json
            modelSchemaConfig = JacksonUtils.readValue(result, new TypeReference<List<ModelSchemaConfigRsp>>() {
            });
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.TAG_JSON_PASER_ERROR);
        }

        for (ModelSchemaConfigRsp schema : modelSchemaConfig) {
            if (!tableNames.contains(schema.getTableName())) {
                throw BizException.of(KgmsErrorCodeEnum.EMTRY_TABLE_NOT_UPLOAD_MODEL_ERROR);
            }

            Set<String> entry = schema.getEntity();

            if (schema.getAttr() != null && !schema.getAttr().isEmpty()) {
                schema.getAttr().forEach(attrBean -> {
                    if (!entry.contains(attrBean.getDomain())) {
                        throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_DOMAIN_NOT_EXIST_ERROR);
                    }

                    if (!PaserYaml2SchemaUtil.attrTypeList.contains(attrBean.getDataType())) {
                        throw BizException.of(KgmsErrorCodeEnum.TAG_ATTR_TYPE_PARSER_ERROR);
                    }
                });


            }

            if (schema.getRelation() != null && !schema.getRelation().isEmpty()) {
                schema.getRelation().forEach(relationBean -> {
                    if (!entry.contains(relationBean.getDomain())) {
                        throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_DOMAIN_NOT_EXIST_ERROR);
                    }
                    if (!entry.containsAll(relationBean.getRange())) {
                        throw BizException.of(KgmsErrorCodeEnum.SCHEMA_PASER_RANGE_NOT_EXIST_ERROR);
                    }

                    Set<ModelRelationAttrBeanRsp> relationAttrs = relationBean.getAttrs();
                    if (relationAttrs != null && !relationAttrs.isEmpty()) {
                        relationAttrs.forEach(relationAttr -> {
                            if (!PaserYaml2SchemaUtil.attrTypeList.contains(relationAttr.getDataType())) {
                                throw BizException.of(KgmsErrorCodeEnum.TAG_ATTR_TYPE_PARSER_ERROR);
                            }
                        });
                    }

                });
            }
        }

        database.setTagJson(modelSchemaConfig);

        dwRepository.save(database);
        return;
    }

    private List<ModelSchemaConfigRsp> getDatabseModelSchema(String userId, Long id) {

        DWDatabaseRsp database = getDetail(id);

        userId = userClient.getCurrentUserDetail().getData().getId();
        if (!database.getUserId().equals(userId)) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }

        List<DWTableRsp> tables = findTableAll(SessionHolder.getUserId(), id);
        if (tables == null || tables.isEmpty()) {
            throw BizException.of(KgmsErrorCodeEnum.EMTRY_TABLE_NOT_UPLOAD_MODEL_ERROR);
        }

        if (DWDataFormat.isStandard(database.getDataFormat())) {

            //行业模板 根据引入的表获取模式
            Map<String, String> tableMappings = new HashMap<>();
            for (DWTableRsp tableRsp : tables) {
                tableMappings.put(tableRsp.getMapper(), tableRsp.getTableName());
            }

            if (tableMappings == null || tableMappings.isEmpty()) {
                return Lists.newArrayList();
            }


            List<ModelSchemaConfigRsp> schemas = Lists.newArrayList();
            database.getTagJson().forEach(schema -> {

                if (schema != null) {
                    ModelSchemaConfigRsp s = new ModelSchemaConfigRsp();
                    BeanUtils.copyProperties(schema, s);
                    if (tableMappings.containsKey(s.getTableName())) {
                        s.setTableName(tableMappings.get(s.getTableName()));
                        schemas.add(s);
                    }
                }
            });
            return schemas;
        } else if(DWDataFormat.isPDd2r(database.getDataFormat()) || DWDataFormat.isPDdoc(database.getDataFormat())) {

            return database.getTagJson();

        }else if(DWDataFormat.isCustom(database.getDataFormat())){

            List<CustomTableRsp> tableRsps = database.getTableLabels();
            if(tableRsps == null || tableRsps.isEmpty()){
                return new ArrayList<>();
            }

            List<ModelSchemaConfigRsp> modelSchemaConfig = PaserYaml2SchemaUtil.parserLabel2TagJson(tableRsps, tables);
            return modelSchemaConfig;
        }

        return null;
    }

    @Override
    public Integer push(String userId, ModelPushReq req) {

        DWDatabaseRsp database = getDetail(req.getId());
        if (!database.getUserId().equals(userId)) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }

        List<PreBuilderConceptRsp> preBuilderConceptRspList;
        if (DWDataFormat.isStandard(database.getDataFormat()) || DWDataFormat.isPDdoc(database.getDataFormat()) || DWDataFormat.isPDd2r(database.getDataFormat()) || DWDataFormat.isCustom(database.getDataFormat())) {
            //行业标准 or pddoc or pdd2r or 自定义
            List<ModelSchemaConfigRsp> modelSchemaConfigRsp = getDatabseModelSchema(userId, req.getId());

            preBuilderConceptRspList = modelSchema2PreBuilder(modelSchemaConfigRsp);

            if (preBuilderConceptRspList == null || preBuilderConceptRspList.isEmpty()) {
                throw BizException.of(KgmsErrorCodeEnum.EMTRY_MODEL_PUDH_ERROR);
            }


            return preBuilderService.createModel(database, preBuilderConceptRspList, req.getModelType(), database.getTableLabels());

        }

        return null;

        /*else if (DWDataFormat.isCustom(database.getDataFormat())) {
            //自定义
            String yamlContent = database.getYamlContent();

            if (yamlContent == null || yamlContent.isEmpty()) {
                throw BizException.of(KgmsErrorCodeEnum.EMTRY_MODEL_PUDH_ERROR);
            }

            Object value = new Yaml().load(yamlContent);

            List<DWTableRsp> tableRsps = findTableAll(userId, database.getId());

            //生成json
            JSONObject json = JacksonUtils.readValue(JacksonUtils.writeValueAsString(value), JSONObject.class);

            preBuilderConceptRspList = PaserYaml2SchemaUtil.parserYaml2Schema(json, tableRsps);

            if (preBuilderConceptRspList == null || preBuilderConceptRspList.isEmpty()) {
                throw BizException.of(KgmsErrorCodeEnum.EMTRY_MODEL_PUDH_ERROR);
            }

            preBuilderService.createModel(database, preBuilderConceptRspList, req.getModelType(), yamlContent);
        }*/

    }

    @Override
    public void setTableScheduling(String userId, DWTableSchedulingReq req) {
        Optional<DWTable> tableOpt = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(req.getDatabaseId()).id(req.getTableId()).build()));

        if (!tableOpt.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
        }

        DWTable table = tableOpt.get();

        if (StringUtils.hasText(table.getTableName())) {

            table.setSchedulingSwitch(req.getSchedulingSwitch());

            tableRepository.save(table);

            //连接的表，落地才建立任务
            if (table.getCreateWay().equals(1) && table.getIsWriteDW() != null && table.getIsWriteDW().equals(1)) {

                //增量没字段不开启
                if (table.getIsAll() != null && table.getIsAll().equals(2) && table.getQueryField() == null) {
                    return;
                } else {
                    createTableSchedulingConfig(table);
                }
            }

        }

    }

    private void createTableSchedulingConfig(DWTable table) {

        String dwTaskName = AccessTaskType.DW.getDisplayName() + "_" + table.getDwDataBaseId() + "_" + table.getTableName();
        List<String> diss = new ArrayList<>();
        if (table.getIsWriteDW() != null && table.getIsWriteDW().equals(1)) {
            diss.add(dwTaskName);
            accessTaskService.createDwTask(table.getTableName(), table.getDwDataBaseId());
        }

        if (table != null && table.getSchedulingSwitch() != null && table.getSchedulingSwitch().equals(1)) {

            //生成任务配置
            accessTaskService.createKtrTask(table.getTableName(), table.getDwDataBaseId(), table.getTableName(), 1, table.getTableName());
            if (StringUtils.hasText(table.getMapper())) {
                accessTaskService.createTransfer(false, null, table.getTableName(), table.getDwDataBaseId(), diss, null, null, null, table.getTableName());
            } else {
                accessTaskService.createTransfer(false, null, table.getTableName(), table.getDwDataBaseId(), null, diss, null, null, table.getTableName());
            }
        } else {
            //生成任务配置
            accessTaskService.createKtrTask(table.getTableName(), table.getDwDataBaseId(), table.getTableName(), 0, table.getTableName());
            if (StringUtils.hasText(table.getMapper())) {
                accessTaskService.createTransfer(false, null, table.getTableName(), table.getDwDataBaseId(), null, null, diss, null, table.getTableName());
            } else {
                accessTaskService.createTransfer(false, null, table.getTableName(), table.getDwDataBaseId(), null, null, null, diss, table.getTableName());
            }
        }
    }


    @Override
    public ModelSchemaConfigRsp getModel(String userId, Long id) {

        return mergeModel(getDatabseModelSchema(userId, id));

    }

    @Override
    public DWTableRsp findTableByTableName(String userId, Long databaseId, String tableName) {

        Optional<DWTable> dwTable = tableRepository.findOne(Example.of(DWTable.builder().tableName(tableName).dwDataBaseId(databaseId).build()));

        return ConvertUtils.convert(DWTableRsp.class).apply(dwTable.get());
    }

    @Override
    public void unifiedScheduling(String userId, DWTableCronReq req) {

        List<DWTableRsp> tableRspList = findTableAll(userId, req.getDatabaseId());
        if (tableRspList == null || tableRspList.isEmpty()) {
            return;
        }

        DWDatabaseRsp database = getDetail(req.getDatabaseId());
        if (database == null) {
            return;
        }

        for (DWTableRsp tableRsp : tableRspList) {

            if (tableRsp.getTableName() == null || tableRsp.getTableName().isEmpty()) {
                continue;
            }

            String cron = req.getCron();

            tableRsp.setCron(cron);
            if (tableRsp.getFields().contains(req.getField()) || req.getField() == null) {
                tableRsp.setQueryField(req.getField());
            }
            tableRsp.setIsAll(req.getIsAll());

            if (tableRsp.getCreateWay().equals(1)) {
                tableRsp.setSchedulingSwitch(req.getSchedulingSwitch());
                tableRsp.setIsWriteDW(req.getIsWriteDW());
            }

            DWTable table = ConvertUtils.convert(DWTable.class).apply(tableRsp);
            tableRepository.save(table);

            //增量更新但是没有字段，不更新信息
            if (tableRsp.getIsAll() != null && tableRsp.getIsAll().equals(2) && tableRsp.getQueryField() == null) {
                continue;
            }
            updateSchedulingConfig(database, tableRsp, tableRsp.getDwDataBaseId(), tableRsp.getTableName(), req.getCron(), req.getIsAll(), req.getField());

            if (tableRsp.getCreateWay().equals(1) && req.getIsWriteDW().equals(1)) {
                createTableSchedulingConfig(table);
            }

        }

    }

    private void updateSchedulingConfig(DWDatabaseRsp database, DWTableRsp tableRsp, Long dwDataBaseId, String tableName, String cron, Integer isAll, String field) {

        accessTaskService.updateTableSchedulingConfig(database, tableRsp, cron, isAll, field);
    }

    @Override
    public void modelUpload(Long databaseId, MultipartFile file) {
        if (file == null || (!file.getOriginalFilename().endsWith(".json") && !file.getOriginalFilename().endsWith(".yaml"))) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_TYPE_ERROR);
        }

        DWDatabaseRsp database = getDetail(databaseId);

        //不是PD类型数据库不用上传tagjson
        if ((DWDataFormat.isPDd2r(database.getDataFormat()) || DWDataFormat.isPDdoc(database.getDataFormat())) && file.getOriginalFilename().endsWith(".json")) {
            tagUpload(databaseId, file);
        } else if (DWDataFormat.isCustom(database.getDataFormat()) && file.getOriginalFilename().endsWith(".yaml")) {
            yamlUpload(databaseId, file);
        } else {
            throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
        }

    }

    @Override
    public List<DWDatabaseRsp> databaseTableList(String userId) {

        List<DWDatabaseRsp> databases = findAll(userId);
        if (databases == null || databases.isEmpty()) {
            return new ArrayList<>();
        }

        for (DWDatabaseRsp databaseRsp : databases) {
            List<DWTableRsp> tableRsps = findTableAll(userId, databaseRsp.getId());
            databaseRsp.setTables(tableRsps);
            if (DWDataFormat.isFile(databaseRsp.getDataFormat())) {
                //文件系统，增加文件夹拥有文件数量
                for (DWTableRsp tableRsp : tableRsps) {
                    tableRsp.setFileCount(setTableFileCount(tableRsp.getId(), databaseRsp.getId()));
                }
            }
        }
        return databases;
    }

    private Long setTableFileCount(Long tbId, Long dbId) {

        return fileTableRepository.count(Example.of(DWFileTable.builder().tableId(tbId).dataBaseId(dbId).build()));

    }

    @Override
    public List<JSONObject> getDatabaseMappingTable(String userId, Long databaseId) {

        DWDatabaseRsp database = getDetail(databaseId);

        if (database == null || !DWDataFormat.isStandard(database.getDataFormat()) || database.getTagJson() == null || database.getTagJson().isEmpty()) {
            return Lists.newArrayList();
        }

        Map<String, List<Integer>> tables = Maps.newHashMap();
        for (ModelSchemaConfigRsp tagjson : database.getTagJson()) {

            if (tagjson == null) {
                continue;
            }

            List<Integer> modelIds = tables.get(tagjson.getTableName());
            if (modelIds == null) {
                modelIds = new ArrayList<>();
                tables.put(tagjson.getTableName(), modelIds);
            }

            if (tagjson.getModelId() != null) {
                modelIds.add(tagjson.getModelId());
            }
            tables.put(tagjson.getTableName(), modelIds);
        }

        List<JSONObject> rs = Lists.newArrayList();

        if (tables != null && !tables.isEmpty()) {
            List<DWTableRsp> tableRsps = findTableAll(userId, databaseId);
            Map<String, DWTableRsp> t = Maps.newHashMap();
            if (tableRsps != null && !tableRsps.isEmpty()) {
                for (DWTableRsp tableRsp : tableRsps) {

                    if (tableRsp.getMapper() == null) {
                        continue;
                    }
                    t.put(tableRsp.getMapper(), tableRsp);
                }


            }

            for (Map.Entry<String, List<Integer>> table : tables.entrySet()) {

                if (table.getValue() == null || table.getValue().isEmpty()) {

                    JSONObject json = new JSONObject();
                    json.put("tableName", table.getKey());
                    if (t.containsKey(table.getKey())) {
                        json.put("status", 1);
                        json.put("mapper", t.get(table.getKey()).getTableName());
                    } else {
                        json.put("status", 0);
                    }
                    rs.add(json);
                } else {

                    for (Integer modelId : table.getValue()) {
                        JSONObject json = new JSONObject();
                        json.put("tableName", table.getKey());
                        Optional<DWPrebuildModel> model = modelRepository.findOne(Example.of(DWPrebuildModel.builder().id(modelId).build()));
                        if (model.isPresent()) {
                            json.put("modelName", model.get().getName());
                            json.put("modelId", modelId);
                        }

                        if (t.containsKey(table.getKey())) {
                            json.put("status", 1);
                            json.put("mapper", t.get(table.getKey()).getTableName());
                        } else {
                            json.put("status", 0);
                        }
                        rs.add(json);
                    }


                }

            }

        }

        return rs;
    }

    @Override
    public DWDatabaseRsp getDatabase(String userId, Long id) {

        Optional<DWDatabase> database = dwRepository.findOne(Example.of(DWDatabase.builder().userId(userId).id(id).build()));
        if (!database.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }
        return ConvertUtils.convert(DWDatabaseRsp.class).apply(database.get());
    }

    @Override
    public void deteleDatabase(String userId, Long id) {
        Optional<DWDatabase> dwOpt = dwRepository.findOne(Example.of(DWDatabase.builder().userId(userId).id(id).build()));
        if (!dwOpt.isPresent()) {
            return;
        }

        if (DWDataFormat.isFile(dwOpt.get().getDataFormat())) {
            //文件系统
            List<DWFileTable> files = fileTableRepository.findAll(Example.of(DWFileTable.builder().dataBaseId(id).build()));
            if (files != null && !files.isEmpty()) {

                for (DWFileTable file : files) {
                    fileTableRepository.deleteById(file.getId());
                }
            }
        }


        List<DWTableRsp> tableRsps = findTableAll(userId, id);
        if (tableRsps != null && !tableRsps.isEmpty()) {
            for (DWTableRsp tableRsp : tableRsps) {
                deleteTable(userId, id, tableRsp.getId());
            }
        }


        dwRepository.deleteById(id);

        preBuilderService.updateStatusByDatabaseId(id, 2);
    }

    @Override
    public void deleteTable(String userId, Long databaseId, Long tableId) {

        Optional<DWDatabase> dwOpt = dwRepository.findOne(Example.of(DWDatabase.builder().userId(userId).id(databaseId).build()));
        if (!dwOpt.isPresent()) {
            return;
        }

        if (DWDataFormat.isFile(dwOpt.get().getDataFormat())) {
            //文件系统

            List<DWFileTable> files = fileTableRepository.findAll(Example.of(DWFileTable.builder().tableId(tableId).build()));
            if (files != null && !files.isEmpty()) {

                for (DWFileTable file : files) {
                    fileTableRepository.deleteById(file.getId());
                }
            }
        }


        Optional<DWTable> opt = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(databaseId).id(tableId).build()));
        if (opt.isPresent()) {

            try (DataOptProvider provider = getProvider(true, userId, databaseId, tableId, mongoProperties)) {
                provider.dropTable();
            } catch (Exception e) {
                throw BizException.of(KgmsErrorCodeEnum.TABLE_CONNECT_ERROR);
            }

            deleteCountData(databaseId,opt.get().getTableName());

            tableRepository.deleteById(tableId);
        }
    }

    private void deleteCountData(Long databaseId, String tableName) {
        Map<String, Object> search = new HashMap<>();
        search.put("dbId", databaseId);
        search.put("tableName", tableName);
        Map<String, Object> query = new HashMap<>();
        query.put("search", search);
        try(DataOptProvider provider = getProvider(KETTLE_LOGS_DATABASE, KETTLE_LOGS_COLLECTION)) {
            List<Map<String, Object>> rs = provider.find(0,999999,query);
            if(rs != null && !rs.isEmpty()){
                List<String> ids = rs.stream().map(map -> map.get("_id").toString()).collect(Collectors.toList());
                if(ids != null && !ids.isEmpty()){
                    provider.batchDelete(ids);
                }
            }
        }catch (Exception e){
        }

    }

    private DataOptProvider getProvider(Boolean isLocal, String userId, Long datasetId, Long tableId, MongoProperties mongoProperties) {

        DWDatabaseRsp database = getDetail(datasetId);

        if (database == null) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }

        DWTable table = getTableDetail(tableId);
        if (table == null) {
            throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
        }

        DataOptConnect connect = DataOptConnect.of(isLocal, database, table, mongoProperties);
        return DataOptProviderFactory.createProvider(connect);
    }


    @Override
    public List<PreBuilderConceptRsp> modelSchema2PreBuilder(List<ModelSchemaConfigRsp> modelSchemaConfig) {
        Map<String, PreBuilderConceptRsp> conceptRspMap = new HashMap<>();

        if (modelSchemaConfig == null || modelSchemaConfig.isEmpty()) {
            return new ArrayList<>();
        }

        for (ModelSchemaConfigRsp schema : modelSchemaConfig) {
            if (schema == null) {
                continue;
            }
            Set<String> entities = schema.getEntity();

            for (String entity : entities) {

                if (conceptRspMap.containsKey(entity)) {

                    if (!conceptRspMap.get(entity).getTables().contains(schema.getTableName())) {
                        conceptRspMap.get(entity).getTables().add(schema.getTableName());
                    }
                } else {
                    PreBuilderConceptRsp conceptRsp = new PreBuilderConceptRsp();
                    conceptRsp.setName(entity);
                    conceptRsp.setAttrs(new ArrayList<>());
                    conceptRsp.setTables(Lists.newArrayList(schema.getTableName()));
                    conceptRspMap.put(entity, conceptRsp);
                }
            }

            if (schema.getAttr() != null && !schema.getAttr().isEmpty()) {
                for (ModelAttrBeanRsp attrBean : schema.getAttr()) {
                    String domain = attrBean.getDomain();

                    if (!conceptRspMap.containsKey(domain)) {
                        throw BizException.of(KgmsErrorCodeEnum.TAG_JSON_PASER_ERROR);
                    }

                    PreBuilderAttrRsp attrRsp = new PreBuilderAttrRsp();
                    attrRsp.setAttrType(0);
                    attrRsp.setDataType(attrBean.getDataType());
                    attrRsp.setName(attrBean.getName());
                    attrRsp.setTables(Lists.newArrayList(schema.getTableName()));
                    conceptRspMap.get(domain).getAttrs().add(attrRsp);
                }
            }


            if (schema.getRelation() != null && !schema.getRelation().isEmpty()) {

                for (ModelRelationBeanRsp relationBean : schema.getRelation()) {

                    String domain = relationBean.getDomain();

                    if (!conceptRspMap.containsKey(domain)) {
                        throw BizException.of(KgmsErrorCodeEnum.TAG_JSON_PASER_ERROR);
                    }

                    PreBuilderAttrRsp attrRsp = new PreBuilderAttrRsp();
                    attrRsp.setAttrType(1);
                    attrRsp.setName(relationBean.getName());

                    List<ModelRangeRsp> rangeRsps = new ArrayList<>();
                    if(relationBean.getRange() != null && !relationBean.getRange().isEmpty()){
                        for(String r : relationBean.getRange()){
                            rangeRsps.add(ModelRangeRsp.builder().rangeName(r).build());
                        }
                        attrRsp.setRange(rangeRsps);
                    }
                    attrRsp.setTables(Lists.newArrayList(schema.getTableName()));

                    if (relationBean.getAttrs() != null && !relationBean.getAttrs().isEmpty()) {

                        List<PreBuilderRelationAttrRsp> relationAttrRspList = new ArrayList<>();
                        for (ModelRelationAttrBeanRsp relationAttr : relationBean.getAttrs()) {

                            PreBuilderRelationAttrRsp relationAttrRsp = new PreBuilderRelationAttrRsp();
                            relationAttrRsp.setName(relationAttr.getName());
                            relationAttrRsp.setDataType(relationAttr.getDataType());
                            relationAttrRsp.setTables(Lists.newArrayList(schema.getTableName()));
                            relationAttrRspList.add(relationAttrRsp);

                        }

                        attrRsp.setRelationAttrs(relationAttrRspList);
                    }

                    conceptRspMap.get(domain).getAttrs().add(attrRsp);

                }

            }

        }

        List<PreBuilderConceptRsp> conceptRsps = Lists.newArrayList(conceptRspMap.values());
        PaserYaml2SchemaUtil.distinc(conceptRsps);
        return conceptRsps;
    }

    public List<DataSetSchema> getTableSchema(DWDatabaseRsp dwDatabase, String tbName) {

        List<DataSetSchema> rsList = new ArrayList<>();

        if (DataType.MONGO.equals(DataType.findType(dwDatabase.getDataType()))) {

            DataOptConnect connect = DataOptConnect.builder()
                    .database(dwDatabase.getDbName())
                    .addresses(dwDatabase.getAddr())
                    .username(dwDatabase.getUsername())
                    .password(dwDatabase.getPassword())
                    .table(tbName)
                    .build();

            try (DataOptProvider provider = DataOptProviderFactory.createProvider(connect, DataType.MONGO);) {
                List<Map<String, Object>> maps = provider.find(0, 1, null);

                if (maps == null || maps.isEmpty()) {
                    return rsList;
                }

                Map<String, Object> value = maps.get(0);
                for (Map.Entry<String, Object> column : value.entrySet()) {

                    String field = column.getKey();

                    DataSetSchema dataSetSchema = new DataSetSchema();
                    dataSetSchema.setField(field);

                    dataSetSchema.setType(ExampleYaml.readType(column.getValue()).getCode());
                    rsList.add(dataSetSchema);
                }


            } catch (IOException e) {
                throw BizException.of(KgmsErrorCodeEnum.TABLE_CONNECT_ERROR);
            }

        } else if (DataType.MYSQL.equals(DataType.findType(dwDatabase.getDataType()))) {
            DataSource dataSource = getDataSource(dwDatabase);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            String sql = "show full COLUMNS from  " + dwDatabase.getDbName() + "." + tbName;
            List<Map<String, Object>> rs = jdbcTemplate.queryForList(sql);
            for (Map<String, Object> coulmn : rs) {

                String field = coulmn.get("Field").toString();

                DataSetSchema dataSetSchema = new DataSetSchema();
                dataSetSchema.setField(field);
                dataSetSchema.setType(ExampleYaml.readMysqlType(coulmn.get("Type").toString()).getCode());
                dataSetSchema.setDesc(coulmn.get("Comment").toString());
                rsList.add(dataSetSchema);
            }

            try {
                if (dataSource != null && dataSource.getConnection() != null) {
                    dataSource.getConnection().close();
                }
            } catch (Exception e) {
            }
        }

        return rsList;
    }

    private DataSource getDataSource(DWDatabaseRsp dwDatabase) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        if (DataType.MYSQL.equals(DataType.findType(dwDatabase.getDataType()))) {
            dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
            dataSourceBuilder.url("jdbc:mysql://" + dwDatabase.getAddr().get(0) + "/" + dwDatabase.getDbName() + "?characterEncoding=utf8&useSSL=false&connectTimeout=1000&socketTimeout=1000");
        } else if (DataType.ORACLE.equals(DataType.findType(dwDatabase.getDataType()))) {
            dataSourceBuilder.driverClassName("oracle.jdbc.OracleDriver");
            dataSourceBuilder.url("jdbc:oracle:thin:@" + dwDatabase.getAddr().get(0) + ":" + dwDatabase.getDbName());
        } else if (DataType.HIVE.equals(DataType.findType(dwDatabase.getDataType()))) {
            dataSourceBuilder.driverClassName("org.apache.hive.jdbc.HiveDriver");
            dataSourceBuilder.url("jdbc:hive2://" + dwDatabase.getAddr().get(0) + "/" + dwDatabase.getDbName());
        } else if (DataType.DM.equals(DataType.findType(dwDatabase.getDataType()))) {
            dataSourceBuilder.driverClassName("dm.jdbc.driver.DmDriver");
            dataSourceBuilder.url("jdbc:dm://" + dwDatabase.getAddr().get(0) + "/" + dwDatabase.getDbName() + "?characterEncoding=utf8&useSSL=false&connectTimeout=1000&socketTimeout=1000");
        }
        dataSourceBuilder.username(dwDatabase.getUsername());
        dataSourceBuilder.password(dwDatabase.getPassword());
        return dataSourceBuilder.build();

    }

    private DataSource getDataSource(DWConnceReq req) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        if (DataType.MYSQL.equals(req.getType())) {
            dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
            dataSourceBuilder.url("jdbc:mysql://" + req.getAddr().get(0) + "/" + req.getDbName() + "?characterEncoding=utf8&useSSL=false&connectTimeout=1000&socketTimeout=1000");
        } else if (DataType.ORACLE.equals(req.getType())) {
            dataSourceBuilder.driverClassName("oracle.jdbc.OracleDriver");
            dataSourceBuilder.url("jdbc:oracle:thin:@" + req.getAddr().get(0) + ":" + req.getDbName());
        } else if (DataType.HIVE.equals(req.getType())) {
            dataSourceBuilder.driverClassName("org.apache.hive.jdbc.HiveDriver");
            dataSourceBuilder.url("jdbc:hive2://" + req.getAddr().get(0) + "/" + req.getDbName());
        } else if (DataType.DM.equals(req.getType())) {
            dataSourceBuilder.driverClassName("dm.jdbc.driver.DmDriver");
            dataSourceBuilder.url("jdbc:dm://" + req.getAddr().get(0) + "/" + req.getDbName() + "?characterEncoding=utf8&useSSL=false&connectTimeout=1000&socketTimeout=1000");
        }
        dataSourceBuilder.username(req.getUsername());
        dataSourceBuilder.password(req.getPassword());
        return dataSourceBuilder.build();

    }

    private DataOptConnect getDefaultOpt(String dbName, String tbName) {

        DataOptConnect info = new DataOptConnect();

        info.setAddresses(Arrays.asList(mongoProperties.getAddrs()));
        info.setDatabase(dbName);
        info.setTable(tbName);
        info.setUsername(mongoProperties.getUsername());
        info.setPassword(mongoProperties.getPassword());

        return info;
    }

    private List<String> transformFields(List<DataSetSchema> schema) {

        if (schema == null || schema.isEmpty()) {
            return new ArrayList<>();
        }
        LinkedHashSet<String> fields = new LinkedHashSet<>();
        for (DataSetSchema dataSetSchema : schema) {
            fields.add(dataSetSchema.getField());
        }
        return new ArrayList<>(fields);
    }


    private Object fieldFormat(Object o, FieldType field) throws Exception {
        if (o != null && StringUtils.hasText(o.toString())) {
            return field.deserialize(o);
        }
        return o;
    }

    private Long excelFileHandle(DataOptProvider provider, Map<String, DataSetSchema> schemaMap, MultipartFile file) throws Exception {
        List<String> error = new ArrayList<>();
        final Long[] sum = {0L};
        EasyExcel.read(file.getInputStream(), new AnalysisEventListener<Map<Integer, Object>>() {
            Map<Integer, String> head;
            List<Map<String, Object>> mapList = new ArrayList<>();

            @Override
            public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
                head = headMap;
            }

            @Override
            public void invoke(Map<Integer, Object> data, AnalysisContext context) {
                Integer rowIndex = context.readRowHolder().getRowIndex();
                Map<String, Object> map = new HashMap<>();
                try {
                    for (Map.Entry<Integer, Object> entry : data.entrySet()) {
                        String field = head.get(entry.getKey());
                        DataSetSchema dataSetSchema = schemaMap.get(field);
                        if (dataSetSchema != null) {
                            FieldType code = FieldType.findCode(dataSetSchema.getType());
                            Object format = fieldFormat(entry.getValue(), code);
                            if (format != null) {
                                map.put(field, format);
                            }
                        } else {
                            if (field.length() <= 20) {
                                map.put(field, entry.getValue());
                            }
                        }
                    }
                    map.remove("_id");
                    if (!map.isEmpty()) {
                        map.put(DataConst.CREATE_AT, DateUtils.formatDatetime());
                        map.put(DataConst.UPDATE_AT, DateUtils.formatDatetime());
                        mapList.add(map);
                    }
                } catch (Exception e) {
                    error.add("第" + rowIndex + "数据格式不正确");
                }
                if (mapList.size() >= 10000) {
                    provider.batchInsert(mapList);
                    sum[0] += mapList.size();
                    mapList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                if (!mapList.isEmpty()) {
                    provider.batchInsert(mapList);
                    sum[0] += mapList.size();
                    mapList.clear();
                }
            }
        }).sheet().doRead();
        return sum[0];
    }

    private Long jsonFileHandle(DataOptProvider provider, Map<String, DataSetSchema> schemaMap, MultipartFile file) throws Exception {
        List<Map<String, Object>> dataList = JacksonUtils.readValue(file.getInputStream(), new TypeReference<List<Map<String, Object>>>() {
        });
        List<Map<String, Object>> mapList = new ArrayList<>();

        Long sum = 0L;
        for (Map<String, Object> map : dataList) {
            try {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String field = entry.getKey();
                    DataSetSchema dataSetSchema = schemaMap.get(field);
                    if (dataSetSchema != null) {
                        FieldType code = FieldType.findCode(dataSetSchema.getType());
                        Object format = fieldFormat(entry.getValue(), code);
                        if (format != null) {
                            map.put(field, format);
                        }
                    } else {
                        if (field.length() <= 20) {
                            map.put(field, entry.getValue());
                        }
                    }
                }
            } catch (Exception e) {

            }
            map.remove("_id");
            if (!map.isEmpty()) {
                map.put(DataConst.CREATE_AT, DateUtils.formatDatetime());
                map.put(DataConst.UPDATE_AT, DateUtils.formatDatetime());
                mapList.add(map);
            }
            if (mapList.size() >= 10000) {
                provider.batchInsert(mapList);
                sum += mapList.size();
                mapList.clear();
            }
        }
        if (!mapList.isEmpty()) {
            provider.batchInsert(mapList);
            sum += mapList.size();
            mapList.clear();
        }

        return sum;
    }

    @Override
    public BasePage<TableLogListRsp> tableLogList(DWTableLogReq req) {
        MongoCollection<Document> collection = mongoClient.getDatabase(KETTLE_LOGS_DATABASE).getCollection(KETTLE_LOGS_RECODE);
        Integer size = req.getSize();
        Integer page = (req.getPage() - 1) * size;
        String userId = SessionHolder.getUserId() == null ? userClient.getCurrentUserDetail().getData().getId() : SessionHolder.getUserId();
        List<Bson> bsons = new ArrayList<>(2);
        bsons.add(Filters.eq("userId", userId));
        bsons.add(Filters.eq("tableName", req.getTableName()));
        FindIterable<Document> findIterable;
        long count = 0;
        count = collection.countDocuments(Filters.and(bsons));
        findIterable = collection.find(Filters.and(bsons)).sort(Sorts.descending("logTimeStamp")).skip(page).limit(size);
        List<DWDataStatusDatail> dataStores = documentConverter.toBeans(findIterable, DWDataStatusDatail.class);
        List<TableLogListRsp> dataStoreRsps = MapperUtils.map(dataStores, TableLogListRsp.class);
        return new BasePage<>(count, dataStoreRsps);
    }

    @Override
    public List<DWDataStatusRsp> DataStatusList(Long databaseId) {
        MongoCollection<Document> collection = mongoClient.getDatabase(KETTLE_LOGS_DATABASE).getCollection(KETTLE_LOGS_RECODE);
        String userId = SessionHolder.getUserId() == null ? userClient.getCurrentUserDetail().getData().getId() : SessionHolder.getUserId();
        List<Bson> bsons = new ArrayList<>(2);
        bsons.add(Filters.eq("dbId", databaseId));
        bsons.add(Filters.eq("userId", userId));
        FindIterable<Document> findIterable = collection.find(Filters.and(bsons));
        List<DWDataStatusDatail> dwDataList = documentConverter.toBeans(findIterable, DWDataStatusDatail.class);
        List<String> collect = dwDataList.stream().map(DWDataStatusDatail::getTableName).distinct().collect(Collectors.toList());
        List<DWDataStatusDatail> dWDataRspList = new ArrayList<>();
        for (String dWData : collect) {
            List<Bson> bson = new ArrayList<>(3);
            bson.add(Filters.eq("dbId", databaseId));
            bson.add(Filters.eq("tableName", dWData));
            bson.add(Filters.eq("userId", userId));
            FindIterable<Document> dWDataDocuments = collection.find(Filters.and(bson)).sort(Sorts.descending("logTimeStamp")).limit(1);
            List<DWDataStatusDatail> data = documentConverter.toBeans(dWDataDocuments, DWDataStatusDatail.class);
            if (null != data && data.size() != 0) {
                dWDataRspList.add(data.get(0));
            }
        }
        List<DWDataStatusRsp> dWDataRsps = MapperUtils.map(dWDataRspList, DWDataStatusRsp.class);
        return dWDataRsps;
    }
}
