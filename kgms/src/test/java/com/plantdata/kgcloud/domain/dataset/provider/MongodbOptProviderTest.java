package com.plantdata.kgcloud.domain.dataset.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-11 16:48
 **/
public class MongodbOptProviderTest {

    @Test
    public void insert() {
        DataOptConnect datasetInfo = new DataOptConnect();
        datasetInfo.setAddresses(Collections.singletonList("192.168.4.11:19130"));
        datasetInfo.setDatabase("u_test001_data");
        datasetInfo.setTable("asdsd_test001");
        MongodbOptProvider provider = new MongodbOptProvider(datasetInfo);
        Map<String,Object> node = new HashMap<>();
        node.put("a",1);
        node.put("b","2");
        node.put("c",1.1);
        Map<String,Object> node1 = new HashMap<>();
        node1.put("dd",1);
        node.put("d",node1);
        provider.insert(node);
    }
}