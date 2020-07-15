package ai.plantdata.kgcloud.plantdata.constant;

/**
 * @author cx
 * @version 1.0
 * @date 2020/4/16 11:55
 */

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DataTypeEnum {
    /**
     *
     */
    CONCEPT(0, "概念"),

    ENTITY(1, "实体"),

    ATTRIBUTE(2, "属性");
    @Getter
    private int value;
    @Getter
    private String desc;
}
