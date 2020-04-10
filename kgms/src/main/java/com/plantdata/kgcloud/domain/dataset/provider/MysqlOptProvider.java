package com.plantdata.kgcloud.domain.dataset.provider;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.plantdata.kgcloud.constant.CommonConstants;
import com.plantdata.kgcloud.domain.dataset.constant.DataConst;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MysqlOptProvider implements DataOptProvider {

    private final JdbcTemplate jdbcTemplate;
    private final String table;

    public MysqlOptProvider(DataOptConnect info) {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://" + info.getAddresses().get(0) + "/" + info.getDatabase() + "?characterEncoding=utf8&useSSL=false&connectTimeout=1000&socketTimeout=1000");

        dataSourceBuilder.username(info.getUsername());
        dataSourceBuilder.password(info.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSourceBuilder.build());

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
        return null;
    }

    @Override
    public List<Map<String, Object>> find(Integer offset, Integer limit, Map<String, Object> query) {
        return findWithSort(offset, limit, query, null);
    }

    @Override
    public List<Map<String, Object>> findWithSort(Integer offset, Integer limit, Map<String, Object> query, Map<String, Object> sort) {

        String sql = buildQuery(query);
        if (offset != null && offset >= 0 ) {
            int size = 10;
            if(limit != null && limit > 0){
                size = limit;
            }
            sql += " limit "+offset+","+size;
        }

        List<Map<String, Object>> rs = jdbcTemplate.queryForList(sql);
        return rs;
    }

    @Override
    public long count(Map<String, Object> query) {
        String sql = buildCount(query);

        List<Long> countList = jdbcTemplate.queryForList(sql,Long.class);
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
}
