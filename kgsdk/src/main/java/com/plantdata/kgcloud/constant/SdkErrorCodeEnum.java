package com.plantdata.kgcloud.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/21 10:55
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SdkErrorCodeEnum implements ErrorCode {
    /**
     *
     */
    CONFIG_PARAM_ERROR(125001, "配置参数错误"),
    ;

    private final int errorCode;

    private final String message;

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
