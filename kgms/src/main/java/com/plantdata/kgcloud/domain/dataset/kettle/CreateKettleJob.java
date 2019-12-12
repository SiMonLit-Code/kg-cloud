package com.plantdata.kgcloud.domain.dataset.kettle;

import com.plantdata.kgcloud.domain.dataset.req.EtlSaveRequest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;


/**
 * Date: 2019-05-08
 * Time: 下午1:18
 *
 * @author: logic
 * Description:
 * 该方法用以创建kettle任务
 */
public class CreateKettleJob {

    private static final int RADIX = 16;
    private static final String SEED = "0933910847463829827159347601486730416058";

    /**
     * 功能执行方法
     *
     * @param etlSaveRequest
     * @param mongoFields
     * @param targetFile
     * @return
     * @throws IOException
     */
    public static File getKettleXml(EtlSaveRequest etlSaveRequest, List<String> mongoFields, String targetFile) throws IOException {
        String xml = KettleXml.xml;
        String connectionXml = KettleXml.connectionxml;
        String sqlXml = KettleXml.sqlxml;
        String mongoXml = KettleXml.mongoxml;

        // 数据库类型
        String type = etlSaveRequest.getConnectType();
        // 数据库ip
        String ip = etlSaveRequest.getHost();
        // 数据库端口
        String port = etlSaveRequest.getPort();
        // 数据库名称
        String dbName = etlSaveRequest.getDatabase();
        // SQL语句
        String sql = etlSaveRequest.getSql();
        // 用户名
        String username = etlSaveRequest.getUsername();
        // 密码
        String password = etlSaveRequest.getPassword();
        List<String> mongoAddresses = etlSaveRequest.getMongoip();
        String mongoDbName = etlSaveRequest.getMongodbname();
        String mongoTbName = etlSaveRequest.getMongotbname();
        sqlXml = changeSql(sqlXml, sql);

        connectionXml = changeDBConnection(connectionXml, ip, port, dbName, username, password, type);

        mongoXml = changeMongoConnection(mongoXml, mongoAddresses, mongoDbName, mongoTbName);

        mongoXml = changeMongoField(mongoXml, mongoFields);

        String data = xml + connectionXml + sqlXml + mongoXml;
        // 创建临时路径
        File file = new File(targetFile);

        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fileWriter)) {
            bw.write(data);
            bw.flush();
        }
        return file;
    }

    /**
     * 修改sql语句
     *
     * @param sqlXml
     * @param sql
     * @return
     */
    private static String changeSql(String sqlXml, String sql) {
        return sqlXml.replace("<sql>select * from test</sql>", "<sql>" + sql + "</sql>");
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
                                             String username,
                                             String password,
                                             String type) {

        String encodePassword = encodePassword(password);
        return connectionXml.replace("ipQAQ", ip)
                .replace("portQAQ", port)
                .replace("dbnameQAQ", dbName)
                .replace("usernameQAQ", username)
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
