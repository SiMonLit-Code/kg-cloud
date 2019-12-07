package com.plantdata.kgcloud.aop;

import com.plantdata.kgcloud.producer.KafkaMessageProducer;
import com.plantdata.kgcloud.sdk.mq.ApiAuditMessage;
import com.plantdata.kgcloud.util.WebUtils;
import org.apache.commons.lang.StringUtils;
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
        if (StringUtils.isEmpty(kgName)) {
            return pjp.proceed();
        }
        String uri = WebUtils.getHttpRequest().getRequestURI();
        String page = WebUtils.getHttpRequest().getParameter(PAGE);
        if (StringUtils.isEmpty(page)) {
            page = PAGE_DEVELOPER;
        }
        int status = 1;
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            status = 1;
            throw throwable;
        } finally {
            kafkaMessageProducer.sendMessage("topic.api.audit", new ApiAuditMessage(kgName, page, uri, status));
        }
    }
}
