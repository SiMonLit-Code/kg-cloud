package com.plantdata.kgcloud.mq;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.plantdata.graph.logging.core.*;
import com.plantdata.graph.logging.core.segment.EntitySegment;
import com.plantdata.kgcloud.domain.graph.manage.repository.GraphRepository;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bson.Document;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    private RestClient restClient;
    @Autowired
    private GraphRepository graphRepository;

    /**
     * 数据层日志监听
     * @author xiezhenxiang 2020/1/15
     **/
    @KafkaListener(containerFactory = "kafkaListenerContainerFactory",topics = {"${topic.kg.log}"}, groupId = "log")
    public void logListener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {

        try {
            Map<String, List<Document>> dataMap = new HashMap<>(50);
            records.forEach(record -> {
                String kgDbName = record.key();
                String value = record.value();
                String scope = JSONPath.read(value, "scope").toString();
                Class segmentClass = GraphLogScope.valueOf(scope).segmentClass();
                JavaType javaType = JacksonUtils.getInstance().getTypeFactory().constructParametricType(GraphLog.class, segmentClass);
                GraphLog log = JacksonUtils.readValue(record.value(), javaType);
                GraphLogMessage.appendMessage(mongoClient, kgDbName, log);

                if (StringUtils.isNotBlank(log.getBatch()) && StringUtils.isNotBlank(log.getMessage())) {
                    List<Document> ls = dataMap.getOrDefault(kgDbName, new ArrayList<>());
                    ls.add(Document.parse(JacksonUtils.writeValueAsString(log)));
                    String kgName = graphRepository.findByDbName(kgDbName).getKgName();
                    dataMap.put(kgName, ls);
                    pinyinSyn(kgName, log);
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
     * @author xiezhenxiang 2020/1/15
     **/
    @KafkaListener(containerFactory = "kafkaListenerContainerFactory",topics = {"${topic.kg.service.log}"}, groupId = "log")
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

    /**
     * 拼音检索数据同步
     * @author xiezhenxiang 2020/1/16
     **/
    private void pinyinSyn (String kgName, GraphLog log) {

        if (log.getScope().equals(GraphLogScope.ENTITY) && openPinyin(kgName)) {
            EntitySegment segment =(EntitySegment)log.getNewValue();

            if (log.getOperation().equals(GraphLogOperation.ADD)) {
                JSONObject doc = new JSONObject()
                        .fluentPut("concept_id", segment.getConceptId())
                        .fluentPut("concept_list", Lists.newArrayList(segment.getConceptId()))
                        .fluentPut("name", segment.getName())
                        .fluentPut("entity_id", segment.getId());
                upsertById(kgName, doc, segment.getId());
            } else if (log.getOperation().equals(GraphLogOperation.UPDATE)) {
                if (segment.getName() != null) {
                    JSONObject doc = new JSONObject().fluentPut("name", segment.getName());
                    updateById(kgName, segment.getId(), doc);
                }
            } else {
                segment =(EntitySegment)log.getOldValue();
                deleteById(kgName, segment.getId());
            }
        }
    }

    private boolean openPinyin(String kgName) {

        String endpoint = "/" + kgName + "/_mapping/_doc";
        try {
            Request request = new Request("HEAD", endpoint);
            Response response = restClient.performRequest(request);
            return response.getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void upsertById(String kgName, JSONObject data, Long id) {

        String endpoint = "/" + kgName + "/_doc/" + id;
        NStringEntity entity = new NStringEntity(data.toJSONString(), ContentType.APPLICATION_JSON);
        try {
            Request request = new Request("PUT", endpoint);
            request.setEntity(entity);
            restClient.performRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateById(String kgName, Long id, JSONObject doc) {

        String endpoint = "/" + kgName + "/_doc/" + id + "/_update";
        JSONObject paraData = new JSONObject();
        paraData.put("doc", doc);
        NStringEntity entity = new NStringEntity(paraData.toJSONString(), ContentType.APPLICATION_JSON);
        try {
            Request request = new Request("POST", endpoint);
            request.setEntity(entity);
            restClient.performRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteById(String kgName, Long id) {

        String endpoint = "/" + kgName + "/_doc/" + id;
        try {
            Request request = new Request("DELETE", endpoint);
            restClient.performRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
