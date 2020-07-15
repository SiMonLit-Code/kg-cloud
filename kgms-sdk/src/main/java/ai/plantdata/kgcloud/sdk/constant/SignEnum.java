package ai.plantdata.kgcloud.sdk.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw 2019-11-12 14:52:43
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SignEnum {
    /**
     * todo
     */
    EQ(0, "EQ", "$eq"), GT(1, "GT", "$gt"), LT(2, "LT", "$lt"), GTE(3, "GTE", "$gte"), LTE(4, "LTE", "$lte"), IN(5, "IN", "$in"), ALL(6, "ALL", "$all"), NE(7, "NE", "$ne");

    private int index;
    private String expression;
    private String function;

    SignEnum(int index) {
        this.index = index;
    }

    public static SignEnum getSignByExpression(String expression) {
        for (SignEnum signEnum : SignEnum.values()) {
            if (signEnum.getExpression().equals(expression)) {
                return signEnum;
            }
        }
        return null;
    }

    public SignEnum getReverse() {
        switch (this.getIndex()) {
            case 0:
            case 7:
                return this;
            case 1:
                return LT;
            case 2:
                return GT;
            case 3:
                return LTE;
            case 4:
                return GTE;
            default:
                return null;
        }
    }

}
