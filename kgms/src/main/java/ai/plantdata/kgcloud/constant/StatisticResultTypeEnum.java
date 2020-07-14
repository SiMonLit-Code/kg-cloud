package ai.plantdata.kgcloud.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 10:01
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatisticResultTypeEnum {
    /**
     * 统计结果显示类型
     */
    NAME("name"),
    VALUE("value");
    @Getter
    private String value;

}
