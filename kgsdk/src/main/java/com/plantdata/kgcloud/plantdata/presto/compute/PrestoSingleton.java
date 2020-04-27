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

    static Config appConfig;

    private static String ip;
    private static String port;
    private static  String user;
    private static  String password;

    @SuppressWarnings("static-access")
    @Value("${presto.ip}")
    public void setIp(String ip) {
        this.ip = ip;
    }

    @SuppressWarnings("static-access")
    @Value("${presto.port}")
    public void setPort(String port) {
        this.port = port;
    }

    @SuppressWarnings("static-access")
    @Value("${presto.user}")
    public void setUser(String user) {
        this.user = user;
    }

    @SuppressWarnings("static-access")
    @Value("${presto.password}")
    public void setPaasword(String password) {
        this.password = password;
    }

    private static void init(){
        try {
            //appConfig = ConfigService.getConfig("kgsdk");
            String jdbc_url = "jdbc:presto://"+ip+":"+port+"/system/runtime";
            String userName = user;
            if(password !=null && password.equals("none")){
                password = "";
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
