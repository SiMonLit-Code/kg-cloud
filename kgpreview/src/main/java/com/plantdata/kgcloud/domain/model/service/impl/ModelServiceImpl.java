package com.plantdata.kgcloud.domain.model.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.common.util.DriverUtil;
import com.plantdata.kgcloud.common.util.HttpUtil;
import com.plantdata.kgcloud.domain.model.entity.*;
import com.plantdata.kgcloud.domain.model.service.ModelService;
import com.plantdata.kgcloud.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiezhenxiang 2019/12/10
 */
@Service
public class ModelServiceImpl implements ModelService {

    @Override
    public void dbTest(String path, Integer type, String ip, Integer port, String database, String userName, String pwd) {
        if (StringUtils.isBlank(path)) {
            boolean flag  =StringUtils.isAnyBlank(ip, database) || port == null || (type == 0 && StringUtils.isAnyBlank( userName, pwd));
            if (flag) {
                throw new BizException(50078, "数据库信息不完整！");
            }
            DriverUtil driverUtil = getDriverUtil(type, ip, port, database, userName, pwd);
        } else {
            getSchemaByApi(path);
        }
    }

    private static DriverUtil getDriverUtil(Integer type, String ip, Integer port, String database, String userName, String pwd) {

        String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?serverTimezone=UTC&characterEncoding=utf8"
                + "&autoReconnect=true&failOverReadOnly=false&useSSL=false";
        if (type == 1) {
            url = "jdbc:hive2://" + ip + ":" + port + "/" + database;
        }
        return DriverUtil.getInstance(url, userName, pwd);
    }

    private List<TableSchemaRsp> getSchemaByApi(String path) {

        Map<String, String> head = new HashMap<>(2);
        head.put("Accept", "application/json");
        String str = HttpUtil.sendGet(path, head);
        if (StringUtils.isBlank(str)) {
            throw new BizException(50079, "接口地址错误！");
        }
        // todo read str
        return new ArrayList<>();
    }

    @Override
    public BasePage<String> getTables(String path, Integer type, String ip, Integer port, String database, String userName, String pwd, String kw, Integer pageNo, Integer pageSize) {

        List<String> ls;
        if (StringUtils.isBlank(path)) {

            DriverUtil driverUtil = getDriverUtil(type, ip, port, database, userName, pwd);
            ls = driverUtil.getTables();
            if (StringUtils.isNotBlank(kw)) {
                ls = ls.stream().filter(s -> s.contains(kw)).collect(Collectors.toList());
            }
        } else {

            List<TableSchemaRsp> schemas = getSchemaByApi(path);
            if (StringUtils.isNotBlank(kw)) {
                ls = schemas.stream().filter(s -> s.getTableName().contains(kw)).map(TableSchemaRsp::getTableName).collect(Collectors.toList());
            } else {
                ls = schemas.stream().map(TableSchemaRsp::getTableName).collect(Collectors.toList());
            }
        }

        pageNo = (pageNo - 1) * pageSize;
        pageSize = Math.min(pageNo + pageSize, ls.size());
        return new BasePage<>(ls.size(), ls.subList(pageNo, pageSize));
    }

    @Override
    public List<TableSchemaRsp> getTableSchema(String path, Integer type, String ip, Integer port, String database, String userName, String pwd, List<String> tables) {

        List<TableSchemaRsp> schemas;

        if (StringUtils.isBlank(path)) {
            if (type == 0) {
                schemas = getMysqlTableSchema(ip, port, database, userName, pwd, tables);
            } else {
                schemas = getHiveTableSchema(ip, port, database, userName, pwd, tables);
            }
        } else {
            schemas = getSchemaByApi(path).stream().filter(s -> tables.contains(s.getTableName())).collect(Collectors.toList());
        }
        return schemas;
    }

    private List<TableSchemaRsp> getHiveTableSchema(String ip, Integer port, String database, String userName, String pwd, List<String> tables) {

        List<TableSchemaRsp> ls = new ArrayList<>();
        DriverUtil driverUtil = getDriverUtil(1, ip, port, database, userName, pwd);

        for (String table : tables) {

            TableSchemaRsp tableSchema = new TableSchemaRsp();
            tableSchema.setTableName(table);
            String sql = "desc " + table;
            List<JSONObject> tableDesc = driverUtil.find(sql);

            List<FieldInfoRsp> fieldInfoLs = new ArrayList<>();
            for (JSONObject desc : tableDesc) {
                FieldInfoRsp fieldInfo = new FieldInfoRsp();
                fieldInfo.setAttrType(0);
                fieldInfo.setName(desc.getString("col_name"));

                if (StringUtils.isBlank(fieldInfo.getName()) || fieldInfo.getName().startsWith("#")) {
                    break;
                }
                String comment = desc.getString("comment");
                // fix wrong charset
                if (StringUtils.isNotBlank(comment) && comment.startsWith("?") && comment.endsWith("?")) {
                    comment = "";
                }

                fieldInfo.setComment(comment);
                fieldInfo.setType(getDataType(desc.getString("data_type")) + "");
                fieldInfoLs.add(fieldInfo);
            }
            if (!fieldInfoLs.isEmpty()) {
                tableSchema.setIdField(fieldInfoLs.get(0).getName());
            }
            tableSchema.setFieldInfoLs(fieldInfoLs);
            ls.add(tableSchema);
        }
        return ls;
    }

