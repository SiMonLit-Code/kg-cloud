package com.plantdata.kgcloud.domain.task.service.impl;


import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.domain.task.util.CreateKettleJob;
import com.plantdata.kgcloud.domain.task.req.KettleReq;
import com.plantdata.kgcloud.domain.task.service.KettleService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author: Bovin
 */
@Service
public class KettleServiceImpl implements KettleService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(KettleServiceImpl.class);

    private final static String DATA_PREFIX = "dataset";
    private final static String JOIN = "_";

    @Autowired
    private MongoProperties mongoProperties;
    @Autowired
    private FastFileStorageClient storageClient;

    @Override
    public Object kettleService(KettleReq etl) {
        DataSource dataSource = getDataSource(etl);
        Map<String, String> map = new HashMap<>();
        String s = "";
        Connection connection = null;
        try {
            if ("oracle".equalsIgnoreCase(etl.getConnectType())) {
                Class.forName("oracle.jdbc.OracleDriver");
                DriverManager.setLoginTimeout(1);
                String url = "jdbc:oracle:thin:@" + etl.getHost() + ":" + etl.getPort() + ":" + etl.getDatabase();
                Properties props = new Properties();
                props.put("user", etl.getUsername());
                props.put("password", etl.getPassword());
                props.put("oracle.net.CONNECT_TIMEOUT", "1000");
                props.put("oracle.jdbc.ReadTimeout", "1000");
                connection = DriverManager.getConnection(url, props);
            } else {
                connection = dataSource.getConnection();
            }
            s = "连接测试成功!";
            map.put("status", "success");
        } catch (Exception e) {
            log.warn("kettle连接失败", e);
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
    public Object kettlePreview(KettleReq etlSaveRequest) {
        DataSource dataSource = getDataSource(etlSaveRequest);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        if (etlSaveRequest.getSql() == null) {
            return "请输入sql语句!";
        }
        String sql = getSql(etlSaveRequest);
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            return "sql输入错误!";
        }
    }

    public List<String> dbFiledName(KettleReq etlSaveRequest) {
        DataSource dataSource = getDataSource(etlSaveRequest);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = getSql(etlSaveRequest);
        List<String> tableFieldList = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        SqlRowSetMetaData sqlRsmd = sqlRowSet.getMetaData();
        int columnCount = sqlRsmd.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            tableFieldList.add(sqlRsmd.getColumnName(i));
        }
        return tableFieldList;
    }

    private DataSource getDataSource(KettleReq etl) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        if ("mysql".equalsIgnoreCase(etl.getConnectType())) {
            dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
            dataSourceBuilder.url("jdbc:mysql://" + etl.getHost() + ":" + etl.getPort() + "/" + etl.getDatabase() + "?characterEncoding=utf8&useSSL=false&connectTimeout=1000&socketTimeout=1000");
        } else if ("oracle".equalsIgnoreCase(etl.getConnectType())) {
            dataSourceBuilder.driverClassName("oracle.jdbc.OracleDriver");
            dataSourceBuilder.url("jdbc:oracle:thin:@" + etl.getHost() + ":" + etl.getPort() + ":" + etl.getDatabase());
        } else if ("hive2".equalsIgnoreCase(etl.getConnectType())) {
            dataSourceBuilder.driverClassName("org.apache.hive.jdbc.HiveDriver");
            dataSourceBuilder.url("jdbc:hive2://" + etl.getHost() + ":" + etl.getPort() + "/" + etl.getDatabase());
        }
        dataSourceBuilder.username(etl.getUsername());
        dataSourceBuilder.password(etl.getPassword());
        return dataSourceBuilder.build();
    }

    private String getSql(KettleReq etlSaveRequest) {
        String sql = "";
        String s = "select * from ( ";
        if ("mysql".equalsIgnoreCase(etlSaveRequest.getConnectType())) {
            sql = s + etlSaveRequest.getSql() + " ) a limit 10";
        } else if ("oracle".equalsIgnoreCase(etlSaveRequest.getConnectType())) {
            sql = s + etlSaveRequest.getSql() + " ) a where rownum <= 10 ";
        } else if ("hive2".equalsIgnoreCase(etlSaveRequest.getConnectType())) {
            sql = s + etlSaveRequest.getSql() + " ) a limit 10";
        }
        return sql;
    }

    @Override
    public String kettleSave(String userId, KettleReq kettleReq) {
        kettleReq.setMongoAddress(Arrays.asList(mongoProperties.getAddrs()));
        kettleReq.setMongoTbName("kettleConfig_" + Long.toHexString(System.currentTimeMillis()));
        kettleReq.setMongoDbName(userId + JOIN + DATA_PREFIX);
        //获取从mysql接受的字段值，传递到MongoDB
        List<String> arrayList = dbFiledName(kettleReq);
        //转变成kettle文件
        String string = UUID.randomUUID().toString();
        String file = string + ".ktr";
        try {
            File kettleXml = CreateKettleJob.getKettleXml(kettleReq, arrayList, file);
            StorePath ktr = storageClient.uploadFile(new FileInputStream(kettleXml), kettleXml.length(), "ktr", null);
            return ktr.getFullPath();
        } catch (IOException e) {
            log.warn("etl文件保存失败", e);
        }
        return "";
    }
}

