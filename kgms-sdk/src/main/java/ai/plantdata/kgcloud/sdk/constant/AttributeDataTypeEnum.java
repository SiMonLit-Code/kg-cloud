package ai.plantdata.kgcloud.sdk.constant;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/19 10:04
 */
@Getter
public enum AttributeDataTypeEnum {

    /**
     *
     */
    OBJECT(0, "对象型"),
    INTEGER(1, "整数值"),
    FLOAT(2, "浮点值"),
    BOOLEAN(3, "布尔"),
    DATETIME(4, "日期时间"),
    DATE(41, "日期"),
    TIME(42, "时间"),
    STRING(5, "短文本型"),
    RANGE(6, "范围"),
    LIST(7, "集合"),
    MAP(8, "Map型"),
    URL(9, "超链接"),
    TEXT(10, "文本型"),
    IMAGE(91, "图片"),
    VIDEO(92, "音、视频"),
    ATTACHMENT(93, "文件");
    public static final Set<AttributeDataTypeEnum> DATA_VALUE_SET = Sets.newHashSet(INTEGER, FLOAT);
    public static final Set<AttributeDataTypeEnum> DATE_SET = Sets.newHashSet(DATE, TIME, DATETIME);
    public static final Set<Integer> FROM_TO_TIME_SET = Sets.newHashSet(-1, -2);

    AttributeDataTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private int value;
    private String desc;

    public static AttributeDataTypeEnum parseById(Integer dataType) {
        if (dataType == null) {
            return null;
        }
        for (AttributeDataTypeEnum dataTypeEnum : AttributeDataTypeEnum.values()) {
            if (dataTypeEnum.value == dataType) {
                return dataTypeEnum;
            }
        }
        return null;
    }
}