    private Integer getDataType(String typeName) {

        Integer dataType = 5;

        if (StringUtils.isNotBlank(typeName)) {

            typeName = typeName.toLowerCase();

            if (typeName.contains("date") || typeName.contains("time")) {
                dataType = 4;
            } else if (typeName.contains("int")) {
                dataType = 1;
            } else if (typeName.contains("float") || typeName.contains("double") || typeName.contains("decimal") || typeName.contains("numeric")) {
                dataType = 2;
            } else if (typeName.contains("clob") || typeName.contains("blob")) {
                dataType = null;
            } else if (typeName.contains("text")) {
                dataType = 10;
            }
        }

        return dataType;
    }

    private List<TableSchemaRsp> getMysqlTableSchema(String ip, Integer port, String database, String userName, String pwd, List<String> tables) {

        DriverUtil driverUtil = getDriverUtil(0, ip, port, "information_schema", userName, pwd);

        List<TableSchemaRsp> ls = new ArrayList<>();
        String sql = "select TABLE_NAME, TABLE_COMMENT from TABLES where TABLE_SCHEMA = ?";
        List<JSONObject> tableLs =  driverUtil.find(sql, database);

        for (JSONObject obj : tableLs) {

            TableSchemaRsp schema = new TableSchemaRsp();
            String tableName = obj.getString("TABLE_NAME");

            if (!tables.contains(tableName)) {
                continue;
            }
            schema.setTableName(tableName);
            schema.setTableComment(obj.getString("TABLE_COMMENT"));
            // 封装字段信息
            sql = "select COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT from COLUMNS where TABLE_SCHEMA = ? and TABLE_NAME = ?";
            List<JSONObject> fieldLs = driverUtil.find(sql, database, tableName);

            List<FieldInfoRsp> fieldInfoList = new ArrayList<>();
            for (JSONObject field : fieldLs) {

                FieldInfoRsp fieldInfo = new FieldInfoRsp();
                fieldInfo.setName(field.getString("COLUMN_NAME"));
                fieldInfo.setType(getDataType(field.getString("DATA_TYPE")) + "");
                fieldInfo.setComment(field.getString("COLUMN_COMMENT"));
                fieldInfo.setAttrType(0);
                fieldInfoList.add(fieldInfo);
            }

            schema.setFieldInfoLs(fieldInfoList);

            // 封装外键信息
            List<ForeignKeyRsp> keyLs = new ArrayList<>();
            sql = "select * from KEY_COLUMN_USAGE  where TABLE_SCHEMA = ? and TABLE_NAME = ? and REFERENCED_COLUMN_NAME is not null";
            List<JSONObject> foreignKeyLs = driverUtil.find(sql, database, tableName);

            for (JSONObject foreignKey : foreignKeyLs) {

                ForeignKeyRsp key = new ForeignKeyRsp();
                key.setFieldName(foreignKey.getString("COLUMN_NAME"));
                key.setReferTbName(foreignKey.getString("REFERENCED_TABLE_NAME"));
                key.setReferFieldName(foreignKey.getString("REFERENCED_COLUMN_NAME"));
                keyLs.add(key);
            }
            if (foreignKeyLs.size() == 2) {
                schema.setTableType(1);
            }

            // 默认主键配置
            sql = "select * from KEY_COLUMN_USAGE  where TABLE_SCHEMA = ? and TABLE_NAME = ? and CONSTRAINT_NAME = ?";
            List<JSONObject> mainKeyLs = driverUtil.find(sql, database, tableName, "PRIMARY");
            if (!mainKeyLs.isEmpty()) {
                schema.setIdField(mainKeyLs.get(0).getString("COLUMN_NAME"));
            } else if (!fieldInfoList.isEmpty()) {
                schema.setIdField(fieldInfoList.get(0).getName());
            }

            schema.setForeignKeyLs(keyLs);
            ls.add(schema);
        }

        pkgRelation(ls);
        return ls;
    }

