package ai.plantdata.kgcloud.constant;

/**
 * @author cjw
 */

import ai.plantdata.kgcloud.sdk.constant.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 19:23
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum TimeFilterTypeEnum implements BaseEnum {
    NO_FILTER(0),
    ENTITY(1),
    RELATION(2),
    ENTITY_RELATION(3)
    ;
    private int id;

    @Override
    public Integer fetchId() {
        return id;
    }
}
