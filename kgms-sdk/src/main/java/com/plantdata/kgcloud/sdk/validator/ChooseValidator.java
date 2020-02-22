package com.plantdata.kgcloud.sdk.validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.NumberUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class ChooseValidator implements ConstraintValidator<ChooseCheck, Object> {

    private boolean isBlank;
    private String name;
    private String value;
    private Class<?> type;

    public static <T> T jsonToObj(String jsonString, TypeReference<T> tr) {
        if (jsonString != null && !("".equals(jsonString))) {
            try {
                return JacksonUtils.getInstance().readValue(jsonString, tr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        return jsonToObj(jsonString, new TypeReference<List<T>>() {
        });
    }

    @Override
    public void initialize(ChooseCheck constraintAnnotation) {
        isBlank = constraintAnnotation.isBlank();
        name = constraintAnnotation.name();
        value = constraintAnnotation.value();
        type = constraintAnnotation.type();
        constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (type == String.class) {
            if (Objects.isNull(value) || "".equals(value)) {
                return isBlank;
            }
        }
        if (Objects.nonNull(value)) {
            context.disableDefaultConstraintViolation();
            String msg = "";
            if (StringUtils.isNoneBlank(name)) {
                msg = "参数" + name;
            }
            Class<? extends Number> numberClass = getNumberClass(type);
            if (numberClass != null && org.apache.commons.lang.math.NumberUtils.isNumber(value.toString())) {
                value = NumberUtils.parseNumber(value.toString(), Integer.class);
            }
            List<?> list;
            try {
                list = jsonToList(this.value, type);
            } catch (Exception e) {
                msg += "json数据格式错误";
                context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
                return false;
            }
            if (list.contains(value)) {
                return true;
            } else {
                msg += "错误，参数填写范围" + this.value;
                context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
                return false;
            }
        }
        return isBlank;
    }

    private Class<? extends Number> getNumberClass(Class targetClass) {
        if (Byte.class == targetClass) {
            return Byte.class;
        } else if (Short.class == targetClass) {
            return Short.class;
        } else if (Integer.class == targetClass) {
            return Integer.class;
        } else if (Long.class == targetClass) {
            return Long.class;
        } else if (BigInteger.class == targetClass) {
            return BigInteger.class;
        } else if (Float.class == targetClass) {
            return Float.class;
        } else if (Double.class == targetClass) {
            return Double.class;
        } else if (BigDecimal.class == targetClass) {
            return BigDecimal.class;
        } else {
            return null;
        }
    }
}

