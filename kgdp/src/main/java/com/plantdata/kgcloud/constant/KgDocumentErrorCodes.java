package com.plantdata.kgcloud.constant;

import lombok.Getter;

@Getter
public enum KgDocumentErrorCodes implements ErrorCode {

    /**
     * errorCode 统一为6位数字
     * 前 两 位: 服务标识，默认以服务端口后两位 固定为12
     * 中间两位: 模块标识，
     * 后 两 位: 业务错误标识
     */
    DOCUMENT_NOT_EXISTS(120101, "文档不存在"),

    RESOURCE_UPLOAD_ERROR(120102, "资源上传失败"),

    RESOURCE_DOWNLOAD_ERROR (120103, "资源下载失败"),

    WORD_CONVERT_HTML_ERROR (120104, "word转html失败"),

    DOCUMENT_TYPE_ERROR (120105, "文档类型错误"),

    DOCUMENT_PARSE_ERROR (120106, "文档解析错误"),

    MODEL_IS_IMPORT_GROUP (120107, "模式已入图"),

    SAVE_DATA_TO_DATABASE_ERROR(120107,"非图谱引入不可提取模式"),

    HTTP_ERROR(120201,"远程服务错误"),

    PARAMETER_ERROR(120202,"参数异常，请检查参数"),

    SCENE_NOT_EXISTS(120301,"场景不存在");



    private final int errorCode;

    private final String message;

    KgDocumentErrorCodes(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
