package ai.plantdata.kgcloud.sdk.constant;

import lombok.Getter;

/**
 * @ClassName ApiAuditEnum
 * @Function TODO
 * @Author wanglong
 * @Date 2019/12/7 10:18
 * @Version 3.0.0
 **/
@Getter
public enum ApiAuditStatusEnum {
    INVOKE_SUCCESS(1),
    INVOKE_FAILURE(0);

    private Integer code;

    ApiAuditStatusEnum(Integer code) {
        this.code = code;
    }

    public static ApiAuditStatusEnum findByCode(Integer code) {
        for (ApiAuditStatusEnum item : ApiAuditStatusEnum.values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }
}
