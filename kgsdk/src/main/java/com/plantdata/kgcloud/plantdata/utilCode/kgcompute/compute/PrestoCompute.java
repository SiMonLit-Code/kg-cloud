package com.plantdata.kgcloud.plantdata.utilCode.kgcompute.compute;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.plantdata.utilCode.kgcompute.bean.chart.ChartTableBean;
import com.plantdata.kgcloud.plantdata.utilCode.kgcompute.dax.DaxContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PrestoCompute {

    public Object compute(DaxContext dc){

        String sql = dc.sql;

        Connection conn = PrestoSingleton.getConnection();

        ResultSet rs = null;

        try {

            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            return wrapResultSet(rs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public Object compute(String sql){

        Connection conn = PrestoSingleton.getConnection();

        ResultSet rs = null;

        try {

            Statement pstmt = conn.createStatement();
            rs = pstmt.executeQuery(sql);
            return wrapResultSet(rs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private ChartTableBean wrapResultSet(ResultSet rs) throws Exception{

        ChartTableBean ctb = new ChartTableBean();

        int colunm_count = rs.getMetaData().getColumnCount();

        for(int i=1;i<=colunm_count;i++){
            String name = rs.getMetaData().getColumnName(i);
            ctb.addName(name);
        }

        while(rs.next()){

            List<Object> data = Lists.newArrayList();

            for(int i=1;i<=colunm_count;i++){
                data.add(rs.getObject(i));
            }

            ctb.addData(data);
        }

        return ctb;
    }

}
