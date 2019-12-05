package com.plantdata.kgcloud.domain.dataset.provider;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import org.apache.http.HttpHost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-11 13:48
 **/
public class ElasticSearchOptProviderTest {

    @Test
    public void createTable() {
        DataOptConnect datasetInfo = new DataOptConnect();
        datasetInfo.setAddresses("192.168.4.11:9200");
        datasetInfo.setDatabase("0000000000_000");
        datasetInfo.setTable("attrs");
        ElasticSearchOptProvider provider = new ElasticSearchOptProvider(datasetInfo);
        List<DataSetSchema> colList =new ArrayList<>();

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
    public void getAliases() {
        DataOptConnect datasetInfo = new DataOptConnect();
        datasetInfo.setAddresses("192.168.4.11:9200");
        datasetInfo.setDatabase("0000000000_000");
        datasetInfo.setTable("attrs");
        ElasticSearchOptProvider provider = new ElasticSearchOptProvider(datasetInfo);
        provider.dropTable();
    }

    @Test
    public void putAliases() {
        DataOptConnect datasetInfo = new DataOptConnect();
        datasetInfo.setAddresses("192.168.4.11:9200");
        datasetInfo.setDatabase("aaaaaaaaaaaaa_1555333065195");
        datasetInfo.setTable("attrs");
        ElasticSearchOptProvider provider = new ElasticSearchOptProvider(datasetInfo);

    }

    @Test
    public void clear(){
        HttpHost httpHost = new HttpHost("192.168.4.11", 9200);
        RestClient client = RestClient.builder(httpHost).setMaxRetryTimeoutMillis(60000).build();

        ObjectMapper objectMapper = new ObjectMapper();
        Request stats = new Request("GET", "/_stats");

        try {
            Response response = client.performRequest(stats);
            String string = EntityUtils.toString(response.getEntity());
            JsonNode node = objectMapper.readTree(string);
            Iterator<String> iterator = node.get("indices").fieldNames();
            while (iterator.hasNext()){
                String next = iterator.next();
                Request put = new Request("PUT", "/" + next + "/_settings");
                put.setEntity(new StringEntity("{\"number_of_replicas\":0}"));
                client.performRequest(put);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}