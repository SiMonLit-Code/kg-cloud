package com.plantdata.kgcloud.plantdata.presto.compute;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class PrestoSingleton {

    private static Connection connection = null;

    @Value("${presto.ip:}")
    private static String ip;
    @Value("${presto.port:}")
    private static String port;
    @Value("${presto.user:}")
    private static  String user;
    @Value("${presto.password:}")
    private static  String password;

    private static void init(){
        try {
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
