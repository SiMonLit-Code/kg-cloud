package com.plantdata.kgcloud.domain.access.util;

import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.rsp.DWTableRsp;
import com.plantdata.kgcloud.sdk.constant.DataType;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class CreateKtrFile {

    private static final int RADIX = 16;
    private static final String SEED = "0933910847463829827159347601486730416058";

    /**
     * 功能执行方法
     *
     * @return
     * @throws IOException
     */
    public static String getKettleXmlPath(DWDatabase database, DWTableRsp table, String kafkaServers,String[] mongoAddrs,String mongoUserrname,String mongoPassword,String userId) {

        if(database.getDataFormat().equals(1)){
            //行业标准

            return getKtrIndustry(database,table,kafkaServers,mongoAddrs,mongoUserrname,mongoPassword,userId);
        }else{
            //非标准
            return getKtrNotIndustry(database,table,kafkaServers,mongoAddrs,mongoUserrname,mongoPassword);
        }


    }

    private static String getKtrIndustry(DWDatabase database, DWTableRsp table, String kafkaServers, String[] mongoAddrs, String mongoUserrname, String mongoPassword,String userId) {

        String xml = IndustryKtrXml.xml;
        String defaultXml = IndustryKtrXml.defaultStepXml;
        String orderXml = IndustryKtrXml.orderXml;
        String kafkaTruexml = IndustryKtrXml.kafkaTrueXml;
        String kafkaErrorxml = IndustryKtrXml.kafkaErrorXml;
        String paramAndFilterXml = IndustryKtrXml.paramAndFilterXml;

        String customizationXml = IndustryKtrXml.customizationXml;

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


        if(table.getCreateWay().equals(1)){
            //远程表
            type = DataType.findType(database.getDataType()).name();

            ip = database.getAddr().get(0).split(":")[0];

            port = database.getAddr().get(0).split(":")[1];

            dbName = database.getDbName();

            username = database.getUsername();

            password = database.getPassword();

            tableName = table.getTbName();

            if(DataType.MONGO.equals(DataType.findType(database.getDataType()))){
                connXml = "";
                jsonFieldxml = "";
                inputXml = IndustryKtrXml.mongoInputXml;
            }else{
                connXml = IndustryKtrXml.connectionXml;
                jsonFieldxml = IndustryKtrXml.jsonFieldxml;
                inputXml = IndustryKtrXml.mysqlInputXml;
            }
        }else{
            //本地表
            type = DataType.MONGO.name();

            ip = mongoAddrs[0].split(":")[0] ;

            port = mongoAddrs[0].split(":")[1];

            dbName = database.getDataName();

            username = mongoUserrname;

            password = mongoPassword;

            tableName = table.getTableName();

            connXml ="";

            jsonFieldxml = "";

            inputXml = IndustryKtrXml.mongoInputXml;

        }

        // 查询语句
        String queryXml = getTableSql(database.getDataType(),table,tableName);

        inputXml = changeSql(inputXml, queryXml);

        connXml = changeDBConnection(connXml, ip, port, dbName,tableName, username, password, type);

        inputXml = changeDBConnection(inputXml, ip, port, dbName,tableName, username, password, type);

        jsonFieldxml = changeJsonField(jsonFieldxml,table.getFields());

        defaultXml = changeDefaultXmlField(defaultXml,jsonFieldxml);

        kafkaTruexml = changeKafkaConnection(kafkaTruexml, kafkaServers);

        kafkaErrorxml = changeKafkaConnection(kafkaErrorxml, kafkaServers);

        paramAndFilterXml = changeParamAndFilterXml(paramAndFilterXml,database,table,userId);

        customizationXml = changeCustomizationXml(customizationXml,table);

        String data = xml + connXml + orderXml + defaultXml + inputXml + kafkaTruexml+ kafkaErrorxml + customizationXml + paramAndFilterXml;
        // 创建临时路径
        return data;

    }

    private static String changeCustomizationXml(String customizationXml, DWTableRsp table) {

        return customizationXml.replace("${code}",table.getKtr());
    }

    private static String changeParamAndFilterXml(String paramAndFilterXml, DWDatabase database, DWTableRsp table,String userId) {

        return paramAndFilterXml.replace("${db}", database.getDataName())
                                    .replace("${tb}",table.getTableName())
                                    .replace("${userId}", userId);

    }

    private static String getKtrNotIndustry(DWDatabase database, DWTableRsp table, String kafkaServers,String[] mongoAddrs,String mongoUserrname,String mongoPassword){
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


        if(table.getCreateWay().equals(1)){
            //远程表
            type = DataType.findType(database.getDataType()).name();

            ip = database.getAddr().get(0).split(":")[0];

            port = database.getAddr().get(0).split(":")[1];

            dbName = database.getDbName();

            username = database.getUsername();

            password = database.getPassword();

            tableName = table.getTbName();

            if(DataType.MONGO.equals(DataType.findType(database.getDataType()))){
                connXml = "";
                jsonFieldxml = "";
                inputXml = KtrXml.mongoInputXml;
            }else{
                connXml = KtrXml.connectionXml;
                jsonFieldxml = KtrXml.jsonFieldxml;
                inputXml = KtrXml.mysqlInputXml;
            }
        }else{
            //本地表
            type = DataType.MONGO.name();

            ip = mongoAddrs[0].split(":")[0] ;

            port = mongoAddrs[0].split(":")[1];

            dbName = database.getDataName();

            username = mongoUserrname;

            password = mongoPassword;

            tableName = table.getTableName();

            connXml ="";

            jsonFieldxml = "";

            inputXml = KtrXml.mongoInputXml;

        }


        // 查询语句
        String queryXml = getTableSql(database.getDataType(),table,tableName);

        inputXml = changeSql(inputXml, queryXml);

        connXml = changeDBConnection(connXml, ip, port, dbName,tableName, username, password, type);

        inputXml = changeDBConnection(inputXml, ip, port, dbName,tableName, username, password, type);

        jsonFieldxml = changeJsonField(jsonFieldxml,table.getFields());

        defaultXml = changeDefaultXmlField(defaultXml,jsonFieldxml);

        kafkaxml = changeKafkaConnection(kafkaxml, kafkaServers);

        String data = xml + connXml + orderXml + defaultXml + inputXml + kafkaxml;
        // 创建临时路径
        return data;
    }

    private static String changeDefaultXmlField(String defaultXml, String jsonFieldxml) {
        return defaultXml.replaceAll("fieldsQAQ",jsonFieldxml);
    }

    private static String changeJsonField(String jsonFieldxml,List<String> fields) {


        StringBuilder jsonFieldStr = new StringBuilder();
        for(String field : fields){
            jsonFieldStr.append(jsonFieldxml.replaceAll("fieldQAQ",field));
        }

        return jsonFieldStr.toString();
    }

    private static String changeKafkaConnection(String kafkaxml, String kafkaServers) {

        return kafkaxml.replace("kafkaQAQ", kafkaServers);
    }

    private static String getTableSql(Integer dataType,DWTableRsp table,String tableName) {

        StringBuilder sql = new StringBuilder();


        if(dataType == null || DataType.MONGO.equals(DataType.findType(dataType))){

            if(table.getIsAll() == null || table.getIsAll().equals(1)){
                return sql.toString();
            }else{
                return KtrXml.mongoTimeQueryXMl.replaceAll("timeField",table.getQueryField());
            }

        }else{
            sql.append("SELECT ");

            for(String field : table.getFields()){
                sql.append("`").append(field).append("`").append(",");

            }
            sql = sql.deleteCharAt(sql.length()-1);
            sql.append(" FROM ")
                    .append(tableName);

            if(table.getIsAll() != null && !table.getIsAll().equals(1)){

                sql.append(" WHERE ")
                        .append(table.getQueryField())
                        .append(" > ")
                        .append("${time}");

            }

            return sql.toString();
        }


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
                                             String type) {


        String encodePassword = encodePassword(password);

        return connectionXml.replace("ipQAQ", ip)
                .replace("postQAQ", port)
                .replace("dbnameQAQ", dbName)
                .replace("tbNameQAQ",tbName == null ? "":tbName)
                .replace("usernameQAQ", username == null ? "" : username)
                .replace("typeQAQ", type.toUpperCase())
                .replace("passwordQAQ", encodePassword);

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
