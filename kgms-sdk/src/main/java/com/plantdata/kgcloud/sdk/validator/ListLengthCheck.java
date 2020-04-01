package com.plantdata.kgcloud.sdk.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ListLengthValidator.class})
public @interface ListLengthCheck {

    String message() default "参数长度错误";

    int min() default Integer.MIN_VALUE;
    int max() default Integer.MAX_VALUE;


    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
