package com.plantdata.kgcloud.domain.dw.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.hiekn.pddocument.bean.PdDocument;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.constant.AccessTaskType;
import com.plantdata.kgcloud.constant.KgmsConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.access.service.AccessTaskService;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.provider.MongodbOptProvider;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWPrebuildModel;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.repository.DWDatabaseRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWPrebuildModelRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWTableRepository;
import com.plantdata.kgcloud.domain.dw.req.*;
import com.plantdata.kgcloud.domain.dw.rsp.*;
import com.plantdata.kgcloud.domain.dw.service.DWService;
import com.plantdata.kgcloud.domain.dw.service.PreBuilderService;
import com.plantdata.kgcloud.domain.dw.service.StandardTemplateService;
import com.plantdata.kgcloud.domain.dw.util.PaserYaml2SchemaUtil;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.DWDataFormat;
import com.plantdata.kgcloud.sdk.constant.DataType;
import com.plantdata.kgcloud.sdk.req.DWConnceReq;
import com.plantdata.kgcloud.sdk.req.DWDatabaseReq;
import com.plantdata.kgcloud.sdk.req.DWTableReq;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.UUIDUtils;
import org.apache.commons.io.IOUtils;
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
import javax.sql.DataSource;
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

    @Autowired
    private DWDatabaseRepository dwRepository;

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
    private DWPrebuildModelRepository modelRepository;

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

    @Override
    public DWDatabaseRsp createDatabase(String userId, DWDatabaseReq req) {

        DWDatabase dw = saveDatabase(req);

        List<DWTableRsp> tables = new ArrayList<>();
        if (DWDataFormat.isStandard(req.getDataFormat())) {

            List<ModelSchemaConfigRsp> modelSchemas = new ArrayList<>();

            for (Integer stId : req.getStandardTemplateId()) {
                StandardTemplateRsp standardTemplateRsp = standardTemplateService.findOne(userId, stId);

                /*Map<String,String> ktrMap = standardTemplateRsp.getKtr().stream().collect(Collectors.toMap(TableKtrRsp::getTableName,TableKtrRsp::getKtr));

                List<StandardTemplateSchema> stSchemas = standardTemplateRsp.getSchemas();

                for (StandardTemplateSchema schema : stSchemas) {
                    DWTableRsp table = createTable(userId, DWTableReq.builder()
                            .dwDatabaseId(dw.getId())
                            .schemas(schema.getSchemas())
                            .title(schema.getTitle())
                            .ktr(ktrMap.get(schema.getTableName()))
                            .tableName(schema.getTableName())
                            .build());

                    tables.add(table);
                }*/

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

                Map<String, ModelSchemaConfigRsp.RelationBean> map = new HashMap<>();

                for (ModelSchemaConfigRsp.RelationBean relation : modelSchema.getRelation()) {
                    map.put(relation.getDomain() + "-" + relation.getName(), relation);
                }

                for (ModelSchemaConfigRsp.RelationBean relation : tagJson.getRelation()) {
                    if (map.containsKey(relation.getDomain() + relation.getName())) {
                        ModelSchemaConfigRsp.RelationBean rela = map.get(relation.getDomain() + relation.getName());

                        //值域相加
                        rela.getRange().addAll(relation.getRange());

                        if (rela.getAttrs() == null) {
                            rela.setAttrs(new HashSet<>());
                        }

                        if (relation.getAttrs() != null && !relation.getAttrs().isEmpty()) {
                            rela.getAttrs().addAll(relation.getAttrs());
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

        DWDatabase database = getDetail(databaseId);

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

        DWDatabase database = getDetail(databaseId);

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

        DWDatabase database = getDetail(databaseId);

        //不是自定义类型数据库不用上传yaml
        if (!database.getDataFormat().equals(3)) {
            throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
        }

        try {

            String result = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);

            Object value = new Yaml().load(result);

            //生成json
            JSONObject json = JacksonUtils.readValue(JacksonUtils.writeValueAsString(value), JSONObject.class);

            List<ModelSchemaConfigRsp> modelSchemaConfig = PaserYaml2SchemaUtil.parserYaml2TagJson(json);

            database.setYamlContent(result);
            database.setTagJson(modelSchemaConfig);

            dwRepository.save(database);
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.YAML_PARSE_ERROR);
        }
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

        DWDatabase database = getDetail(req.getDwDatabaseId());
        database.setAddr(req.getAddr());
        database.setPassword(req.getPassword());
        database.setUsername(req.getUsername());
        database.setDbName(req.getDbName());
        database.setDataType(req.getType().getDataType());
        dwRepository.save(database);
        return dw2rsp.apply(database);
    }

    @Override
    public DWDatabase getDetail(Long dwDatabaseId) {
        Optional<DWDatabase> dwDatabase = dwRepository.findById(dwDatabaseId);

        if (!dwDatabase.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }
        return dwDatabase.get();
    }

    @Override
    public void upload(String userId, Long databaseId, Long tableId, MultipartFile file) {

        List<DataSetSchema> schemas = null;
        String tableName = null;

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
                tableName = table.getTableName();

            }
        }

        //文件上传 本地库
        DWDatabase database = getDetail(databaseId);


        if (tableName == null) {
            //文件上传 如果table没有创建，先创建
            try {
                schemas = schemaResolve(file, database.getDataFormat());
            } catch (Exception e) {
                throw BizException.of(KgmsErrorCodeEnum.FILE_SCHEMAPASER_ERROR);
            }
            DWTableRsp tableRsp = createTable(userId, DWTableReq.builder().dwDataBaseId(databaseId).schemas(schemas).build());
            tableName = tableRsp.getTableName();
        }

        //写入数据

        Map<String, DataSetSchema> schemaMap = new HashMap<>();
        for (DataSetSchema o : schemas) {
            schemaMap.put(o.getField(), o);
        }

        try (DataOptProvider provider = getProvider(database.getDataName(), tableName)) {
            String filename = file.getOriginalFilename();
            if (filename != null) {
                int i = filename.lastIndexOf(".");
                String extName = filename.substring(i);
                if (KgmsConstants.FileType.XLSX.equalsIgnoreCase(extName) || KgmsConstants.FileType.XLS.equalsIgnoreCase(extName)) {
                    excelFileHandle(provider, schemaMap, file);
                } else if (KgmsConstants.FileType.JSON.equalsIgnoreCase(extName)) {
                    jsonFileHandle(provider, schemaMap, file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.FILE_IMPORT_ERROR);
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

        DWDatabase dwDatabase = getDetail(req.getDwDataBaseId());

        Optional<DWTable> opt = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(req.getDwDataBaseId()).tableName(req.getTitle()).build()));

        if(opt.isPresent()){
            throw BizException.of(KgmsErrorCodeEnum.TABLE_NAME_EXIST);
        }

        if (StringUtils.hasText(req.getTableName())) {

            Optional<DWTable> optTb = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(req.getDwDataBaseId()).mapper(req.getTableName()).build()));

            if(optTb.isPresent()){
                throw BizException.of(KgmsErrorCodeEnum.MAP_TABLE_EXIST);
            }

            target.setTableName(req.getTitle());
            target.setMapper(req.getTableName());
        } else {
            target.setTableName(req.getTitle());
        }
        target.setTitle(req.getTitle());
        //本地库创建结构
        List<DataSetSchema> schema;
        if (StringUtils.hasText(req.getTableName())) {
            schema = getIndustryTableSchema(req.getDwDataBaseId(), req.getTableName());
            target.setKtr(getIndustryTableKtr(req.getDwDataBaseId(), req.getTableName()));
        } else if (DWDataFormat.isPDdoc(dwDatabase.getDataFormat())) {
            schema = schemaResolve(null, dwDatabase.getDataFormat());
        } else {
            schema = req.getSchemas();
        }
        target.setFields(transformFields(schema));
        target.setSchema(schema);
        target.setKtr(req.getKtr());
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
            return pddocSchema();
        }


        return dataSetService.schemaResolve(null, file);
    }

    private List<DataSetSchema> pddocSchema() {

        Field[] fields = PdDocument.class.getDeclaredFields();

        List<DataSetSchema> schemas = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {

            Field field = fields[i];

            String name = field.getName();
            DataSetSchema schema = new DataSetSchema();
            schema.setField(name);
            schemas.add(schema);

        }
        return schemas;
    }

    @Override
    public List<DWTableRsp> findTableAll(String userId, Long databaseId) {

        List<DWTable> dwTableList = tableRepository.findAll(Example.of(DWTable.builder().dwDataBaseId(databaseId).build()), Sort.by(Sort.Order.desc("createAt")));

        return dwTableList.stream().map(table2rsp).collect(Collectors.toList());
    }

    @Override
    public List<String> getRemoteTables(String userId, Long databaseId) {

        DWDatabase dwDatabase = getDetail(databaseId);

        if (dwDatabase.getAddr() == null || dwDatabase.getAddr().isEmpty() || (dwDatabase.getAddr().size() == 1 && !StringUtils.hasText(dwDatabase.getAddr().get(0)))) {
            return new ArrayList<>();
        }

        if (dwDatabase.getDataType().equals(DataType.MONGO.getDataType())) {

            try {
                return getMongoCollection(dwDatabase);
            } catch (Exception e) {
                throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_FIND_ERROR);
            }
        }

        DataSource dataSource = getDataSource(dwDatabase);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = getQueryTableSql(dwDatabase.getDataType());

        try {
            return jdbcTemplate.queryForList(sql, String.class);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_FIND_ERROR);
        }
    }

    private List<String> getMongoCollection(DWDatabase dwDatabase) {

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

        DWDatabase database = getDetail(databaseId);

        for (RemoteTableAddReq req : reqList) {

            Optional<DWTable> optTb = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(databaseId).tbName(req.getTbName()).build()));

            if(optTb.isPresent()){
                throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_EXIST);
            }


            //行业表映射
            if (StringUtils.hasText(req.getTableName())) {

                Optional<DWTable> opt = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(databaseId).mapper(req.getTableName()).build()));

                if(opt.isPresent()){
                    throw BizException.of(KgmsErrorCodeEnum.MAP_TABLE_EXIST);
                }

                List<DataSetSchema> schemaList = getIndustryTableSchema(databaseId, req.getTableName());
                if (schemaList == null) {
                    schemaList = getTableSchema(database, req.getTbName());
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
                        .build();

                tableRepository.save(table);
            } else {

                List<DataSetSchema> schemaList = getTableSchema(database, req.getTbName());

                DWTable table = DWTable.builder()
                        .dwDataBaseId(databaseId)
                        .tableName(req.getTbName())
                        .schema(schemaList)
                        .tbName(req.getTbName())
                        .title(req.getTbName())
                        .createWay(1)
                        .fields(transformFields(schemaList))
                        .build();

                tableRepository.save(table);

            }

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
            s = "连接测试成功!";
            map.put("status", "success");
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

        DWDatabase database = dwRepository.getOne(reqs.get(0).getDatabaseId());

        for (DWTableCronReq req : reqs) {

            Optional<DWTable> tableOpt = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(req.getDatabaseId()).id(req.getTableId()).build()));

            if (!tableOpt.isPresent()) {

                throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
            }

            DWTable table = tableOpt.get();
            DWTableRsp tableRsp = ConvertUtils.convert(DWTableRsp.class).apply(table);

            table.setQueryField(req.getField());
            table.setCron(req.getCron());
            table.setIsAll(req.getIsAll());
            table.setSchedulingSwitch(req.getSchedulingSwitch());
            table.setIsWriteDW(req.getIsWriteDW());

            updateSchedulingConfig(database, tableRsp, tableRsp.getDwDataBaseId(), tableRsp.getTableName(), req.getCron(), req.getIsAll(), req.getField());


            tableRepository.save(table);

            createTableSchedulingConfig(table);

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

        DWDatabase database = getDetail(databaseId);

        //不是PDDOC类型数据库不用上传tagjson
        if (!database.getDataFormat().equals(2)) {
            throw BizException.of(KgmsErrorCodeEnum.DATABASE_DATAFORMAT_ERROR);
        }

        try {

            String result = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);

            //生成json
            List<ModelSchemaConfigRsp> modelSchemaConfig = JacksonUtils.readValue(result, new TypeReference<List<ModelSchemaConfigRsp>>() {
            });

            database.setTagJson(modelSchemaConfig);

            dwRepository.save(database);

        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.YAML_PARSE_ERROR);
        }
        return;
    }

    private List<ModelSchemaConfigRsp> getDatabseModelSchema(String userId, Long id){

        DWDatabase database = getDetail(id);
        if (!database.getUserId().equals(userId)) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }

        if(database.getDataFormat().equals(1)){
            //行业模板 根据引入的表获取模式
            List<DWTableRsp> tables = findTableAll(userId, id);

            if (tables == null || tables.isEmpty()) {
                return Lists.newArrayList();
            }


            Map<String,String> tableMappings = new HashMap<>();
            for(DWTableRsp tableRsp : tables){
                tableMappings.put(tableRsp.getMapper(),tableRsp.getTableName());
            }

            if (tableMappings == null || tableMappings.isEmpty()) {
                return Lists.newArrayList();
            }


            List<ModelSchemaConfigRsp> schemas = Lists.newArrayList();
            database.getTagJson().forEach(schema -> {
                if(tableMappings.containsKey(schema.getTableName())){
                    schema.setTableName(tableMappings.get(schema.getTableName()));
                    schemas.add(schema);
                }
            });
            return schemas;
        }else{
            return database.getTagJson();
        }

    }

    @Override
    public void push(String userId, ModelPushReq req) {

        DWDatabase database = getDetail(req.getId());
        if (!database.getUserId().equals(userId)) {
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }

        List<PreBuilderConceptRsp> preBuilderConceptRspList;
        if (database.getDataFormat().equals(1) || database.getDataFormat().equals(2)) {
            //行业标准 or pddoc
            List<ModelSchemaConfigRsp> modelSchemaConfigRsp = getDatabseModelSchema(userId,req.getId());


            preBuilderConceptRspList = modelSchema2PreBuilder(modelSchemaConfigRsp);

            if(preBuilderConceptRspList == null || preBuilderConceptRspList.isEmpty()){
                throw BizException.of(KgmsErrorCodeEnum.EMTRY_MODEL_PUDH_ERROR);
            }


            preBuilderService.createModel(database, preBuilderConceptRspList, req.getModelType(), null);

        } else if (database.getDataFormat().equals(3)) {
            //自定义
            String yamlContent = database.getYamlContent();

            Object value = new Yaml().load(yamlContent);

            //生成json
            JSONObject json = JacksonUtils.readValue(JacksonUtils.writeValueAsString(value), JSONObject.class);

            preBuilderConceptRspList = PaserYaml2SchemaUtil.parserYaml2Schema(json);

            if(preBuilderConceptRspList == null || preBuilderConceptRspList.isEmpty()){
                throw BizException.of(KgmsErrorCodeEnum.EMTRY_MODEL_PUDH_ERROR);
            }

            preBuilderService.createModel(database, preBuilderConceptRspList, req.getModelType(), yamlContent);
        }

    }

    @Override
    public void setTableScheduling(String userId, DWTableSchedulingReq req) {
        Optional<DWTable> tableOpt = tableRepository.findOne(Example.of(DWTable.builder().dwDataBaseId(req.getDatabaseId()).id(req.getTableId()).build()));

        if (!tableOpt.isPresent()) {
            throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
        }

        DWTable table = tableOpt.get();

        if(StringUtils.hasText(table.getTableName())){
            table.setSchedulingSwitch(req.getSchedulingSwitch());

            tableRepository.save(table);

            createTableSchedulingConfig(table);

        }

    }

    private void createTableSchedulingConfig(DWTable table) {

        String dwTaskName = AccessTaskType.DW.getDisplayName() + "_" + table.getDwDataBaseId() + "_" + table.getTableName();
        List<String> diss = new ArrayList<>();
        if (table.getIsWriteDW() != null && table.getIsWriteDW().equals(1)) {
            diss.add(dwTaskName);
            accessTaskService.createDwTask(table.getTableName(), table.getDwDataBaseId());
        }

        if (table.getSchedulingSwitch().equals(1)) {

            //生成任务配置
            accessTaskService.createKtrTask(table.getTableName(), table.getDwDataBaseId(), table.getTableName(), 1);
            if(StringUtils.hasText(table.getMapper())){
                accessTaskService.createTransfer(table.getTableName(), table.getDwDataBaseId(), diss,null, null, null, table.getTableName());
            }else{
                accessTaskService.createTransfer(table.getTableName(), table.getDwDataBaseId(), null, diss, null, null, table.getTableName());
            }
        } else {
            //生成任务配置
            accessTaskService.createKtrTask(table.getTableName(), table.getDwDataBaseId(), table.getTableName(), 0);
            if(StringUtils.hasText(table.getMapper())){
                accessTaskService.createTransfer(table.getTableName(), table.getDwDataBaseId(), null, null, diss,null, table.getTableName());
            }else{
                accessTaskService.createTransfer(table.getTableName(), table.getDwDataBaseId(), null, null, null, diss, table.getTableName());
            }
        }
    }


    @Override
    public ModelSchemaConfigRsp getModel(String userId, Long id) {

       return mergeModel(getDatabseModelSchema(userId,id));

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

        DWDatabase database = dwRepository.getOne(req.getDatabaseId());

        for (DWTableRsp tableRsp : tableRspList) {

            if (tableRsp.getTableName() == null || tableRsp.getTableName().isEmpty()) {
                continue;
            }

            if (tableRsp.getCreateWay() == null || !tableRsp.getCreateWay().equals(1)) {
                continue;
            }

            String cron = req.getCron();

            tableRsp.setCron(cron);
            tableRsp.setSchedulingSwitch(req.getSchedulingSwitch());

            updateSchedulingConfig(database, tableRsp, tableRsp.getDwDataBaseId(), tableRsp.getTableName(), req.getCron(), req.getIsAll(), req.getField());

            DWTable table = ConvertUtils.convert(DWTable.class).apply(tableRsp);

            tableRepository.save(table);

            createTableSchedulingConfig(table);

        }

    }

    private void updateSchedulingConfig(DWDatabase database, DWTableRsp tableRsp, Long dwDataBaseId, String tableName, String cron, Integer isAll, String field) {

        String ktrTaskName = AccessTaskType.KTR.getDisplayName() + "_" + dwDataBaseId + "_" + tableName;

        accessTaskService.updateTableSchedulingConfig(database, tableRsp, ktrTaskName, cron, isAll, field);
    }

    @Override
    public void modelUpload(Long databaseId, MultipartFile file) {
        if (file == null || (!file.getOriginalFilename().endsWith(".json") && !file.getOriginalFilename().endsWith(".yaml"))) {
            throw BizException.of(KgmsErrorCodeEnum.FILE_TYPE_ERROR);
        }

        DWDatabase database = getDetail(databaseId);

        //不是PDDOC类型数据库不用上传tagjson
        if (database.getDataFormat().equals(2) && file.getOriginalFilename().endsWith(".json")) {
            tagUpload(databaseId, file);
        } else if (database.getDataFormat().equals(3) && file.getOriginalFilename().endsWith(".yaml")) {
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
        }
        return databases;
    }

    @Override
    public List<JSONObject> getDatabaseMappingTable(String userId, Long databaseId) {

        DWDatabase database = dwRepository.getOne(databaseId);

        if (database == null || !database.getDataFormat().equals(1) || database.getTagJson() == null || database.getTagJson().isEmpty()) {
            return Lists.newArrayList();
        }

        List<String> tables = database.getTagJson().stream().map(ModelSchemaConfigRsp::getTableName).collect(Collectors.toList());

        List<JSONObject>rs=Lists.newArrayList();

        if (tables != null && !tables.isEmpty()) {
            List<DWTableRsp> tableRsps = findTableAll(userId, databaseId);
            List<String> t = Lists.newArrayList();
            if (tableRsps !=null && !tableRsps.isEmpty()){
                for (DWTableRsp tableRsp : tableRsps) {

                    if(tableRsp.getMapper() == null){
                        continue;
                    }
                    t.add(tableRsp.getMapper());
                }


            }

            for (String table : tables){
                JSONObject json = new JSONObject();
                json.put("tableName",table);
                if (t.contains(table)){
                    json.put("status",1);
                }else{
                    json.put("status",0);
                }
                rs.add(json);
            }

        }

        return rs;
    }

    @Override
    public DWDatabaseRsp getDatabase(String userId, Long id) {

        Optional<DWDatabase> database = dwRepository.findOne(Example.of(DWDatabase.builder().userId(userId).id(id).build()));
        if(!database.isPresent()){
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }
        return ConvertUtils.convert(DWDatabaseRsp.class).apply(database.get());
    }


    @Override
    public List<PreBuilderConceptRsp> modelSchema2PreBuilder(List<ModelSchemaConfigRsp> modelSchemaConfig) {
        Map<String, PreBuilderConceptRsp> conceptRspMap = new HashMap<>();

        if(modelSchemaConfig == null || modelSchemaConfig.isEmpty()){
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
                for (ModelSchemaConfigRsp.AttrBean attrBean : schema.getAttr()) {
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

                for (ModelSchemaConfigRsp.RelationBean relationBean : schema.getRelation()) {

                    String domain = relationBean.getDomain();

                    if (!conceptRspMap.containsKey(domain)) {
                        throw BizException.of(KgmsErrorCodeEnum.TAG_JSON_PASER_ERROR);
                    }

                    PreBuilderAttrRsp attrRsp = new PreBuilderAttrRsp();
                    attrRsp.setAttrType(1);
                    attrRsp.setName(relationBean.getName());
                    attrRsp.setRangeName(relationBean.getRange().iterator().next());
                    attrRsp.setTables(Lists.newArrayList(schema.getTableName()));

                    if (relationBean.getAttrs() != null && !relationBean.getAttrs().isEmpty()) {

                        List<PreBuilderRelationAttrRsp> relationAttrRspList = new ArrayList<>();
                        for (ModelSchemaConfigRsp.RelationAttr relationAttr : relationBean.getAttrs()) {

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

    public List<DataSetSchema> getTableSchema(DWDatabase dwDatabase, String tbName) {

        List<DataSetSchema> rsList = new ArrayList<>();
        DataSource dataSource = getDataSource(dwDatabase);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "desc " + dwDatabase.getDbName() + "." + tbName;
        List<Map<String, Object>> rs = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> coulmn : rs) {

            String field = coulmn.get("Field").toString();

            DataSetSchema dataSetSchema = new DataSetSchema();
            dataSetSchema.setField(field);
            dataSetSchema.setType(1);
            rsList.add(dataSetSchema);
        }
        return rsList;
    }

    private DataSource getDataSource(DWDatabase dwDatabase) {
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

    private List excelFileHandle(DataOptProvider provider, Map<String, DataSetSchema> schemaMap, MultipartFile file) throws Exception {
        List<String> error = new ArrayList<>();
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
                    mapList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                if (!mapList.isEmpty()) {
                    provider.batchInsert(mapList);
                    mapList.clear();
                }
            }
        }).sheet().doRead();
        return error;
    }

    private void jsonFileHandle(DataOptProvider provider, Map<String, DataSetSchema> schemaMap, MultipartFile file) throws Exception {
        List<Map<String, Object>> dataList = JacksonUtils.readValue(file.getInputStream(), new TypeReference<List<Map<String, Object>>>() {
        });
        List<Map<String, Object>> mapList = new ArrayList<>();
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
                mapList.clear();
            }
        }
        if (!mapList.isEmpty()) {
            provider.batchInsert(mapList);
            mapList.clear();
        }
    }
}
