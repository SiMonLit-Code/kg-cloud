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
            String jdbc_url = appConfig.getProperty("presto.path",null);
            String userName = appConfig.getProperty("presto.user",null);
            String password = appConfig.getProperty("presto.password",null);
            Class.forName(appConfig.getProperty("presto.class-name",null));
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
