package ai.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 17:49
 * @Description:
 */
@Getter
public enum TaskStatus {
    PROCESSING("processing"),
    SUCCESS("success"),
    FAIL("fail");

    private String status;

    TaskStatus(String status) {
        this.status = status;
    }
}
