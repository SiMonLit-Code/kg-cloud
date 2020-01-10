package com.plantdata.kgcloud.sdk.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author Administrator
 */
public class ListLengthValidator implements ConstraintValidator<ListLengthCheck, List> {


    private int min;
    private int max;

    @Override
    public void initialize(ListLengthCheck constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(List value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (value == null) {
            return true;
        }
        if (min > value.size()) {
            String msg = "数据长度必须大于等于" + min;
            context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
            return false;
        }
        if (max < value.size()) {
            String msg = "数据长度必须小于等于" + max;
            context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
            return false;
        }
        return true;
    }
}

