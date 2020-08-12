package ai.plantdata.kgcloud.domain.audit.listener;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.domain.audit.service.ApiAuditService;
import ai.plantdata.kgcloud.sdk.mq.ApiAuditMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @ClassName ApiAuditMessageListener
 * @Function TODO
 * @Author wanglong
 * @Date 2019/12/6 13:19
 * @Version 3.0.0
 **/
@Component
@Slf4j
public class ApiAuditMessageListener {
    @Autowired
    private ApiAuditService apiAuditService;

    @KafkaListener(topics = "${topic.api.audit}")
    public void onKgLog(String message, Acknowledgment acknowledgment) {
        try {
            log.debug("[收到kafka<topic.api.audit>消息] - [{}]", message);
            apiAuditService.logApiAudit(JacksonUtils.readValue(message, ApiAuditMessage.class));
        } catch (Exception e) {
            log.error("消费kafka<topic.api.audit>消息失败", e);
            //todo
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
