package ai.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RelationTypeEnum implements BaseEnum {

    /**
     * 排序通用
     */
    COMMON(0, "common"),

    REASONING(1, "reasoning");

    private Integer value;
    private String name;


    @Override
    public Object getValue() {
        return name;
    }

    @Override
    public Integer fetchId() {
        return value;
    }
}
