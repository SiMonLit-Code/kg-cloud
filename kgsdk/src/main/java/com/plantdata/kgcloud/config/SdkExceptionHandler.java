package com.plantdata.kgcloud.config;

import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.exception.SdkException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

/**
 * @author cjw
 * @version 1.0
 * @date 2020/1/2 14:58
 */
@Aspect
@Configuration
public class SdkExceptionHandler {

    @AfterThrowing(pointcut = "execution(* com.plantdata.kgcloud.plantdata.controller.*.*(..))", throwing = "ex")
    public void log(JoinPoint pjp, Throwable ex) {
        if (ex instanceof BizException) {
            BizException bizException = (BizException) ex;
            throw new SdkException(bizException.getErrCode(), bizException.getMessage());
        }
        throw new SdkException(ex.getMessage());
    }
}
