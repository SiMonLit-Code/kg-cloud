package com.plantdata.kgcloud.domain.access.util;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptConnect;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProvider;
import com.plantdata.kgcloud.domain.dataset.provider.DataOptProviderFactory;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.sdk.rsp.DWTableRsp;
import com.plantdata.kgcloud.sdk.constant.DataType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateKtrFile {

    private static final int RADIX = 16;
    private static final String SEED = "0933910847463829827159347601486730416058";
    private final static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /**
     * 功能执行方法
     *
     * @return
     * @throws IOException
     */
    public static String getKettleXmlPath(DWDatabaseRsp database, DWTableRsp table, String isAllKey, String resourceName, String kafkaServers, String[] mongoAddrs, String mongoUserrname, String mongoPassword, String userId, String kafkaTransferTopic, String kafkaCheckTopic) {

        if (database.getDataFormat().equals(1)) {
            //行业标准
            return getKtrIndustry(database, table, isAllKey, resourceName, kafkaServers, mongoAddrs, mongoUserrname, mongoPassword, userId, kafkaTransferTopic, kafkaCheckTopic);
        } else {
            //非标准
            return getKtrNotIndustry(database, table, resourceName, kafkaServers, mongoAddrs, mongoUserrname, mongoPassword, kafkaTransferTopic);
        }


    }

    private static String getKtrIndustry(DWDatabaseRsp database, DWTableRsp table, String isAllKey, String resourceName, String kafkaServers, String[] mongoAddrs, String mongoUserrname, String mongoPassword, String userId, String kafkaTransferTopic, String kafkaCheckTopic) {

        String xml = IndustryKtrXml.xml;
        String defaultXml = IndustryKtrXml.defaultStepXml;
        String orderXml = IndustryKtrXml.orderXml;
        String kafkaTruexml = IndustryKtrXml.kafkaTrueXml;
        String kafkaErrorxml = IndustryKtrXml.kafkaErrorXml;
        String paramAndFilterXml = IndustryKtrXml.paramAndFilterXml;

        String customizationXml = IndustryKtrXml.customizationXml;
        String customizationXmlRerun = IndustryKtrXml.customizationXml;

        //数据接入xml
        String inputXml;

        //字段xml
        String jsonFieldxml;

        // 数据库类型
        String type;

        // 数据库ip
        String ip;

        // 数据库端口
        String port;

        // 数据库名称
        String dbName;

        // 用户名
        String username;

        // 密码
        String password;

        //表名
        String tableName;

        //连接信息
        String connXml;

        //错误数据接入
        String mongoErrorInput = IndustryKtrXml.mongoErrorInput;

        boolean isMongo = true;

        if (table.getCreateWay().equals(1)) {
            //远程表
            type = DataType.findType(database.getDataType()).name();

            ip = database.getAddr().get(0).split(":")[0];

            port = database.getAddr().get(0).split(":")[1];

            dbName = database.getDbName();

            username = database.getUsername();

            password = database.getPassword();

            tableName = table.getTbName();

            if (DataType.MONGO.equals(DataType.findType(database.getDataType()))) {
                connXml = "";
                jsonFieldxml = IndustryKtrXml.jsonFieldxml;
                inputXml = IndustryKtrXml.mongoInputXml;
            } else {
                connXml = IndustryKtrXml.connectionXml;
                jsonFieldxml = IndustryKtrXml.jsonFieldxml;
                inputXml = IndustryKtrXml.mysqlInputXml;
                isMongo = false;
            }
        } else {
            //本地表
            type = DataType.MONGO.name();

            ip = mongoAddrs[0].split(":")[0];

            port = mongoAddrs[0].split(":")[1];

            dbName = database.getDataName();

            username = mongoUserrname;

            password = mongoPassword;

            tableName = table.getTableName();

            connXml = "";

            jsonFieldxml = IndustryKtrXml.jsonFieldxml;

            inputXml = IndustryKtrXml.mongoInputXml;

        }


        // 查询语句
        String queryXml = getTableSql(database, table, tableName, isMongo, mongoAddrs, mongoUserrname, mongoPassword);

        inputXml = changeSql(inputXml, queryXml);

        connXml = changeDBConnection(connXml, ip, port, dbName, tableName, username, password, type, table.getIsAll());

        inputXml = changeDBConnection(inputXml, ip, port, dbName, tableName, username, password, type, table.getIsAll());

        jsonFieldxml = changeJsonField(jsonFieldxml, table.getFields(), isMongo);

        defaultXml = changeDefaultXmlField(defaultXml, jsonFieldxml);

        kafkaTruexml = changeKafkaConnection(kafkaTruexml, kafkaServers, kafkaTransferTopic);

        kafkaErrorxml = changeKafkaConnection(kafkaErrorxml, kafkaServers, kafkaCheckTopic);

        mongoErrorInput = changeMongoErrorInput(mongoErrorInput, mongoAddrs[0].split(":")[0], mongoAddrs[0].split(":")[1], mongoUserrname, mongoPassword, database, table);


        customizationXml = changeCustomizationXml(customizationXml, table);

        customizationXmlRerun = changeCustomizationXmlRerun(customizationXmlRerun, table);

        String data = xml + connXml + orderXml + defaultXml + inputXml + kafkaTruexml + kafkaErrorxml + mongoErrorInput + customizationXml + customizationXmlRerun + paramAndFilterXml;
        // 创建临时路径
        return data.replaceAll("resourceNameQAQ", resourceName);

    }

    private static String changeCustomizationXmlRerun(String customizationXmlRerun, DWTableRsp table) {

        return customizationXmlRerun.replace("${code}", table.getKtr() == null ? "" : table.getKtr()).replace("<name>数据转换</name>", "<name>数据转换 2</name>");
    }

    private static String changeCustomizationXml(String customizationXml, DWTableRsp table) {

        return customizationXml.replace("${code}", table.getKtr() == null ? "" : table.getKtr());
    }


    private static String getKtrNotIndustry(DWDatabaseRsp database, DWTableRsp table, String resourceName, String kafkaServers, String[] mongoAddrs, String mongoUserrname, String mongoPassword, String topic) {
        String xml = KtrXml.xml;
        String defaultXml = KtrXml.defaultStepXml;
        String kafkaxml = KtrXml.kafkaxml;
        String orderXml = KtrXml.orderXml;

        //数据接入xml
        String inputXml;

        //字段xml
        String jsonFieldxml;

        // 数据库类型
        String type;

        // 数据库ip
        String ip;

        // 数据库端口
        String port;

        // 数据库名称
        String dbName;

        // 用户名
        String username;

        // 密码
        String password;

        //表名
        String tableName;

        //连接信息
        String connXml;

        //错误数据接入
        String mongoErrorInput = KtrXml.mongoErrorInput;

        boolean isMongo = true;

        if (table.getCreateWay().equals(1)) {
            //远程表
            type = DataType.findType(database.getDataType()).name();

            ip = database.getAddr().get(0).split(":")[0];

            port = database.getAddr().get(0).split(":")[1];

            dbName = database.getDbName();

            username = database.getUsername();

            password = database.getPassword();

            tableName = table.getTbName();

            if (DataType.MONGO.equals(DataType.findType(database.getDataType()))) {
                connXml = "";
                jsonFieldxml = KtrXml.jsonFieldxml;
                inputXml = KtrXml.mongoInputXml;
            } else {
                connXml = KtrXml.connectionXml;
                jsonFieldxml = KtrXml.jsonFieldxml;
                inputXml = KtrXml.mysqlInputXml;
                isMongo = false;
            }
        } else {
            //本地表
            type = DataType.MONGO.name();

            ip = mongoAddrs[0].split(":")[0];

            port = mongoAddrs[0].split(":")[1];

            dbName = database.getDataName();

            username = mongoUserrname;

            password = mongoPassword;

            tableName = table.getTableName();

            connXml = "";

            jsonFieldxml = KtrXml.jsonFieldxml;

            inputXml = KtrXml.mongoInputXml;

        }


        // 查询语句
        String queryXml = getTableSql(database, table, tableName, isMongo, mongoAddrs, mongoUserrname, mongoPassword);

        inputXml = changeSql(inputXml, queryXml);

        connXml = changeDBConnection(connXml, ip, port, dbName, tableName, username, password, type, table.getIsAll());

        inputXml = changeDBConnection(inputXml, ip, port, dbName, tableName, username, password, type, table.getIsAll());

        jsonFieldxml = changeJsonField(jsonFieldxml, table.getFields(), isMongo);

        defaultXml = changeDefaultXmlField(defaultXml, jsonFieldxml);

        kafkaxml = changeKafkaConnection(kafkaxml, kafkaServers, topic);

        mongoErrorInput = changeMongoErrorInput(mongoErrorInput, mongoAddrs[0].split(":")[0], mongoAddrs[0].split(":")[1], mongoUserrname, mongoPassword, database, table);

        String data = xml + connXml + orderXml + defaultXml + inputXml + mongoErrorInput + kafkaxml;
        // 创建临时路径
        return data.replaceAll("resourceNameQAQ", resourceName);
    }

    private static String changeMongoErrorInput(String mongoErrorInput, String mongoIp, String mongoPort, String mongoUserrname, String mongoPassword, DWDatabaseRsp database, DWTableRsp table) {

        String encodePassword = encodePassword(mongoPassword);

        return mongoErrorInput.replace("errorMongoIpQAQ", mongoIp)
                .replace("errorMongoPortQAQ", mongoPort)
                .replace("errorMongoDbQAQ", "dw_rerun_" + database.getDataName())
                .replace("errorMongoCollectionQAQ", table.getTableName())
                .replace("errorMongoUserQAQ", mongoUserrname == null ? "" : mongoUserrname)
                .replace("errorMongoPasswordQAQ", encodePassword == null ? "" : encodePassword);
    }


    private static String changeDefaultXmlField(String defaultXml, String jsonFieldxml) {
        return defaultXml.replaceAll("fieldsQAQ", jsonFieldxml);
    }

    private static String changeJsonField(String jsonFieldxml, List<String> fields, boolean isMongo) {

        if (isMongo) {
            fields = Lists.newArrayList("json");
        }

        StringBuilder jsonFieldStr = new StringBuilder();
        for (String field : fields) {
            jsonFieldStr.append(jsonFieldxml.replaceAll("fieldQAQ", field));
        }

        return jsonFieldStr.toString();
    }

    private static String changeKafkaConnection(String kafkaxml, String kafkaServers, String kafkaTopic) {

        return kafkaxml.replaceAll("kafkaQAQ", kafkaServers).replaceAll("topicQAQ", kafkaTopic);
    }

    private static String getTableSql(DWDatabaseRsp databaseRsp, DWTableRsp table, String tableName, Boolean isMongo, String[] mongoAddrs, String mongoUserrname, String mongoPassword) {

        StringBuilder sql = new StringBuilder();


        if (isMongo) {

            if (table.getIsAll() == null || table.getIsAll().equals(1)) {

                return KtrXml.mongoAllQueryXMl;
//                return sql.toString();
            } else {

                List<DataSetSchema> schemas = table.getSchema();

                boolean isDateField = false;
                for (DataSetSchema schema : schemas) {
                    if (schema.getField().equals(table.getQueryField())) {
                        if (schema.getType() == FieldType.DATE.getCode() || schema.getType() == FieldType.DATETIME.getCode()) {
                            isDateField = true;
                        }
                        break;
                    }
                }

                if (isDateField) {

                    FieldType fieldType = getFileType(databaseRsp, table, table.getQueryField(), mongoAddrs, mongoUserrname, mongoPassword);

                    if (FieldType.DATE.equals(fieldType)) {
                        return KtrXml.mongoTimeQueryDateFieldXMl.replaceAll("timeFieldQAQ", table.getQueryField());
                    }
                }

                return KtrXml.mongoTimeQueryXMl.replaceAll("timeFieldQAQ", table.getQueryField());
            }

        } else {
            sql.append("SELECT ");

            for (String field : table.getFields()) {
                sql.append("`").append(field).append("`").append(",");

            }
            sql = sql.deleteCharAt(sql.length() - 1);
            sql.append(" FROM ")
                    .append(tableName);

            if (table.getIsAll() != null && !table.getIsAll().equals(1)) {

                sql.append(" WHERE 'true'='${key}' and ")
                        .append(table.getQueryField())
                        .append(" &gt;= ")
                        .append("'${StartTime}'")
                        .append(" and ")
                        .append(table.getQueryField())
                        .append(" &lt; ")
                        .append("'${EndTime}'");

            } else {
                sql.append(" WHERE 'true'='${key}' ");
            }

            return sql.toString();
        }


    }

    public static FieldType getFileType(DWDatabaseRsp databaseRsp, DWTableRsp table, String field, String[] mongoAddrs, String mongoUserrname, String mongoPassword) {


        DataSet dataSet;
        //远程
        if (table.getCreateWay().equals(1)) {
            if (!DataType.MONGO.equals(DataType.findType(databaseRsp.getDataType()))) {
                return FieldType.STRING;
            }
            dataSet = DataSet.builder()
                    .addr(databaseRsp.getAddr())
                    .username(databaseRsp.getUsername())
                    .password(databaseRsp.getPassword())
                    .tbName(table.getTbName())
                    .dbName(databaseRsp.getDbName())
                    .build();
        } else {
            //本地
            dataSet = DataSet.builder()
                    .addr(CollectionUtils.arrayToList(mongoAddrs))
                    .username(mongoUserrname)
                    .password(mongoPassword)
                    .tbName(table.getTableName())
                    .dbName(databaseRsp.getDataName())
                    .build();
        }

        DataOptConnect dataOptConnect = DataOptConnect.of(dataSet);
        dataOptConnect.setDataType(DataType.MONGO);
        try (DataOptProvider provider = DataOptProviderFactory.createProvider(dataOptConnect)) {

            List<Map<String, Object>> maps = provider.find(0, 1, null);
            if (maps == null || maps.isEmpty()) {
                return FieldType.STRING;
            }

            Map<String, Object> map = maps.get(0);
            if (map.containsKey(field)) {
                format.parse(map.get(field) + "");
                return FieldType.DATE;
            }
        } catch (Exception e) {
        }

        return FieldType.STRING;
    }

    /**
     * 修改sql语句
     *
     * @param sqlXml
     * @param sql
     * @return
     */
    private static String changeSql(String sqlXml, String sql) {
        return sqlXml.replace("queryQAQ", sql);
    }

    /**
     * 修改数据库连接参数
     *
     * @param connectionXml
     * @param ip
     * @param port
     * @param dbName
     * @param username
     * @param password
     * @param type
     * @return
     */
    private static String changeDBConnection(String connectionXml,
                                             String ip,
                                             String port,
                                             String dbName,
                                             String tbName,
                                             String username,
                                             String password,
                                             String type,
                                             Integer isAll) {


        String encodePassword = encodePassword(password);

        return connectionXml.replace("ipQAQ", ip)
                .replace("portQAQ", port)
                .replace("dbnameQAQ", dbName)
                .replace("tbNameQAQ", tbName == null ? "" : tbName)
                .replace("usernameQAQ", username == null ? "" : username)
                .replace("typeQAQ", type.toUpperCase())
                .replace("passwordQAQ", encodePassword)
                .replace("isCronQAQ", isAll != null && isAll.equals(2) ? "Y" : "N");

    }

    /**
     * 数据库密码加密方法
     *
     * @param password
     * @return
     */
    private static String encodePassword(String password) {
        return "Encrypted " + encryptPassword(password);
    }

    /**
     * 修改mongo的连接参数
     *
     * @param mongoXml
     * @param addresses
     * @param dbName
     * @param tbName
     * @return
     */
    private static String changeMongoConnection(String mongoXml,
                                                List<String> addresses,
                                                String dbName,
                                                String tbName) {

        String mongo = "    <mongo_host>192.168.4.11</mongo_host>\n" +
                "    <mongo_port>19130</mongo_port>\n" +
                "    <use_all_replica_members>N</use_all_replica_members>\n" +
                "    <auth_mech/>\n" +
                "    <auth_kerberos>N</auth_kerberos>\n" +
                "    <mongo_db>zdss_test</mongo_db>\n" +
                "    <mongo_collection>test4</mongo_collection>\n";

        String address = addresses.get(0);
        String[] split = address.split(":");
        String ip = split[0];
        String port = split[1];

        mongo = mongo.replace("192.168.4.11", ip)
                .replace("19130", port)
                .replace("zdss_test", dbName)
                .replace("test4", tbName);

        return mongoXml.replace("    <mongo_host>192.168.4.11</mongo_host>\n" +
                "    <mongo_port>19130</mongo_port>\n" +
                "    <use_all_replica_members>N</use_all_replica_members>\n" +
                "    <auth_mech/>\n" +
                "    <auth_kerberos>N</auth_kerberos>\n" +
                "    <mongo_db>zdss_test</mongo_db>\n" +
                "    <mongo_collection>test4</mongo_collection>\n", mongo);

    }

    /**
     * 修改mongo的写入字段
     *
     * @param mongoXml
     * @param arr
     * @return
     */
    private static String changeMongoField(String mongoXml, List<String> arr) {
        StringBuilder field = new StringBuilder();
        field.append("    <mongo_fields>\n");
        for (String fieldName : arr) {
            field.append(getMongoField(fieldName));
        }
        field.append("    </mongo_fields>\n");
        return mongoXml.replace("    <mongo_fields>\n" +
                "    </mongo_fields>\n", field);
    }

    /**
     * 生成mongo的字段参数
     */
    private static String getMongoField(String name) {
        String re = "<mongo_field>\n" +
                "    <incoming_field_name>QAQname</incoming_field_name>\n" +
                "    <mongo_doc_path/>\n" +
                "    <use_incoming_field_name_as_mongo_field_name>Y</use_incoming_field_name_as_mongo_field_name>\n" +
                "    <update_match_field>N</update_match_field>\n" +
                "    <modifier_update_operation>N/A</modifier_update_operation>\n" +
                "    <modifier_policy/>\n" +
                "    <json_field>N</json_field>\n" +
                "    <allow_null>N</allow_null>\n" +
                "</mongo_field>\n";

        re = re.replace("QAQname", name);
        return re;
    }

    private static String encryptPassword(String password) {
        if (password == null) {
            return "";
        }
        if (password.length() == 0) {
            return "";
        }
        BigInteger biPassword = new BigInteger(password.getBytes());
        BigInteger biR0 = new BigInteger(SEED);
        BigInteger biR1 = biR0.xor(biPassword);
        return biR1.toString(RADIX);
    }

}
