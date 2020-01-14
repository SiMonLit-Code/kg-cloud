package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.graph.logging.core.GraphServiceLog;
import com.plantdata.graph.logging.core.ServiceEnum;
import com.plantdata.kgcloud.domain.edit.util.ThreadLocalUtils;
import com.plantdata.kgcloud.security.SessionHolder;
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
            kafkaTemplate.send(topicKgLog, kgName, new GraphServiceLog(serviceEnum,ThreadLocalUtils.setBatchNo(),
                    SessionHolder.getUserId()));
            ThreadLocalUtils.remove();
        }
    }
}
