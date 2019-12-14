package com.plantdata.kgcloud.plantdata.link;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/14 17:00
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LinkModel {

    Class clazz() default Object.class;
}
