package ai.plantdata.kgcloud.domain.prebuilder.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bovin
 * @description
 * @since 2020-05-27 22:32
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostHandler {
    /**
     * 接口唯一 id
     *
     * @return id
     */
    int id() default 0;
}
