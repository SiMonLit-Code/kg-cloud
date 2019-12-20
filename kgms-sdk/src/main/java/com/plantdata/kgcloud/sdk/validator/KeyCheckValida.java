package com.plantdata.kgcloud.sdk.validator;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: LinHo
 * @Date: 2019/12/19 11:03
 * @Description:
 */
public class KeyCheckValida implements ConstraintValidator<KeyCheck,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(value)){
            return true;
        }
        String newValue = value.replaceAll("_", "");
        return "".equals(newValue) || newValue.matches("^[A-Za-z]+$");
    }
}
