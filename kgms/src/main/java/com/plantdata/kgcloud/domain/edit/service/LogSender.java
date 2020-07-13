package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.graph.logging.core.GraphLog;
import com.plantdata.graph.logging.core.GraphServiceLog;
import com.plantdata.graph.logging.core.ServiceEnum;
import com.plantdata.kgcloud.domain.common.util.KGUtil;
import com.plantdata.kgcloud.domain.edit.util.ThreadLocalUtils;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: LinHo
 * @Date: 2020/1/11 18:24
 * @Description:
 */
@Component
public class LogSender {

    @Value("${topic.kg.service.log}")
    private String topicKgServiceLog;
    @Value("${topic.kg.log}")
    private String topicKgDataLog;
    @Value("${topic.kg.log}")
    private String topicKgLog;

    @Value("${kg.log.enable}")
    private boolean enableLog;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public boolean isEnableLog() {
        return enableLog;
    }

    public void sendLog(String kgName, ServiceEnum serviceEnum) {
        if (enableLog) {
            String userId = "";
            try {
                userId = SessionHolder.getUserId();
            } catch (Exception e) {

            }
            kafkaTemplate.send(topicKgServiceLog, kgName,
                    JacksonUtils.writeValueAsString(new GraphServiceLog(KGUtil.dbName(kgName), serviceEnum,
                            ThreadLocalUtils.getBatchNo(), userId)));
        }
    }

    public void sendKgLog(String kgDbName,GraphLog log){
        if (enableLog){
            ProducerRecord<String, String> record  = new ProducerRecord<>(topicKgLog, kgDbName, JacksonUtils.writeValueAsString(log));
            kafkaTemplate.send(record);
        }
    }

    public void sendDataLog(String kgDbName, GraphLog log) {
        if (enableLog) {
            ProducerRecord<String, String> record  = new ProducerRecord<>(topicKgDataLog, kgDbName, JacksonUtils.writeValueAsString(log));
            kafkaTemplate.send(record);
        }
    }

    public void setActionId(){
        ThreadLocalUtils.setBatchNo();
    }

    public void remove() {
        if (enableLog) {
            ThreadLocalUtils.remove();
        }
    }
}
