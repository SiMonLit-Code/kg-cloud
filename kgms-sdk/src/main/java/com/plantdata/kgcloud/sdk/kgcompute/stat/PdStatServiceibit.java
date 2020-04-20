package com.plantdata.kgcloud.sdk.kgcompute.stat;

import com.plantdata.kgcloud.sdk.kgcompute.compute.PrestoCompute;
import com.plantdata.kgcloud.sdk.kgcompute.stat.bean.*;
import tech.ibit.sqlbuilder.*;
import tech.ibit.sqlbuilder.aggregate.*;

import java.util.List;

public class PdStatServiceibit {

    private PrestoCompute prestoCompute = new PrestoCompute();

    public Object excute(PdStatBean pdStatBean,String dbName,String tbName) {

        String sql = pdStatToSql(pdStatBean,dbName,tbName);

        System.out.println(sql);

        Object rs = prestoCompute.compute(sql);

        return rs;
    }


    private String pdStatToSql(PdStatBean pdStatBean,String dbName,String tbName) {

        Sql sql = new Sql();

        if (pdStatBean == null) {
            return null;
        }

        //select + groupby
        if (pdStatBean.getDimensions() != null) {
            for (PdStatBaseBean dimension : pdStatBean.getDimensions()) {
                IColumn column = getColumn(dimension);
                sql.select(column);
                sql.groupBy(column);
            }
        }

        //select agg
        if (pdStatBean.getMeasures() != null) {
            for (PdStatBaseBean measure : pdStatBean.getMeasures()) {
                IColumn column = getColumn(measure);
                sql.select(column);
            }
        }

        //where + having
        if (pdStatBean.getFilters() != null) {
            for (PdStatFilterBean filter : pdStatBean.getFilters()) {
                IColumn column = getColumn(filter);
                for (PdStatOneFilterBean pdStatOneFilterBean : filter.getFilter()) {
                    getCondition(sql, column, pdStatOneFilterBean);
                }
            }
        }

        // order by
        if (pdStatBean.getOrders() != null) {
            for (PdStatOrderBean order : pdStatBean.getOrders()) {
                IColumn column = getColumn(order);
                NameOrderBy o = new NameOrderBy(column.getName(), PdStatBaseBean.OrderEnum.DESC.equals(order.getOrder()));
                sql.orderBy(o);
            }
        }

        // from
        sql.from(new Table("", dbName+"."+tbName));

        String sql_str = getCompeletedSql(sql.getSqlParams().getSql(), sql.getSqlParams().getParams());

        if (pdStatBean.getLimit() > 0) {
            sql_str += " LIMIT "  + pdStatBean.getLimit();
        }

        return sql_str;

    }

    private Table getTableByColumn(PdStatBaseBean pdstat) {

        return new Table("", "mysql_145.cmdb.t_trans_log");

    }

    private IColumn getColumn(PdStatBaseBean pdstat) {

        Table table = getTableByColumn(pdstat);
        Column column = new Column(table, pdstat.getName());

        if (pdstat.getAggregator() == null) {
            return column;
        }

        switch (pdstat.getAggregator()) {
            case SUM:
                SumColumn sumColumn = new SumColumn(column, pdstat.getAlias());
                return sumColumn;
            case COUNT:
                CountColumn countColumn = new CountColumn(column, pdstat.getAlias());
                return countColumn;
            case MIN:
                MinColumn minColumn = new MinColumn(column, pdstat.getAlias());
                return minColumn;
            case MAX:
                MaxColumn maxColumn = new MaxColumn(column, pdstat.getAlias());
                return maxColumn;
            case AVG:
                AvgColumn avgColumn = new AvgColumn(column, pdstat.getAlias());
                return avgColumn;
        }

        return column;
    }

    private void getCondition(Sql sql, IColumn column, PdStatOneFilterBean pstat) {

        if (column instanceof AggregateColumn) {
            getConditionHaving(sql, column, pstat);
        }
        else {
            getConditionWhere(sql, column, pstat);
        }
    }


    private void getConditionHaving(Sql sql, IColumn column, PdStatOneFilterBean pstat) {

        AggregateColumn aggregateColumn = (AggregateColumn) column;
        String aggregateColumnName = aggregateColumn.getName();
        HavingItem hi = null;

        switch (pstat.getOper()) {
            case EQ:
                hi = HavingItemMaker.equalsTo(aggregateColumnName, pstat.getValues().get(0));
                break;
            case GT:
                hi = HavingItemMaker.greaterThan(aggregateColumnName, pstat.getValues().get(0));
                break;
            case GTE:
                hi = HavingItemMaker.greaterThanOrEqualsTo(aggregateColumnName, pstat.getValues().get(0));
                break;
            case LT:
                hi = HavingItemMaker.lessThan(aggregateColumnName, pstat.getValues().get(0));
                break;
            case LTE:
                hi = HavingItemMaker.lessThanOrEqualTo(aggregateColumnName, pstat.getValues().get(0));
                break;
            case NE:
                hi = HavingItemMaker.notEqualsTo(aggregateColumnName, pstat.getValues().get(0));
                break;
        }

        if (PdStatBaseBean.FilterBoolEnum.AND.equals(pstat.getBool())) {
            sql.andHaving(hi);
        } else {
            sql.orHaving(hi);
        }
    }


    private void getConditionWhere(Sql sql, IColumn column, PdStatOneFilterBean pstat) {

        Column normalColumn = (Column) column;
        CriteriaItem ci = null;

        switch (pstat.getOper()) {
            case EQ:
                ci = CriteriaItemMaker.equalsTo(normalColumn, pstat.getValues().get(0));
                break;
            case GT:
                ci = CriteriaItemMaker.greaterThan(normalColumn, pstat.getValues().get(0));
                break;
            case GTE:
                ci = CriteriaItemMaker.greaterThanOrEqualsTo(normalColumn, pstat.getValues().get(0));
                break;
            case LT:
                ci = CriteriaItemMaker.lessThan(normalColumn, pstat.getValues().get(0));
                break;
            case LTE:
                ci = CriteriaItemMaker.lessThanOrEqualTo(normalColumn, pstat.getValues().get(0));
                break;
            case IN:
                ci = CriteriaItemMaker.in(normalColumn, pstat.getValues());
                break;
            case NE:
                ci = CriteriaItemMaker.notEqualsTo(normalColumn, pstat.getValues().get(0));
                break;
        }

        if (PdStatBaseBean.FilterBoolEnum.AND.equals(pstat.getBool())) {
            sql.andWhere(ci);
        } else {
            sql.orWhere(ci);
        }
    }

    private String getCompeletedSql(String sql, List<Object> params){

        if(params==null || params.size()==0){
            return sql;
        }

        int last_find_pos = 0;
        StringBuffer sb = new StringBuffer(sql);

        while( (last_find_pos = sb.indexOf("?", last_find_pos) ) >0){
            Object p = params.remove(0);
            String value = p.toString();

            if(p instanceof String){
                value = "'" + value + "'";
            }

            sb.replace(last_find_pos, last_find_pos+1, value);
        }

        return sb.toString();
    }
}
