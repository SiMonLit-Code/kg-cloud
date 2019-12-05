package com.plantdata.kgcloud.domain.common.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author dinghao
 */
public class ValidatorForDateCheck implements ConstraintValidator<DateCheck, String> {

    private boolean isBlank;
    private String name;
    private String format;

    @Override
    public void initialize(DateCheck constraintAnnotation) {
        isBlank = constraintAnnotation.isBlank();
        name = constraintAnnotation.name();
        format = constraintAnnotation.format();
        constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isNoneBlank(value)) {
            String msg = "";
            if (StringUtils.isNoneBlank(name)) {
                msg = "参数" + name;
            }
            context.disableDefaultConstraintViolation();
            if (!Pattern.matches(format, value)) {
                msg += "日期格式错误,正确格式yyyy-MM-dd";
                context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
                return false;
            }
            return true;
        }
        return isBlank;
    }
}

