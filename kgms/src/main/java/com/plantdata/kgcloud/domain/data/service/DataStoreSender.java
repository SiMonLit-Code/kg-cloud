package com.plantdata.kgcloud.domain.data.service;

import com.plantdata.kgcloud.domain.data.bo.DataStoreBO;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @Author: LinHo
 * @Date: 2020/3/24 15:22
 * @Description:
 */
@Component
public class DataStoreSender {

    //默认设置topic
    private String topicChannelTransfer = "channel_transfer";

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMsg(String resourceName, DataStoreBO bo){
        Random rand = new Random();
        ProducerRecord<String,String> record = new ProducerRecord<>(topicChannelTransfer,rand.nextInt(10),
                resourceName, JacksonUtils.writeValueAsString(bo));
    }

}
