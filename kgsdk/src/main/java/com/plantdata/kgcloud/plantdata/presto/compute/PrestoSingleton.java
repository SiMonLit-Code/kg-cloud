package com.plantdata.kgcloud.plantdata.presto.compute;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class PrestoSingleton {

    private static Connection connection = null;

    static Config appConfig;

    private static void init(){
        try {
            appConfig = ConfigService.getConfig("kgsdk");
            String ip = appConfig.getProperty("presto.ip",null);
            String port = appConfig.getProperty("presto.port",null);
            String user = appConfig.getProperty("presto.user",null);
            String password = appConfig.getProperty("presto.password",null);
            String jdbc_url = "jdbc:presto://"+ip+":"+port+"/system/runtime";
            if(password ==null || "none".equals(password)){
                password = "";
            }
            Class.forName("com.facebook.presto.jdbc.PrestoDriver");
            connection = DriverManager.getConnection(jdbc_url,user,password);
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
