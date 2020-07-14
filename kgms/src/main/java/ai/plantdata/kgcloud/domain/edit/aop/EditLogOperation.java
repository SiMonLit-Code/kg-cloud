package ai.plantdata.kgcloud.domain.edit.aop;


import com.plantdata.graph.logging.core.ServiceEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: LinHo
 * @Date: 2020/1/11 17:00
 * @Description:
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EditLogOperation {
    ServiceEnum serviceEnum() default ServiceEnum.CONCEPT_DEFINE;
}
