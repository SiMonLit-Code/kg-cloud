package com.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 18:22
 **/
@Getter
public enum KgmsErrorCodeEnum implements ErrorCode {
    /**
     * errorCode 统一为6位数字
     * 前 两 位: 服务标识，默认以服务端口后两位 固定为12
     * 中间两位: 模块标识，
     * 后 两 位: 业务错误标识
     */
    DATASET_NOT_EXISTS(120101, "数据集不存在"),
    FOLDER_NOT_EXISTS(120102, "文件夹不存在"),
    FOLDER_DISABLE_DELETE(120103, "默认文件夹不允许删除"),
    DATASET_TYPE_NONSUPPORT(120104, "不支持的数据集类型"),
    DATASET_CONNECT_ERROR(120105, "数据连接失败"),
    DATASET_KEY_EXISTS(120106,"唯一标识已存在"),
    MODEL_NOT_EXISTS(120201, "模型不存在"),

    CONF_ALGORITHM_NOT_EXISTS(120601, "算法配置不存在"),
    CONF_KGQL_NOT_EXISTS(120602, "图谱业务不存在"),
    CONF_QA_NOT_EXISTS(120603, "图谱问答不存在"),
    CONF_REASONING_NOT_EXISTS(120604, "图谱统计不存在"),
    CONF_STATISTICAL_NOT_EXISTS(120606, "图谱统计不存在"),
    ES_CONFIG_NOT_FOUND(120605, "未找到es配置"),
    GRAPH_TYPE_ERROR(120606, "图谱类型错误"),

    DICTIONARY_NOT_EXISTS(120301, "词典不存在"),

    WORD_NOT_EXISTS(120302, "词条不存在"),

    GRAPH_NOT_EXISTS(120401, "图谱不存在"),
    GRAPH_CREATE_FAIL(120402, "图谱创建失败"),

    REMOTE_SERVICE_ERROR(120501, "远程服务错误"),

    BASIC_INFO_NOT_EXISTS(120502, "概念或实体不存在"),

    ATTRIBUTE_DEFINITION_NOT_EXISTS(120503, "属性定义不存在"),

    DATA_CONVERSION_ERROR(120504, "数据转换错误"),

    FILE_IMPORT_ERROR(120505, "文件导入错误"),

    ATTR_GROUP_NOT_EXISTS(120506, "属性分组不存在"),

    ATTR_TEMPLATE_ERROR(120507, "属性模板配置不正确"),

    ATTR_TEMPLATE_NOT_EXISTS(120508, "属性模板不存在"),
    ;

    private final int errorCode;

    private final String message;

    KgmsErrorCodeEnum(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

}
