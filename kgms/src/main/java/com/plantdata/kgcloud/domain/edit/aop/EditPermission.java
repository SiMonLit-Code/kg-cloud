package com.plantdata.kgcloud.domain.edit.aop;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.manage.service.GraphService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.GraphRsp;
import com.plantdata.kgcloud.security.SessionHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: LinHo
 * @Date: 2019/12/25 15:01
 * @Description: 只能编辑自己的图
 */
@Component
@Aspect
@Slf4j
public class EditPermission {

    @Autowired
    private GraphService graphService;

    @Pointcut("execution(* com.plantdata.kgcloud.domain.edit.controller.*.*(..)) && !@annotation" +
            "(EditPermissionUnwanted)")
    public void pointCutPermission() {
    }

    @Pointcut("execution(* com.plantdata.kgcloud.domain.graph.attr.controller.*.*(..)) && !@annotation" +
            "(EditPermissionUnwanted)")
    public void pointCutAttrGroup() {

    }

    @Pointcut("@annotation(EditPermissionRequired)")
    public void pointCutAnnotation() {
    }

    @Around("pointCutPermission() || pointCutAttrGroup() || pointCutAnnotation()")
    public Object check(ProceedingJoinPoint p) throws Throwable {
        Object[] args = p.getArgs();
        String kgName = (String) args[0];
        String userId = SessionHolder.getUserId();
        log.info("kgName : " + kgName);
        log.info("userId : " + userId);
        try {
            GraphRsp graphRsp = graphService.findById(userId, kgName);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.PERMISSION_NOT_ENOUGH_ERROR);
        }
        return p.proceed();
    }
}
