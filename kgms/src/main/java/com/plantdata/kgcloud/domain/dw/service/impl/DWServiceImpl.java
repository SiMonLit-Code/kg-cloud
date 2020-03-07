package com.plantdata.kgcloud.domain.dw.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.constant.KgmsConstants;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.domain.dataset.provider.MongodbOptProvider;
import com.plantdata.kgcloud.domain.dataset.service.DataSetService;
import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.repository.DWDatabaseRepository;
import com.plantdata.kgcloud.domain.dw.repository.DWTableRepository;
import com.plantdata.kgcloud.domain.dw.req.DWTableCronReq;
import com.plantdata.kgcloud.domain.dw.req.RemoteTableAddReq;
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
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.UUIDUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class DWServiceImpl implements DWService {

    private final static String DW_PREFIX = "dw_";
    private final static String TABLE_PREFIX = "tb_";
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
    private MongoProperties mongoProperties;

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

        //如果是行业标准  创建好对应tables

        List<DWTableRsp> tables = new ArrayList<>();
        if(DWDataFormat.isStandard(req.getDataFormat())){

            StandardTemplateRsp standardTemplateRsp = standardTemplateService.findOne(userId, req.getStandardTemplateId());
            //createTable;
            List<StandardTemplateSchema> stSchemas = standardTemplateRsp.getStSchemas();


            for(StandardTemplateSchema schema : stSchemas){
                DWTableRsp table = createTable(userId,DWTableReq.builder()
                        .dwDatabaseId(dw.getId())
                        .schemas(schema.getSchemas())
                        .title(schema.getTbName())
                        .build());

                tables.add(table);
            }



            //获取yaml文件，生成映射
            String yamlContent = standardTemplateRsp.getYamlContent();
            Object value = new Yaml().load(yamlContent);
            JSONObject json = JacksonUtils.readValue( JacksonUtils.writeValueAsString(value), JSONObject.class);

            List<PreBuilderConceptRsp> preBuilderConceptRspList = PaserYaml2SchemaUtil.parserYaml2Schema(json);
            dw.setYamlContent(yamlContent);
            preBuilderService.createModelByYaml(dw,preBuilderConceptRspList);
            dwRepository.save(dw);
        }

        DWDatabaseRsp databaseRsp = dw2rsp.apply(dw);
        databaseRsp.setTables(tables);
        return databaseRsp;
    }

    private DWDatabase saveDatabase(DWDatabaseReq req) {

        DWDatabase dw= DWDatabase.builder().userId(SessionHolder.getUserId()).title(req.getTitle()).dataFormat(req.getDataFormat()).standardTemplateId(req.getStandardTemplateId()).build();

        dw.setDataName(DW_PREFIX+JOIN+SessionHolder.getUserId()+JOIN+ UUIDUtils.getShortString().substring(0,5));

        return dwRepository.save(dw);
    }

    @Override
    public void yamlUpload(String userId, Long databaseId, MultipartFile file) {

        if(file == null || !file.getOriginalFilename().endsWith(".yaml")){
            throw BizException.of(KgmsErrorCodeEnum.FILE_TYPE_ERROR);
        }

        DWDatabase database = getDetail(databaseId);


        try {

            String result = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);

            Object value = new Yaml().load(result);

            //生成json
            JSONObject json = JacksonUtils.readValue(JacksonUtils.writeValueAsString(value), JSONObject.class);

            List<PreBuilderConceptRsp> preBuilderConceptRspList = PaserYaml2SchemaUtil.parserYaml2Schema(json);
            database.setYamlContent(result);

            dwRepository.save(database);

            preBuilderService.createModelByYaml(database,preBuilderConceptRspList);
        }catch (Exception e){
            e.printStackTrace();
            throw BizException.of(KgmsErrorCodeEnum.YAML_PARSE_ERROR);
        }
        return ;
    }

    @Override
    public List<DWDatabaseRsp> findAll(String userId) {
        DWDatabase probe = DWDatabase.builder()
                .userId(SessionHolder.getUserId())
                .build();
        List<DWDatabase> all = dwRepository.findAll(Example.of(probe));
        return all.stream().map(dw2rsp).collect(Collectors.toList());
    }

    @Override
    public DWDatabaseRsp setConn(String userId, DWConnceReq req) {

        DWDatabase database = getDetail(req.getDwDatabaseId());
        database.setAddr(req.getAddr());
        database.setPassword(req.getPassword());
        database.setUsername(req.getUsername());
        database.setDbName(req.getDbName());
        database.setCreateWay(1);
        database.setDataType(req.getType().getDataType());
        dwRepository.save(database);

        return dw2rsp.apply(database);
    }

    @Override
    public DWDatabase getDetail(Long dwDatabaseId){
        Optional<DWDatabase> dwDatabase = dwRepository.findById(dwDatabaseId);

        if(!dwDatabase.isPresent()){
            throw BizException.of(KgmsErrorCodeEnum.DW_DATABASE_NOT_EXIST);
        }
        return dwDatabase.get();
    }

    @Override
    public void upload(String userId, Long databaseId, Long tableId, MultipartFile file) {

        List<DataSetSchema> schemas = null;
        String tableName = null;

        if(tableId != null){
            Optional<DWTable> tableOptional = tableRepository.findById(tableId);
            if(tableOptional.isPresent()){
                DWTable table = tableOptional.get();
                schemas = table.getSchema();
                tableName = table.getTableName();
            }
        }

        if(tableName == null){
            schemas = schemaResolve(file);
            DWTableRsp tableRsp = createTable(userId,DWTableReq.builder().dwDatabaseId(databaseId).schemas(schemas).build());
            tableName = tableRsp.getTableName();
        }


        //文件上传 如果table没有创建，先创建


        //文件上传 本地库
        DWDatabase database = getDetail(databaseId);
        database.setCreateWay(2);

        //写入数据

        Map<String, DataSetSchema> schemaMap = new HashMap<>();
        for (DataSetSchema o : schemas) {
            schemaMap.put(o.getField(), o);
        }
        try (DataOptProvider provider = getProvider(database.getDataName(),tableName)) {
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
        }catch (Exception e){
            throw BizException.of(KgmsErrorCodeEnum.FILE_IMPORT_ERROR);
        }

    }

    public DataOptProvider getProvider(String dbName, String tbName) {
        DataOptConnect connect = getDefaultOpt(dbName,tbName);
        return DataOptProviderFactory.createProvider(connect, DataType.MONGO);
    }

    @Override
    public DWTableRsp createTable(String userId, DWTableReq req) {

        DWTable target = new DWTable();
        BeanUtils.copyProperties(req, target);

        DWDatabase dwDatabase = getDetail(req.getDwDatabaseId());

        target.setTableName(TABLE_PREFIX+JOIN+UUIDUtils.getShortString().substring(0,5));
        //本地库创建结构
        List<DataSetSchema> schema = req.getSchemas();
        target.setFields(transformFields(schema));
        target.setSchema(schema);
        try (DataOptProvider provider = new MongodbOptProvider(getDefaultOpt(dwDatabase.getDataName(),target.getTableName()))) {
            provider.createTable(schema);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.DATASET_CONNECT_ERROR);
        }

        target = tableRepository.save(target);

        return table2rsp.apply(target);
    }

    @Override
    public List<DataSetSchema> schemaResolve(MultipartFile file) {
        return dataSetService.schemaResolve(null,file);
    }

    @Override
    public List<DWTableRsp> findTableAll(String userId, Long databaseId) {

        List<DWTable> dwTableList = tableRepository.findAll(Example.of(DWTable.builder().dwDatabaseId(databaseId).build()));

        return dwTableList.stream().map(table2rsp).collect(Collectors.toList());
    }

    @Override
    public List<String> getRemoteTables(String userId, Long databaseId) {

        DWDatabase dwDatabase = getDetail(databaseId);

        if(!dwDatabase.getCreateWay().equals(1)){
            return new ArrayList<>();
        }

        DataSource dataSource = getDataSource(dwDatabase);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);


        String sql = "show tables;";
        try {
            return jdbcTemplate.queryForList(sql, String.class);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.REMOTE_TABLE_FIND_ERROR);
        }
    }

    @Override
    public List<Long> addRemoteTables(String userId, Long databaseId, List<RemoteTableAddReq> reqList) {

        List<Long> ids = new ArrayList<>();

        if(reqList == null || reqList.isEmpty()){
            return ids;
        }

        DWDatabase database = getDetail(databaseId);

        for(RemoteTableAddReq req : reqList){

            if(req.getTableName() != null){

                Optional<DWTable> opt = tableRepository.findOne(Example.of(DWTable.builder().dwDatabaseId(databaseId).tbName(req.getTbName()).build()));
                if(opt.isPresent()){
                    DWTable table = opt.get();
                    table.setTableName(req.getTableName());
                    tableRepository.updateByTableName(req.getTableName(),table.getTbName(),databaseId);

                    ids.add(table.getId());

                    continue;
                }

            }


            //新增表
            //获取schema
            List<DataSetSchema> schemaList = getTableSchema(database,req.getTbName());

            //设置表信息，创建
            DWTable table = DWTable.builder()
                                .dwDatabaseId(databaseId)
                                .tableName(TABLE_PREFIX+JOIN+UUIDUtils.getShortString().substring(0,5))
                                .schema(schemaList)
                                .tbName(req.getTbName())
                                .title(req.getTbName())
                                .fields(transformFields(schemaList))
                                .build();

            table = tableRepository.save(table);

            ids.add(table.getId());

        }

        return ids;

    }

    @Override
    public Object testConnect(DWConnceReq req) {
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

    @Override
    public List<DWTable> getTableByIds(List<Long> tableIds) {

        List<DWTable> tables = tableRepository.findByIdIn(tableIds);
        return tables;
    }

    @Override
    public void setTableCron(String userId, DWTableCronReq req) {

        Optional<DWTable> tableOpt = tableRepository.findOne(Example.of(DWTable.builder().dwDatabaseId(req.getDatabaseId()).id(req.getTableId()).build()));

        if(!tableOpt.isPresent()){

            throw BizException.of(KgmsErrorCodeEnum.DW_TABLE_NOT_EXIST);
        }

        DWTable table = tableOpt.get();

        table.setQueryField(req.getField());
        table.setCron(req.getCron());
        table.setIsAll(req.getIsAll());

        tableRepository.save(table);
    }

    public List<DataSetSchema> getTableSchema(DWDatabase dwDatabase,String tbName) {

        List<DataSetSchema> rsList = new ArrayList<>();
        DataSource dataSource = getDataSource(dwDatabase);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "desc "+dwDatabase.getDbName()+"."+tbName;
        List<Map<String,Object>> rs = jdbcTemplate.queryForList(sql);
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
        } else if (DataType.HIVE.equals(DataType.findType(dwDatabase.getDataType()))){
            dataSourceBuilder.driverClassName("org.apache.hive.jdbc.HiveDriver");
            dataSourceBuilder.url("jdbc:hive2://" + dwDatabase.getAddr().get(0) + "/" + dwDatabase.getDbName());
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
        } else if (DataType.HIVE.equals(req.getType())){
            dataSourceBuilder.driverClassName("org.apache.hive.jdbc.HiveDriver");
            dataSourceBuilder.url("jdbc:hive2://" + req.getAddr().get(0) + "/" + req.getDbName());
        }
        dataSourceBuilder.username(req.getUsername());
        dataSourceBuilder.password(req.getPassword());
        return dataSourceBuilder.build();

    }

    private DataOptConnect getDefaultOpt(String dbName, String tbName){

        DataOptConnect info = new DataOptConnect();

        info.setAddresses(Arrays.asList(mongoProperties.getAddrs()));
        info.setDatabase(dbName);
        info.setTable(tbName);
        info.setUsername(mongoProperties.getUsername());
        info.setPassword(mongoProperties.getPassword());

        return info;
    }

    private List<String> transformFields(List<DataSetSchema> schema) {
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
