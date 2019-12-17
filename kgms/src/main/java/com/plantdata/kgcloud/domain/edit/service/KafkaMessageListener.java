package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.constant.TaskStatus;
import com.plantdata.kgcloud.domain.task.entity.TaskGraphStatus;
import com.plantdata.kgcloud.domain.task.service.TaskGraphStatusService;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 20:46
 * @Description:
 */
@Component
@Slf4j
public class KafkaMessageListener {

    @Autowired
    private TaskGraphStatusService taskGraphStatusService;

    @KafkaListener(topics = "${topic.kg.task}")
    public void onKgTask(String message, Acknowledgment acknowledgment) {
        try {
            TaskGraphStatus taskGraphStatus = JacksonUtils.readValue(message, TaskGraphStatus.class);
            if (!TaskStatus.PROCESSING.getStatus().equals(taskGraphStatus.getStatus())) {
                log.info("[收到kafka日志监听消息] - [{}]", message);
                taskGraphStatusService.updateTaskStatus(taskGraphStatus.getId(), taskGraphStatus.getStatus());
            }
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("消费kafka日志消息失败", e);
        }
    }


//    public void listen(ConsumerRecord<String, String> record) {
//        try {
//            Optional<String> kafkaMessage = Optional.ofNullable(record.value());
//            if (kafkaMessage.isPresent()) {
//                TaskGraphStatus taskGraphStatus = JacksonUtils.readValue(kafkaMessage.get(), TaskGraphStatus.class);
//                if (!TaskStatus.PROCESSING.getStatus().equals(taskGraphStatus.getStatus())) {
//                    log.info("[收到kafka日志监听消息] - [{}]", record.value());
//                    taskGraphStatusService.updateTaskStatus(taskGraphStatus.getId(), taskGraphStatus.getStatus());
//                }
//            }
//        } catch (Exception e) {
//            log.error("消费kafka日志消息失败", e);
//        }
//    }
}
