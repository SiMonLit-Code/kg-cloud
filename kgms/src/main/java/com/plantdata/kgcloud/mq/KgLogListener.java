package com.plantdata.kgcloud.mq;

import com.alibaba.fastjson.JSONPath;
import com.fasterxml.jackson.databind.JavaType;
import com.mongodb.MongoClient;
import com.plantdata.graph.logging.core.*;
import com.plantdata.graph.logging.core.segment.EntitySegment;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.graph.logging.core.GraphLog;
import com.plantdata.graph.logging.core.GraphLogMessage;
import com.plantdata.graph.logging.core.GraphLogScope;
import com.plantdata.graph.logging.core.GraphServiceLog;
import com.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.plantdata.kgcloud.constant.KgmsConstants.*;

/**
 * @author xiezhenxiang 2020/1/15
 */
@Component
@Slf4j
public class KgLogListener {

    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private GraphRepository graphRepository;

    /**
     * 数据层日志监听
     *
     * @author xiezhenxiang 2020/1/15
     **/
    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = {"${topic.kg.log}"}, groupId = "graphLog")
    public void logListener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {

        try {
            Map<String, List<Document>> dataMap = new HashMap<>(50);
            records.forEach(record -> {
                String kgDbName = record.key();
                String value = record.value();
                String scope = JSONPath.read(value, "scope").toString();
                Class segmentClass = GraphLogScope.valueOf(scope).segmentClass();
                JavaType javaType = JacksonUtils.getInstance().getTypeFactory().constructParametricType(GraphLog.class, segmentClass);
                GraphLog graphLog = JacksonUtils.readValue(record.value(), javaType);
                GraphLog tmpLog = JacksonUtils.readValue(record.value(), javaType);
                GraphLogMessage.appendMessage(mongoClient, kgDbName, tmpLog);
                graphLog.setMessage(tmpLog.getMessage());

                if (StringUtils.isNotBlank(graphLog.getBatch()) && StringUtils.isNotBlank(graphLog.getMessage())) {
                    log.info("opt log: {}", graphLog.getMessage());
                    List<Document> ls = dataMap.getOrDefault(kgDbName, new ArrayList<>());
                    ls.add(Document.parse(JacksonUtils.writeValueAsString(graphLog)));
                    BasicConverter.consumerIfNoNull(graphRepository.findByDbName(kgDbName), a -> {
                        String kgName = a.getKgName();
                        dataMap.put(kgName, ls);
                    });
                }
            });

            for (Map.Entry<String, List<Document>> entry : dataMap.entrySet()) {

                String dbName = LOG_DB_PREFIX + entry.getKey();
                mongoClient.getDatabase(dbName).getCollection(LOG_DATA_TB).insertMany(entry.getValue());
            }
        } catch (Exception e) {
            records.forEach(s -> log.info(s.value()));
            log.error("Kafka消费异常", e);
        } finally {
            ack.acknowledge();
        }
    }

    /**
     * 业务层日志监听
     *
     * @author xiezhenxiang 2020/1/15
     **/
    @KafkaListener(containerFactory = "kafkaListenerContainerFactory",topics = {"${topic.kg.service.log}"}, groupId = "graphLog")
    public void serviceLogListener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {

        try {
            Map<String, List<Document>> dataMap = new HashMap<>(50);
            records.forEach(record -> {
                String kgName = record.key();
                GraphServiceLog serviceLog = JacksonUtils.readValue(record.value(), GraphServiceLog.class);
                if (StringUtils.isNotBlank(serviceLog.getBatch())) {
                    List<Document> ls = dataMap.getOrDefault(kgName, new ArrayList<>());
                    ls.add(Document.parse(record.value()));
                    dataMap.put(kgName, ls);
                }
            });

            for (Map.Entry<String, List<Document>> entry : dataMap.entrySet()) {
                String dbName = LOG_DB_PREFIX + entry.getKey();
                mongoClient.getDatabase(dbName).getCollection(LOG_SERVICE_TB).insertMany(entry.getValue());
            }
        } catch (Exception e) {
            records.forEach(s -> log.info(s.value()));
            log.error("Kafka消费异常", e);
        } finally {
            ack.acknowledge();
        }
    }

}
