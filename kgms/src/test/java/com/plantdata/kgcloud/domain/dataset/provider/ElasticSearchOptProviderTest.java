package com.plantdata.kgcloud.domain.dataset.provider;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        datasetInfo.setDatabase("0000000000_0004");
        datasetInfo.setTable("_doc");
        provider = new ElasticSearchOptProvider(datasetInfo);
    }


    @Test
    public void createTable() {
        List<DataSetSchema> colList = new ArrayList<>();
        DataSetSchema schema = new DataSetSchema();
        schema.setField("a");
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
        ObjectNode objectNode = JacksonUtils.getInstance().createObjectNode();
        objectNode.putPOJO("a","111");
        objectNode.putPOJO("b","222");
        objectNode.put("c", DateUtils.formatDatetime());

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
        provider.find(null,null,null);
    }

    @Test
    public void count() {
        long count = provider.count(null);
        System.out.println(count);
    }
}