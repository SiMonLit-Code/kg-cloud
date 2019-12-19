package com.plantdata.kgcloud.sdk.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: LinHo
 * @Date: 2019/12/19 11:02
 * @Description:
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {KeyCheckValida.class})
public @interface KeyCheck {

    String message() default "唯一标示不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
