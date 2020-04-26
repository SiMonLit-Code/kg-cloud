package com.plantdata.kgcloud.domain.dataset.provider;

import com.mongodb.*;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.AggregateEnum;
import com.plantdata.kgcloud.sdk.constant.DataStoreSearchEnum;
import com.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.sdk.req.DwTableDataStatisticReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class MysqlOptProvider implements DataOptProvider {

    private final JdbcTemplate jdbcTemplate;
    private final String table;
    private final String dataBase;

    public MysqlOptProvider(DataOptConnect info) {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://" + info.getAddresses().get(0) + "/" + info.getDatabase() + "?characterEncoding=utf8&useSSL=false&connectTimeout=1000&socketTimeout=1000&zeroDateTimeBehavior=convertToNull");

        dataSourceBuilder.username(info.getUsername());
        dataSourceBuilder.password(info.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSourceBuilder.build());
        this.dataBase = info.getDatabase();
        this.table = info.getTable();
    }


    private String buildQuery(Map<String, Object> query) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM `").append(table).append("` ");
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            if (Objects.equals(entry.getKey(), "search")) {
                sql.append(" WHERE ");
                Map<String, String> value = (Map<String, String>) entry.getValue();
                for (Map.Entry<String, String> objectEntry : value.entrySet()) {
                    sql.append("`").append(objectEntry.getKey()).append("`").append(" like \"%").append(objectEntry.getValue()).append("%\"");
                }

            }
        }

        return sql.toString();
    }

    private String buildCount(Map<String, Object> query) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT count(*) FROM `").append(table).append("` ");
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            if (Objects.equals(entry.getKey(), "search")) {
                sql.append(" WHERE ");
                Map<String, String> value = (Map<String, String>) entry.getValue();
                for (Map.Entry<String, String> objectEntry : value.entrySet()) {
                    sql.append(objectEntry.getKey()).append(" like \"%").append(objectEntry.getValue()).append("%\"");
                }

            }
        }

        return sql.toString();
    }

    @Override
    public List<String> getFields() {
        StringBuilder query = new StringBuilder();
        convertSql(query, "SELECT", "COLUMN_NAME", "FROM", "information_schema.COLUMNS",
                "WHERE", "table_schema", "=", "'" + dataBase + "'", "AND", "table_name", "=", "'" + table + "'");
        List<String> query1 = jdbcTemplate.query(query.toString(), new SingleColumnRowMapper(String.class));
        return query1;
    }

    @Override
    public List<Map<String, Object>> find(Integer offset, Integer limit, Map<String, Object> query) {
        return findWithSort(offset, limit, query, null);
    }

    @Override
    public List<Map<String, Object>> findWithSort(Integer offset, Integer limit, Map<String, Object> query, Map<String, Object> sort) {

        String sql = buildQuery(query);
        if (offset != null && offset >= 0) {
            int size = 10;
            if (limit != null && limit > 0) {
                size = limit;
            }
            sql += " limit " + offset + "," + size;
        }

        try {
            List<Map<String, Object>> rs = jdbcTemplate.queryForList(sql);
            return rs;
        }catch (DataAccessException e){
            throw BizException.of(KgmsErrorCodeEnum.EXECUTE_SQL_ERROR);
        }

    }

    @Override
    public long count(Map<String, Object> query) {
        String sql = buildCount(query);

        List<Long> countList = jdbcTemplate.queryForList(sql, Long.class);
        return countList.get(0);

    }

    @Override
    public Map<String, Object> findOne(String id) {
        String sql = buildOne(id);

        List<Map<String, Object>> rs = jdbcTemplate.queryForList(sql);
        return rs.get(0);
    }

    private String buildOne(String id) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM `").append(table).append("` WHERE id = ").append(id);

        return sql.toString();
    }

    @Override
    public void createTable(List<DataSetSchema> colList) {

    }

    @Override
    public void dropTable() {

    }

    @Override
    public Map<String, Object> insert(Map<String, Object> node) {
        return null;
    }

    @Override
    public Map<String, Object> update(String id, Map<String, Object> node) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void batchInsert(List<Map<String, Object>> nodes) {

    }

    @Override
    public void batchDelete(Collection<String> ids) {

    }

    @Override
    public List<Map<String, Long>> statistics() {
        return null;
    }


    @Override
    public void close() throws IOException {

    }

    @Override
    public List<Map<String, Object>> search(Map<String, String> searchMap, int offset, int limit) {
        String field = searchMap.keySet().stream().reduce((a, b) -> a + "," + b).orElse(StringUtils.SPACE);
        StringBuilder builder = new StringBuilder();
        searchMap.forEach((key, value) -> {
            if (builder.length() > 0) {
                convertSql(builder, "or");
            }
            convertSql(builder, key, "like", "\"%" + value + "%\"");
        });
        StringBuilder select = new StringBuilder();
        convertSql(select, "select", field, "from", table, "where", builder.toString(), "limit", offset + "," + limit);
        return jdbcTemplate.queryForList(select.toString());
    }

    @Override
    public List<Map<String, Object>> aggregateStatistics(Map<String, Object> filterMap, Map<String, DwTableDataStatisticReq.GroupReq> groupMap, Map<SortTypeEnum, List<String>> sortMap) {
        StringBuilder querySb = new StringBuilder();
        StringBuilder groupSb = new StringBuilder();
        groupMap.forEach((k, v) -> {
            if (querySb.length() > 0) {
                querySb.append(",");
            }
            if (AggregateEnum.SUM == v.getAggregateType()) {
                convertSql(querySb, "sum(" + stringStr(v.getJsonPath()) + ")", "as", stringStr(k));
            } else if (AggregateEnum.COUNT == v.getAggregateType()) {
                convertSql(querySb, "count(" + stringStr(v.getJsonPath()) + ")", "as", stringStr(k));
            } else if (AggregateEnum.SHOW == v.getAggregateType()) {
                convertSql(querySb, stringStr(v.getJsonPath()), "as", stringStr(k));
                if (groupSb.length() > 0) {
                    groupSb.append(",");
                }
                groupSb.append(StringUtils.SPACE);
                convertSql(groupSb, stringStr(v.getJsonPath()));
            } else {
                throw BizException.of(KgmsErrorCodeEnum.DATA_STORE_STATISTIC_TYPE_ERROR);
            }
        });
        StringBuilder select = new StringBuilder("select");

        convertSql(select, querySb.toString(), "from", table);
        //筛选
        BasicConverter.consumerIfNoNull(filterMap, a -> convertSql(select, "where", getFilterSql(a)));
        //分组
        BasicConverter.consumerIfNoNull(groupSb.toString(), a -> convertSql(select, "group by", a));

        //排序
        Function<List<String>, Optional<String>> con = a -> a.stream().reduce((c, d) -> stringStr(c) + "," + stringStr(d));
        BasicConverter.consumerIfNoNull(sortMap, a -> {
            convertSql(select, "order by");
            sortMap.forEach((k, v) -> {
                convertSql(select, v.size() == 1 ? stringStr(v.get(0)) : con.apply(v).orElse(StringUtils.SPACE), k.getName());
            });
        });
        log.warn("(aggregateStatistics)sql:" + select.toString());
        return jdbcTemplate.queryForList(select.toString());
    }


    private String getFilterSql(Map<String, Object> filterMap) {
        StringBuilder sb = new StringBuilder();
        filterMap.forEach((k, v) -> {
            if (sb.length() > 0) {
                sb.append("and");
            }
            if (v instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) v;
                Function<Object, Optional<String>> function = a -> JsonUtils.objToList(a, String.class).stream().reduce((c, d) -> c + "," + d);
                Function<Object, Object> appendIfString = a -> a instanceof String ? "\"" + a + "\"" : a;
                BasicConverter.consumerIfNoNull(map.get("$eq"), a -> convertSql(sb, k, "=", appendIfString.apply(a)));
                BasicConverter.consumerIfNoNull(map.get(DataStoreSearchEnum.LIKE.getName()), a -> convertSql(sb, k, "like", "\"%" + a + "\"%"));
                BasicConverter.consumerIfNoNull(map.get(DataStoreSearchEnum.NOL_LIKE.getName()), a -> convertSql(sb, k, "not", "like", appendIfString.apply(a)));
                BasicConverter.consumerIfNoNull(map.get("$in"), a -> function.apply(a)
                        .ifPresent(s -> convertSql(sb, k, "in", "(", appendIfString.apply(s), ")")));
                BasicConverter.consumerIfNoNull(map.get("$nin"), a -> function.apply(a)
                        .ifPresent(s -> convertSql(sb, k, "not in", "(", appendIfString.apply(s), ")")));
            }
        });
        return sb.toString();
    }

    private String stringStr(String str) {
        return "`" + str + "`";
    }

    private void convertSql(StringBuilder sb, Object... strArray) {
        Arrays.stream(strArray).forEach(s -> sb.append(StringUtils.SPACE).append(s));
    }


}
