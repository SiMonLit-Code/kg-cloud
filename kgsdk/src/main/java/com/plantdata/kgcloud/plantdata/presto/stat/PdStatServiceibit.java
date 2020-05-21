package com.plantdata.kgcloud.plantdata.presto.stat;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.plantdata.kgcloud.plantdata.presto.compute.PrestoCompute;
import com.plantdata.kgcloud.plantdata.presto.stat.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import tech.ibit.sqlbuilder.*;
import tech.ibit.sqlbuilder.aggregate.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdStatServiceibit {
    Config appConfig = ConfigService.getConfig("kgsdk");

    private PrestoCompute prestoCompute = new PrestoCompute();

    public Object excute(PdStatBean pdStatBean, String dbName, String tbName) throws Exception {

        List<String> sqls = pdStatToSql(pdStatBean, dbName, tbName);
        ArrayList<Object> re = new ArrayList<>();
        for (String sql : sqls) {
            Object rs = prestoCompute.compute(sql);
            re.add(rs);
        }


        return re;
    }


    private List<String> pdStatToSql(PdStatBean pdStatBean, String dbName, String tbName) {

        String alias = appConfig.getProperty("presto.dw.alias", null);
        if (pdStatBean == null) {
            return null;
        }
        ArrayList<String> sqls = new ArrayList<>();

        Map<String, PdStatFilterBean> filterBeanMap = new HashMap<>();

        for (PdStatFilterBean filter : pdStatBean.getFilters()) {
            String name = filter.getName();
            filterBeanMap.put(name, filter);
        }

        for (PdStatBaseBean measure : pdStatBean.getMeasures()) {


            Sql sql = new Sql();


            //select + groupby
            if (pdStatBean.getDimensions() != null) {
                for (PdStatBaseBean dimension : pdStatBean.getDimensions()) {
                    IColumn column = getColumn(dimension, alias, dbName, tbName);
                    sql.select(column);
                    sql.groupBy(column);
                }
            }

            //select agg

            IColumn column = getColumn(measure, alias, dbName, tbName);
            sql.select(column);


            //where + having
            if (filterBeanMap.containsKey(measure.getName())) {
                PdStatFilterBean filter = filterBeanMap.get(measure.getName());

                IColumn column_tmp = getColumn(filter, alias, dbName, tbName);
                for (PdStatOneFilterBean pdStatOneFilterBean : filter.getFilter()) {
                    getCondition(sql, column_tmp, pdStatOneFilterBean);
                }
            }


            // order by
            if (pdStatBean.getOrders() != null) {
                for (PdStatOrderBean order : pdStatBean.getOrders()) {
                    IColumn column_tmp = getColumn(order, alias, dbName, tbName);
                    NameOrderBy o = new NameOrderBy(column_tmp.getName(), PdStatBaseBean.OrderEnum.DESC.equals(order.getOrder()));
                    sql.orderBy(o);
                }
            }

            // from
            sql.from(new Table("", alias + "." + dbName + "." + tbName));

            String sql_str = getCompeletedSql(sql.getSqlParams().getSql(), sql.getSqlParams().getParams());

            if (pdStatBean.getLimit() > 0) {
                sql_str += " LIMIT " + pdStatBean.getLimit();
            }
            sqls.add(sql_str);
        }
        return sqls;

    }

    private Table getTableByColumn(PdStatBaseBean pdstat, String alias, String dbName, String tbName) {
        return new Table("", alias + "." + dbName + "." + tbName);

    }

    private IColumn getColumn(PdStatBaseBean pdstat, String alias, String dbName, String tbName) {

        Table table = getTableByColumn(pdstat, alias, dbName, tbName);
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
        } else {
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

    private String getCompeletedSql(String sql, List<Object> params) {

        if (params == null || params.size() == 0) {
            return sql;
        }

        int last_find_pos = 0;
        StringBuffer sb = new StringBuffer(sql);

        while ((last_find_pos = sb.indexOf("?", last_find_pos)) > 0) {
            Object p = params.remove(0);
            String value = p.toString();

            if (p instanceof String) {
                value = "'" + value + "'";
            }

            sb.replace(last_find_pos, last_find_pos + 1, value);
        }

        return sb.toString();
    }
}
