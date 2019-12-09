package com.plantdata.kgcloud.domain.dataset.constant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-07 14:31
 **/
@Getter
public enum FieldType {
    /**
     *
     */
    INDEX(11, DataConst.JSON_INDEX),
    SMOKE_MSG(12, DataConst.JSON_SMOKE_MSG),


    INTEGER(0, DataConst.JSON_INTEGER),
    STRING(1, DataConst.JSON_STRING),
    LONG(2, DataConst.JSON_LONG),
    DATE(3, DataConst.JSON_DATE),
    OBJECT(4, DataConst.JSON_OBJECT),
    NESTED(5, DataConst.JSON_NESTED),
    ARRAY(6, DataConst.JSON_ARRAY),
    STRING_ARRAY(7, DataConst.JSON_STRING_ARRAY),
    DOUBLE(8, DataConst.JSON_DOUBLE),
    FLOAT(9, DataConst.JSON_FLOAT);

    private final int code;
    private final JsonNode esProp;

    private final ObjectMapper objectMapper = new ObjectMapper();

    FieldType(int code, JsonNode esProp) {
        this.code = code;
        this.esProp = esProp;
    }

    public static FieldType findCode(int code) {
        for (FieldType value : FieldType.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return FieldType.STRING;
    }
}
