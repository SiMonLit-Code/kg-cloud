package com.plantdata.kgcloud.plantdata.presto.compute;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

import java.sql.Connection;
import java.sql.DriverManager;

public class PrestoSingleton {

    private static Connection connection = null;

    static Config appConfig;

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
