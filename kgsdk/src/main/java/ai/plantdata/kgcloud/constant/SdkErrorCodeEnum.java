package ai.plantdata.kgcloud.constant;

import ai.plantdata.cloud.constant.ErrorCode;
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
    NUMBER_FORMAT_ERROR(57702, "数字转换异常"),
    CONFIG_PARAM_ERROR(125001, "配置参数错误"),
    APK_NOT_IS_ADMIN(125002, "非管理员apk"),
    JSON_NOT_FIT(130001, "JSON格式或字段不符合接口要求"),
    DB_NOT_EXIST(130002, "数据库不存在"),
    REMOTE_TABLE_NOT_SUPPORTED(130003, "暂不支持查询远程表"),
    SQL_ERROR(130004, "连接sql服务器出错"),
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
