package com.plantdata.kgcloud.sdk.kgcompute.dax;

import com.google.common.collect.Lists;
import com.sun.deploy.util.StringUtils;

import java.util.List;

public class DaxSqlGenerator {

    public void generateSQL(DaxContext dc){

        String select = "SELECT ";
        String table = "FROM ";
        String where = "";
        String groupby = "";
        String join = "";

        //必须得有tables
        table = table.concat(StringUtils.join(dc.sql_tables,",")).concat(" ");

        //join也写在WHERE里
        if(dc.sql_join.size()>0 || dc.sql_where.size()>0){

            for(int i=0;i<dc.sql_where.size();i++){
                dc.sql_where.set(i,dc.sql_where.get(i).replaceAll("\\|\\|"," OR ").replaceAll("\\&\\&"," AND "));
            }

            List<String> tmp_where = Lists.newArrayList();
            tmp_where.addAll(dc.sql_join);
            tmp_where.addAll(dc.sql_where);

            where = where.concat("WHERE ")
                    .concat(StringUtils.join(tmp_where," AND ")).concat(" ");
        }


        //处理GROUPBY，还是得在select上补
        if(dc.sql_groupby.size()>0){
            groupby = groupby.concat("GROUP BY ")
                             .concat(StringUtils.join(dc.sql_groupby,",")).concat(" ");
        }

        if(dc.sql_select.size()==0 && dc.sql_groupby.size()==0){
            select = select
                    .concat("*").concat(" ");
        }
        else {

            List<String> tmp_select = Lists.newArrayList();

            for(int i=0;i<dc.sql_groupby.size();i++){
                String col = dc.sql_groupby.get(i);
                String alias = dc.sql_groupby_alias.get(i);
                if(alias!=""){
                    col = col +" \""+ alias + "\"";
                }
                tmp_select.add(col);
            }

            for(int i=0;i<dc.sql_select.size();i++){
                String col = dc.sql_select.get(i);
                String alias = dc.sql_select_alias.get(i);
                if(alias!=""){
                    col = col +" \""+ alias + "\"";
                }
                tmp_select.add(col);
            }

            select = select
                    .concat(StringUtils.join(tmp_select,",")).concat(" ");
        }

        String whole_sql = select + " " + table + " " + where + " " + join + groupby;

        dc.sql = whole_sql;

        System.out.println(whole_sql);

    }
}
