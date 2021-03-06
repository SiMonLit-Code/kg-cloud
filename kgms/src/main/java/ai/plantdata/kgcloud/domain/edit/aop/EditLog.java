package ai.plantdata.kgcloud.domain.edit.aop;


import ai.plantdata.kgcloud.domain.edit.service.LogSender;
import com.plantdata.graph.logging.core.ServiceEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Author: LinHo
 * @Date: 2020/1/11 17:00
 * @Description:
 */
@Aspect
@Component
public class EditLog {

    @Autowired
    private LogSender logSender;

    @Around("@annotation(ai.plantdata.kgcloud.domain.edit.aop.EditLogOperation)")
    public Object logOperation(ProceedingJoinPoint p) throws Throwable {
        Object[] args = p.getArgs();
        logSender.setActionId();
        Object proceed = p.proceed();
        if (logSender.isEnableLog()) {
            String kgName = (String) args[0];
            Signature sig = p.getSignature();
            MethodSignature msig;
            if (!(sig instanceof MethodSignature)) {
                throw new IllegalArgumentException("该注解只能用于方法");
            }
            msig = (MethodSignature) sig;
            Object target = p.getTarget();
            Method method;
            try {
                method = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
                Annotation annotation = method.getAnnotation(EditLogOperation.class);
                ServiceEnum serviceEnum = ((EditLogOperation) annotation).serviceEnum();
                logSender.sendLog(kgName, serviceEnum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logSender.remove();
        return proceed;
    }

}
