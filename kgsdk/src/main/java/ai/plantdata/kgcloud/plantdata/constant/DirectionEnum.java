package ai.plantdata.kgcloud.plantdata.constant;

/**
 * @author cjw
 * @version 1.0
 * @date 2020/1/6 11:32
 */

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DirectionEnum {

    /**
     * 正向
     */
    FORWARD(1, "forward"),
    /**
     * 反向
     */
    BACKWARD(-1, "backward "),
    /**
     * 双向
     */
    TWO_WAY(0, "twoWay");
    @Getter
    private Integer value;
    @Getter
    private String desc;
}
