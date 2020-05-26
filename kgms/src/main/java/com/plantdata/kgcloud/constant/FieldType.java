package com.plantdata.kgcloud.constant;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
    INDEX(11, DataConst.JSON_INDEX) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            return null;
        }
    },
    SMOKE_MSG(12, DataConst.JSON_SMOKE_MSG) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            return null;
        }
    },


    INTEGER(0, DataConst.JSON_INTEGER) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            BigDecimal bigDecimal = new BigDecimal(obj.toString());
            return bigDecimal.intValue();
        }
    },
    STRING(1, DataConst.JSON_STRING) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            return obj;
        }
    },
    LONG(2, DataConst.JSON_LONG) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            BigDecimal bigDecimal = new BigDecimal(obj.toString());
            return bigDecimal.longValue();
        }
    },
    DATE(3, DataConst.JSON_DATE) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            String string = obj.toString();
            String date = "\\d{4}-\\d{2}-\\d{2}";
            String time = "\\d{2}:\\d{2}:\\d{2}";
            if (Pattern.matches(date, string)) {
                LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return string;
            } else if (Pattern.matches(time, string)) {
                LocalTime.parse(string, DateTimeFormatter.ofPattern("HH:mm:ss"));
                return string;
            } else {
                throw new RuntimeException();
            }
        }
    },
    DATETIME(13, DataConst.JSON_DATE) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            String string = obj.toString();
            String date = "\\d{4}-\\d{2}-\\d{2}";
            String time = "\\d{2}:\\d{2}:\\d{2}";
            String dateTime = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
            if (Pattern.matches(dateTime, string)) {
                LocalDate.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                return string;
            } else {
                throw new RuntimeException();
            }
        }
    },
    OBJECT(4, DataConst.JSON_OBJECT) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            return JacksonUtils.getInstance().readValue(obj.toString(), Map.class);
        }
    },
    NESTED(5, DataConst.JSON_NESTED) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            return JacksonUtils.getInstance().readValue(obj.toString(), Map.class);
        }
    },
    ARRAY(6, DataConst.JSON_ARRAY) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            return JacksonUtils.getInstance().readValue(obj.toString(), List.class);
        }
    },
    STRING_ARRAY(7, DataConst.JSON_STRING_ARRAY) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            return JacksonUtils.getInstance().readValue(obj.toString(), new TypeReference<List<String>>() {
            });
        }
    },
    DOUBLE(8, DataConst.JSON_DOUBLE) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            BigDecimal bigDecimal = new BigDecimal(obj.toString());
            return bigDecimal.doubleValue();
        }
    },
    FLOAT(9, DataConst.JSON_FLOAT) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            BigDecimal bigDecimal = new BigDecimal(obj.toString());
            return bigDecimal.floatValue();
        }
    },
    TEXT(10, DataConst.JSON_INDEX) {
        @Override
        public Object deserialize(Object obj) throws Exception {
            return null;
        }
    };

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

    public abstract Object deserialize(Object obj) throws Exception;
}
