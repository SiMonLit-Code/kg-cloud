package com.plantdata.kgcloud.plantdata.utilCode.kgcompute.compute;

import java.sql.Connection;
import java.sql.DriverManager;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

public class PrestoSingleton {

    private static Connection connection = null;

    private static void init(){
        try {
            String presto_conn_info = "192.168.4.145:16666";
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
