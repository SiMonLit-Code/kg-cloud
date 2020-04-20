package com.plantdata.kgcloud.sdk.kgcompute.compute;

import java.sql.Connection;
import java.sql.DriverManager;

public class PrestoSingleton {

    private static String presto_conn_info = "192.168.4.145:16666";

    private static String jdbc_url = "jdbc:presto://"+presto_conn_info+"/system/runtime";

    private static Connection connection = null;

    private static void init(){
        try {
            Class.forName("com.facebook.presto.jdbc.PrestoDriver");
            connection = DriverManager.getConnection(jdbc_url,"root","");
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
