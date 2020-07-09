package com.plantdata.kgcloud.exception;

import ai.plantdata.cloud.constant.ErrorCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @version 1.0
 * @date 2020/1/2 14:52
 */
@Getter
@Setter
public class SdkException extends RuntimeException {

    protected int errCode;

    public SdkException(String message) {
        super(message);
    }

    public SdkException(int errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public SdkException(Throwable cause) {
        super(cause);
    }

    public SdkException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errCode = errorCode.getErrorCode();
    }

    public SdkException(int errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
    }
}