    /**
     * 根据外键封装关系
     * @author xiezhenxiang 2019/7/31
     **/
    private void pkgRelation(List<TableSchemaRsp> ls) {

        for (TableSchemaRsp schema : ls) {

            if (schema.getTableType() == 1) {
                continue;
            }

            for (ForeignKeyRsp foreignKey : schema.getForeignKeyLs()) {

                String domainField = foreignKey.getFieldName();
                String valueTable = foreignKey.getReferTbName();
                String valueField = foreignKey.getReferFieldName();

                Optional<TableSchemaRsp> opt = ls.stream().filter(s -> s.getTableName().equals(valueTable)).findFirst();
                if (!opt.isPresent()) {
                    continue;
                }
                TableSchemaRsp valueTableSchema = opt.get();

                FieldInfoRsp domainFieldInfo = schema.getFieldInfoLs().stream().filter(s -> s.getName().equals(domainField)).findFirst().get();
                FieldInfoRsp valueFieldInfo = valueTableSchema.getFieldInfoLs().stream().filter(s -> s.getName().equals(valueField)).findFirst().get();

                domainFieldInfo.setAttrType(1);
                List<RangeTableRsp> rangeTables = domainFieldInfo.getValues();
                RangeTableRsp rangeTable = new RangeTableRsp();
                rangeTable.setTableName(foreignKey.getReferTbName());
                rangeTable.setTableComment(null);
                rangeTable.setFieldName(valueFieldInfo.getName());
                rangeTable.setFieldComment(valueFieldInfo.getComment());
                rangeTables.add(rangeTable);
                domainFieldInfo.setValues(rangeTables);
            }
        }
    }

    @Override
    public void testConfig(ModelSetting setting) {

        if (setting.getReadType() == null) {
            exit("没有设置读取方式!");
        } else if (setting.getReadType() == 1 && StringUtils.isBlank(setting.getPath())) {
            exit("没有设置接口路径!");
        }
        boolean missDbConfig = setting.getReadType() == 0 && (StringUtils.isAnyBlank(setting.getIp(), setting.getUserName(), setting.getPassword()
                , setting.getDatabase())) || setting.getPort() == null;

        if (missDbConfig) {
            exit("没有设置数据库连接信息!");
        } else if (StringUtils.isBlank(setting.getKgName())) {
            exit("没有设置kgName!");
        } else if (setting.getModel() == null) {
            exit("没有设置入图方式!");
        }

        for (EntityTable entityTable : setting.getEntityTables()) {

            for (ModelConceptConfig conceptConfig : entityTable.getConceptConfigs()) {

                if (StringUtils.isBlank(conceptConfig.getConceptName())) {
                    exit(entityTable.getTableName() + "表 没有配置概念名称!");
                } else if (conceptConfig.getId().isEmpty()) {
                    exit(entityTable.getTableName() + "表 没有配置实体ID字段!");
                } else if (StringUtils.isBlank(conceptConfig.getEntityField())) {
                    exit(entityTable.getTableName() + "表 没有配置实体名称字段!");
                }

                for (AttrConfig attrConfig : conceptConfig.getAttrConfigs()) {

                    if (StringUtils.isAnyBlank(attrConfig.getAttrName(), attrConfig.getFieldName()) || attrConfig.getType() == null) {
                        exit(entityTable.getTableName() + "表 配置的属性信息不完整!");
                    }

                    for (ReferTable referTable : attrConfig.getReferTable()) {
                        if (StringUtils.isBlank(referTable.getTableName())) {
                            exit("关系表没有设置表名称!");
                        }

                        Optional<EntityTable> opt = setting.getEntityTables().stream().filter(s ->
                                s.getTableName().equals(referTable.getTableName())).findFirst();
                        if (!opt.isPresent() || opt.get().getConceptConfigs().isEmpty()) {
                            exit("关系属性“"+ attrConfig.getAttrName() +"”的值域未配置!");
                        }
                        referTable.setReferField(opt.get().getConceptConfigs().get(0).getId().get(0));
                    }
                }
            }
        }

        for (RelationConfig relationConfig : setting.getRelationTables()) {

            if (StringUtils.isAnyBlank(relationConfig.getAttrName(), relationConfig.getDomainConcept(), relationConfig.getDomainTable(),
                    relationConfig.getRangeConcept(), relationConfig.getRangeTable())) {
                exit("没有配置完整的关系！");
            }
        }
    }

    private static void exit(String msg) {
        throw new BizException(50041, msg);
    }
}
