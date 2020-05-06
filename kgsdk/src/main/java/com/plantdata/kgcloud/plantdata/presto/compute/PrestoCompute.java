package com.plantdata.kgcloud.plantdata.presto.compute;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.plantdata.presto.bean.chart.ChartTableBean;
import com.plantdata.kgcloud.plantdata.presto.dax.DaxContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PrestoCompute {


    public Object compute(String sql)throws Exception{

        Connection conn = PrestoSingleton.getConnection();

        ResultSet rs = null;

        Statement pstmt = null;

        try {

            pstmt = conn.createStatement();
            rs = pstmt.executeQuery(sql);
            return wrapResultSet(rs);

        } catch(SQLException e){
            throw e;
        } catch(Exception e) {
            throw e;
        } finally {
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(pstmt!=null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //return null;
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
