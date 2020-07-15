package ai.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 19:23
 * @Description:
 */
@Getter
public enum TaskType {
    CLEAR_ENTITY("clearEntity"),
    ;

    private String type;

    TaskType(String type) {
        this.type = type;
    }
}
