package com.plantdata.kgcloud.domain.dataset.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.junit.Test;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-11 16:48
 **/
public class MongodbOptProviderTest {

    @Test
    public void insert() {
        DataOptConnect datasetInfo = new DataOptConnect();
        datasetInfo.setAddresses("192.168.4.11:19130");
        datasetInfo.setDatabase("u_test001_data");
        datasetInfo.setTable("asdsd_test001");
        MongodbOptProvider provider = new MongodbOptProvider(datasetInfo);
        ObjectMapper objectMapper = JacksonUtils.getInstance();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("a",1);
        node.put("b","2");
        node.put("c",1.1);
        ObjectNode node1 = objectMapper.createObjectNode();
        node1.put("dd",1);
        node.putPOJO("d",node1);
        provider.insert(node);
    }
}