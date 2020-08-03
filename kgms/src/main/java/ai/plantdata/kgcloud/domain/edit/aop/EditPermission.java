package ai.plantdata.kgcloud.domain.edit.aop;

import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.cloud.web.util.SessionHolder;
import ai.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import ai.plantdata.kgcloud.domain.graph.manage.service.GraphService;
import ai.plantdata.kgcloud.sdk.rsp.GraphRsp;
import lombok.extern.slf4j.Slf4j;
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


    @Pointcut("execution(* ai.plantdata.kgcloud.domain.edit.controller.*.*(..)) && !@annotation" +
            "(ai.plantdata.kgcloud.domain.edit.aop.EditPermissionUnwanted)")
    public void pointCutPermission() {
    }

    @Pointcut("execution(* ai.plantdata.kgcloud.domain.graph.attr.controller.*.*(..)) && !@annotation" +
            "(ai.plantdata.kgcloud.domain.edit.aop.EditPermissionUnwanted)")
    public void pointCutAttrGroup() {

    }

    @Pointcut("@annotation(ai.plantdata.kgcloud.domain.edit.aop.EditPermissionRequired)")
    public void pointCutAnnotation() {
    }

    @Around("pointCutPermission() || pointCutAttrGroup() || pointCutAnnotation()")
    public Object check(ProceedingJoinPoint p) throws Throwable {
        Object[] args = p.getArgs();
        String kgName = (String) args[0];
        String userId = SessionHolder.getUserId();
        try {
            GraphRsp graphRsp = graphService.findById(userId, kgName);
        } catch (Exception e) {
            throw BizException.of(KgmsErrorCodeEnum.PERMISSION_NOT_ENOUGH_ERROR);
        }
        return p.proceed();
    }
}
