package com.plantdata.kgcloud.plantdata.utilCode.kgcompute.compute;

import java.sql.Connection;
import java.sql.DriverManager;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

public class PrestoSingleton {
    static Config appConfig;

    private static Connection connection = null;

    private static void init(){
        try {
            appConfig = ConfigService.getConfig("kgsdk");
            String presto_conn_info = appConfig.getProperty("presto.server",null);
            String jdbc_url = "jdbc:presto://"+presto_conn_info+"/system/runtime";
            String userName = "root";
            String password = "";
            Class.forName("com.facebook.presto.jdbc.PrestoDriver");
            connection = DriverManager.getConnection(jdbc_url,userName,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        if(connection==null){
            init();
        }
        return connection;
    }
}
