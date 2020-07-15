package ai.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 14:25
 * @Description:
 */
@Getter
public enum BasicInfoType {
    /**
     * 概念
     */
    CONCEPT(0),
    /**
     * 实体
     */
    ENTITY(1),
    /**
     * 同义
     */
    SYNONYM(2);

    private Integer type;

    BasicInfoType(Integer type) {
        this.type = type;
    }

    public static boolean isConcept(Integer type) {
        return CONCEPT.getType().equals(type);
    }

    public static boolean isEntity(Integer type) {
        return ENTITY.getType().equals(type);
    }

    public static boolean isSynonym(Integer type) {
        return SYNONYM.getType().equals(type);
    }
}
