package ai.plantdata.kgcloud.aop;

import ai.plantdata.cloud.kafka.producer.KafkaMessageProducer;
import ai.plantdata.cloud.web.util.WebUtils;
import ai.plantdata.kgcloud.sdk.constant.ApiAuditStatusEnum;
import ai.plantdata.kgcloud.sdk.mq.ApiAuditMessage;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApiAuditAspect {
    private static final String KG_NAME = "kgName";
    private static final String PAGE = "p";
    private static final String PAGE_DEVELOPER = "developer";

    @Value("${topic.api.audit}")
    private String topicApiAudit;
    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    @Pointcut("execution(* com.plantdata.kgcloud..*Controller.*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames == null || parameterNames.length == 0) {
            return pjp.proceed();
        }
        Object[] args = pjp.getArgs();
        String kgName = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if (KG_NAME.equalsIgnoreCase(parameterNames[i])) {
                kgName = String.valueOf(args[i]);
                break;
            }
        }
        kgName = StringUtils.isEmpty(kgName) ? WebUtils.getHttpRequest().getParameter(KG_NAME) : kgName;
        if (StringUtils.isEmpty(kgName)) {
            return pjp.proceed();
        }
        String uri = WebUtils.getHttpRequest().getRequestURI();
        String page = WebUtils.getHttpRequest().getParameter(PAGE);
        if (StringUtils.isEmpty(page)) {
            page = PAGE_DEVELOPER;
        }
        ApiAuditStatusEnum status = ApiAuditStatusEnum.INVOKE_SUCCESS;
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            status = ApiAuditStatusEnum.INVOKE_FAILURE;
            throw throwable;
        } finally {
            kafkaMessageProducer.sendMessage(topicApiAudit, new ApiAuditMessage(kgName, page, uri, status));
        }
    }
}
