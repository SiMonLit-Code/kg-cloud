package ai.plantdata.kgcloud.domain.edit.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: LinHo
 * @Date: 2019/12/25 14:54
 * @Description: aop不能切在类上, 需要拥有改图谱
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EditPermissionRequired {
}
