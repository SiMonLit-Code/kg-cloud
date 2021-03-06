package ai.plantdata.kgcloud.sdk.constant;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/9 12:15
 */
public interface BaseEnum {

    default Object getValue() {
        return null;
    }

    default Integer fetchId() {
        return null;
    }
}
