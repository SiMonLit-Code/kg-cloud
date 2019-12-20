package com.plantdata.kgcloud.domain.dataset.provider;


import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-11 13:48
 **/
public class ElasticSearchOptProviderTest {

    private DataOptProvider provider;

    @Before
    public void before() {
        DataOptConnect datasetInfo = new DataOptConnect();
        List<String> addrs = new ArrayList<>();
        addrs.add("192.168.4.16:9200");
        addrs.add("192.168.4.17:9200");
        addrs.add("192.168.4.18:9200");
        datasetInfo.setAddresses(addrs);
        datasetInfo.setDatabase("bj73pb4s_dataset_");
        datasetInfo.setTable("_doc");
        provider = new ElasticSearchOptProvider(datasetInfo);
    }


    @Test
    public void createTable() {
        List<DataSetSchema> colList = new ArrayList<>();
        DataSetSchema schema = new DataSetSchema();
        schema.setField("name");
        schema.setIsIndex(1);
        schema.setType(1);
        colList.add(schema);

        DataSetSchema schema2 = new DataSetSchema();
        schema2.setField("b");
        schema2.setType(2);
        colList.add(schema2);

        DataSetSchema schema3 = new DataSetSchema();
        schema3.setField("c");
        schema3.setType(3);
        colList.add(schema3);

        provider.createTable(colList);
    }

    @Test
    public void findOne() {

        provider.findOne("mfaN-W4BAruisY1fX8XP");
    }

    @Test
    public void insert() {
        Map<String, Object> objectNode = new HashMap<>();
        objectNode.put("name", "111");
        objectNode.put("age", 222);

        provider.insert(objectNode);


    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void find() {
        provider.find(null, null, null);
    }

    @Test
    public void count() {
        long count = provider.count(null);
        System.out.println(count);
    }

    @Test
    public void batchInsert() {

        Map<String, Object> objectNode = new HashMap<>();
        objectNode.put("name", "111");
        objectNode.put("age", 222);


        List<Map<String, Object>> nodes = new ArrayList<>();
        nodes.add(objectNode);
        nodes.add(objectNode);
        nodes.add(objectNode);
        provider.batchInsert(nodes);

    }

    @Test
    public void batchDelete() {
        provider.batchDelete(Collections.singletonList("AvbFHG8BAruisY1fWsc1"));
    }
}