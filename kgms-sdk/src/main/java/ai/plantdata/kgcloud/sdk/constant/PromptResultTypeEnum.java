package ai.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/29 14:03
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PromptResultTypeEnum implements BaseEnum {
    /**
     *
     */
    ENTITY(1, "entity"),
    CONCEPT(0, "concept"),
    SYNONYM(2, "synonym"),
    CONCEPT_ENTITY(10, "entity_concept");
    private int id;
    private String desc;

    @Override
    public Object getValue() {
        return desc;
    }

    @Override
    public Integer fetchId() {
        return id;
    }
}
