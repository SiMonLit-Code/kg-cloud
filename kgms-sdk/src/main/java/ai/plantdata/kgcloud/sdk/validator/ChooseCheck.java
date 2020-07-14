package ai.plantdata.kgcloud.sdk.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @author Administrator
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ChooseValidator.class})
public @interface ChooseCheck {

    String message() default "格式错误";

    boolean isBlank() default false;

    Class<?> type() default Integer.class;

    String name() default "";

    String value() ;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
