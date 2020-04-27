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
            //appConfig = ConfigService.getConfig("kgsdk");
            String jdbc_url = "jdbc:presto://"+System.getProperty("presto.ip")+":"+System.getProperty("presto.port")+"/system/runtime";
            String userName = System.getProperty("presto.user");
            String password;
            if(System.getProperty("presto.password")!=null && System.getProperty("presto.password").equals("none")){
                password = "";
            }else{
                password = System.getProperty("presto.password");
            }
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
