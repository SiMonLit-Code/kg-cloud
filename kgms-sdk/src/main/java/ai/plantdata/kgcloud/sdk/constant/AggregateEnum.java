package ai.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw 2019-11-12 14:52:43
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AggregateEnum {
    /**
     * todo
     */
    COUNT(0, "count"), SUM(1, "sum"),SHOW(2,"show");

    private int index;
    private String expression;

    AggregateEnum(int index) {
        this.index = index;
    }


    public static AggregateEnum getSignByExpression(String expression) {
        for (AggregateEnum aggregateEnum : AggregateEnum.values()) {
            if (aggregateEnum.getExpression().equals(expression)) {
                return aggregateEnum;
            }
        }
        return null;
    }

}
