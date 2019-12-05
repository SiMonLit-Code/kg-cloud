package com.plantdata.kgcloud.service.impl;

import com.plantdata.kgcloud.service.KettleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Slf4j
@Service
public class KettleServiceImpl implements KettleService {
//    @Autowired
//    private ConfigManage configManage;
//
//    @Value("${model_path}")
//    private String path;
//
//    @Value("${upload.basePath}")
//    private String basePath;
//
//
//    // kettle配置测试
//    @Override
//    public Object kettleService(EtlSaveRequest etl) {
//        DataSource dataSource = getDataSource(etl);
//        Map<String, String> map = new HashMap<>();
//        String s = "";
//        Connection connection = null;
//        try {
//            if ("oracle".equalsIgnoreCase(etl.getConnectType())) {
//                Class.forName("oracle.jdbc.OracleDriver");
//                DriverManager.setLoginTimeout(1);
//                String url = "jdbc:oracle:thin:@" + etl.getHost() + ":" + etl.getPort() + ":" + etl.getDatabase();
//                Properties props = new Properties();
//                props.put("user", etl.getUsername());
//                props.put("password", etl.getPassword());
//                props.put("oracle.net.CONNECT_TIMEOUT", "1000");
//                props.put("oracle.jdbc.ReadTimeout", "1000");
//                connection = DriverManager.getConnection(url, props);
//            } else {
//                connection = dataSource.getConnection();
//            }
//            s = "连接测试成功!";
//            map.put("status", "success");
//        } catch (Exception e) {
//            log.warn("kettle连接失败", e);
//            String sysErr = e.getMessage();
//            s = "连接失败";
//            if ("The url cannot be null".equals(sysErr) || sysErr.indexOf("STRING index out of range") != -1) {
//                s = "请输入正确的数据库类型!";
//            }
//            if (sysErr.indexOf("Communications link failure") != -1) {
//                s = "请输入正确的数据库地址或者端口!";
//            }
//            if (sysErr.indexOf("Access denied for user") != -1) {
//                s = "请输入正确的用户名和密码!";
//            }
//            map.put("status", "fail");
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                }
//            }
//        }
//        map.put("msg", s);
//        return map;
//    }
//
//    // sql预览
//    @Override
//    public Object previewSqlEtl(EtlSaveRequest etlSaveRequest) {
//        DataSource dataSource = getDataSource(etlSaveRequest);
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        if (etlSaveRequest.getSql() == null) {
//            return "请输入sql语句!";
//        }
//        String sql = getSql(etlSaveRequest);
//        try {
//            return jdbcTemplate.queryForList(sql);
//        } catch (Exception e) {
//            return "sql输入错误!";
//        }
//    }
//
//    // 获取数据库字段名
//    public List dbFiledName(EtlSaveRequest etlSaveRequest) {
//        DataSource dataSource = getDataSource(etlSaveRequest);
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        String sql = getSql(etlSaveRequest);
//        List tableFieldList = new ArrayList();
//        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
//        SqlRowSetMetaData sqlRsmd = sqlRowSet.getMetaData();
//        int columnCount = sqlRsmd.getColumnCount();
//        for (int i = 1; i <= columnCount; i++) {
//            tableFieldList.add(sqlRsmd.getColumnName(i));
//        }
//        return tableFieldList;
//    }
//
//    // 获取数据库链接
//    private DataSource getDataSource(EtlSaveRequest etl) {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//
//        if ("mysql".equalsIgnoreCase(etl.getConnectType())) {
//            dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
//            dataSourceBuilder.url("jdbc:mysql://" + etl.getHost() + ":" + etl.getPort() + "/" + etl.getDatabase() + "?characterEncoding=utf8&useSSL=false&connectTimeout=1000&socketTimeout=1000");
//        } else if ("oracle".equalsIgnoreCase(etl.getConnectType())) {
//            dataSourceBuilder.driverClassName("oracle.jdbc.OracleDriver");
//            dataSourceBuilder.url("jdbc:oracle:thin:@" + etl.getHost() + ":" + etl.getPort() + ":" + etl.getDatabase());
//        } else if ("hive2".equalsIgnoreCase(etl.getConnectType())) {
//            dataSourceBuilder.driverClassName("org.apache.hive.jdbc.HiveDriver");
//            dataSourceBuilder.url("jdbc:hive2://" + etl.getHost() + ":" + etl.getPort() + "/" + etl.getDatabase());
//        }
//        dataSourceBuilder.username(etl.getUsername());
//        dataSourceBuilder.password(etl.getPassword());
//        return dataSourceBuilder.build();
//    }
//
//    // 对sql语句做处理
//    private String getSql(EtlSaveRequest etlSaveRequest) {
//        String sql = "";
//        String s = "select * from ( ";
//        if ("mysql".equalsIgnoreCase(etlSaveRequest.getConnectType())) {
//            sql = s + etlSaveRequest.getSql() + " ) a limit 10";
//        } else if ("oracle".equalsIgnoreCase(etlSaveRequest.getConnectType())) {
//            sql = s + etlSaveRequest.getSql() + " ) a where rownum <= 10 ";
//        } else if ("hive2".equalsIgnoreCase(etlSaveRequest.getConnectType())) {
//            sql = s + etlSaveRequest.getSql() + " ) a limit 10";
//        }
//        return sql;
//    }
//
//    // 保存 kettle
//    @Override
//    public String saveEtl(EtlSaveRequest etlSaveRequest, String userName) {
//        etlSaveRequest.setMongoip(configManage.getCloudMongoIp());
//        etlSaveRequest.setMongoport(configManage.getCloudMongoPort());
//        etlSaveRequest.setMongotbname("kettleConfig_" + Long.toHexString(System.currentTimeMillis()));
//        etlSaveRequest.setMongodbname("u_" + userName + "_data");
//        //获取从mysql接受的字段值，传递到MongoDB
//        List arrayList = dbFiledName(etlSaveRequest);
//        //转变成kettle文件
//        String relativePath = new StringBuilder(basePath).append("etl").append("/").toString();
//        String targetFile = new StringBuilder(McnUtils.simpleUUID()).append(".").append("ktr").toString();
//        try {
//            CreateKettleJob.getKettleXml(etlSaveRequest, arrayList, relativePath, targetFile);
//        } catch (IOException e) {
//            log.warn("etl文件保存失败", e);
//        }
//        return relativePath + targetFile;
//    }
}

