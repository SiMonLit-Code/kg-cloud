package ai.plantdata.kgcloud.plantdata.req.reason;

/**
 * @author Administrator
 */

public enum SecEnum {
    /**
     *
     */
    OR(0, "OR"), AND(1, "AND");

    private int index;
    private String expression;

    SecEnum(int index) {
        this.index = index;
    }

    SecEnum(int index, String expression) {
        this.index = index;
        this.expression = expression;
    }

    public static SecEnum getSignByExpression(String expression) {
        for (SecEnum secEnum : SecEnum.values()) {
            if (secEnum.getExpression().equals(expression)) {
                return secEnum;
            }
        }
        return null;
    }

    public int getIndex() {
        return this.index;
    }

    public String getExpression() {
        return expression;
    }
}
