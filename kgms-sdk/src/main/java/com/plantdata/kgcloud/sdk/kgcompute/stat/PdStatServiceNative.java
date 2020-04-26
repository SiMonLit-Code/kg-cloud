package com.plantdata.kgcloud.sdk.kgcompute.stat;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.sdk.kgcompute.compute.PrestoCompute;
import com.plantdata.kgcloud.sdk.kgcompute.stat.bean.*;

import java.util.List;

public class PdStatServiceNative {

    private PrestoCompute prestoCompute = new PrestoCompute();

    public Object excute(PdStatBean pdStatBean){

        String sql = pdStatToSql(pdStatBean);

        Object rs = prestoCompute.compute(sql);

        return rs;
    }

    private String pdStatToSql(PdStatBean pdStatBean){

        String sql = "";

        if(pdStatBean==null){
            return sql;
        }

        String sql_select = "SELECT ";
        String sql_from = "FROM mysql_145.cmdb.t_trans_log ";
        String sql_where = "";
        String sql_having = "";
        String sql_groupby = "";
        String sql_orderby = "";
        String sql_limit = "";

        if(pdStatBean.getDimensions()!=null || pdStatBean.getMeasures()!=null){

            List<String> sql_select_list = Lists.newArrayList();
            List<String> sql_groupby_list = Lists.newArrayList();

            for (PdStatBaseBean dimension : pdStatBean.getDimensions()) {
                sql_select_list.add(dimension.getName());
                sql_groupby_list.add(dimension.getName());
            }

            for (PdStatBaseBean measure : pdStatBean.getMeasures()) {
                String m = measure.getAggregator().getValue() + "(" + measure.getName() + ") ";
                sql_select_list.add(m);
            }

            sql_select += String.join(", ", sql_select_list);

            if(pdStatBean.getMeasures()!=null){
                sql_groupby = "GROUP BY ";
                sql_groupby += String.join(", ", sql_groupby_list);
            }
        }

        if(pdStatBean.getFilters()!=null){

            List<String> sql_where_list = Lists.newArrayList();
            List<String> sql_having_list = Lists.newArrayList();

            for (PdStatFilterBean filter : pdStatBean.getFilters()) {

                boolean hasAgg = false;
                String col;

                if(filter.getAggregator()!=null){
                    hasAgg = true;
                    col = filter.getAggregator().getValue() + "(" + filter.getName() + ")";
                }
                else{
                    col = filter.getName();
                }

                for (PdStatOneFilterBean pdStatOneFilterBean : filter.getFilter()) {

                    if(PdStatBaseBean.FilterOperEnum.IN.equals(pdStatOneFilterBean.getOper())){

                        if(hasAgg){

                        }
                        else{

                        }
                    }
                    else{

                        String f = col + " " + pdStatOneFilterBean.getOper().getValue() + " ";
                        if(pdStatOneFilterBean.getValues().get(0) instanceof String){
                            f += "'" + pdStatOneFilterBean.getValues().get(0) + "'";
                        }
                        else{
                            f += pdStatOneFilterBean.getValues().get(0);
                        }

                        if(hasAgg){

                            if(sql_having_list.size()>0){
                                f = pdStatOneFilterBean.getBool().getValue() + " " + f + " ";
                            }
                            sql_having_list.add(f);
                        }
                        else{

                            if(sql_where_list.size()>0){
                                f = pdStatOneFilterBean.getBool().getValue() + " " + f + " ";
                            }
                            sql_where_list.add(f);
                        }
                    }
                }
            }

            if(sql_where_list.size()>0){
                sql_where = "WHERE ";
                sql_where += String.join(" ", sql_where_list);
            }


            if(sql_having_list.size()>0){
                sql_having = "HAVING ";
                sql_having += String.join(" ", sql_having_list);
            }

        }

        if(pdStatBean.getOrders()!=null){

            List<String> sql_orderby_list = Lists.newArrayList();

            for (PdStatOrderBean order : pdStatBean.getOrders()) {

                String o = "";
                if(order.getAggregator()!=null){
                    o = order.getAggregator().getValue() + " (" + order.getName() + ") " + order.getOrder();
                }
                else{
                    o =  order.getName() + " " + order.getOrder();
                }

                sql_orderby_list.add(o);
            }

            if(sql_orderby_list.size()>0){
                sql_orderby = "ORDER BY ";
                sql_orderby += String.join(", ", sql_orderby_list);
            }

        }

        if(pdStatBean.getLimit()>0){
            sql_limit = "LIMIT ";
            sql_limit += pdStatBean.getLimit();
        }

        sql = sql_select
            + " " + sql_from
            + " " + sql_where
            + " " +  sql_groupby
            + " " +  sql_having
            + " " + sql_orderby
            + " " + sql_limit;

        System.out.println(sql);

        return sql;

    }
}
