package ai.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 17:32
 * @Description:
 */
@Getter
public enum AttributeValueType {
    /**
     * 数值属性
     */
    NUMERIC(0),
    /**
     * 对象属性
     */
    OBJECT(1);

    private Integer type;

    AttributeValueType(Integer type) {
        this.type = type;
    }

    public static boolean isNumeric(Integer type) {
        return NUMERIC.getType().equals(type);
    }

    public static boolean isObject(Integer type) {
        return OBJECT.getType().equals(type);
    }
}
